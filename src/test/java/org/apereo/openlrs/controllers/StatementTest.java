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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apereo.openlrs.Application;
import org.apereo.openlrs.controllers.xapi.StatementController;
import org.apereo.openlrs.model.xapi.Statement;
import org.apereo.openlrs.model.xapi.StatementResult;
import org.apereo.openlrs.model.xapi.XApiActor;
import org.apereo.openlrs.model.xapi.XApiObject;
import org.apereo.openlrs.model.xapi.XApiVerb;
import org.apereo.openlrs.utils.StatementUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * 
 * @author Robert E. Long (rlong @ unicon.net)
 *
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class StatementTest {

  @Autowired
  private StatementController statementController;
  private XApiActor actor;
  private XApiVerb verb;
  private XApiObject object;

  @Before
  public void setup() {
    actor = new XApiActor();
    actor.setMbox("mailto:test@test.com");
    verb = new XApiVerb();
    verb.setId("http://example.com/verb");
    verb.setDisplay(Collections.singletonMap("en-US", "verb"));
    object = new XApiObject();
    object.setId("http://example.com/object");
    
    Statement statement = new Statement();
    statement.setActor(actor);
    statement.setVerb(verb);
    statement.setObject(object);

    statementController.postStatement(statement.toJSON());
  }

  @Test
  public void getFilteredStatementTest() {
    String actor = "actor1";
    String activity = "activity1";
    StatementResult response = statementController.getStatement(actor, activity, null, null, null);
    Assert.assertNotNull(response);
  }

  @Test
  public void getAllStatementTest() {
    String actor = "";
    String activity = "";
    StatementResult response = statementController.getStatement(actor, activity, null, null, null);
    Assert.assertNotNull(response);
  }

  @Test
  public void postStatementTest() throws JsonParseException, JsonMappingException, IOException {
    Statement statement = new Statement();
    statement.setActor(actor);
    statement.setVerb(verb);
    statement.setObject(object);

    List<String> response = statementController.postStatement(statement.toJSON());
    Assert.assertNotNull(response);
  }

  @Test
  public void statementJsonValidTest() {
    String json = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"},"
        + "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}},"
        + "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";

    boolean valid = StatementUtils.hasAllRequiredProperties(json);
    Assert.assertTrue(valid);
  }

  @Test
  public void statementJsonInvalidTest() {
    String noActorJson = "{\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}},"
        + "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
    String noActorIfiKeyJson = "{\"actor\" : {\"objectType\": \"Agent\",\"INVALID\":\"mailto:test@example.com\"},"
        + "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}},"
        + "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
    String noActorIfiValueJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"\"},"
        + "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}},"
        + "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
    String noVerbJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"},"
        + "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
    String noVerbIdKeyJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"},"
        + "\"verb\" : {\"display\": {\"en-US\":\"commented\"}},"
        + "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
    String noVerbIdValueJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"},"
        + "\"verb\" : {\"id\":\"\",\"display\": {\"en-US\":\"commented\"}},"
        + "\"object\": {\"id\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
    String noObjectJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"},"
        + "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}}}";
    String noObjectIdKeyJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"},"
        + "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}},"
        + "\"object\": {\"INVALID\":\"http://example.com/website\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";
    String noObjectIdValueJson = "{\"actor\" : {\"objectType\": \"Agent\",\"mbox\":\"mailto:test@example.com\"},"
        + "\"verb\" : {\"id\":\"http://example.com/commented\",\"display\": {\"en-US\":\"commented\"}},"
        + "\"object\": {\"id\":\"\",\"definition\": {\"name\" : {\"en-US\":\"Some Awesome Website\"}}}}";

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
