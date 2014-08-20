package org.apereo.openlrs.model.statement;

import org.apache.commons.lang3.StringUtils;

/**
 * Holds a representation of a statement actor
 *
 * @author Robert E. Long (rlong @ unicon.net)
 */
public class Actor {

    private BaseActorObjectType objectType;

    public enum InverseFunctionalIdentifiers {
        /**
         * The required format is "mailto:email address". Only email addresses that have only ever been and 
         * will ever be assigned to this Agent, but no others, should be used for this property
         */
        mbox,
        /**
         * The SHA1 hash of a mailto IRI (i.e. the value of an mbox property). An LRS MAY include Agents with 
         * a matching hash when a request is based on an mbox.
         */
        mbox_sha1sum,
        /**
         * An openID that uniquely identifies the Agent.
         */
        openid,
        /**
         * A user account on an existing system e.g. an LMS or intranet.
         * If used, you must supply a hashmap with a "homepage" and "name" as string keys
         */
        account
    }

    /**
     * Default constructor
     * Creates an Agent Actor
     */
    public Actor() {
        this(null, null);
    }

    public Actor(String objectType) {
        this(objectType, null);
    }

    /**
     * Create basic Actor object
     * 
     * @param objectType
     * @param name
     */
    public Actor(String objectType, String name) {
        this(objectType, name, null, "");
    }

    /**
     * Create a non-account actor object
     * 
     * @param objectType
     * @param name
     * @param inverseFunctionalIdentifier
     * @param ifiValue
     */
    public Actor(String objectType, String name, String inverseFunctionalIdentifier, String ifiValue) {
        determineObjectType(objectType);
        this.objectType.setName(name);
        if (StringUtils.isNotEmpty(inverseFunctionalIdentifier)) {
            this.objectType.setInverseFunctionalIdentifier(inverseFunctionalIdentifier, ifiValue);
        }
    }

    /**
     * Create an account actor object
     * 
     * @param objectType
     * @param name
     * @param inverseFunctionalIdentifier
     * @param ifiValue
     */
    /*public Actor(String objectType, String name, String inverseFunctionalIdentifier, Map<String, String> ifiValue) {
        determineObjectType(objectType);
        this.objectType.setName(name);
        if (StringUtils.isNotEmpty(inverseFunctionalIdentifier)) {
            this.objectType.setInverseFunctionalIdentifier(inverseFunctionalIdentifier, ifiValue);
        }
    }*/

    /**
     * A user account on an existing system, such as a private system (LMS or intranet) or a public system (social networking site).
     * 
     * @author Robert E. Long (rlong @ unicon.net)
     */
    protected class Account {

        private String homepage;
        private String name;

        public Account(String homepage, String name) {
            this.homepage = homepage;
            this.name = name;
        }

        public String getHomepage() {
            return homepage;
        }
        public void setHomepage(String homepage) {
            this.homepage = homepage;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

    }

    /**
     * Determines whether the Actor is an Agent or Group
     * 
     * @param objectType the string representing the object type
     */
    private void determineObjectType(String objectType) {
        // default to Agent
        this.objectType = new Agent();

        if (StringUtils.equalsIgnoreCase(objectType, "Group")) {
            this.objectType = new Group();
        }
    }

    public BaseActorObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(BaseActorObjectType objectType) {
        this.objectType = objectType;
    }

}
