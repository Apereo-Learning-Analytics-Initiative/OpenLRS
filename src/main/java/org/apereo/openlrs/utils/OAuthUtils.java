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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;


/**
 * @author ggilbert
 *
 */
public class OAuthUtils {
	private static Logger log = Logger.getLogger(OAuthUtils.class);
	
	private static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("\\s*(\\w*)\\s+(.*)");
	private static final Pattern KEYVALUEPAIR_PATTERN = Pattern.compile("(\\S*)\\s*\\=\\s*\"([^\"]*)\"");
	
	private static final Map<String, String> algorithms;
	
	static {
		algorithms = new HashMap<String, String>();
		algorithms.put("HMAC-SHA1", "HmacSHA1");
	}
	
	public static Map<String, String> decodeAuthorization(String authorization) {
		Map<String, String> oauthParameters = new HashMap<String, String>();
		if (authorization != null) {
			Matcher m = AUTHORIZATION_PATTERN.matcher(authorization);
			if (m.matches()) {
				if ("oauth".equalsIgnoreCase(m.group(1))) {
					for (String keyValuePair : m.group(2).split("\\s*,\\s*")) {
						m = KEYVALUEPAIR_PATTERN.matcher(keyValuePair);
						if (m.matches()) {
							String key = OAuthUtils.decodePercent(m.group(1));
							String value = OAuthUtils.decodePercent(m.group(2));
							oauthParameters.put(key, value);
						}
					}
				}
			}
		}
		
		return oauthParameters;
	}
	
	public static String sign(String secret, Map<String, String> oauthParameters, String algorithm, String method,
			String url) {
		
		StringBuilder signatureBase = new StringBuilder(OAuthUtils.percentEncode(method));
		signatureBase.append("&");
		signatureBase.append(OAuthUtils.percentEncode(url));
		signatureBase.append("&");
		
		Map<String, String> treeMap = new TreeMap<String, String>(oauthParameters);
		treeMap.remove("oauth_signature");
		treeMap.remove("realm");
		
		boolean first = true;
		for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			if (!first)
				signatureBase.append(OAuthUtils.percentEncode("&"));
			else
				first = false;
			
			signatureBase.append(OAuthUtils.percentEncode(entry.getKey()+"="+entry.getValue()));
		}
		
		Mac mac = null;
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(
					(OAuthUtils.percentEncode(secret) + "&").getBytes(), algorithm);
					
			mac = Mac.getInstance(secretKeySpec.getAlgorithm());
			mac.init(secretKeySpec);		
			
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		if (log.isDebugEnabled()) {
			log.debug("signatureBaseString: " + signatureBase.toString());
		}
		
		byte[] bytes = mac.doFinal(signatureBase.toString().getBytes());
		byte[] encodedMacBytes = Base64.encodeBase64(bytes);

		return new String(encodedMacBytes);
	}
	
	public static final String mapToJava(String name) {
		String algorithm = algorithms.get(name);
		if (algorithm == null) {
			throw new UnsupportedOperationException("Signature algorithm of " + name + " is unsupported.");
		}
		return algorithm;		
	}

	private static String percentEncode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLEncoder.encode(s, "UTF-8")
                    .replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee.getMessage(), uee);
        }
	}
	
    private static String decodePercent(String s) {
    	try {
    		return URLDecoder.decode(s, "UTF-8");
    	} 
    	catch (java.io.UnsupportedEncodingException e) {
    		throw new RuntimeException(e.getMessage(), e);
    	}
    }

}
