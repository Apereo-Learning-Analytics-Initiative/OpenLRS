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
import org.apereo.openlrs.storage.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 *
 */
@ConditionalOnProperty(name="openlrs.reader", havingValue="MongoReader")
@Component("MongoReader")
public class MongoReader implements Reader {
  
  @Autowired private MongoEventRepository mongoEventRepository;

  @Override
  public Page<Event> findByTenantId(String tenantId, Pageable pageable) {
    Page<Event> events = null;
    Page<EventMongo> eventMongos = mongoEventRepository.findByTenantId(tenantId, pageable);
    if (eventMongos != null && eventMongos.hasContent()) {
      List<Event> eventList = new ArrayList<>();
      for (EventMongo em : eventMongos.getContent()) {
        eventList.add(em.getEvent());
      }
      
      events = new PageImpl<>(eventList);
    }
    return events;
  }

  @Override
  public Page<Event> findByTenantIdAndContext(String tenantId, String context, Pageable pageable) {
    Page<Event> events = null;
    Page<EventMongo> eventMongos = mongoEventRepository.findByTenantIdAndEventGroupId(tenantId, context, pageable);
    if (eventMongos != null && eventMongos.hasContent()) {
      List<Event> eventList = new ArrayList<>();
      for (EventMongo em : eventMongos.getContent()) {
        eventList.add(em.getEvent());
      }
      
      events = new PageImpl<>(eventList);
    }
    return events;
  }

  @Override
  public Page<Event> findByTenantIdAndUser(String tenantId, String user, Pageable pageable) {
    Page<Event> events = null;
    Page<EventMongo> eventMongos = mongoEventRepository.findByTenantIdAndActorId(tenantId, user, pageable);
    if (eventMongos != null && eventMongos.hasContent()) {
      List<Event> eventList = new ArrayList<>();
      for (EventMongo em : eventMongos.getContent()) {
        eventList.add(em.getEvent());
      }
      
      events = new PageImpl<>(eventList);
    }
    return events;
  }

  @Override
  public Event findByTenantIdAndEventId(String tenantId, String eventId) {
    Event event = null;
    EventMongo eventMongo = mongoEventRepository.findByTenantIdAndEventId(tenantId, eventId);
    if (eventMongo != null) {
      event = eventMongo.getEvent();
    }
    return event;
  }

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
