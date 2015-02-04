
package org.apereo.openlrs.model.caliper;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)

/**
 * TODO Document this file
 * @author steve cody scody@unicon.net
 */
public class Data {

    @JsonProperty("@context")
    private String Context;
    @JsonProperty("@type")
    private String Type;
    @JsonProperty("actor")
    private Actor actor;
    @JsonProperty("action")
    private String action;
    @JsonProperty("object")
    private Object object;
    @JsonProperty("target")
    private Target target;
    @JsonProperty("generated")
    private Object generated;
    @JsonProperty("startedAtTime")
    private String startedAtTime;
    @JsonProperty("endedAtTime")
    private Object endedAtTime;
    @JsonProperty("duration")
    private Object duration;
    @JsonProperty("edApp")
    private EdApp edApp;
    @JsonProperty("group")
    private Group group;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The Context
     */
    @JsonProperty("@context")
    public String getContext() {
        return Context;
    }

    /**
     * 
     * @param Context
     *     The @context
     */
    @JsonProperty("@context")
    public void setContext(String Context) {
        this.Context = Context;
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
     *     The actor
     */
    @JsonProperty("actor")
    public Actor getActor() {
        return actor;
    }

    /**
     * 
     * @param actor
     *     The actor
     */
    @JsonProperty("actor")
    public void setActor(Actor actor) {
        this.actor = actor;
    }

    /**
     * 
     * @return
     *     The action
     */
    @JsonProperty("action")
    public String getAction() {
        return action;
    }

    /**
     * 
     * @param action
     *     The action
     */
    @JsonProperty("action")
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * 
     * @return
     *     The object
     */
    @JsonProperty("object")
    public Object getObject() {
        return object;
    }

    /**
     * 
     * @param object
     *     The object
     */
    @JsonProperty("object")
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * 
     * @return
     *     The target
     */
    @JsonProperty("target")
    public Target getTarget() {
        return target;
    }

    /**
     * 
     * @param target
     *     The target
     */
    @JsonProperty("target")
    public void setTarget(Target target) {
        this.target = target;
    }

    /**
     * 
     * @return
     *     The generated
     */
    @JsonProperty("generated")
    public Object getGenerated() {
        return generated;
    }

    /**
     * 
     * @param generated
     *     The generated
     */
    @JsonProperty("generated")
    public void setGenerated(Object generated) {
        this.generated = generated;
    }

    /**
     * 
     * @return
     *     The startedAtTime
     */
    @JsonProperty("startedAtTime")
    public String getStartedAtTime() {
        return startedAtTime; 
    }

    /**
     * 
     * @param startedAtTime
     *     The startedAtTime
     */
    @JsonProperty("startedAtTime")
    public void setStartedAtTime(String startedAtTime) {
        this.startedAtTime = startedAtTime;
    }

    /**
     * 
     * @return
     *     The endedAtTime
     */
    @JsonProperty("endedAtTime")
    public Object getEndedAtTime() {
        return endedAtTime;
    }

    /**
     * 
     * @param endedAtTime
     *     The endedAtTime
     */
    @JsonProperty("endedAtTime")
    public void setEndedAtTime(Object endedAtTime) {
        this.endedAtTime = endedAtTime;
    }

    /**
     * 
     * @return
     *     The duration
     */
    @JsonProperty("duration")
    public Object getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The duration
     */
    @JsonProperty("duration")
    public void setDuration(Object duration) {
        this.duration = duration;
    }

    /**
     * 
     * @return
     *     The edApp
     */
    @JsonProperty("edApp")
    public EdApp getEdApp() {
        return edApp;
    }

    /**
     * 
     * @param edApp
     *     The edApp
     */
    @JsonProperty("edApp")
    public void setEdApp(EdApp edApp) {
        this.edApp = edApp;
    }

    /**
     * 
     * @return
     *     The group
     */
    @JsonProperty("group")
    public Group getGroup() {
        return group;
    }

    /**
     * 
     * @param group
     *     The group
     */
    @JsonProperty("group")
    public void setGroup(Group group) {
        this.group = group;
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
