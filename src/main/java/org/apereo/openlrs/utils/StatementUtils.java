package org.apereo.openlrs.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.model.statement.LRSActor;
import org.apereo.openlrs.model.statement.LRSContext;
import org.apereo.openlrs.model.statement.LRSObject;
import org.apereo.openlrs.model.statement.LRSResult;
import org.apereo.openlrs.model.statement.LRSVerb;

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
        for (Enum<LRSActor.InverseFunctionalIdentifiers> e : LRSActor.InverseFunctionalIdentifiers.values()) {
            if (actorMap.containsKey(e.toString())) {
                actorValid = StringUtils.isNotBlank(actorMap.get(e.toString()));
            }
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

    /**
     * Prepare the statement object from a map of the statement properties
     * 
     * @param statementProperties hashmap of the properties
     * @return the Statement object
     */
    @SuppressWarnings("unchecked")
    public static Statement prepareStatement(Map<String, Object> statementProperties) {
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
