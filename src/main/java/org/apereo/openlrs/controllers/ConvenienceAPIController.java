/*******************************************************************************
 * Copyright (c) 2015 Unicon (R) Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *******************************************************************************/
package org.apereo.openlrs.controllers;

import java.util.List;

import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.model.xapi.Statement;
import org.apereo.openlrs.services.NormalizedEventService;
import org.apereo.openlrs.services.xapi.XApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ggilbert
 *
 */
@RestController
public class ConvenienceAPIController {
	
	@Autowired private XApiService xapiService;
	@Autowired private NormalizedEventService normalizedEventService;
	
	public static final String XAPI = "xapi";
	
	@RequestMapping(value = "/api/context/{id}", method = RequestMethod.GET, 
			produces = "application/json;charset=utf-8")
	public Page<OpenLRSEntity> getEventsForContext(@PathVariable("id") String contextId,
								@RequestParam(value = "page", required = false, defaultValue = "0") String page,
								@RequestParam(value = "limit", required = false, defaultValue = "100") String limit,
								@RequestParam(value = "format", required = false) String format) {
		Page<OpenLRSEntity> entities = null;
		PageRequest pageRequest = new PageRequest(Integer.parseInt(page), Integer.parseInt(limit));

		if (XAPI.equalsIgnoreCase(format)) {
			entities = (Page<OpenLRSEntity>)(Page<?>)xapiService.getByContext(contextId, pageRequest);
		}
		else {
			entities = (Page<OpenLRSEntity>)(Page<?>)normalizedEventService.getByContext(contextId, pageRequest);
		}
		
		return entities;
	}

	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.GET, 
			produces = "application/json;charset=utf-8")
	public Page<OpenLRSEntity> getEventsForUser(@PathVariable("id") String userId,
			@RequestParam(value = "page", required = false, defaultValue = "0") String page,
			@RequestParam(value = "limit", required = false, defaultValue = "100") String limit,
			@RequestParam(value = "format", required = false) String format) {
		
		Page<OpenLRSEntity> entities = null;
		PageRequest pageRequest = new PageRequest(Integer.parseInt(page), Integer.parseInt(limit));

		if (XAPI.equalsIgnoreCase(format)) {
			entities = (Page<OpenLRSEntity>)(Page<?>)xapiService.getByUser(userId, pageRequest);
		}
		else {
			entities = (Page<OpenLRSEntity>)(Page<?>)normalizedEventService.getByUser(userId, pageRequest);
		}
		
		return entities;
	}

	@RequestMapping(value = "/api/user/{userId}/context/{contextId}", method = RequestMethod.GET, 
			produces = "application/json;charset=utf-8")
	public Page<OpenLRSEntity> getEventsForContextAndUser(@PathVariable("contextId") String contextId, @PathVariable("userId") String userId,
			@RequestParam(value = "page", required = false, defaultValue = "0") String page,
			@RequestParam(value = "limit", required = false, defaultValue = "100") String limit,
			@RequestParam(value = "format", required = false) String format) {
		
		Page<OpenLRSEntity> entities = null;
		PageRequest pageRequest = new PageRequest(Integer.parseInt(page), Integer.parseInt(limit));

		if (XAPI.equalsIgnoreCase(format)) {
			entities = (Page<OpenLRSEntity>)(Page<?>)xapiService.getByContextAndUser(contextId, userId, pageRequest);
		}
		else {
			entities = (Page<OpenLRSEntity>)(Page<?>)normalizedEventService.getByContextAndUser(contextId, userId, pageRequest);
		}
		
		return entities;
	}

}
