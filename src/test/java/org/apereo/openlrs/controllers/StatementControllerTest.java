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

import static org.mockito.BDDMockito.given;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.apereo.openlrs.exceptions.InvalidXAPIRequestException;
import org.apereo.openlrs.exceptions.NotFoundException;
import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.services.StatementService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * JUnit test class for {@link StatementController}.
 * @author Gary Roybal, groybal@unicon.net
 */
public class StatementControllerTest {

	@Mock private Statement statement;
	@Mock private StatementService statementService;

	private String statementId;
	private Map<String,String> allRequestParams;
	private StatementController controller;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.statementId = "12345678";
		this.allRequestParams = new HashMap<String,String>();
		this.allRequestParams.put("statementId", this.statementId);
		this.controller = new StatementController(this.statementService);
	}

	@Test(expected = NotFoundException.class)
	public void getStatementMethodShouldThrowExceptionIfStatementNotFound() {
		// given
		given(this.statementService.getStatement(this.statementId)).willReturn(null);
		// when
		this.controller.getStatement(this.statementId, this.allRequestParams);
	}

	@Test(expected = InvalidXAPIRequestException.class)
	public void getStatementMethodShouldThrowExceptionIfBothStatementIdAndVoidedStatementIdParametersAreSubmitted() {
		// given
		given(this.statementService.getStatement(this.statementId)).willReturn(null);
		this.allRequestParams.put("voidedStatementId", "112233");
		// when
		this.controller.getStatement(this.statementId, this.allRequestParams);
	}

	@Test(expected = NotImplementedException.class)
	public void putStatementMethodShouldThrowExceptionIfStatementNotFound() {
		// when
		this.controller.putStatement(this.statement);
	}	
}
