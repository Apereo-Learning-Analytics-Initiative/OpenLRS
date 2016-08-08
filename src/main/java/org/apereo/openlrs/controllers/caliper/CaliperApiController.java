/**
 * 
 */
package org.apereo.openlrs.controllers.caliper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.KeyManager;
import org.apereo.openlrs.Tenant;
import org.apereo.openlrs.exceptions.InvalidRequestException;
import org.apereo.openlrs.exceptions.NotFoundException;
import org.apereo.openlrs.exceptions.caliper.InvalidCaliperFormatException;
import org.apereo.openlrs.model.event.Event;
import org.apereo.openlrs.model.event.EventEnvelope;
import org.apereo.openlrs.model.event.Stats;
import org.apereo.openlrs.storage.Reader;
import org.apereo.openlrs.storage.Writer;
import org.apereo.openlrs.utils.AuthorizationUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
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
        throws JsonProcessingException, IOException, InvalidCaliperFormatException, InvalidRequestException {
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
        throw new NotFoundException("Tenant not found");
      }
    }
    else {
      throw new InvalidRequestException("Tenant Key is required");
    }
    return ids;
  }
  
  @RequestMapping(value = { "", "/" },
      method = RequestMethod.GET,
      consumes = "application/json", produces = "application/json;charset=utf-8")
  public Page<Event> getHandler(@RequestHeader(value="Authorization") String authorizationHeader,
      @RequestParam(value="id",required=false) String eventId,
      @RequestParam(value = "page", required = false, defaultValue = "0") String page,
      @RequestParam(value = "limit", required = false, defaultValue = "1000") String limit,
      @RequestParam(value = "groupId", required = false) String course,
      @RequestParam(value = "actorId", required = false) String user)
        throws JsonProcessingException, IOException, InvalidCaliperFormatException, InvalidRequestException {
    Page<Event> events = null;
    String key = AuthorizationUtils.getKeyFromHeader(authorizationHeader);
    
    if (StringUtils.isNotBlank(key)) {
      Tenant tenant = keyManager.getTenantForKey(key);       

      if (tenant != null) {
        if (StringUtils.isNotBlank(eventId)) {
          Event event = reader.findByTenantIdAndEventId(String.valueOf(tenant.getId()), eventId);
          events = new PageImpl<>(Collections.singletonList(event), new PageRequest(Integer.valueOf(page), Integer.valueOf(limit)), 1);
        }
        else if (StringUtils.isNotBlank(course)) {
          events = reader.findByTenantIdAndContext(String.valueOf(tenant.getId()), course, new PageRequest(Integer.valueOf(page), Integer.valueOf(limit)));
        }
        else if (StringUtils.isNotBlank(user)) {
          events = reader.findByTenantIdAndUser(String.valueOf(tenant.getId()), user, new PageRequest(Integer.valueOf(page), Integer.valueOf(limit)));
        }
        else {
          events = reader.findByTenantId(String.valueOf(tenant.getId()), new PageRequest(Integer.valueOf(page), Integer.valueOf(limit)));
        }
      }
      else {
        throw new NotFoundException("Tenant not found");
      }
    }
    else {
      throw new InvalidRequestException("Tenant Key is required");
    }
    return events;
  }
  
  @RequestMapping(value = { "/stats" },
      method = RequestMethod.GET,
      consumes = "application/json", produces = "application/json;charset=utf-8")
  public Stats stats(@RequestHeader(value="Authorization") String authorizationHeader,
      @RequestParam(value = "groupId", required = true) String course)
        throws JsonProcessingException, IOException, InvalidCaliperFormatException, InvalidRequestException {
    Stats stats = new Stats();
    String key = AuthorizationUtils.getKeyFromHeader(authorizationHeader);
    
    if (StringUtils.isNotBlank(key)) {
      Tenant tenant = keyManager.getTenantForKey(key);       

      if (tenant != null) {
        
        Page<Event> events = reader.findByTenantIdAndContext(String.valueOf(tenant.getId()), course, new PageRequest(0, 100000));
        if (events != null) {
          List<Event> content = events.getContent();
          if (content != null) {
            stats.setTotal(content.size());
            
            Map<LocalDate,Long> totalsByDate = content.stream().collect(Collectors.groupingBy(event -> ((Event)event).getEventTime().toLocalDate(), Collectors.counting()));
            
            stats.setTotalByDate(totalsByDate);
          }
        }
        
      }
      else {
        throw new NotFoundException("Tenant not found");
      }
    }
    else {
      throw new InvalidRequestException("Tenant Key is required");
    }
    return stats;
  }
  


}
