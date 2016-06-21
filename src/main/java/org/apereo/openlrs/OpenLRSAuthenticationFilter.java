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
package org.apereo.openlrs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apereo.openlrs.utils.OAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author ggilbert
 *
 */
@Component
public class OpenLRSAuthenticationFilter extends OncePerRequestFilter {
	
	private Logger log = Logger.getLogger(OpenLRSAuthenticationFilter.class);
	
	@Value("${auth.enabled}")
	private boolean enabled;
	@Autowired KeyManager keyManager;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if (!enabled) {
			log.warn("Authentication is disabled");
			filterChain.doFilter(request, response);
		}
		
		log.debug("start authentication");
		
		String authorizationHeader = request.getHeader("Authorization");
		
		if (log.isDebugEnabled()) {
			log.debug(String.format("Authorization Header: %s", authorizationHeader));
		}
		
		if (StringUtils.isNotBlank(authorizationHeader)) {
			if (StringUtils.containsIgnoreCase(authorizationHeader, "oauth")) {
			  log.debug("is oauth");
				authenticateOAuth(authorizationHeader, request, response, filterChain);
			}
			else {
			  log.debug("is basic");
				authenticateBasic(authorizationHeader, request, response, filterChain);
			}
		}
		else if ("OPTIONS".equals(request.getMethod())) {
			log.warn("OPTIONS request - returning no content");
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		else {
			unauthorized(response, "Missing Authorization Header", "None");
		}
	}
	
	private void authenticateOAuth(String authorizationHeader, HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		Map<String,String> oauth_parameters = OAuthUtils.decodeAuthorization(authorizationHeader);
		if (oauth_parameters != null && oauth_parameters.containsKey("oauth_consumer_key")) {
			final String oauth_consumer_key = oauth_parameters.get("oauth_consumer_key");
			
			if (oauth_consumer_key != null) {
			  
			  String secret = keyManager.getSecretForKey(oauth_consumer_key);
			  Tenant tenant = keyManager.getTenantForKey(oauth_consumer_key);
				
				TreeMap<String, String> normalizedParams = new TreeMap<String, String>(oauth_parameters);
				Map<String, String []> params = request.getParameterMap();
				if (params != null && !params.isEmpty()) {
					for (String key : params.keySet()) {
						String [] values = params.get(key);
						String value = null;
						if (values != null) {
							value = values[0];
						}
						normalizedParams.put(key, value);
					}
				}
				
				final String signature = oauth_parameters.get("oauth_signature");
				final String calculatedSignature = OAuthUtils.sign(secret, normalizedParams, 
						OAuthUtils.mapToJava(oauth_parameters.get("oauth_signature_method")), request.getMethod(), request.getRequestURL().toString());
				
				if (signature.equals(calculatedSignature)) {	
					request.setAttribute("tenant", tenant);
					//response.setHeader("institution", tenant.getName());
					filterChain.doFilter(request, response);
				}
				else {
				  log.error("Signatures do not match");
					unauthorized(response, "Signatures do not match", "OAuth");
				}
			}
			else {
			  log.error("Invalid consumer key");
				unauthorized(response, "Invalid consumer key", "OAuth");
			}
		}
		else {
		  log.error("Invalid authentication token");
			unauthorized(response, "Invalid authentication token", "OAuth");
		}
	}
	
	
	
	private void authenticateBasic(String authorizationHeader, HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		
		StringTokenizer st = new StringTokenizer(authorizationHeader);
		if (st.hasMoreTokens()) {
	        String basic = st.nextToken();
	 
	        if (basic.equalsIgnoreCase("Basic") || basic.equalsIgnoreCase("Base64")) {
	        	
	        	try {
	        		String credentials = new String(Base64.decodeBase64(st.nextToken()), "UTF-8");

	        		int colon = credentials.indexOf(":");
	            
	        		if (colon != -1) {
	        			String _username = credentials.substring(0, colon).trim();
	        			String _password = credentials.substring(colon + 1).trim();
	        			
	        			String password = keyManager.getSecretForKey(_username);
	        			Tenant tenant = keyManager.getTenantForKey(_username);
	        			
	        			if (!password.equals(_password)) {
	        				unauthorized(response, "Bad credentials", "Basic");
	        			}
	        			else {
	        				request.setAttribute("tenant", tenant);
	        				filterChain.doFilter(request, response);
	        			}
	        		} 
	        		else {
	        			unauthorized(response, "Invalid authentication token", "Basic");
	        		}
	        	} 
	        	catch (UnsupportedEncodingException e) {
	        		throw new Error("Couldn't retrieve authentication", e);
	        	}
	        }
		}
	}
	
	private void unauthorized(HttpServletResponse response, String message, String type) throws IOException {
		 response.setHeader("WWW-Authenticate", type + " realm=\"OpenLRS\"");
		 response.sendError(401, message);
	}

}
