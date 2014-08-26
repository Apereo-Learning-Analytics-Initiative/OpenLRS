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
package org.apereo.openlrs.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.apache.commons.codec.binary.Base64;
import org.apereo.openlrs.Application;
import org.apereo.openlrs.OpenLRSAuthenticationFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author ggilbert
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
public class StatementControllerIntegrationTest {
	MockMvc mockMvc;
	@Autowired StatementController controller;
	@Autowired OpenLRSAuthenticationFilter filter;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = standaloneSetup(controller).addFilter(filter, "/xAPI/statements").build();
	}

	@Test
	public void thatStatementGetWithNoAuthorizationHeaderReturns401() throws Exception {
		this.mockMvc.perform(
				get("/xAPI/statements")
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void thatStatementGetWithOAuthButNotConsumerKeyReturns401() throws Exception {
		this.mockMvc.perform(
				get("/xAPI/statements")
					.header("Authorization", "OAuth")
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void thatStatementGetWithOAuthReturns200() throws Exception {
		String oauth = "OAuth realm=\"http://localhost/xAPI/statements\",oauth_consumer_key=\"openlrs\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1409015534\",oauth_nonce=\"n2QqSd\",oauth_version=\"1.0\",oauth_signature=\"baU0sY1SpgtmLGfRMyvpq%2BXv48A%3D\"";
		this.mockMvc.perform(
				get("/xAPI/statements")
					.header("Authorization", oauth)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andExpect(status().isOk());
	}
	
	@Test
	public void thatStatementGetWithBasicReturns200() throws Exception {
		String basic = "openlrs:openlrs";
		final byte[] encodedBytes = Base64.encodeBase64(basic.getBytes());
		this.mockMvc.perform(
				get("/xAPI/statements")
					.header("Authorization", "Basic " + new String(encodedBytes))
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andExpect(status().isOk());
	}
}
