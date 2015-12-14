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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apereo.openlrs.Application;
import org.apereo.openlrs.controllers.xapi.StatementController;
import org.apereo.openlrs.model.xapi.Statement;
import org.apereo.openlrs.model.xapi.StatementResult;
import org.apereo.openlrs.model.xapi.XApiActor;
import org.apereo.openlrs.model.xapi.XApiObject;
import org.apereo.openlrs.model.xapi.XApiVerb;
import org.apereo.openlrs.repositories.statements.ElasticSearchStatementSpringDataRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ggilbert
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@IfProfileValue(name="include-integration-tests",values={"elasticsearch"})
public class StatementControllerGetCombinationsTest {
	
	@Autowired private ElasticSearchStatementSpringDataRepository elasticSearchStatementSpringDataRepository;
	@Autowired private StatementController statmentController;
	
	private String statement1Id = RandomStringUtils.random(32);

	@Before
	public void before() {		
		//set up data	
		XApiActor actor1 = new XApiActor();
		actor1.setMbox("mailto:"+statement1Id+"@test.com");
		XApiVerb verb1 = new XApiVerb();
		verb1.setId("http://example.com/"+statement1Id);
		verb1.setDisplay(Collections.singletonMap("en-US", "verb"));
		XApiObject object1 = new XApiObject();
		object1.setId("http://example.com/"+statement1Id);
		Statement statement1 = new Statement();
		statement1.setId(statement1Id);
		statement1.setActor(actor1);
		statement1.setVerb(verb1);
		statement1.setObject(object1);

		elasticSearchStatementSpringDataRepository.save(statement1);
	}
	
	@After
	public void after() {
		elasticSearchStatementSpringDataRepository.delete(statement1Id);
	}

	@Test
	public void shouldReturnOneStatementWithGivenId() {
		Statement s = statmentController.getStatement(statement1Id, new HashMap<String, String>());
		Assert.assertNotNull(s);
	}
	
	@Test
	public void shouldReturnAtLeastOneStatement() {
		StatementResult sr = statmentController.getStatement(null,null,null,null,null);
		Assert.assertNotNull(sr);
		List<Statement> statements = sr.getStatements();
		Assert.assertNotNull(statements);
		Assert.assertTrue(statements.size() > 0);
	}
}
