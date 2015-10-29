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
import org.apereo.openlrs.exceptions.NotFoundException;
import org.apereo.openlrs.exceptions.xapi.InvalidXAPIRequestException;
import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.model.xapi.About;
import org.apereo.openlrs.services.caliper.CaliperService;
import org.apereo.openlrs.utils.StatementUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
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


    /**
     *
     * @param statementId ID of the statement to find
     * @param allRequestParams other search parameters
     * @return JSON node of requested Caliper event
     */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET, produces = "application/json;charset=utf-8", params = "statementId")
    public JsonNode getByIdHandler(
            @RequestParam(value = "statementId", required = true) String statementId,
            @RequestParam Map<String, String> allRequestParams) {
        logger.debug("Statement GET request received with parameters: {}",
                allRequestParams);
        JsonNode result = null;
        try {
            result = caliperService.getJsonNode(statementId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InvalidXAPIRequestException(e.getMessage(), e);
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
     * @param limit
     * @param since
     * @param until
     * @return JSON string of the statement objects matching the specified filter
     */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET, produces = "application/json;charset=utf-8", params = "!statementId")
    public List<JsonNode> getByFiltersHandler(
            @RequestParam(value = "actor", required = false) String actor,
            @RequestParam(value = "activity", required = false) String activity,
            @RequestParam(value = "since", required = false) String since,
            @RequestParam(value = "until", required = false) String until,
            @RequestParam(value = "limit", required = false) String limit
    ) {
        try {
            Map<String, String> filterMap = null;
            filterMap = StatementUtils.createStatementFilterMap(actor, activity, since, until, limit);
            return caliperService.getJsonNodes(filterMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InvalidXAPIRequestException(e.getMessage(), e);
        }
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
    public List<String> postHandler(@RequestBody String json) throws JsonProcessingException, IOException {
        logger.debug(json);

        JsonNode jsonNode = objectMapper.readTree(json);
        logger.debug("Type: " + jsonNode.findValue("@type").textValue());
        CaliperEvent caliperEvent = new CaliperEvent(jsonNode);
        caliperService.post(null, caliperEvent);
        List<String> caliperEventIds = new ArrayList<>();
        caliperEventIds.add(caliperEvent.getKey());
        return caliperEventIds;
    }

    @RequestMapping(value = "about", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public About about() {
        return new About(version);
    }
}