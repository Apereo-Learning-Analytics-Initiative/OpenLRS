package org.apereo.openlrs.services;

import java.util.Map;

import org.apereo.openlrs.utils.JsonUtils;
import org.apereo.openlrs.utils.StatementUtils;
import org.springframework.stereotype.Service;

@Service
public class StatementService {

    /**
     * Get all the statements that exist in the database
     * 
     * @return JSON string of statement objects
     */
    public String getAllStatements() {
        return "{\"GET all statements\": true}";
    }

    /**
     * Gets a statement object for a given UUID
     * 
     * @param statementId the statement object ID
     * @return JSON string of the statement object
     */
    public String getStatement(String statementId) {
        return "{\"GET statementId\": \"" + statementId + "\"}";
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
