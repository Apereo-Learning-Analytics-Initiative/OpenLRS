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
package org.apereo.openlrs.model.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apereo.openlrs.exceptions.xapi.InvalidXApiFormatException;
import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.model.caliper.CaliperEventResult;
import org.apereo.openlrs.model.xapi.Statement;
import org.apereo.openlrs.model.xapi.StatementResult;
import org.apereo.openlrs.model.xapi.XApiActor;
import org.apereo.openlrs.model.xapi.XApiContext;
import org.apereo.openlrs.model.xapi.XApiContextActivities;
import org.apereo.openlrs.model.xapi.XApiObject;
import org.apereo.openlrs.model.xapi.XApiObjectDefinition;
import org.apereo.openlrs.model.xapi.XApiVerb;
import org.imsglobal.caliper.entities.agent.SoftwareApplication;
import org.imsglobal.caliper.entities.lis.Group;
import org.imsglobal.caliper.entities.session.Session;
import org.imsglobal.caliper.entities.w3c.Organization;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ggilbert
 *
 */
@Service
public class EventConversionService {
	private Logger log = Logger.getLogger(EventConversionService.class);
	@Autowired private ObjectMapper objectMapper;
	
	public boolean isEvent(OpenLRSEntity entity) {
		return Event.OBJECT_KEY.equals(entity.getObjectKey());
	}
	
	public boolean isXApi(OpenLRSEntity entity) {
		return Statement.OBJECT_KEY.equals(entity.getObjectKey());
	}
	
	public boolean isCaliper(OpenLRSEntity entity) {
	  return CaliperEvent.OBJECT_KEY.equals(entity.getObjectKey());
	}
	
	public Event toEvent(OpenLRSEntity entity) {
		Event event = null;
		if (isEvent(entity)) {
			event = (Event)event;
		}
		else if (isXApi(entity)) {
			Statement statement = (Statement)entity;
			event = fromXAPI(statement);
		}
		else if (isCaliper(entity)) {
		  CaliperEvent caliperEvent = (CaliperEvent)entity;
		  event = fromCaliper(caliperEvent);
		}
		else {
			throw new UnsupportedOperationException(String.format("Conversion from %s to event is not yet supported.", entity.getObjectKey()));
		}

		return event;
	}
	
	public Page<Event> toEventPage(Page<OpenLRSEntity> page) {
		Page<Event> events = null;
    	if (page != null && page.getContent() != null && !page.getContent().isEmpty()) {
    		List<OpenLRSEntity> entities = page.getContent();
    		List<Event> eventList = new ArrayList<Event>();
    		for (OpenLRSEntity entity : entities) {
    			eventList.add(toEvent(entity));
    		}
    		
    		events = new PageImpl<Event>(eventList);
    	}
    	
    	return events;
	}
	
	public Statement toXApi(OpenLRSEntity entity) {
		Statement statement = null;
		if (entity != null) {
			if (isXApi(entity)) {
				statement = (Statement)entity;
			}
			else if (isEvent(entity)) {
				Event event = (Event)entity;
				try {
					statement = objectMapper.readValue(event.getRaw().getBytes(), Statement.class);
				} 
				catch (Exception e) {
					log.error(e.getMessage(), e);
					throw new InvalidXApiFormatException();
				} 
			}
			else {
				throw new UnsupportedOperationException(String.format("Conversion from %s to xApi is not yet supported.", entity.getObjectKey()));
			}
		}
		
		return statement;
	}

    public String toCaliperString(OpenLRSEntity entity) {
        String caliperEvent = null;
        if (entity != null) {
            if (isCaliper(entity)) {
                caliperEvent = entity.toJSON();
            }
//            else if (isEvent(entity)) {
//                Event event = (Event) entity;
//
//                try {
//                    // caliperEvent = objectMapper.readValue(event.getRaw().getBytes(), Statement.class);
//                    // TODO: maybe build Caliper object?
//                    caliperEvent = org.imsglobal.caliper.events.SessionEvent.builder().build();
//                }
//                catch (Exception e) {
//                    log.error(e.getMessage(), e);
//                    throw new InvalidXApiFormatException();
//                }
//            }
            else {
                throw new UnsupportedOperationException(String.format("Conversion from %s to Caliper string is not yet supported.", entity.getObjectKey()));
            }
        }

        return caliperEvent;
    }

