/*******************************************************************************
 * Copyright (c) 2015 Unicon (R) Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *******************************************************************************/
package org.apereo.openlrs.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

public class StatementUtils {
	
	public static final String ACTOR_FILTER = "actor";
	public static final String ACTIVITY_FILTER = "activity";
	public static final String SINCE_FILTER = "since";
	public static final String UNTIL_FILTER = "until";
	public static final String LIMIT_FILTER = "limit";

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
    @SuppressWarnings("unchecked")
    public static boolean hasAllRequiredProperties(Map<String, Object> propertyMap) {
        if (MapUtils.isEmpty(propertyMap)) {
            return false;
        }

        if (!propertyMap.containsKey("actor") || !propertyMap.containsKey("verb") || !propertyMap.containsKey("object")) {
            return false;
        }

        // Actor
        // check for the required Inverse Functional Identifier
        boolean actorValid = false;
        Map<String, String> actorMap = (Map<String, String>) propertyMap.get("actor");
        String mbox = actorMap.get("mbox");
        if (StringUtils.isNotBlank(mbox)) {
        	actorValid = true;
        }

        // Verb
        // check for the required ID
        boolean verbValid = false;
        Map<String, String> verbMap = (Map<String, String>) propertyMap.get("verb");
        if (verbMap.containsKey("id")) {
            verbValid = StringUtils.isNotBlank(verbMap.get("id"));
        }

        // Object
        // check for the required ID
        boolean objectValid = false;
        Map<String, String> objectMap = (Map<String, String>) propertyMap.get("object");
        if (objectMap.containsKey("id")) {
            objectValid = StringUtils.isNotBlank(objectMap.get("id"));
        }

        return actorValid && verbValid && objectValid;
    }

    /**
     * Creates a map of the statement filter criteria. If a value is not set, it is not added to the map/
     * 
     * @param statementId the UUID of the statement
     * @param actor the ID of the actor
     * @param activity the activity
     * @return the HashMap containing the filter criteria
     */
    public static Map<String, String> createStatementFilterMap(String actor, String activity, String since, String until, String limit) {
        Map<String, String> filterMap = new HashMap<String, String>(3);

       // actor
        if (!StringUtils.isEmpty(actor)) {
            filterMap.put(ACTOR_FILTER, actor);
        }

        // activity
        if (!StringUtils.isEmpty(activity)) {
            filterMap.put(ACTIVITY_FILTER, activity);
        }
        
        if (!StringUtils.isEmpty(since)) {
        	filterMap.put(SINCE_FILTER, since);
        }
        
        if (!StringUtils.isEmpty(until)) {
        	filterMap.put(UNTIL_FILTER, until);
        }
        
        if (!StringUtils.isEmpty(limit)) {
        	try{
    		  int number = Integer.parseInt(limit);
    		  if(number > 0)
    		  {
    			  filterMap.put(LIMIT_FILTER, limit);
    		  }
    		} catch (NumberFormatException e) {
    		  
    		}
        }

        return filterMap;
    }

}
