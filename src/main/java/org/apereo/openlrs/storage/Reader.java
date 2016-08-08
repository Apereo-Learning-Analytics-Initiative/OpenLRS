/**
 * 
 */
package org.apereo.openlrs.storage;

import java.util.Collection;
import java.util.List;

import org.apereo.openlrs.model.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author ggilbert
 *
 */
public interface Reader {
  Page<Event> findByTenantId(String tenantId, Pageable pageable);
  Page<Event> findByTenantIdAndContext(String tenantId, String context, Pageable pageable);
  Page<Event> findByTenantIdAndUser(String tenantId, String user, Pageable pageable);
  Event findByTenantIdAndEventId(String tenantId, String eventId);
  Event save(Event event, String tenantId);
  List<Event> saveAll(Collection<Event> events, String tenantId);
}
