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
package org.apereo.openlrs.model.caliper;

import org.imsglobal.caliper.actions.Action;
import org.imsglobal.caliper.entities.DigitalResource;
import org.imsglobal.caliper.entities.Targetable;
import org.imsglobal.caliper.entities.agent.Person;
import org.imsglobal.caliper.entities.agent.SoftwareApplication;
import org.imsglobal.caliper.entities.foaf.Agent;
import org.imsglobal.caliper.entities.lis.Group;
import org.imsglobal.caliper.entities.session.Session;
import org.imsglobal.caliper.events.Event;
import org.imsglobal.caliper.events.EventType;
import org.imsglobal.caliper.events.SessionEvent;
import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ggilbert
 *
 */
public class CaliperUtils {
  
  public static Event toEvent(JsonNode caliperJsonNode) {
    Event event = null;
    if (caliperJsonNode != null) {
      JsonNode eventTypeJsonNode = caliperJsonNode.get("@type");
      if (eventTypeJsonNode != null) {
        String eventType = eventTypeJsonNode.asText();
        
        if (eventType.equals(EventType.SESSION.getValue())) {          
          event = CaliperUtils.toSessionEvent(caliperJsonNode);
        }
      }
    }
    
    return event;
  }
  
  public static String getType(JsonNode caliperJsonNode) {
    String type = null;
    if (caliperJsonNode != null) {
      JsonNode eventTypeJsonNode = caliperJsonNode.get("@type");
      if (eventTypeJsonNode != null) {
        type = eventTypeJsonNode.asText();
      }
    }
    return type;
  }
  
  public static SessionEvent toSessionEvent(JsonNode sessionEventJsonNode) {
    SessionEvent sessionEvent = null;
    if (sessionEventJsonNode != null) {
      
      Action action = CaliperUtils.toAction(sessionEventJsonNode.get("action"));;
      Group group = CaliperUtils.toGroup(sessionEventJsonNode.get("group"));
      
      JsonNode generatedJsonNode = sessionEventJsonNode.get("generated");
      JsonNode actorJsonNode = sessionEventJsonNode.get("actor");
      JsonNode objectJsonNode = sessionEventJsonNode.get("object");
      
      if (generatedJsonNode != null && 
          actorJsonNode != null && 
          objectJsonNode != null) {
        
        Agent agent = null;
        Object object = null;
        Targetable target = null;
        
        if (action == Action.TIMED_OUT) {
          agent = CaliperUtils.toSoftwareApplication(actorJsonNode);
          //TODO
          object = CaliperUtils.toSession(objectJsonNode, null, null);
        }
        else {
          target = CaliperUtils.toDigitalResource(objectJsonNode);
          object = CaliperUtils.toSoftwareApplication(objectJsonNode);
          agent = CaliperUtils.toPersonAgent(actorJsonNode);
        }
        
        Agent sessionAgent = CaliperUtils.toPersonAgent(generatedJsonNode.get("actor"));
        
        DateTime startedAtTime = null;
        JsonNode startedAtTimeJsonNode = generatedJsonNode.get("startedAtTime");
        if (startedAtTimeJsonNode != null) {
          startedAtTime = new DateTime(Long.valueOf(startedAtTimeJsonNode.asText()));
        }
        
        Session session = CaliperUtils.toSession(generatedJsonNode, sessionAgent, startedAtTime);

        sessionEvent = 
            SessionEvent.builder()
            .startedAtTime(startedAtTime)
            .action(action)
            .generated(session)
            .actor(agent)
            .object(object)
            .group(group)
            .target(target)
            .build();

      }

    }
    
    return sessionEvent;
  }

  public static Action toAction(JsonNode actionJsonNode) {
    Action action = null;
    if (actionJsonNode != null) {
      String actionString = actionJsonNode.asText();
      action = Action.valueOf(actionString);
    }
    
    return action;
  }
  
  public static Person toPersonAgent(JsonNode agentJsonNode) {
    Person person = null;
    if (agentJsonNode != null) {
      person =
          Person.builder()
          .id(agentJsonNode.get("@id").asText())
          .name(agentJsonNode.get("name") != null ? agentJsonNode.get("name").asText() : null)
          .build();
    }
    
    return person;
  }
  
  public static Group toGroup(JsonNode groupJsonNode) {
    Group group = null;
    if (groupJsonNode != null) {
      group =
          Group.builder()
          .id(groupJsonNode.get("@id").asText())
          .name(groupJsonNode.get("name") != null ? groupJsonNode.get("name").asText() : null)
          .build();
    }
    
    return group;
  }
  
  public static DigitalResource toDigitalResource(JsonNode digitalResourceJsonNode) {
    DigitalResource digitalResource = null;
    if (digitalResourceJsonNode != null) {
      digitalResource = 
        DigitalResource.builder()
        .id(digitalResourceJsonNode.get("@id").asText())
        .build();
    }
    
    return digitalResource;
  }
  
  public static SoftwareApplication toSoftwareApplication(JsonNode softwareApplicationJsonNode) {
    SoftwareApplication softwareApplication = null;
    if (softwareApplicationJsonNode != null) {
      softwareApplication = 
        SoftwareApplication.builder()
        .id(softwareApplicationJsonNode.get("@id").asText())
        .name(softwareApplicationJsonNode.get("name") != null ? softwareApplicationJsonNode.get("name").asText() : null)
        .build();            
    }
    
    return softwareApplication;
  }
  
  public static Session toSession(JsonNode sessionJsonNode, Agent sessionAgent, DateTime startedAtTime) {
    Session session = null;
    if (sessionJsonNode != null) {
      session =
          Session.builder()
          .id(sessionJsonNode.get("@id").asText())
          .name(sessionJsonNode.get("name") != null ? sessionJsonNode.get("name").asText() : null)
          .actor(sessionAgent)
          .startedAtTime(startedAtTime)
          .build();
    }
    
    return session;
  }
}
