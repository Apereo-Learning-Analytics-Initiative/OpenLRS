/**
 * Copyright 2014 Unicon (R) Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package org.apereo.openlrs.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apereo.openlrs.KeyManager;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author scody
 *
 */
public class AuthorizationUtils {
	private static Logger log = Logger.getLogger(AuthorizationUtils.class);
	
	@Autowired KeyManager keyManager;
	
    public static String getKeyFromHeader(String authorizationHeader) {
    			
		if (log.isDebugEnabled()) {
			log.debug(String.format("Authorization Header: %s", authorizationHeader));
		}
		
		if (StringUtils.isNotBlank(authorizationHeader)) {
			if (StringUtils.containsIgnoreCase(authorizationHeader, "oauth")) {
				return getKeyOauth(authorizationHeader);
			}
			else {
				return getKeyBasic(authorizationHeader);
			}
		}
		throw new Error("Couldn't retrieve key from header: " + authorizationHeader);
    }
    
    

	private static String getKeyOauth(String authorizationHeader) {
		Map<String,String> oauth_parameters = OAuthUtils.decodeAuthorization(authorizationHeader);
		
		if (oauth_parameters != null && oauth_parameters.containsKey("oauth_consumer_key")) {
			final String oauth_consumer_key = oauth_parameters.get("oauth_consumer_key");
			return oauth_consumer_key;
		}
		else {
			throw new Error("Couldn't retrieve key from header: " + authorizationHeader);
		}		
	}
		
	
	
	private static String getKeyBasic(String authorizationHeader) {
		StringTokenizer st = new StringTokenizer(authorizationHeader);
		if (st.hasMoreTokens()) {
	        String basic = st.nextToken();
	 
	        if (basic.equalsIgnoreCase("Basic") || basic.equalsIgnoreCase("Base64")) {
	        	
	        	try {
	        		String credentials = new String(Base64.decodeBase64(st.nextToken()), "UTF-8");

	        		int colon = credentials.indexOf(":");
	            
	        		if (colon != -1) {
	        			String _username = credentials.substring(0, colon).trim();
	        			return _username;
	        		} 
	        		else {
	        			return "";
	        		}
	        	} 
	        	catch (UnsupportedEncodingException e) {
	        		throw new Error("Couldn't retrieve key", e);
	        	}
	        }
	        else {
	        	throw new Error("Couldn't retrieve key from header: " + authorizationHeader);
	        }
		}
        else {
        	throw new Error("Couldn't retrieve key from header: " + authorizationHeader);
        }
	}
}