    public org.imsglobal.caliper.events.Event toCaliper(OpenLRSEntity entity) {
        org.imsglobal.caliper.events.Event caliperEvent = null;
        if (entity != null) {
            if (isCaliper(entity)) {
                caliperEvent = (org.imsglobal.caliper.events.Event) entity;
            }
            else if (isEvent(entity)) {
                Event event = (Event) entity;

                try {
//                    caliperEvent = objectMapper.readValue(event.getRaw().getBytes(), Statement.class);
                    // TODO: construct proper Caliper object
                    caliperEvent = org.imsglobal.caliper.events.SessionEvent.builder().build();
                }
                catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new InvalidXApiFormatException();
                }
            }
            else {
                throw new UnsupportedOperationException(String.format("Conversion from %s to Caliper is not yet supported.", entity.getObjectKey()));
            }
        }

        return caliperEvent;
    }


    public Page<Statement> toXApiPage(Page<OpenLRSEntity> page) {
		Page<Statement> statements = null;
    	if (page != null && page.getContent() != null && !page.getContent().isEmpty()) {
    		List<OpenLRSEntity> entities = page.getContent();
    		List<Statement> statementList = new ArrayList<Statement>();
    		for (OpenLRSEntity entity : entities) {
    			statementList.add(toXApi(entity));
    		}
    		
    		statements = new PageImpl<Statement>(statementList);
    	}
    	
    	return statements;
	}
	
	public Page<org.imsglobal.caliper.events.Event> toCaliperPage(Page<OpenLRSEntity> page) {

	  Page<org.imsglobal.caliper.events.Event> events = null;
	  if (page != null && page.getContent() != null && !page.getContent().isEmpty()) {
	    List<OpenLRSEntity> entities = page.getContent();
      List<org.imsglobal.caliper.events.Event> eventList = new ArrayList<org.imsglobal.caliper.events.Event>();
      for (OpenLRSEntity entity : entities) {
        eventList.add(toCaliper(entity));
      }

      events = new PageImpl<org.imsglobal.caliper.events.Event>(eventList);
	  }

	  return events;
	}
	
	public StatementResult toXApiCollection(Collection<OpenLRSEntity> entities) {
		StatementResult statementResult = null;
		List<Statement> statements = null;
		
		if (entities != null && !entities.isEmpty()) {
			statements = new ArrayList<Statement>();
			
			for (OpenLRSEntity entity : entities) {
				statements.add(toXApi(entity));
			}
			
			statementResult = new StatementResult(statements);
		}
		
		
		return statementResult;
	}

    public List<org.imsglobal.caliper.events.Event> toCaliperCollection(Collection<OpenLRSEntity> entities) {
        List<org.imsglobal.caliper.events.Event> events = null;

        if (entities != null && !entities.isEmpty()) {
            events = new ArrayList<org.imsglobal.caliper.events.Event>();

            for (OpenLRSEntity entity : entities) {
                events.add(toCaliper(entity));
            }

        }

        return events;
    }

    public List<String> toCaliperStringCollection(Collection<OpenLRSEntity> entities) {
        List<String> events = null;

        if (entities != null && !entities.isEmpty()) {
            events = new ArrayList<String>();

            for (OpenLRSEntity entity : entities) {
                events.add(toCaliperString(entity));
            }

        }

        return events;
    }

    public Event fromXAPI(Statement xapi) {
		Event event = null;
		
		if (xapi != null) {
			event = new Event();
			
			event.setActor(parseActorXApi(xapi));
			event.setContext(parseContextXApi(xapi));
			event.setEventFormatType(EventFormatType.XAPI);
			
			Map<String,String> object = parseObjectXApi(xapi);
			if (object != null && !object.isEmpty()) {
				event.setObject(object.get("ID"));
				event.setObjectType(object.get("TYPE"));
			}
			
			//TODO
			event.setOrganization(null);
			event.setRaw(xapi.toJSON());
			event.setSourceId(xapi.getId());
			event.setTimestamp(xapi.getTimestamp());
			event.setVerb(parseVerbXApi(xapi));
		}
		
		return event;
	}
	
	public Event fromCaliper(CaliperEvent caliper) {
    Event event = null;
    
    if (caliper != null && caliper.getEvent() != null) {
      event = new Event();
      org.imsglobal.caliper.events.Event baseCaliperEvent =
          caliper.getEvent();
      
      event.setActor(baseCaliperEvent.getActor().getId());
      event.setVerb(baseCaliperEvent.getAction().getValue());
      
      Object caliperEventObject = baseCaliperEvent.getObject();
      if (caliperEventObject instanceof SoftwareApplication) {
        SoftwareApplication softwareApplication = (SoftwareApplication)caliperEventObject;
        event.setObject(softwareApplication.getId());
        event.setObjectType(softwareApplication.getType().getValue());
      }
      else if (caliperEventObject instanceof Session) {
        Session session = (Session)caliperEventObject;
        event.setObject(session.getId());
        event.setObjectType(session.getType().getValue());
      }

      event.setRaw(caliper.toJSON());
      event.setEventFormatType(EventFormatType.CALIPER);
      event.setSourceId(caliper.getKey());

      Organization caliperEventGroup = baseCaliperEvent.getGroup();
      if (caliperEventGroup != null) {
        event.setContext(caliperEventGroup.getId());
      }
      
      DateTime startedAtTime = baseCaliperEvent.getStartedAtTime();
      if (startedAtTime != null) {
        event.setTimestamp(String.valueOf(startedAtTime.getMillis()));
      }
      
      //TODO
      event.setOrganization(null);
    }
    
    return event;
	}
	
	private String parseContextXApi(Statement xapi) {
		String context = null;
		
		XApiContext xApiContext = xapi.getContext();
		if (xApiContext != null) {
			XApiContextActivities xApiContextActivities = xApiContext.getContextActivities();
			if (xApiContextActivities != null) {
				List<XApiObject> parentContext = xApiContextActivities.getParent();
				if (parentContext != null && !parentContext.isEmpty()) {
					for (XApiObject object : parentContext) {
						String id = object.getId();
						if (StringUtils.contains(id, "portal/site/")) {
							context = StringUtils.substringAfterLast(id, "/");
							break;
						}
					}
				}
			}
		}

		return context;
	}
	
	private String parseActorXApi(Statement xapi) {
		String actor = null;
		
		XApiActor xApiActor = xapi.getActor();
		if (xApiActor != null) {
			String mbox = xApiActor.getMbox();
			if (StringUtils.isNotBlank(mbox)) {
				actor = StringUtils.substringBetween(mbox, "mailto:", "@");
			}
		}

		return actor;
	}
	
	private String parseVerbXApi(Statement xapi) {
		String verb = null;
		
		XApiVerb xApiVerb = xapi.getVerb();
		if (xApiVerb != null) {
			Map<String,String> display = xApiVerb.getDisplay();
			if (display != null && !display.isEmpty()) {
				verb = display.get("en-US");
			}
			
			if (StringUtils.isBlank(verb)) {
				String id = xApiVerb.getId();
				if (StringUtils.isNotBlank(id)) {
					verb = StringUtils.substringAfterLast(id, "/");
				}
			}
		}
		
		return verb;
	}
	
	private Map<String,String> parseObjectXApi(Statement xapi) {
		Map<String,String> objectIdandType = null;
		
		XApiObject xApiObject = xapi.getObject();
		if (xApiObject != null) {
			objectIdandType = new HashMap<String, String>();
			XApiObjectDefinition xApiObjectDefinition = xApiObject.getDefinition();
			if (xApiObjectDefinition != null) {
				String type = xApiObjectDefinition.getType();
				if (StringUtils.isNotBlank(type)) {
					objectIdandType.put("TYPE", StringUtils.substringAfterLast(type, "/"));
				}
			}
			
			String id = xApiObject.getId();
			if (StringUtils.isNotBlank(id)) {
				objectIdandType.put("ID", StringUtils.substringAfterLast(id, "/"));
			}
		}
		
		return objectIdandType;
	}

}
