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

import org.apache.log4j.Logger;
import org.apereo.openlrs.model.OpenLRSEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ggilbert
 *
 */
public class CaliperEvent implements OpenLRSEntity {
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(CaliperEvent.class);
	@JsonIgnore public static final String OBJECT_KEY = "CALIPEREVENT";


	@Override
	@JsonIgnore
	public String getKey() {
		return null;
	}

	@Override
	@JsonIgnore
	public String getObjectKey() {
		return OBJECT_KEY;
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


}
