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

/**
 * The statement model represents all the available properties of a learning event
 * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#stmtprops
 * 
 * @author Robert E. Long (rlong @ unicon.net)
 */
public class Statement implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private String actor;

    /**
     * Action between actor and activity
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#verb
     * 
     * Required
     */
    private String verb;

    /**
     * an Activity, Agent/Group, Sub-Statement, or Statement Reference. It is the "this" part of the Statement, i.e. "I did this"
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#object
     * 
     * Required
     */
    private String object;

    /**
     * optional field that represents a measured outcome related to the Statement in which it is included
     * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#result
     * 
     * Optional
     */
    private String result;

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
    public Statement(String id, String actor, String verb, String object, String result, String context, String timestamp,
            String version, Object[] attachments) {
        this.id = id;
        this.actor = actor;
        this.verb = verb;
        this.object = object;
        this.result = result;
        this.context = context;
        this.timestamp = timestamp;
        this.version = version;
        this.attachments = attachments;
    }

    /**
     * Constructor using only required properties, plus the UUID
     * @param id
     * @param actor
     * @param verb
     * @param object
     */
    public Statement(String id, String actor, String verb, String object) {
        this.id = id;
        this.actor = actor;
        this.verb = verb;
        this.object = object;
    }

    /**
     * Constructor using only required properties, without the UUID
     * @param actor
     * @param verb
     * @param object
     */
    public Statement(String actor, String verb, String object) {
        this.actor = actor;
        this.verb = verb;
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
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

}
