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

import org.apache.log4j.Logger;
import org.apereo.openlrs.model.OpenLRSEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ggilbert
 *
 */
@Document(indexName="event")
@JsonInclude(Include.NON_NULL)
public class Event implements OpenLRSEntity {

	private static final long serialVersionUID = 1L;
	@Transient private Logger log = Logger.getLogger(Event.class);
	@JsonIgnore public static final String OBJECT_KEY = "NORMALIZED";
	
	@Id private String id;
	private String sourceId;
	private String actor;
	private String verb;
	private String object;
	private String objectType;
	private String context;
	private String organization;
	private String timestamp;
	private EventFormatType eventFormatType;
	private String raw;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public EventFormatType getEventFormatType() {
		return eventFormatType;
	}
	public void setEventFormatType(EventFormatType eventFormatType) {
		this.eventFormatType = eventFormatType;
	}
	public String getRaw() {
		if (raw == null) {
			this.raw = toJSON();
		}
		return raw;
	}
	public void setRaw(String raw) {
		this.raw = raw;
	}
	
    @JsonIgnore
    public String toJSON() {
    	ObjectMapper om = new ObjectMapper();
    	String rawJson = null;
    	try {
			rawJson = om.writer().writeValueAsString(this);
		} 
    	catch (JsonProcessingException e) {
			log.error(e.getMessage(), e); 
		}
		return rawJson;
    }

    @Override
    public String toString() {
        return toJSON();        
    }

	@JsonIgnore
	@Override
	public String getKey() {
		return id;
	}

	@JsonIgnore
	@Override
	public String getObjectKey() {
		return OBJECT_KEY;
	}
}
