package org.apereo.openlrs.services;

import java.util.Map;

import org.apereo.openlrs.utils.JsonUtils;
import org.apereo.openlrs.utils.StatementUtils;
import org.springframework.stereotype.Service;

@Service
public class StatementService {

    /**
     * Gets statement objects for the specified criteria
     * 
     * @param filterMap a hashmap of the filtering criteria
     * @return JSON string of the statement objects
     */
    public String getStatement(Map<String, String> filterMap) {
        String json = JsonUtils.parseJsonMapToString(filterMap);

        return "{\"GET statement filter\": \"" + json + "\"}";
    }

    /**
     * Post a statement to the database
     * 
     * @param statementData JSON string containing the statement properties
     * @return JSON string of the response
     */
    public String postStatement(String statementData) {
        Map<String, Object> statementProperties = JsonUtils.parseJsonStringToMap(statementData);
        if (!StatementUtils.hasAllRequiredProperties(statementProperties)) {
            throw new IllegalArgumentException("Statement properties must contain all required properties.");
        }

        return "{\"POST statement map\": " + JsonUtils.parseJsonMapToString(statementProperties) + "}";
    }

}
