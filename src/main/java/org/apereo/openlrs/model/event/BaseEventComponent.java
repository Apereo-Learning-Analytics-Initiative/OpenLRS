/**
 * 
 */
package org.apereo.openlrs.model.event;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;

/**
 * @author ggilbert
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class BaseEventComponent implements EventComponent {
  
  @JsonProperty("@id")
  @SerializedName("@id")
  protected String id;
  
  @JsonProperty("@context")
  @SerializedName("@context")
  protected String context;
  
  @JsonProperty("@type")
  @SerializedName("@type")
  protected String type;
  
  protected String name;
  protected String description;
  protected Map<String, String> extensions;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getContext() {
    return context;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Map<String, String> getExtensions() {
    if (extensions == null) {
      extensions = new HashMap<>();
    }
    return extensions;
  }

}
