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
package org.apereo.openlrs.services;

import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apereo.openlrs.Application;
import org.apereo.openlrs.exceptions.StatementStateConflictException;
import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.model.StatementResult;
import org.apereo.openlrs.model.statement.XApiActor;
import org.apereo.openlrs.model.statement.XApiObject;
import org.apereo.openlrs.model.statement.XApiVerb;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ggilbert
 *
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
public class StatementServiceTest {
	@Autowired StatementService service;
	private XApiActor actor;
	private XApiVerb verb;
	private XApiObject object;
	
	@Before
	public void setup() {
		actor = new XApiActor();
		actor.setMbox("mailto:test@test.com");
		verb = new XApiVerb();
		verb.setId("http://example.com/verb");
		verb.setDisplay(Collections.singletonMap("en-US", "verb"));
		object = new XApiObject();
		object.setId("http://example.com/object");
	}
	
	@Test
	public void shouldReturnStatementId() throws Exception {
		Statement statement = new Statement();
		statement.setActor(actor);
		statement.setVerb(verb);
		statement.setObject(object);

		List<String> statementIds = service.postStatement(statement);
		Assert.assertNotNull(statementIds);
		Assert.assertTrue(statementIds.size() == 1);
	}
	
	@Test
	public void shouldReturn3StatementsWithDifferentIds() throws Exception {
		for (int i = 0; i < 2; i++) {
			Statement statement = new Statement();
			statement.setActor(actor);
			statement.setVerb(verb);
			statement.setObject(object);
			service.postStatement(statement);
		}
		
		StatementResult result = service.getStatement(new HashMap<String, String>());
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getStatements());
		// Need to account for the statements added in other tests
		Assert.assertTrue(result.getStatements().size() >= 3);
		
		Set<String> statementIds = new HashSet<String>();
		for (Statement s : result.getStatements()) {
			statementIds.add(s.getId());
		}
		Assert.assertTrue(statementIds.size() == result.getStatements().size());
	}
	
	@Test
	public void shouldReturn1Statement() throws Exception {
		String id = UUID.randomUUID().toString();
		
		Statement statement = new Statement();
		statement.setId(id);
		statement.setActor(actor);
		statement.setVerb(verb);
		statement.setObject(object);
		service.postStatement(statement);

		Statement retval = service.getStatement(id);
		Assert.assertNotNull(retval);
		Assert.assertTrue(id.equals(retval.getId()));
	}
	
	@Test(expected=StatementStateConflictException.class)
	public void postStatementMethodShouldThrowExceptionIfStatementIsDuplicate() {
		String id = UUID.randomUUID().toString();
		
		Statement statement = new Statement();
		statement.setId(id);
		statement.setActor(actor);
		statement.setVerb(verb);
		statement.setObject(object);
		//ok
		service.postStatement(statement);
		//conflict
		service.postStatement(statement);
	}
}
