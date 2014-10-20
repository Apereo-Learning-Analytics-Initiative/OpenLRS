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

import java.util.List;

import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.services.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ggilbert
 *
 */
@RestController
public class ConvenienceAPIController {
	
	@Autowired private StatementService service;
	
	@RequestMapping(value = "/api/context/{id}", method = RequestMethod.GET, 
			produces = "application/json;charset=utf-8")
	public List<Statement> getStatementsForContext(@PathVariable("id") String contextId) {
		return service.getByContext(contextId);
	}

	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.GET, 
			produces = "application/json;charset=utf-8")
	public List<Statement> getStatementsForUser(@PathVariable("id") String userId) {
		return service.getByUser(userId);
	}

	@RequestMapping(value = "/api/user/{userId}/context/{contextId}", method = RequestMethod.GET, 
			produces = "application/json;charset=utf-8")
	public List<Statement> getStatementsForUser(@PathVariable("contextId") String contextId, @PathVariable("userId") String userId) {
		return service.getByContextAndUser(contextId, userId);
	}

}
