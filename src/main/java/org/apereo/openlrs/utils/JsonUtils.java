package org.apereo.openlrs.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;


public class JsonUtils {

    /**
     * Creates a map from the given JSON string
     * 
     * @param jsonString the string representing a object
     * @return a Map representing the objects contained in the given JSON string
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseJsonStringToMap(String jsonString) {
        assert !StringUtils.isEmpty(jsonString);

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Gson gson = new Gson();
        jsonMap = gson.fromJson(jsonString, Map.class);

        return jsonMap;
    }

    /**
     * Creates a JSON string from the given map
     * 
     * @param jsonMap the map holding the objects to be converted to JSON the JSON string representing the map
     * @return
     */
    public static String parseJsonMapToString(Map<String, ?> jsonMap) {
        assert jsonMap != null;

        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonMap, Map.class);

        return jsonString;
    }

}
