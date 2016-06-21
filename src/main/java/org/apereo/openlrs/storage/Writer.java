/**
 * 
 */
package org.apereo.openlrs.storage;

import java.util.Collection;
import java.util.List;

import org.apereo.openlrs.model.event.Event;

/**
 * @author ggilbert
 *
 */
public interface Writer {
  Event save(Event event, String tenantId);
  List<Event> saveAll(Collection<Event> events, String tenantId);
}
