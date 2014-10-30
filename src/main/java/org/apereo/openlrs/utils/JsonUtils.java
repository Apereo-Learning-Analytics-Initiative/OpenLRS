package org.apereo.openlrs.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtils {
	private static Logger log = Logger.getLogger(JsonUtils.class);
    /**
     * Creates a map from the given JSON string
     * 
     * @param jsonString the string representing a object
     * @return a Map representing the objects contained in the given JSON string
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseJsonStringToMap(String jsonString) {
        assert !StringUtils.isEmpty(jsonString);

       ObjectMapper om = new ObjectMapper();
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};

        HashMap<String, Object> o = null;
		try {
			o = om.readValue(jsonString, typeRef);
		} catch (IOException e) {
			log.error(e.getMessage(), e); 
		} 

        return o;
    }

    /**
     * Creates a JSON string from the given map
     * 
     * @param jsonMap the map holding the objects to be converted to JSON the JSON string representing the map
     * @return
     */
    public static String parseJsonMapToString(Map<String, ?> jsonMap) {
        assert jsonMap != null;

    	ObjectMapper om = new ObjectMapper();
    	String rawJson = null;
    	try {
			rawJson = om.writer().writeValueAsString(jsonMap);
		} 
    	catch (JsonProcessingException e) {
			log.error(e.getMessage(), e); 
		}
		return rawJson;
    }

}
