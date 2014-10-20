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

import java.util.List;
import java.util.Map;

import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.repositories.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author ggilbert
 *
 */
@org.springframework.stereotype.Repository("TwoTierStatementRepository")
public class TwoTierStatementRepository implements Repository<Statement> {
	
	@Value("${xapi.statements.repository.twotier.write}") private String writeRepositoryType;
	@Value("${xapi.statements.repository.twotier.read}") private String readRepositoryType;
	
	@Autowired private StatementRepositoryFactory statementRepositoryFactory;
	
	private Repository<Statement> writeRepository;
	private Repository<Statement> readRepository;

	@Override
	public Statement post(Statement entity) {
		return getWriteRepository().post(entity);
	}

	@Override
	public Statement get(Statement key) {
		return getReadRepository().get(key);
	}

	@Override
	public List<Statement> get() {
		return getReadRepository().get();
	}
	
	private Repository<Statement> getWriteRepository() {
		if (writeRepository == null) {
			writeRepository = statementRepositoryFactory.getRepository(writeRepositoryType);
		}
		return writeRepository;
	}
	
	private Repository<Statement> getReadRepository() {
		if (readRepository == null) {
			readRepository = statementRepositoryFactory.getRepository(readRepositoryType);
		}
		return readRepository;
	}

	@Override
	public List<Statement> get(Map<String, String> filters) {
		return getReadRepository().get(filters);
	}

	@Override
	public List<Statement> getByUser(String userId) {
		return getReadRepository().getByUser(userId);
	}

	@Override
	public List<Statement> getByContext(String context) {
		return getReadRepository().getByContext(context);
	}

	@Override
	public List<Statement> getByContextAndUser(String context, String userId) {
		return getReadRepository().getByContextAndUser(context, userId);
	}

}
