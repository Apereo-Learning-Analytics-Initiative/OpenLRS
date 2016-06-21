/**
 * 
 */
package org.apereo.openlrs.storage.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.model.event.Event;
import org.apereo.openlrs.storage.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 *
 */
@ConditionalOnProperty(name="openlrs.writer", havingValue="MongoWriter")
@Component("MongoWriter")
public class MongoWriter implements Writer {
  
  @Autowired private MongoEventRepository mongoEventRepository;

  @Override
  public Event save(Event event, String tenantId) {
    if (StringUtils.isBlank(event.getId())) {
      event.setId(UUID.randomUUID().toString());
    }
    EventMongo eventMongo = new EventMongo(tenantId, event);
    EventMongo savedEventMongo = mongoEventRepository.save(eventMongo);
    return savedEventMongo.getEvent();
  }

  @Override
  public List<Event> saveAll(Collection<Event> events, String tenantId) {
    List<Event> savedEvents = null;
    
    if (events != null) {
      savedEvents =new ArrayList<>();
      for (Event e : events) {
        savedEvents.add(this.save(e, tenantId));
      }
    }
    return savedEvents;
  }

}
