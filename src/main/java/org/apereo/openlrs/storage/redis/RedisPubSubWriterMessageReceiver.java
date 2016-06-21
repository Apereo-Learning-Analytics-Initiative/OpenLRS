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

import java.io.IOException;

import org.apereo.openlrs.storage.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ggilbert
 * @author Lance E Sloan (lsloan at umich dot edu)
 */
@ConditionalOnProperty(name = "openlrs.writer", havingValue = "RedisPubSubWriter")
@Component
public class RedisPubSubWriterMessageReceiver {

  private Logger log = LoggerFactory.getLogger(RedisPubSubWriterMessageReceiver.class);
  
  @Autowired private Reader reader;
  @Autowired private ObjectMapper objectMapper;

  public void onMessage(String json) throws JsonParseException, JsonMappingException, IOException {

    if (log.isDebugEnabled()) {
      log.debug(json);
    }
    
    EventRedis event = objectMapper.readValue(json.getBytes(), EventRedis.class);
    reader.save(event.getEvent(),event.getTenantId());
  }
}
