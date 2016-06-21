/**
 * 
 */
package org.apereo.openlrs.storage.mongo;

import java.io.Serializable;

import javax.persistence.Id;

import org.apereo.openlrs.model.event.Event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ggilbert
 *
 */
public class EventMongo implements Serializable {

  private static final long serialVersionUID = -5914344907583651451L;
  @Id private String id;
  private String tenantId;
  private Event event;
  
  @JsonCreator
  public EventMongo(
      @JsonProperty("tenantId") String tenantId, 
      @JsonProperty("event") Event event) {
    super();
    this.tenantId = tenantId;
    this.event = event;
  }

  public String getId() {
    return id;
  }

  public String getTenantId() {
    return tenantId;
  }

  public Event getEvent() {
    return event;
  }

}
