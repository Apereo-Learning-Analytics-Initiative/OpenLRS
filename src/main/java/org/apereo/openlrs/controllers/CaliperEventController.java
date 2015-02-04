/**
 * Copyright 2014 Unicon (R) Licensed under the
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
package org.apereo.openlrs.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.exceptions.InvalidCaliperEventRequestException;
import org.apereo.openlrs.exceptions.InvalidXAPIRequestException;
import org.apereo.openlrs.exceptions.NotFoundException;
import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.model.caliper.CaliperEventResults;
import org.apereo.openlrs.services.CaliperEventService;
import org.apereo.openlrs.utils.StatementUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller to handle GET and POST calls
 * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#stmtapi
 * 
 * @author Steve Cody (scody @ unicon.net)
 */
@RestController
@RequestMapping("/caliper/events")
public class CaliperEventController {
	
	private final Logger logger = LoggerFactory.getLogger(CaliperEventController.class);
	private ObjectMapper objectMapper;
	private Validator validator;
    private final CaliperEventService caliperEventService;

    @Autowired
    public CaliperEventController(CaliperEventService caliperEventService, ObjectMapper objectMapper, Validator validator) {
        this.caliperEventService = caliperEventService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }
 
    /**
     * Sample: http://localhost:8080/caliper/events?eventId=caliper-java_ae7d638e-1eed-4e6d-a9cd-5a855204d73
     * @param eventId
     * @param allRequestParams
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=utf-8", params="eventId")
    public CaliperEvent getCaliperEvent(
            @RequestParam(value = "eventId", required = true) String eventId,
            @RequestParam Map<String,String> allRequestParams) {
        logger.debug("Event GET request received with parameters: {}", allRequestParams);
        if (allRequestParams.containsKey("voidedStatementId")) {
            throw new InvalidXAPIRequestException("Cannot submit both 'eventId' and 'voidedEventId' parameters.");
        }
        final CaliperEvent result = caliperEventService.getCaliperEvent(eventId);
        if (result == null) {
            throw new NotFoundException("Event for ID [" + eventId + "] not found.");
        }
        return result;
    }

    /**
     * Get CaliperEvent objects for the specified criteria
     * Sample: http://localhost:8080/caliper/events
     * 
     * @param actor the ID of the actor
     * @param activity the activity
     * @return JSON string of the statement objects matching the specified filter
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=utf-8", params="!eventId")
    public CaliperEventResults getCaliperEvent(
    		
    		//TODO: Test and Implement filters
            @RequestParam(value = "actor", required = false) String actor,
            @RequestParam(value = "activity", required = false) String activity,
            @RequestParam(value = "since", required = false) String since,
            @RequestParam(value = "until", required = false) String until,
            @RequestParam(value = "limit", required = false) String limit) {

        logger.debug("getStatement with actor: {} and activity: {}", actor, activity);

        Map<String, String> filterMap = StatementUtils.createStatementFilterMap(actor, activity, since, until, limit);
        return caliperEventService.getCaliperEvent(filterMap);
    }
    

    /**
     * Post a caliper event
     * 
     * @param requestBody the JSON containing the CaliperEvent data
     * @return JSON string of the CaliperEvent object with the specified ID
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     * @throws JsonProcessingException 
     */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
    public List<String> postStatement(@RequestBody String json) throws InvalidCaliperEventRequestException {    	
    	
    	List<String> ids = null;
    	if (json != null && StringUtils.isNotBlank(json)) {
    		ids = new ArrayList<String>(); 
    		
    		List<CaliperEvent> caliperEvents = null;
			try { 
				caliperEvents = objectMapper.readValue(json, new TypeReference<List<CaliperEvent>>() { });
				System.out.println("after mapper: ");
			} 
			catch (Exception e) {				
				throw new InvalidCaliperEventRequestException(e);
			} 
			System.out.println("before for");
    		for (CaliperEvent caliperEvent : caliperEvents) {
    			Set<ConstraintViolation<CaliperEvent>> violations = validator.validate(caliperEvent);
    			if (!violations.isEmpty()) {
    				StringBuilder msg = new StringBuilder();
    				for (ConstraintViolation<CaliperEvent> cv : violations) {
    					msg.append(cv.getMessage()+", ");
    				}
    				throw new InvalidCaliperEventRequestException(msg.toString());    				
    			}
    	        logger.debug("Statement POST request received with input CaliperEvent: {}", caliperEvent);
    	        ids.addAll(caliperEventService.postCaliperEvent(caliperEvent)) ;
    		}
    	}
    	return ids;
    }

    /**
     * TODO
     */
    @RequestMapping(method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
    public Statement putStatement(@Valid @RequestBody CaliperEvent caliperEvent) {
        logger.debug("CaliperEvent PUT request received with input statement: {}", caliperEvent);
        throw new NotImplementedException("PUT operation not yet implemented.");
    }

}
