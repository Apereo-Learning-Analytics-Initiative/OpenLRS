/**
 * 
 */
package org.apereo.openlrs.storage.elasticsearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
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
@ConditionalOnProperty(name="openlrs.reader", havingValue="ElasticsearchReader")
@Component("ElasticsearchReader")
public class ElasticsearchReader implements Reader {
  
  @Autowired private ElasticsearchEventRepository repository;

  @Override
  public Page<Event> findByTenantId(String tenantId, Pageable pageable) {
    Page<Event> page = null;
    Page<EventElasticsearch> wrappedPageOfEvents = repository.findByTenantId(tenantId,pageable);
    if (wrappedPageOfEvents != null && wrappedPageOfEvents.hasContent()) {
      List<Event> events = new LinkedList<>();
      for (EventElasticsearch ee : wrappedPageOfEvents.getContent()) {
        events.add(ee.getEvent());
      }
      page = new PageImpl<>(events);
    }
    
     return page;
  }

  @Override
  public Event findByTenantIdAndEventId(String tenantId, String eventId) {
    EventElasticsearch ee = repository.findByTenantIdAndEventId(tenantId, eventId);
    if (ee != null) {
      return ee.getEvent();
    }
    return null;
  }

  @Override
  public Event save(Event event, String tenantId) {
    if (StringUtils.isBlank(event.getId())) {
      event.setId(UUID.randomUUID().toString());
    }
    EventElasticsearch ee = new EventElasticsearch(tenantId, event);
    EventElasticsearch saved = repository.save(ee);
    return saved.getEvent();
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
