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

import java.util.Map;

import org.apereo.openlrs.services.StatementService;
import org.apereo.openlrs.utils.StatementUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle GET and POST calls
 * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#stmtapi
 * 
 * @author Robert E. Long (rlong @ unicon.net)
 *
 */
@RestController
@RequestMapping("/xAPI/statements")
public class StatementController {

    private final StatementService statementService;

    @Autowired
    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    /**
     * Get statement objects for the specified criteria
     * 
     * @param statementId the UUID of the statement
     * @param actor the ID of the actor
     * @param activity the activity
     * @return JSON string of the statement objects matching the specified filter
     */
    @RequestMapping(value = {"", "/", "*"}, method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getStatement(
            @RequestParam(value = "statementId", required = false) String statementId,
            @RequestParam(value = "actor", required = false) String actor,
            @RequestParam(value = "activity", required = false) String activity) {
        Map<String, String> filterMap = StatementUtils.createStatementFilterMap(statementId, actor, activity);

        return statementService.getStatement(filterMap);
    }

    /**
     * Post a statement
     * 
     * @param requestBody the JSON containing the statement data
     * @return JSON string of the statement object with the specified ID
     */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
    public String postStatement(@RequestBody String requestBody) {
        return statementService.postStatement(requestBody);
    }

}
