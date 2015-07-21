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

import java.util.UUID;

import org.apache.log4j.Logger;
import org.apereo.openlrs.model.OpenLRSEntity;
import org.imsglobal.caliper.events.Event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ggilbert
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaliperEvent implements OpenLRSEntity {

  private static final long serialVersionUID = 1L;
  private Logger log = Logger.getLogger(CaliperEvent.class);
  @JsonIgnore
  public static final String OBJECT_KEY = "CALIPEREVENT";
  
  public CaliperEvent() {
    
  }
  
  public CaliperEvent(JsonNode jsonNode) {
    this.json = jsonNode.toString();
    this.key = UUID.randomUUID().toString();
    this.event = CaliperUtils.toEvent(jsonNode);
    this.type = CaliperUtils.getType(jsonNode);
  }

  private String key;
  private Event event;
  private String type;
  private String json;

  @JsonIgnore
  public Event getEvent() {
    return event;
  }

  @JsonIgnore
  public String getType() {
    return type;
  }

  @Override
  @JsonIgnore
  public String getKey() {
    return key;
  }

  @Override
  @JsonIgnore
  public String getObjectKey() {
    return OBJECT_KEY;
  }

  @JsonIgnore
  public String toJSON() {
    return this.json;
  }

  @Override
  public String toString() {
    return "CaliperEvent [getKey()="
        + getKey() + ", getObjectKey()=" + getObjectKey() + "]";
  }

}
