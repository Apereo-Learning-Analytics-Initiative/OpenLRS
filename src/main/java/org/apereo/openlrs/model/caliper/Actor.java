
package org.apereo.openlrs.model.caliper;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "@id",
    "@type",
    "name",
    "lastModifiedTime"
})
public class Actor {

    @JsonProperty("@id")
    private String Id;
    @JsonProperty("@type")
    private String Type;
    @JsonProperty("name")
    private Object name;
    @JsonProperty("lastModifiedTime")
    private String lastModifiedTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The Id
     */
    @JsonProperty("@id")
    public String getId() {
        return Id;
    }

    /**
     * 
     * @param Id
     *     The @id
     */
    @JsonProperty("@id")
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     * 
     * @return
     *     The Type
     */
    @JsonProperty("@type")
    public String getType() {
        return Type;
    }

    /**
     * 
     * @param Type
     *     The @type
     */
    @JsonProperty("@type")
    public void setType(String Type) {
        this.Type = Type;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public Object getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(Object name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The lastModifiedTime
     */
    @JsonProperty("lastModifiedTime")
    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    /**
     * 
     * @param lastModifiedTime
     *     The lastModifiedTime
     */
    @JsonProperty("lastModifiedTime")
    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
