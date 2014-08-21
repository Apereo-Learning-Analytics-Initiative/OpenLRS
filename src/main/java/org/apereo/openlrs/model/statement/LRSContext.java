package org.apereo.openlrs.model.statement;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apereo.openlrs.Constants;

/**
 * Holds a representation of a statement context
 *
 * @author Robert E. Long (rlong @ unicon.net)
 */
public class LRSContext {

    /*
     * The context field provides a place to add some contextual information to a statement. 
     * We can add information such as the instructor for an experience, if this experience 
     * happened as part of a team activity, or how an experience fits into some broader activity.
     */
    /**
     * OPTIONAL
     * Instructor that the statement relates to, 
     * if not included as the actor or object of the overall statement.
     */
    LRSActor instructor;

    /**
     * OPTIONAL
     * Revision of the learning activity associated with this statement.
     * Revisions are to track fixes of minor issues (like a spelling error), 
     * if there is any substantive change to the learning objectives, pedagogy, 
     * or assets associated with an activity, a new activity ID should be used.
     * Revision format is up to the owner of the associated activity.
     */
    String revision;

    /**
     * A map of the types of context to learning activities “activity” this statement is related to.
     * Many Statements do not just involve one Object Activity that is the focus, but relate to other contextually relevant Activities. 
     * "Context Activities" allow for these related Activities to be represented in a structured manner.
     * Valid context types are: "parent", "grouping", "category", and "other".
     * For example, if I am studying a textbook, for a test, the textbook is the activity the statement is about, 
     * but the test is a context activity, and the context type is "other".
     * "other" : {"id" : "http://example.adlnet.gov/xapi/example/test"}
     * There could be an activity hierarchy to keep track of, for example question 1 on test 1 for the course Algebra 1. 
     * When recording results for question 1, it we can declare that the question is part of test 1, 
     * but also that it should be grouped with other statements about Algebra 1. This can be done using parent and grouping:
     * { 
     *   "parent" : {"id" : "http://example.adlnet.gov/xapi/example/test 1"}, 
     *   "grouping" : {"id" : "http://example.adlnet.gov/xapi/example/Algebra1"}
     * }
     */
    Map<String, Map<String, String>> activitiesMap;

    /**
     * Platform used in the experience of this learning activity.
     */
    String platform = "Sakai";

    // TODO include fields like team, platform, language, statement, and extensions

    /**
     * use of the empty constructor is restricted
     */
    protected LRSContext() {
    }

    /**
     * @param instructor Instructor user that the statement relates to
     */
    public LRSContext(LRSActor instructor) {
        this();
        if (instructor == null) {
            throw new IllegalArgumentException("LRSContext instructor cannot be null");
        }
        this.instructor = instructor;
    }

    /**
     * @param contextType must be "parent", "grouping", "category", and "other"
     * @param activityId a URI or key identifying the activity type (e.g. http://example.adlnet.gov/xapi/example/test)
     */
    public LRSContext(String contextType, String activityId) {
        this();
        setActivity(contextType, activityId);
    }

    /**
     * @param instructor Instructor user that the statement relates to
     */
    public void setInstructor(LRSActor instructor) {
        this.instructor = instructor;
    }

    /**
     * @param instructorEmail Instructor user email that the statement relates to
     */
    public void setInstructor(String instructorEmail) {
        this.instructor = new LRSActor(instructorEmail);
    }

    /**
     * @param contextType must be "parent", "grouping", and "other"
     * @param activityId a URI or key identifying the activity type (e.g. http://adlnet.gov/expapi/activities/test)
     */
    public void setActivity(String contextType, String activityId) {
        if (contextType == null || "".equals(contextType)) {
            throw new IllegalArgumentException("contextType MUST be set");
        }
        if (activityId == null || "".equals(activityId)) {
            throw new IllegalArgumentException("activityId MUST be set");
        }
        if (this.activitiesMap == null) {
            this.activitiesMap = new LinkedHashMap<String, Map<String, String>>();
        }
        if (!this.activitiesMap.containsKey(contextType) || this.activitiesMap.get(contextType) == null) {
            this.activitiesMap.put(contextType, new LinkedHashMap<String, String>());
        }
        activityId = (activityId.indexOf("://") == -1 ? Constants.XAPI_ACTIVITIES_PREFIX + activityId : activityId);
        this.activitiesMap.get(contextType).put("id", activityId);
    }

    /**
     * A map of the types of context to learning activities “activity” this statement is related to.
     * Valid context types are: "parent", "grouping", and "other".
     * For example, if I am studying a textbook, for a test, the textbook is the activity the statement is about, 
     * but the test is a context activity, and the context type is "other".
     * "other" : {"id" : "http://example.adlnet.gov/xapi/example/test"}
     * There could be an activity hierarchy to keep track of, for example question 1 on test 1 for the course Algebra 1. 
     * When recording results for question 1, it we can declare that the question is part of test 1, 
     * but also that it should be grouped with other statements about Algebra 1. This can be done using parent and grouping:
     * { 
     *   "parent" : {"id" : "http://example.adlnet.gov/xapi/example/test 1"}, 
     *   "grouping" : {"id" : "http://example.adlnet.gov/xapi/example/Algebra1"}
     * }
     * @param activitiesMap map where the values should be strings or other maps
     */
    public void setActivitiesMap(Map<String, Map<String, String>> activitiesMap) {
        this.activitiesMap = activitiesMap;
    }

    /**
     * @see #instructor
     */
    public LRSActor getInstructor() {
        return instructor;
    }

    /**
     * @see #revision
     */
    public String getRevision() {
        return revision;
    }

    /**
     * @see #activitiesMap
     */
    public Map<String, Map<String, String>> getActivitiesMap() {
        return activitiesMap;
    }

    /**
     * @see #revision
     */
    public String getPlatform() {
        return platform;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Context[instructor=" + instructor + ", rev=" + revision + ", activities=" + activitiesMap + "]";
    }

}
