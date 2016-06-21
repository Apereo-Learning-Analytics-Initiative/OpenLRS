/**
 * 
 */
package org.apereo.openlrs.model.event;

import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;

/**
 * @author ggilbert
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Generated extends BaseEventComponent {
  
  private Actor actor;
  @JsonSerialize(using=DateTimeSerializer.class)
  private DateTime startedAtTime;
  @JsonSerialize(using=DateTimeSerializer.class)
  private DateTime endedAtTime;
  private Duration duration;
  
  @JsonCreator
  public Generated (@JsonProperty("@id") String id,
      @JsonProperty("@context") String context,
      @JsonProperty("@type") String type,
      @JsonProperty("name") String name,
      @JsonProperty("description") String description,
      @JsonProperty("extensions") Map<String,String> extensions,
      @JsonProperty("actor") Actor actor,
      @JsonProperty("startedAtTime") DateTime startedAtTime,
      @JsonProperty("endedAtTime") DateTime endedAtTime,
      @JsonProperty("duration") Duration duration) {
    this.id = id;
    this.context = context;
    this.type = type;
    this.name = name;
    this.description = description;
    this.extensions = extensions;
    this.actor = actor;
    this.startedAtTime = startedAtTime;
    this.endedAtTime = endedAtTime;
    this.duration = duration;
  }

  public Actor getActor() {
    return actor;
  }

  public DateTime getStartedAtTime() {
    return startedAtTime;
  }

  public DateTime getEndedAtTime() {
    return endedAtTime;
  }

  public Duration getDuration() {
    return duration;
  }

  @Override
  public String toString() {
    return "Generated [actor=" + actor + ", startedAtTime=" + startedAtTime + ", endedAtTime=" + endedAtTime + ", duration=" + duration + ", id="
        + id + ", context=" + context + ", type=" + type + ", name=" + name + ", description=" + description + ", extensions=" + extensions + "]";
  }


}
