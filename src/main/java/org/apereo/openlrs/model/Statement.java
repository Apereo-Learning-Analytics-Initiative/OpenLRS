/**
 * Copyright 2014 Unicon (R) Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package org.apereo.openlrs.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apereo.openlrs.model.statement.LRSActor;
import org.apereo.openlrs.model.statement.LRSObject;
import org.apereo.openlrs.model.statement.LRSResult;
import org.apereo.openlrs.model.statement.LRSVerb;

/**
 * The statement model represents all the available properties of a learning event
 * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#stmtprops
 * 
 * @author Robert E. Long (rlong @ unicon.net)
 */
public class Statement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * if true then this LRS_Statement is populated with all required fields (actor, verb, and object),
     * if false then check the raw data fields instead: {@link #rawMap} first and if null or empty then {@link #rawJSON},
     * it should be impossible for object to have none of these fields populated
     */
    private boolean populated = false;

    /**
     * A raw map of the keys and values which should be able to basically be converted directly into a JSON statement,
     * MUST contain at least actor, verb, and object keys and the values for those cannot be null or empty
     */
    private Map<String, Object> rawMap;

    /**
     * The raw JSON string to send as a statement
     * WARNING: this will not be validated
     */
    private String rawJSON;

    /**
     * UUID
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#stmtid
     * 
     * Recommended
     */
    private String id;

    /**
     * An agent (an individual) is a persona or system
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#actor
     * 
     * Required
     */
    private LRSActor actor;

    /**
     * Action between actor and activity
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#verb
     * 
     * Required
     */
    private LRSVerb verb;

    /**
     * an Activity, Agent/Group, Sub-Statement, or Statement Reference. It is the "this" part of the Statement, i.e. "I did this"
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#object
     * 
     * Required
     */
    private LRSObject object;

    /**
     * optional field that represents a measured outcome related to the Statement in which it is included
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#result
     * 
     * Optional
     */
    private LRSResult result;

    /**
     * optional field that provides a place to add contextual information to a Statement. All properties are optional
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#context
     * 
     * Optional
     */
    private String context;

    /**
     *  time at which the experience occurred
     *  see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#timestamp
     *  
     *  Optional
     */
    private String timestamp;

    /**
     * time at which a Statement is stored by the LRS
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#stored
     * 
     * Set by the LRS
     */
    private String stored;

    /**
     * provides information about whom or what has asserted that this Statement is true
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#authority
     * 
     * Optional
     */
    private String authority;

    /**
     * information in Statements helps systems that process data from an LRS get their bearings. Since the Statement data model 
     * is guaranteed consistent through all 1.0.x versions, in order to support data flow among such LRSs the LRS is given some 
     * flexibility on Statement versions that are accepted
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#version
     * 
     * Not recommended to be set
     */
    private String version;

    /**
     * Object array of digital artifacts providing evidence of a learning experience
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#attachments
     */
    private Object[] attachments;

    /**
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#dataconstraints
     */
    private String dataConstraints;

    /**
     * Default constructor with no properties set
     * 
     * If used, the following properties must be set at a minimum: actor, verb, object
     */
    public Statement() {
    }

    /**
     * Constructor using all required and recommended optional properties
     * @param id
     * @param actor
     * @param verb
     * @param object
     * @param result
     * @param context
     * @param timestamp
     * @param version
     * @param attachments
     */
    public Statement(
            String id,
            LRSActor actor,
            LRSVerb verb,
            LRSObject object,
            LRSResult result,
            String context,
            String timestamp,
            String version,
            Object[] attachments) {
        this();
        if (actor == null) {
            throw new IllegalArgumentException("LRSActor cannot be null");
        }
        if (verb == null) {
            throw new IllegalArgumentException("LRSVerb cannot be null");
        }
        if (object == null) {
            throw new IllegalArgumentException("LRSObject cannot be null");
        }

        this.id = id;
        this.actor = actor;
        this.verb = verb;
        this.object = object;
        this.result = result;
        this.context = context;
        this.timestamp = timestamp;
        this.version = version;
        this.attachments = attachments;
        this.populated = true;
    }

    /**
     * Constructor using only required properties, plus the UUID
     * @param id
     * @param actor
     * @param verb
     * @param object
     */
    public Statement(String id, LRSActor actor, LRSVerb verb, LRSObject object) {
        this();
        if (actor == null) {
            throw new IllegalArgumentException("LRSActor cannot be null");
        }
        if (verb == null) {
            throw new IllegalArgumentException("LRSVerb cannot be null");
        }
        if (object == null) {
            throw new IllegalArgumentException("LRSObject cannot be null");
        }

        this.id = id;
        this.actor = actor;
        this.verb = verb;
        this.object = object;
        this.populated = true;
    }

    /**
     * Constructor using only required properties, without the UUID
     * @param actor
     * @param verb
     * @param object
     */
    public Statement(LRSActor actor, LRSVerb verb, LRSObject object) {
        this();
        if (actor == null) {
            throw new IllegalArgumentException("LRSActor cannot be null");
        }
        if (verb == null) {
            throw new IllegalArgumentException("LRSVerb cannot be null");
        }
        if (object == null) {
            throw new IllegalArgumentException("LRSObject cannot be null");
        }

        this.actor = actor;
        this.verb = verb;
        this.object = object;
        this.populated = true;
    }

    /**
     * Construct a simple LRS statement
     * 
     * @param actorEmail the user email address, "I"
     * @param verbStr a string indicating the action, "did"
     * @param objectURI URI indicating the object of the statement, "this"
     */
    public Statement(String actorEmail, String verbStr, String objectURI) {
        this(new LRSActor(actorEmail), new LRSVerb(verbStr), new LRSObject(objectURI));
    }

    /**
     * Construct a simple LRS statement with Result
     * 
     * @param actorEmail the user email address, "I"
     * @param verbStr a string indicating the action, "did"
     * @param objectURI URI indicating the object of the statement, "this"
     * @param resultSuccess true if the result was successful (pass) or false if not (fail), "well"
     * @param resultScaledScore Score from -1.0 to 1.0 where 0=0% and 1.0=100%
     */
    public Statement(String actorEmail, String verbStr, String objectURI, boolean resultSuccess, float resultScaledScore) {
        this(new LRSActor(actorEmail), new LRSVerb(verbStr), new LRSObject(objectURI));
        this.result = new LRSResult(resultScaledScore, resultSuccess);
    }
    /**
     * EXPERT USE ONLY
     * @param rawData map of the keys and values which MUST contain at least actor, verb, and object keys and the values for those cannot be null or empty
     * @throws IllegalArgumentException if any required keys are missing
     * @see #rawMap
     */
    public Statement(Map<String, Object> rawData) {
        this();
        this.populated = false;
        this.rawMap = rawData;
        if (rawData != null) {
            if (!rawData.containsKey("actor") || rawData.get("actor") == null) {
                throw new IllegalArgumentException("actor key MUST be set and NOT null");
            }
            if (!rawData.containsKey("verb") || rawData.get("verb") == null) {
                throw new IllegalArgumentException("verb key MUST be set and NOT null");
            }
            if (!rawData.containsKey("object") || rawData.get("object") == null) {
                throw new IllegalArgumentException("object key MUST be set and NOT null");
            }
            this.rawMap = new LinkedHashMap<String, Object>(rawData);
            this.rawJSON = null;
        }
    }
    /**
     * INTERNAL USE ONLY
     * Probably will not work for anything that is NOT the Experience API
     * @param rawJSON JSON string to send as a statement
     *          WARNING: this will NOT be validated!
     * @see #rawJSON
     */
    public Statement(String rawJSON) {
        this();
        this.populated = false;
        this.rawJSON = rawJSON;
        if (rawJSON != null) {
            this.rawMap = null;
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LRSActor getActor() {
        return actor;
    }

    public void setActor(LRSActor actor) {
        this.actor = actor;
    }

    public LRSVerb getVerb() {
        return verb;
    }

    public void setVerb(LRSVerb verb) {
        this.verb = verb;
    }

    public LRSObject getObject() {
        return object;
    }

    public void setObject(LRSObject object) {
        this.object = object;
    }

    public LRSResult getResult() {
        return result;
    }

    public void setResult(LRSResult result) {
        this.result = result;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStored() {
        return stored;
    }

    public void setStored(String stored) {
        this.stored = stored;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object[] getAttachments() {
        return attachments;
    }

    public void setAttachments(Object[] attachments) {
        this.attachments = attachments;
    }

    public String getDataConstraints() {
        return dataConstraints;
    }

    public void setDataConstraints(String dataConstraints) {
        this.dataConstraints = dataConstraints;
    }

    public boolean isPopulated() {
        return populated;
    }

    public void setPopulated(boolean populated) {
        this.populated = populated;
    }

    public Map<String, Object> getRawMap() {
        return rawMap;
    }

    public void setRawMap(Map<String, Object> rawMap) {
        this.rawMap = rawMap;
    }

    public String getRawJSON() {
        return rawJSON;
    }

    public void setRawJSON(String rawJSON) {
        this.rawJSON = rawJSON;
    }

}
