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

import org.apereo.openlrs.Application;
import org.apereo.openlrs.model.StatementResult;
import org.apereo.openlrs.utils.StatementUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author Robert E. Long (rlong @ unicon.net)
 *
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
public class StatementTest {

    @Autowired
    private StatementController statementController;

    @Test
    public void getFilteredStatementTest() {
        String statementId = "1234-TEST-5678-UUID";
        String actor = "actor1";
        String activity = "activity1";
        StatementResult response = statementController.getStatement(actor, activity);
        Assert.assertNotNull(response);
    }

    @Test
    public void getAllStatementTest() {
        String statementId = "";
        String actor = "";
        String activity = "";
        StatementResult response = statementController.getStatement(actor, activity);
        Assert.assertNotNull(response);
    }

    @Test
    public void postStatementTest() {
        String requestBody = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"}," +
                    "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}}," +
                    "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}," +
                    "\"result\" : {\"response\" : \"Wow, nice work!\"}}";
        List<String> response = statementController.postStatement(requestBody);
        Assert.assertNotNull(response);
    }

    @Test
    public void statementJsonValidTest() {
        String json = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"}," +
                    "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}}," +
                    "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";

        boolean valid = StatementUtils.hasAllRequiredProperties(json);
        Assert.assertTrue(valid);
    }

    @Test
    public void statementJsonInvalidTest() {
        String noActorJson = "{\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}}," +
                    "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
        String noActorIfiKeyJson = "{\"actor\" : {\"objectType\": \"Agent\",\"INVALID\":\"mailto:test@example.com\"}," +
                "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}}," +
                "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
        String noActorIfiValueJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"\"}," +
                "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}}," +
                "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
        String noVerbJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"}," +
                "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
        String noVerbIdKeyJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"}," +
                "\"verb\" : {\"display\": {\"en-US\":\"commented\"}}," +
                "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
        String noVerbIdValueJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"}," +
                "\"verb\" : {\"id\":\"\",\"display\": {\"en-US\":\"commented\"}}," +
                "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
        String noObjectJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"}," +
                "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}}}";
        String noObjectIdKeyJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"}," +
                "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}}," +
                "\"object\": {\"INVALID\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
        String noObjectIdValueJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"}," +
                "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}}," +
                "\"object\": {\"id\":\"\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";

        boolean valid = StatementUtils.hasAllRequiredProperties(noActorJson);
        Assert.assertFalse(valid);

        valid = StatementUtils.hasAllRequiredProperties(noActorIfiKeyJson);
        Assert.assertFalse(valid);

        valid = StatementUtils.hasAllRequiredProperties(noActorIfiValueJson);
        Assert.assertFalse(valid);

        valid = StatementUtils.hasAllRequiredProperties(noVerbJson);
        Assert.assertFalse(valid);

        valid = StatementUtils.hasAllRequiredProperties(noVerbIdKeyJson);
        Assert.assertFalse(valid);

        valid = StatementUtils.hasAllRequiredProperties(noVerbIdValueJson);
        Assert.assertFalse(valid);

        valid = StatementUtils.hasAllRequiredProperties(noObjectJson);
        Assert.assertFalse(valid);

        valid = StatementUtils.hasAllRequiredProperties(noObjectIdKeyJson);
        Assert.assertFalse(valid);

        valid = StatementUtils.hasAllRequiredProperties(noObjectIdValueJson);
        Assert.assertFalse(valid);
    }

}
