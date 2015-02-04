
package org.apereo.openlrs.model.caliper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "objectType",
    "alignedLearningObjective",
    "keyword",
    "partOf",
    "lastModifiedTime"
})
public class Target {

    @JsonProperty("@id")
    private String Id;
    @JsonProperty("@type")
    private String Type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("objectType")
    private List<Object> objectType = new ArrayList<Object>();
    @JsonProperty("alignedLearningObjective")
    private List<Object> alignedLearningObjective = new ArrayList<Object>();
    @JsonProperty("keyword")
    private List<Object> keyword = new ArrayList<Object>();
    @JsonProperty("partOf")
    private PartOf partOf;
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
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The objectType
     */
    @JsonProperty("objectType")
    public List<Object> getObjectType() {
        return objectType;
    }

    /**
     * 
     * @param objectType
     *     The objectType
     */
    @JsonProperty("objectType")
    public void setObjectType(List<Object> objectType) {
        this.objectType = objectType;
    }

    /**
     * 
     * @return
     *     The alignedLearningObjective
     */
    @JsonProperty("alignedLearningObjective")
    public List<Object> getAlignedLearningObjective() {
        return alignedLearningObjective;
    }

    /**
     * 
     * @param alignedLearningObjective
     *     The alignedLearningObjective
     */
    @JsonProperty("alignedLearningObjective")
    public void setAlignedLearningObjective(List<Object> alignedLearningObjective) {
        this.alignedLearningObjective = alignedLearningObjective;
    }

    /**
     * 
     * @return
     *     The keyword
     */
    @JsonProperty("keyword")
    public List<Object> getKeyword() {
        return keyword;
    }

    /**
     * 
     * @param keyword
     *     The keyword
     */
    @JsonProperty("keyword")
    public void setKeyword(List<Object> keyword) {
        this.keyword = keyword;
    }

    /**
     * 
     * @return
     *     The partOf
     */
    @JsonProperty("partOf")
    public PartOf getPartOf() {
        return partOf;
    }

    /**
     * 
     * @param partOf
     *     The partOf
     */
    @JsonProperty("partOf")
    public void setPartOf(PartOf partOf) {
        this.partOf = partOf;
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
