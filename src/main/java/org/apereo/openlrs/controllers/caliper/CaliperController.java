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
import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.model.caliper.CaliperEventResult;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apereo.openlrs.exceptions.xapi.InvalidXAPIRequestException;
import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.model.event.Event;
import org.apereo.openlrs.model.xapi.StatementResult;
import org.apereo.openlrs.services.caliper.CaliperService;
import org.apereo.openlrs.utils.StatementUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


/**
 * @author ggilbert
 */
@RestController
@RequestMapping("/caliper")
public class CaliperController {
    private final Logger logger = LoggerFactory.getLogger(CaliperController.class);
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CaliperService caliperService;

    /**
     * Get statement objects for the specified criteria
     *
     * @param actor    the ID of the actor
     * @param activity the activity
     * @return JSON string of the statement objects matching the specified filter
     */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public List<String> getStatement() {
//        public List<org.imsglobal.caliper.events.Event> getStatement() {

        try {
            Map<String, String> filterMap = null;
            // TODO: enable the following after this method has argument support
//            filterMap = StatementUtils.createStatementFilterMap(actor, activity, since, until, limit);
            return caliperService.getStrings(filterMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InvalidXAPIRequestException(e.getMessage(), e);
        }
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public String event(@RequestBody String json) throws JsonProcessingException, IOException {
        logger.debug(json);

        JsonNode jsonNode = objectMapper.readTree(json);
        logger.debug("Type: " + jsonNode.findValue("@type").textValue());
        CaliperEvent caliperEvent = new CaliperEvent(jsonNode);
        caliperService.post(null, caliperEvent);
        return caliperEvent.getKey();
    }
}