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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.codec.binary.Base64;
import org.apereo.openlrs.Application;
import org.apereo.openlrs.OpenLRSAuthenticationFilter;
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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author ggilbert
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes=Application.class)
@ActiveProfiles("test")
public class StatementControllerIntegrationTest {
	MockMvc mockMvc;
	
	@Autowired WebApplicationContext wac;
	@Autowired OpenLRSAuthenticationFilter filter;
	@Autowired XAPIHeaderFilter xapiHeaderFilter;
	@Autowired XAPIRequestValidationFilter xapiRequestValidationFilter;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
				.addFilter(filter, "/xAPI/statements")
				.addFilter(xapiHeaderFilter, "/*")
				.addFilter(xapiRequestValidationFilter, "/xAPI/*")
				.build();

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
	public void thatStatementGetWithNoVersionHeaderReturns400() throws Exception {
		String basic = "test:test";
		final byte[] encodedBytes = Base64.encodeBase64(basic.getBytes());
		this.mockMvc.perform(
			get("/xAPI/statements")
				.header("Authorization", "Basic " + new String(encodedBytes))
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void thatStatementGetWithOAuthButNotConsumerKeyReturns401() throws Exception {
		this.mockMvc.perform(
				get("/xAPI/statements")
					.header("Authorization", "OAuth")
					.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
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
					.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andExpect(status().isOk());
	}
	
	@Test
	public void thatStatementGetWithBasicReturns200() throws Exception {
		String basic = "test:test";
		final byte[] encodedBytes = Base64.encodeBase64(basic.getBytes());
		this.mockMvc.perform(
				get("/xAPI/statements")
					.header("Authorization", "Basic " + new String(encodedBytes))
					.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andExpect(status().isOk());
	}
	
	@Test
	public void thatStatementReturnsHeaderWithVersion() throws Exception {
		String basic = "test:test";
		final byte[] encodedBytes = Base64.encodeBase64(basic.getBytes());
		this.mockMvc.perform(
			get("/xAPI/statements")
				.header("Authorization", "Basic " + new String(encodedBytes))
				.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(header().string("X-Experience-API-Version", "someversion"));
	}
	
	@Test
	public void shouldHandleSimpleStatement() throws Exception {
		String body = "{\"id\":\"fd41c918-b88b-4b20-a0a5-a4c32391aaa0\",\"actor\":{\"objectType\": \"Agent\",\"name\":\"Project Tin Can API\", "+
							"\"mbox\":\"mailto:user@example.com\"}, "+
							"\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/created\","+
							"\"display\":{\"en-US\":\"created\" }},"+
							"\"object\":{\"id\":\"http://example.adlnet.gov/xapi/example/simplestatement\","+
							"\"definition\":{\"name\":{ \"en-US\":\"simple statement\"},"+
							"\"description\":{ \"en-US\":\"A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the "+
							"verb, or the Activity/object.\" }}}}";	
		
		
		String basic = "test:test";
		final byte[] encodedBytes = Base64.encodeBase64(basic.getBytes());
		this.mockMvc.perform(
			post("/xAPI/statements")
				.header("Authorization", "Basic " + new String(encodedBytes))
				.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(header().string("X-Experience-API-Version", "someversion"));
	}
	
	@Test
	public void shouldHandleSimpleStatementWithNoId() throws Exception {
		String body = "{\"actor\":{\"objectType\": \"Agent\",\"name\":\"Project Tin Can API\", "+
				"\"mbox\":\"mailto:user@example.com\"}, "+
				"\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/created\","+
				"\"display\":{\"en-US\":\"created\" }},"+
				"\"object\":{\"id\":\"http://example.adlnet.gov/xapi/example/simplestatement\","+
				"\"definition\":{\"name\":{ \"en-US\":\"simple statement\"},"+
				"\"description\":{ \"en-US\":\"A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the "+
				"verb, or the Activity/object.\" }}}}";	


		String basic = "test:test";
		final byte[] encodedBytes = Base64.encodeBase64(basic.getBytes());
		this.mockMvc.perform(
		post("/xAPI/statements")
			.header("Authorization", "Basic " + new String(encodedBytes))
			.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string("X-Experience-API-Version", "someversion"));
	}
	
	@Test
	public void shouldHandleStatementWithResult() throws Exception {
		
		String body = "{\"actor\":{\"objectType\": \"Agent\",\"name\":\"Example Learner\",\"mbox\":\"mailto:example.learner@adlnet.gov\"},"+
						"\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"en-US\":\"attempted\"}},\"object\":{"+
						"\"id\":\"http://example.adlnet.gov/xapi/example/simpleCBT\",\"definition\":{\"name\":{\"en-US\":\"simple CBT course\""+
            			"},\"description\":{\"en-US\":\"A fictitious example CBT course.\"}}},\"result\":{\"score\":{\"scaled\":0.95},\"success\":true,\"completion\":true}}";
		
		String basic = "test:test";
		final byte[] encodedBytes = Base64.encodeBase64(basic.getBytes());
		this.mockMvc.perform(
		post("/xAPI/statements")
			.header("Authorization", "Basic " + new String(encodedBytes))
			.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string("X-Experience-API-Version", "someversion"));
	}
	
	@Test
	public void shouldHandleStatementWithPrettyMuchEverything() throws Exception {
		String body = "{\"id\": \"6690e6c9-3ef0-4ed3-8b37-7f3964730bee\",\"actor\": {\"name\": \"Team PB\",\"mbox\": \"mailto:teampb@example.com\",\"member\": ["+
						"{\"name\": \"Andrew Downes\",\"account\": {\"homePage\": \"http://www.example.com\",\"name\": \"13936749\"},\"objectType\": \"Agent\""+
            			"},{\"name\": \"Toby Nichols\",\"openid\": \"http://toby.openid.example.org/\",\"objectType\": \"Agent\"},{\"name\": \"Ena Hills\","+
            			"\"mbox_sha1sum\": \"ebd31e95054c018b10727ccffd2ef2ec3a016ee9\",\"objectType\": \"Agent\"}],\"objectType\": \"Group\"},\"verb\": {"+
            			"\"id\": \"http://adlnet.gov/expapi/verbs/attended\",\"display\": {\"en-GB\": \"attended\",\"en-US\": \"attended\"}},\"result\": {"+
            			"\"extensions\": {\"http://example.com/profiles/meetings/resultextensions/minuteslocation\": \"X:\\\\meetings\\\\minutes\\\\examplemeeting.one\""+
        				"},\"success\": true,\"completion\": true,\"response\": \"We agreed on some example actions.\",\"duration\": \"PT1H0M0S\"},\"context\": {"+
        				"\"registration\": \"ec531277-b57b-4c15-8d91-d292c5b2b8f7\",\"contextActivities\": {\"parent\": [{\"id\": \"http://www.example.com/meetings/series/267\","+
        				"\"objectType\": \"Activity\"}],\"category\": [{\"id\": \"http://www.example.com/meetings/categories/teammeeting\",\"objectType\": \"Activity\","+
        				"\"definition\": {\"name\": {\"en\": \"team meeting\"},\"description\": {\"en\": \"A category of meeting used for regular team meetings.\"},"+
                        "\"type\": \"http://example.com/expapi/activities/meetingcategory\"}}],\"other\": [{\"id\": \"http://www.example.com/meetings/occurances/34257\","+
                        "\"objectType\": \"Activity\"},{\"id\": \"http://www.example.com/meetings/occurances/3425567\",\"objectType\": \"Activity\"}]},\"instructor\" :"+
                        "{\"name\": \"Andrew Downes\",\"account\": {\"homePage\": \"http://www.example.com\",\"name\": \"13936749\"},\"objectType\": \"Agent\"},"+
                        "\"team\":{\"name\": \"Team PB\",\"mbox\": \"mailto:teampb@example.com\",\"objectType\": \"Group\"}, \"platform\" : \"Example virtual meeting software\","+
                        "\"language\" : \"tlh\",\"statement\" : {\"objectType\":\"StatementRef\",\"id\" :\"6690e6c9-3ef0-4ed3-8b37-7f3964730bee\"}},\"timestamp\": \"2013-05-18T05:32:34.804Z\","+
                        "\"stored\": \"2013-05-18T05:32:34.804Z\",\"authority\": {\"account\": {\"homePage\": \"http://cloud.scorm.com/\",\"name\": \"anonymous\"},\"objectType\": \"Agent\""+
    					"},\"version\": \"1.0.0\",\"object\": {\"id\": \"http://www.example.com/meetings/occurances/34534\",\"definition\": {\"extensions\": {\"http://example.com/profiles/meetings/activitydefinitionextensions/room\": {\"name\": \"Kilby\", \"id\" : \"http://example.com/rooms/342\"}"+
            			"},\"name\": {\"en-GB\": \"example meeting\",\"en-US\": \"example meeting\"},\"description\": {\"en-GB\": \"An example meeting that happened on a specific occasion with certain people present.\","+
            			"\"en-US\": \"An example meeting that happened on a specific occasion with certain people present.\"},\"type\": \"http://adlnet.gov/expapi/activities/meeting\","+
            			"\"moreInfo\": \"http://virtualmeeting.example.com/345256\"},\"objectType\": \"Activity\"}}";
		
		String basic = "test:test";
		final byte[] encodedBytes = Base64.encodeBase64(basic.getBytes());
		this.mockMvc.perform(
		post("/xAPI/statements")
			.header("Authorization", "Basic " + new String(encodedBytes))
			.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string("X-Experience-API-Version", "someversion"));

	}
	
	@Test
	public void shouldReturn400WithMissingActor() throws Exception {
		String body = "{\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/created\","+
				"\"display\":{\"en-US\":\"created\" }},"+
				"\"object\":{\"id\":\"http://example.adlnet.gov/xapi/example/simplestatement\","+
				"\"definition\":{\"name\":{ \"en-US\":\"simple statement\"},"+
				"\"description\":{ \"en-US\":\"A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the "+
				"verb, or the Activity/object.\" }}}}";	


		String basic = "test:test";
		final byte[] encodedBytes = Base64.encodeBase64(basic.getBytes());
		this.mockMvc.perform(
		post("/xAPI/statements")
			.header("Authorization", "Basic " + new String(encodedBytes))
			.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(header().string("X-Experience-API-Version", "someversion"));
	}

	@Test
	public void shouldReturn400WithMissingVerb() throws Exception {
		String body = "{\"actor\":{\"objectType\": \"Agent\",\"name\":\"Project Tin Can API\", "+
				"\"mbox\":\"mailto:user@example.com\"}, "+
				"\"object\":{\"id\":\"http://example.adlnet.gov/xapi/example/simplestatement\","+
				"\"definition\":{\"name\":{ \"en-US\":\"simple statement\"},"+
				"\"description\":{ \"en-US\":\"A simple Experience API statement. Note that the LRS does not need to have any prior information about the Actor (learner), the "+
				"verb, or the Activity/object.\" }}}}";	

		String basic = "test:test";
		final byte[] encodedBytes = Base64.encodeBase64(basic.getBytes());
		this.mockMvc.perform(
		post("/xAPI/statements")
			.header("Authorization", "Basic " + new String(encodedBytes))
			.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(header().string("X-Experience-API-Version", "someversion"));
	}
	
	@Test
	public void shouldReturn400WithMissingObject() throws Exception {
		String body = "{\"actor\":{\"objectType\": \"Agent\",\"name\":\"Project Tin Can API\", "+
				"\"mbox\":\"mailto:user@example.com\"}, "+
				"\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/created\","+
				"\"display\":{\"en-US\":\"created\" }}}";	

		String basic = "test:test";
		final byte[] encodedBytes = Base64.encodeBase64(basic.getBytes());
		this.mockMvc.perform(
		post("/xAPI/statements")
			.header("Authorization", "Basic " + new String(encodedBytes))
			.header(XApiConstants.XAPI_VERSION_HEADER, "someversion")
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(header().string("X-Experience-API-Version", "someversion"));
	}
}
