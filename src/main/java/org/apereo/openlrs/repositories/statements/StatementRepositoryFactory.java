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
package org.apereo.openlrs.repositories.statements;

import java.util.Map;

import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.repositories.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 *
 */
@Component
public class StatementRepositoryFactory {
	
	@Value("${xapi.statements.repository}") String repository;
	@Autowired private Map<String, Repository<Statement>> statementRepositories;
	
	public Repository<Statement> getRepository() {
		return statementRepositories.get(repository);
	}
	
	public Repository<Statement> getRepository(String repositoryType) {
		return statementRepositories.get(repositoryType);
	}
}
