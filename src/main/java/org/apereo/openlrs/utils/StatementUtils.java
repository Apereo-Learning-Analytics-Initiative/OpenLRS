package org.apereo.openlrs.utils;

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

}
