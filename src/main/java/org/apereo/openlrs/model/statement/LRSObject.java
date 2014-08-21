package org.apereo.openlrs.model.statement;

import java.util.Map;

/**
 * Holds a representation of a statement object
 *
 * @author Robert E. Long (rlong @ unicon.net)
 */
public class LRSObject {

    private static String XAPI_ACTIVITIES_PREFIX = "http://adlnet.gov/expapi/activities/";

    /*
     * NOTE: For our use, objectType will always be "Activity" and we will only use a limited set of the detail fields
     * 
     * The object of a statement is the Activity, Agent, or Statement that is the object of the statement, "this". 
     * Note that objects which are provided as a value for this field should include an "objectType" field. 
     * If not specified, the object is assumed to be an Activity.
     * 
     * An activity URI must always refer to a single unique activity. 
     * There may be corrections to that activity's definition. Spelling fixes would be appropriate, 
     * for example, but changing correct responses would not.
     * The activity URI is unique, and any reference to it always refers to the same activity. 
     * Activity Providers must ensure this is true and the LRS may not attempt to treat multiple references to 
     * the same URI as references to different activities, regardless of any information which indicates 
     * two authors or organizations may have used the same activity URI.
     */
    /**
     * URI. An activity URI must always refer to a single unique activity.
     * If a URL, the URL should refer to metadata for this activity
     * Example: http://example.adlnet.gov/xapi/example/simpleCBT
     */
    String id;

    /**
     * URI, the type of activity. (e.g. http://sakaiproject.org/expapi/activity/assessment)
     * Note, URI fragments (sometimes called relative URLs) are not valid URIs. 
     * Similar to verbs, we recommend that Learning Activity Providers look for and use established, widely adopted, activity types.
     */
    String activityType;

    /**
     * OPTIONAL: 
     * A language map containing the human readable display representation 
     * of the object in at least one language. This does not have any impact 
     * on the meaning of the statement, but only serves to give a human-readable display 
     * of the meaning already determined by the chosen verb.
     * Example: { "en-US" => "ran", "es" => "corrió" }
     */
    Map<String, String> activityName;

    /**
     * OPTIONAL:
     * A language map containing the human readable description of the Activity.
     * Example: { "en-US" => "User completed quiz 1" }
     */
    Map<String, String> descMap;
    
    /**
     * use of the empty constructor is restricted
     */
    protected LRSObject() {
    }

    /**
     * Create an LRS object
     * 
     * @param uri activity URI that refers to a single unique activity. 
     *      Example: http://example.adlnet.gov/xapi/example/simpleCBT
     */
    public LRSObject(String uri) {
        this();
        if (uri == null) {
            throw new IllegalArgumentException("LRSObject uri cannot be null");
        }
        id = uri;
    }

    /**
     * @param uri activity URI that refers to a single unique activity. (e.g. http://example.com/activity/spelling-test)
     * @param activityType activity URI that refers to the type of activity. (e.g. http://adlnet.gov/expapi/activities/assessment)
     */
    public LRSObject(String uri, String activityType) {
        this(uri);
        if (activityType == null) {
            throw new IllegalArgumentException("LRSObject type cannot be null");
        }
        this.activityType = (activityType.indexOf("://") == -1 ? XAPI_ACTIVITIES_PREFIX + activityType : activityType);
    }

    /**
     * @param activityType activity URI that refers to the type of activity. (e.g. assessment)
     */
    public void setActivityType(String type) {
        this.activityType = type;
    }

    /**
     * OPTIONAL:
     * A language map containing the human readable description of the Activity.
     * Example: { "en-US" => "User completed quiz 1" }
     */
    public void setDescription(Map<String, String> desc) {
        this.descMap = desc;
    }

    /**
     * OPTIONAL: 
     * A language map containing the human readable display representation 
     * of the object in at least one language. This does not have any impact 
     * on the meaning of the statement, but only serves to give a human-readable display 
     * of the meaning already determined by the chosen verb.
     * Example: { "en-US" => "ran", "es" => "corrió" }
     */
    public void setActivityName(Map<String, String> name) {
        this.activityName = name;
    }

    /**
     * @see #id
     */
    public String getId() {
        return id;
    }

    /**
     * @see #name
     */
    public Map<String, String> getActivityName() {
        return activityName;
    }

    /**
     * @see #type
     */
    public String getActivityType() {
        return activityType;
    }

    /**
     * @see #descMap
     */
    public Map<String,String> getDescription() {
        return descMap;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Object[id=" + id + ", activityType=" + activityType + "]";
    }

    /**
     * @see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#activity-id-requirements
     *
     * @author Robert E. Long (rlong @ unicon.net)
     */
    /*protected class Activity {
        private Map<String, String> name;
        private Map<String, String> description;
        private String type;
        private String moreInfo;
        private Map<String, Object> extensions;

        public Map<String, String> getName() {
            return name;
        }

        public void setName(Map<String, String> name) {
            this.name = name;
        }

        public Map<String, String> getDescription() {
            return description;
        }

        public void setDescription(Map<String, String> description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMoreInfo() {
            return moreInfo;
        }

        public void setMoreInfo(String moreInfo) {
            this.moreInfo = moreInfo;
        }

        public Map<String, java.lang.Object> getExtensions() {
            return extensions;
        }

        public void setExtensions(Map<String, Object> extensions) {
            this.extensions = extensions;
        }
    }*/

    /**
     * @see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#interaction-activities
     *
     * @author Robert E. Long (rlong @ unicon.net)
     */
    /*protected class InteractionActivity extends Activity {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    private void determineObjectType() {
        
    }*/

}
