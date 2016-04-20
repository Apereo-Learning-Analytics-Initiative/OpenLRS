/**
 * Copyright 2015 Unicon (R) Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package org.apereo.openlrs.controllers.caliper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.exceptions.NotFoundException;
import org.apereo.openlrs.exceptions.caliper.InvalidCaliperFormatException;
import org.apereo.openlrs.exceptions.InvalidRequestException;
import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.model.xapi.About;
import org.apereo.openlrs.services.caliper.CaliperService;
import org.apereo.openlrs.utils.StatementUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author ggilbert
 * @author Lance E Sloan (lsloan at umich dot edu)
 */
@RestController
@RequestMapping("/caliper")
public class CaliperController {
    private final Logger logger = LoggerFactory.getLogger(CaliperController.class);
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CaliperService caliperService;
    @Value("${caliper.version}")
    private String version;
    @Value("${caliper.pageDefault ?: 0}")
    private Integer pageDefault;
    @Value("${caliper.limitDefault ?: 20}")
    private Integer limitDefault;
    @Value("${caliper.limitMaximum ?: 100}")
    private Integer limitMaximum;


    /**
     * @param statementId      ID of the statement to find
     * @return JSON node of requested Caliper event
     */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET,
            produces = "application/json;charset=utf-8", params = "statementId")
    public JsonNode getByIdHandler (
            @RequestParam(value = "statementId", required = true) String statementId
    ) throws InvalidRequestException {

        JsonNode result = null;

        try {
            result = caliperService.getJsonNode(statementId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InvalidRequestException(e.getMessage(), e);
        }

        if (result == null) {
            throw new NotFoundException("Statement for ID [" + statementId
                    + "] not found.");
        }

        return result;
    }

    /**
     * Get statement objects for the specified criteria
     *
     * @param activity
     * @param actor
     * @param since
     * @param until
     * @param pageString Number of the requested page
     * @param limitString Number of results per page
     * @return JSON string of the statement objects matching the specified filter
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = {"", "/"},
            params = {"!statementId",},
            produces = "application/json;charset=utf-8"
    )
    public Page<JsonNode> getByFiltersHandler(
            @RequestParam(value = "actor", required = false) String actor,
            @RequestParam(value = "activity", required = false) String activity,
            @RequestParam(value = "since", required = false) String since,
            @RequestParam(value = "until", required = false) String until,
            @RequestParam(value = "page", required = false) String pageString,
            @RequestParam(value = "limit", required = false) String limitString
    ) throws InvalidRequestException {
        try {
            Map<String, String> filterMap =
                    StatementUtils.createStatementFilterMap(actor, activity, since, until, limitString);
            Integer page = pageDefault;
            Integer limit = limitDefault;

            if (pageString != null) {
                page = Integer.parseInt(pageString);

                if (page < 0) {
                    page = pageDefault;
                }
            }

            if (limitString != null) {
                limit = Integer.parseInt(limitString);

                if (limit < 0) {
                    limit = limitDefault;
                } else if (limit > limitMaximum) {
                    limit = limitMaximum;
                }
            }

            return caliperService.getJsonNodes(filterMap, new PageRequest(page, limit));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InvalidRequestException(e.getMessage(), e);
        }
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json;charset=utf-8")
    public List<String> postHandler(@RequestBody String json)
            throws JsonProcessingException, IOException, InvalidCaliperFormatException {
        List<String> caliperEventIds = null;

        if (StringUtils.isNotBlank(json)) {
            logger.debug(json);

            JsonNode jsonNode = objectMapper.readTree(json);

            JsonNode dataJsonNodes = jsonNode.path("data");

            if (dataJsonNodes.isArray()) {
                caliperEventIds = new ArrayList<>();

                for (JsonNode dataJsonNode : dataJsonNodes) {
                    CaliperEvent caliperEvent = new CaliperEvent(dataJsonNode);
                    caliperService.post(null, caliperEvent);
                    caliperEventIds.add(caliperEvent.getKey());
                }
            } else {
                throw new InvalidCaliperFormatException("\"data\" attribute must be an array");
            }
        }

        return caliperEventIds;
    }

    @RequestMapping(value = "about", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public About about() {
        About aboutCaliper = new About(version);
        aboutCaliper.setExtensions(new HashMap<URI, String>(){
            {
                String hostAddress;

                try{
                    hostAddress = InetAddress.getLocalHost().getHostAddress();
                } catch (Exception e) {
                    hostAddress = "unknown";
                }

                put(URI.create("hostAddress"), hostAddress);
            }
        });

        return aboutCaliper;
    }
}