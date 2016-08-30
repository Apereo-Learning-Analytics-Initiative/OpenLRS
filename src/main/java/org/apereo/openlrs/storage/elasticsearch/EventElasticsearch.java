/**
 * 
 */
package org.apereo.openlrs.storage.elasticsearch;

import java.io.Serializable;


import javax.persistence.Id;

import org.apereo.openlrs.model.event.Event;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author ggilbert
 *
 */
@Document(indexName="openlrs_eventv2")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EventElasticsearch implements Serializable {
	
  private static final long serialVersionUID = 1L;
  
  @Id private String id;
  
  private String tenantId;
  private Event event;
  
  @JsonCreator
  public EventElasticsearch(
      @JsonProperty("tenantId") String tenantId, 
      @JsonProperty("event") Event event) {
    super();
    this.tenantId = tenantId;
    this.event = event;
  }
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public void setEvent(Event event) {
    this.event = event;
  }

  public String getTenantId() {
    return tenantId;
  }

  public Event getEvent() {
    return event;
  }
  
}
