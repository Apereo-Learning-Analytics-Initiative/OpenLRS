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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.apereo.openlrs.model.OpenLRSEntity;
import org.imsglobal.caliper.events.Event;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @author ggilbert
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaliperEvent implements OpenLRSEntity {

    @JsonIgnore
    public static final String OBJECT_KEY = "CALIPEREVENT";
    private static final long serialVersionUID = 1L;
    @Autowired
    private ObjectMapper objectMapper;
    private Logger log = Logger.getLogger(CaliperEvent.class);
    private String key;
    private Event event;
    private String type;
    private String json;

    public CaliperEvent() {
    }

    public CaliperEvent(JsonNode jsonNode) {
        // this section: hack to keep ID around for later (needed for tier-two storage)
        this.key = jsonNode.path("openlrsSourceId").asText();
        if ((this.key == null) || (this.key.isEmpty())) {
            this.key = UUID.randomUUID().toString();
            ((ObjectNode) jsonNode).put("openlrsSourceId", this.key);
        }

        this.json = jsonNode.toString();
        this.event = CaliperUtils.toEvent(jsonNode);
        this.type = CaliperUtils.getType(jsonNode);
    }

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
