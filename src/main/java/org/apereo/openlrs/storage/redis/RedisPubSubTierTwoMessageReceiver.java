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
package org.apereo.openlrs.storage.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apereo.openlrs.exceptions.InvalidEventFormatException;
import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.model.xapi.Statement;
import org.apereo.openlrs.storage.StorageFactory;
import org.apereo.openlrs.storage.TierTwoStorage;
import org.imsglobal.caliper.databind.JsonObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ggilbert
 *
 */
@Component
@Profile("redis")
public class RedisPubSubTierTwoMessageReceiver {
	
	private Logger log = LoggerFactory.getLogger(RedisPubSubTierTwoMessageReceiver.class);
	@Autowired private ObjectMapper objectMapper;
	@Autowired private StorageFactory storageFactory;

	
	public void onMessage(String json) {
	  
	  if (log.isDebugEnabled()) {
	    log.debug(json);
	  }
	  
		// guess at format
		OpenLRSEntity entity = null;
		
		try {
			entity = objectMapper.readValue(json.getBytes(), Statement.class);
		}
		catch (Exception e) {
			log.warn("unable to parse as xAPI: {}", json);
		}
		
		if (entity == null) {
			// try caliper
            ObjectMapper mapper = JsonObjectMapper.create(JsonInclude.Include.ALWAYS);

			try {
//                JsonNode jsonNode = objectMapper.readTree(json);
                JsonNode jsonNode = mapper.readTree(json);
				entity = new CaliperEvent(jsonNode);
			}
			catch (Exception e) {
				throw new InvalidEventFormatException(
                        String.format("unable to parse as Caliper: %s", json), e);
			}
		}
		
		try {
			TierTwoStorage<OpenLRSEntity> tierTwoStorage = storageFactory.getTierTwoStorage();
			tierTwoStorage.save(entity);
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
		}
				
	}

}
