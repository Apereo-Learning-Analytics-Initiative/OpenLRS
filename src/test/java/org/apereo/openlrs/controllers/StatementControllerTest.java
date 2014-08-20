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

import org.apereo.openlrs.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author Robert E. Long (rlong @ unicon.net)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
public class StatementControllerTest {

    @Autowired
    private StatementController statementController;

    @Test
    public void getFilteredStatementTest() {
        String statementId = "1234-TEST-5678-UUID";
        String actor = "actor1";
        String activity = "activity1";
        String response = statementController.getStatement(statementId, actor, activity);
        Assert.assertNotNull(response);
    }

    @Test
    public void getAllStatementTest() {
        String statementId = "";
        String actor = "";
        String activity = "";
        String response = statementController.getStatement(statementId, actor, activity);
        Assert.assertNotNull(response);
    }

    @Test
    public void postStatementTest() {
        String requestBody = "{\"id\":\"UNIQUE_UUID\",\"actor\":\"actor1\",\"verb\":\"read\",\"object\":\"Little House on the Prairie\"}";
        String response = statementController.postStatement(requestBody);
        Assert.assertNotNull(response);
    }

}
