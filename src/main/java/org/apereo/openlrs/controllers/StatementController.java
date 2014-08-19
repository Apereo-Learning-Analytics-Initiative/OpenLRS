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

import org.apereo.openlrs.services.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
     * Get all statements
     * @return JSON string of all statement objects
     */
    @RequestMapping(value={"", "/", "*"}, method=RequestMethod.GET, produces="application/json")
    public String getAllStatements() {
        return statementService.getAllStatements();
    }

    /**
     * Get a statement for a specified ID
     * 
     * @return JSON string of the statement object with the specified ID
     */
    @RequestMapping(value="/{statementId}", method=RequestMethod.GET, produces="application/json")
    public String getStatement(@PathVariable String statementId) {
        return statementService.getStatement(statementId);
    }

    /**
     * Post a statement
     * 
     * @return JSON string of the statement object with the specified ID
     */
    @RequestMapping(value={"", "/"}, method=RequestMethod.POST, consumes="application/json", produces="application/json")
    public String postStatement(@RequestBody String requestBody) {
        return statementService.postStatement(requestBody);
    }

}
