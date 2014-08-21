package org.apereo.openlrs.services;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.model.statement.LRSActor;
import org.apereo.openlrs.model.statement.LRSContext;
import org.apereo.openlrs.model.statement.LRSObject;
import org.apereo.openlrs.model.statement.LRSResult;
import org.apereo.openlrs.model.statement.LRSVerb;
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

        // prepare the statement for storage
        Statement statement = prepareStatement(statementProperties);
        System.out.println(statement);

        return "{\"POST statement map\": " + JsonUtils.parseJsonMapToString(statementProperties) + "}";
    }

    /**
     * Prepare the statement object from a map of the statement properties
     * 
     * @param statementProperties hashmap of the properties
     * @return the Statement object
     */
    @SuppressWarnings("unchecked")
    private Statement prepareStatement(Map<String, Object> statementProperties) {
        assert MapUtils.isNotEmpty(statementProperties);

        // process the actor - REQUIRED
        Map<String, Object> actorMap = (Map<String, Object>) statementProperties.get("actor");
        assert MapUtils.isNotEmpty(actorMap);
        LRSActor actor = new LRSActor(); // default to Agent actor
        if (actorMap.get("objectType") != null) {
            actor = new LRSActor((String) actorMap.get("objectType"));
        }
        for (Enum<LRSActor.InverseFunctionalIdentifiers> e : LRSActor.InverseFunctionalIdentifiers.values()) {
            if (actorMap.containsKey(e.toString())) {
                actor.getObjectType().setInverseFunctionalIdentifier(e.toString(), (String) actorMap.get(e.toString()));
                break;
            }
        }

        // process the verb - REQUIRED
        Map<String, Object> verbMap = (Map<String, Object>) statementProperties.get("verb");
        assert MapUtils.isNotEmpty(verbMap);
        LRSVerb verb = new LRSVerb();
        if (verbMap.containsKey("id")) {
            verb.setId((String) verbMap.get("id"));
        }
        if (verbMap.containsKey("display")) {
            verb.setDisplay((Map<String, String>) verbMap.get("display"));
        }

        // process the object - REQUIRED
        Map<String, Object> objectMap = (Map<String, Object>) statementProperties.get("object");
        assert MapUtils.isNotEmpty(objectMap);
        // assume only activity object for now
        LRSObject object = new LRSObject(); // defult empty object
        if (objectMap.containsKey("id")) {
            object = new LRSObject((String) objectMap.get("id"));
        }
        if (objectMap.containsKey("definition")) {
            object.setDescription((Map<String, String>) objectMap.get("definition"));
        }

        // create the statement with the required properties
        Statement statement = new Statement(actor, verb, object);

        // process the result - OPTIONAL
        Map<String, Object> resultMap = (Map<String, Object>) statementProperties.get("result");
        if (MapUtils.isNotEmpty(resultMap)) {
            LRSResult result = new LRSResult();
            if (resultMap.containsKey("response")) {
                result.setResponse((String) resultMap.get("response"));
            }
            // TODO process grades, completion, etc.
            statement.setResult(result);
        }

        // TODO process the context - OPTIONAL
        Map<String, Object> contextMap = (Map<String, Object>) statementProperties.get("context");
        if (MapUtils.isNotEmpty(contextMap)) {
            LRSContext context = new LRSContext("", "");
            statement.setContext(context);
        }

        return statement;
    }
}
