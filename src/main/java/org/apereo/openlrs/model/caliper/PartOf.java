
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
    "semester",
    "courseNumber",
    "label",
    "name",
    "parentOrg",
    "lastModifiedTime"
})
public class PartOf {

    @JsonProperty("@id")
    private String Id;
    @JsonProperty("@type")
    private String Type;
    @JsonProperty("semester")
    private String semester;
    @JsonProperty("courseNumber")
    private String courseNumber;
    @JsonProperty("label")
    private String label;
    @JsonProperty("name")
    private String name;
    @JsonProperty("parentOrg")
    private Object parentOrg;
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
     *     The semester
     */
    @JsonProperty("semester")
    public String getSemester() {
        return semester;
    }

    /**
     * 
     * @param semester
     *     The semester
     */
    @JsonProperty("semester")
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /**
     * 
     * @return
     *     The courseNumber
     */
    @JsonProperty("courseNumber")
    public String getCourseNumber() {
        return courseNumber;
    }

    /**
     * 
     * @param courseNumber
     *     The courseNumber
     */
    @JsonProperty("courseNumber")
    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    /**
     * 
     * @return
     *     The label
     */
    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    /**
     * 
     * @param label
     *     The label
     */
    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
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
     *     The parentOrg
     */
    @JsonProperty("parentOrg")
    public Object getParentOrg() {
        return parentOrg;
    }

    /**
     * 
     * @param parentOrg
     *     The parentOrg
     */
    @JsonProperty("parentOrg")
    public void setParentOrg(Object parentOrg) {
        this.parentOrg = parentOrg;
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
