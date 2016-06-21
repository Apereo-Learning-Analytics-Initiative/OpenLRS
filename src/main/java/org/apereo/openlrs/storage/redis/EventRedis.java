/**
 * 
 */
package org.apereo.openlrs.storage.redis;

import java.io.Serializable;

import org.apereo.openlrs.model.event.Event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author ggilbert
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EventRedis implements Serializable {
  private static final long serialVersionUID = 1L;
  private String tenantId;
  private Event event;
  
  @JsonCreator
  public EventRedis(
      @JsonProperty("tenantId") String tenantId, 
      @JsonProperty("event") Event event) {
    super();
    this.tenantId = tenantId;
    this.event = event;
  }

  public String getTenantId() {
    return tenantId;
  }

  public Event getEvent() {
    return event;
  }
}
