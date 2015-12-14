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
package org.apereo.openlrs.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.apereo.openlrs.Application;
import org.apereo.openlrs.controllers.xapi.AboutController;
import org.apereo.openlrs.controllers.xapi.XAPIHeaderFilter;
import org.apereo.openlrs.controllers.xapi.XAPIRequestValidationFilter;
import org.apereo.openlrs.controllers.xapi.XApiConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author ggilbert (ggilbert @ unicon.net)
 *
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
public class AboutControllerIntegrationTest {
	MockMvc mockMvc;
	@Autowired AboutController controller;
	@Autowired XAPIHeaderFilter xapiHeaderFilter;
	@Autowired XAPIRequestValidationFilter xapiRequestValidationFilter;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = standaloneSetup(controller)
				.addFilter(xapiHeaderFilter, "/*")
				.addFilter(xapiRequestValidationFilter, "/xAPI/*")
				.build();
	}
	
	@Test
	public void thatAboutWithNoVersionHeaderReturns400() throws Exception {
		this.mockMvc.perform(
				get("/xAPI/about")
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andExpect(status().isBadRequest());
	}
	
	@Test
	public void thatAboutReturnsJsonWithVersion() throws Exception {
		this.mockMvc.perform(
			get("/xAPI/about")
				.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("version").exists());
	}
	
	@Test
	public void thatAboutReturnsHeaderWithVersion() throws Exception {
		this.mockMvc.perform(
				get("/xAPI/about")
				.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(header().string("X-Experience-API-Version", "someversion"));
	}
}
