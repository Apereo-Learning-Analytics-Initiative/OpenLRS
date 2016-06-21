/**
 * 
 */
package org.apereo.openlrs.controllers.caliper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.KeyManager;
import org.apereo.openlrs.Tenant;
import org.apereo.openlrs.exceptions.caliper.InvalidCaliperFormatException;
import org.apereo.openlrs.model.event.Event;
import org.apereo.openlrs.model.event.EventEnvelope;
import org.apereo.openlrs.storage.Reader;
import org.apereo.openlrs.storage.Writer;
import org.apereo.openlrs.utils.AuthorizationUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ggilbert
 *
 */
@RestController
@RequestMapping("/v1/caliper")
public class CaliperApiController {
  
  @Autowired private ObjectMapper objectMapper;
  @Autowired KeyManager keyManager;

  @Autowired private Writer writer;
  @Autowired private Reader reader;
  
  @RequestMapping(value = { "", "/" },
      method = RequestMethod.POST,
      consumes = "application/json", produces = "application/json;charset=utf-8")
  public List<String> postHandler(@RequestBody String json, @RequestHeader(value="Authorization") String authorizationHeader)
        throws JsonProcessingException, IOException, InvalidCaliperFormatException {
    List<String> ids = null;
    String key = AuthorizationUtils.getKeyFromHeader(authorizationHeader);
    
    if (StringUtils.isNotBlank(key)) {
      Tenant tenant = keyManager.getTenantForKey(key);       

      if (tenant != null) {
        EventEnvelope ee = objectMapper.readValue(json,
            new TypeReference<EventEnvelope>() {});
        
        if (ee != null) {
          List<Event> events = ee.getData();
          if (events != null && !events.isEmpty()) {
            ids = new ArrayList<String>();
            for (Event e : events) {
              if (StringUtils.isBlank(e.getId())) {
                e.setId(UUID.randomUUID().toString());
                e.setStoredTime(new DateTime(DateTimeZone.UTC));
              }
              ids.add(writer.save(e, String.valueOf(tenant.getId())).getId());
            }
          }
        }
      }
      else {
        // TODO exception
      }
    }
    else {
      // TODO exception
    }
    return ids;
  }
  
  @RequestMapping(value = { "", "/" },
      method = RequestMethod.GET,
      consumes = "application/json", produces = "application/json;charset=utf-8")
  public Page<Event> getHandler(@RequestHeader(value="Authorization") String authorizationHeader,
      @RequestParam(value="id",required=false) String eventId,
      @RequestParam(value = "page", required = false, defaultValue = "0") String page,
      @RequestParam(value = "limit", required = false, defaultValue = "1000") String limit)
        throws JsonProcessingException, IOException, InvalidCaliperFormatException {
    Page<Event> events = null;
    String key = AuthorizationUtils.getKeyFromHeader(authorizationHeader);
    
    if (StringUtils.isNotBlank(key)) {
      Tenant tenant = keyManager.getTenantForKey(key);       

      if (tenant != null) {
        if (StringUtils.isNotBlank(eventId)) {
          Event event = reader.findByTenantIdAndEventId(String.valueOf(tenant.getId()), eventId);
          events = new PageImpl<>(Collections.singletonList(event), new PageRequest(Integer.valueOf(page), Integer.valueOf(limit)), 1);
        }
        else {
          events = reader.findByTenantId(String.valueOf(tenant.getId()), new PageRequest(Integer.valueOf(page), Integer.valueOf(limit)));
        }
      }
      else {
        // TODO exception
      }
    }
    else {
      // TODO exception
    }
    return events;
  }


}
