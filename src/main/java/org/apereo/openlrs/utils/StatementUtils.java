package org.apereo.openlrs.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

public class StatementUtils {

    /**
     * Verifies the statement has all required properties:
     * Actor
     * Verb
     * Object
     * 
     * @param propertyMap the map containing the statement properties
     * @return true, if all required properties are present
     */
    public static boolean hasAllRequiredProperties(String jsonProperties) {
        if (StringUtils.isEmpty(jsonProperties)) {
            return false;
        }

        Map<String, Object> propertyMap = JsonUtils.parseJsonStringToMap(jsonProperties);

        return hasAllRequiredProperties(propertyMap);
    }

    /**
     * Verifies the statement has all required properties:
     * Actor
     * Verb
     * Object
     * 
     * @param propertyMap the map containing the statement properties
     * @return true, if all required properties are present
     */
    public static boolean hasAllRequiredProperties(Map<String, Object> propertyMap) {
        if (MapUtils.isEmpty(propertyMap)) {
            return false;
        }

        if (!propertyMap.containsKey("actor")) {
            return false;
        }

        if (!propertyMap.containsKey("verb")) {
            return false;
        }

        if (!propertyMap.containsKey("object")) {
            return false;
        }

        return true;
    }

    /**
     * Creates a map of the statement filter criteria. If a value is not set, it is not added to the map/
     * 
     * @param statementId the UUID of the statement
     * @param actor the ID of the actor
     * @param activity the activity
     * @return the HashMap containing the filter criteria
     */
    public static Map<String, String> createStatementFilterMap(String statementId, String actor, String activity) {
        Map<String, String> filterMap = new HashMap<String, String>(3);

        // statementId
        if (!StringUtils.isEmpty(statementId)) {
            filterMap.put("statementId", statementId);
        }

        // actor
        if (!StringUtils.isEmpty(actor)) {
            filterMap.put("actor", actor);
        }

        // activity
        if (!StringUtils.isEmpty(activity)) {
            filterMap.put("activity", activity);
        }

        return filterMap;
    }
}
