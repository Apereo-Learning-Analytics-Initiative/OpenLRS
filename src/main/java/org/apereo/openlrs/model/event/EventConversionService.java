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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
import org.apereo.openlrs.exceptions.xapi.InvalidXApiFormatException;
import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.model.caliper.CaliperEvent;
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
import org.imsglobal.caliper.events.BaseEventContext;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
//	private Logger log = Logger.getLogger(EventConversionService.class);
    private Logger log = LoggerFactory.getLogger(EventConversionService.class);

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

        log.warn("object key: {}", entity.getObjectKey());

		if (isEvent(entity)) {
			event = (Event)entity;
		}
		else if (isXApi(entity)) {
			Statement statement = (Statement)entity;
			event = fromXAPI(statement);
		}
		else if (isCaliper(entity)) {
            log.warn("entity is a Caliper event");
            CaliperEvent olrsCaliperEvent = (CaliperEvent) entity;
            //event = fromCaliper(olrsCaliperEvent);
            event = new Event();
            event.setRaw(olrsCaliperEvent.toJSON());
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

    public org.imsglobal.caliper.events.Event toCaliper(OpenLRSEntity entity) {
        org.imsglobal.caliper.events.Event caliperEvent = null;
        if (entity != null) {
            if (isCaliper(entity)) {
                caliperEvent = (org.imsglobal.caliper.events.Event) entity;
            }
            else if (isEvent(entity)) {
                Event event = (Event) entity;

                try {
                    caliperEvent = objectMapper.readValue(event.getRaw().getBytes(), org.imsglobal.caliper.events.Event.class);
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

    public JsonNode toCaliperJson(OpenLRSEntity olrsEntity) {
        String caliperRawJson = null;
        JsonNode caliperJson = null;

        if (olrsEntity != null) {
            if (isCaliper(olrsEntity)) {
                caliperRawJson = ((CaliperEvent) olrsEntity).toJSON();
            } else if (isEvent(olrsEntity)) {
                caliperRawJson = ((Event) olrsEntity).getRaw();
            }
            else {
                throw new UnsupportedOperationException(String.format("Conversion from %s to Caliper JSON is not yet supported.", olrsEntity.getObjectKey()));
            }
        }

        try {
            caliperJson = objectMapper.readTree(caliperRawJson);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InvalidXApiFormatException();
        }

        return caliperJson;
    }

    public List<JsonNode> toCaliperJsonList(Collection<OpenLRSEntity> olrsEntities) {
        List<JsonNode> events = null;

        if (olrsEntities != null && !olrsEntities.isEmpty()) {
            events = new ArrayList<JsonNode>();

            for (OpenLRSEntity olrsEntity : olrsEntities) {
                events.add(toCaliperJson(olrsEntity));
            }

        }

        return events;
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
	
	public Event fromCaliper(CaliperEvent caliperEvent) {
    Event openLRSEvent = null;

        if (caliperEvent.getEvent() == null) {
            log.warn("getEvent() fails");
        }

    if (caliperEvent != null && caliperEvent.getEvent() != null) {
      openLRSEvent = new Event();
      org.imsglobal.caliper.events.Event baseCaliperEvent =
          caliperEvent.getEvent();
      
      openLRSEvent.setActor(baseCaliperEvent.getActor().getId());
      openLRSEvent.setVerb(baseCaliperEvent.getAction());
      
      Object caliperEventObject = baseCaliperEvent.getObject();
      if (caliperEventObject instanceof SoftwareApplication) {
        SoftwareApplication softwareApplication = (SoftwareApplication)caliperEventObject;
        openLRSEvent.setObject(softwareApplication.getId());
        openLRSEvent.setObjectType(softwareApplication.getType());
      }
      else if (caliperEventObject instanceof Session) {
        Session session = (Session)caliperEventObject;
        openLRSEvent.setObject(session.getId());
        openLRSEvent.setObjectType(session.getType());
      }

      openLRSEvent.setRaw(caliperEvent.toJSON());
      openLRSEvent.setEventFormatType(EventFormatType.CALIPER);
      openLRSEvent.setSourceId(caliperEvent.getKey());

        try {
            Group caliperEventGroup = (Group) baseCaliperEvent.getClass()
                    .getMethod("getGroup", null).invoke(null, null);
            if (caliperEventGroup != null) {
                openLRSEvent.setContext(caliperEventGroup.getId());
            }
        } catch (NoSuchMethodException e) {
            // do nothing; event doesn't have group
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        DateTime startedAtTime = baseCaliperEvent.getEventTime();
      if (startedAtTime != null) {
        openLRSEvent.setTimestamp(String.valueOf(startedAtTime.getMillis()));
      }
      
      //TODO
      openLRSEvent.setOrganization(null);
    }
    
    return openLRSEvent;
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
