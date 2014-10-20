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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.repositories.Repository;
import org.apereo.openlrs.utils.StatementUtils;

/**
 * @author ggilbert
 *
 */
@org.springframework.stereotype.Repository("InMemoryStatementRepository")
public class InMemoryStatementRepository implements Repository<Statement> {
	
	private static Map<String, Statement> store = new HashMap<String, Statement>();

	@Override
	public Statement post(Statement entity) {
		store.put(entity.getKey(), entity);
		return entity;
	}

	@Override
	public Statement get(Statement key) {
		return store.get(key.getKey());
	}

	@Override
	public List<Statement> get() {
		return new ArrayList<Statement>(store.values());
	}

	@Override
	public List<Statement> get(Map<String, String> filters) {
		List<Statement> statements = get();
		if (statements != null && !statements.isEmpty()) {
			String actor = filters.get(StatementUtils.ACTOR_FILTER);
			String activity = filters.get(StatementUtils.ACTIVITY_FILTER);
			
			List<Statement> filteredStatements = null;
			for (Statement statement : statements) {
				if (StringUtils.isNotBlank(actor) && StringUtils.isNotBlank(activity)) {
					if (statement.getObject().getId().equals(activity)) {
						if (filteredStatements == null) {
							filteredStatements = new ArrayList<Statement>();
						}
						filteredStatements.add(statement);
					}
				}
				else if (StringUtils.isNotBlank(actor)) {
					
				}
				else if (StringUtils.isNotBlank(activity)) {
					if (statement.getObject().getId().equals(activity)) {
						if (filteredStatements == null) {
							filteredStatements = new ArrayList<Statement>();
						}

						filteredStatements.add(statement);
					}
				}
			}
			return filteredStatements;
		}
		return null;
	}

	@Override
	public List<Statement> getByUser(String userId) {
		List<Statement> statements = get();
		if (statements != null && !statements.isEmpty()) {			
			List<Statement> filteredStatements = null;
			for (Statement statement : statements) {
				if (statement.toJSON().contains(userId)) {
					if (filteredStatements == null) {
						filteredStatements = new ArrayList<Statement>();
					}

					filteredStatements.add(statement);
				}
			}
			return filteredStatements;
		}
		return null;
	}

	@Override
	public List<Statement> getByContext(String context) {
		List<Statement> statements = get();
		if (statements != null && !statements.isEmpty()) {			
			List<Statement> filteredStatements = null;
			for (Statement statement : statements) {
				if (statement.toJSON().contains(context)) {
					if (filteredStatements == null) {
						filteredStatements = new ArrayList<Statement>();
					}

					filteredStatements.add(statement);
				}
			}
			return filteredStatements;
		}
		return null;
	}

	@Override
	public List<Statement> getByContextAndUser(String context, String userId) {
		List<Statement> statements = get();
		if (statements != null && !statements.isEmpty()) {			
			List<Statement> filteredStatements = null;
			for (Statement statement : statements) {
				if (statement.toJSON().contains(context) && statement.toJSON().contains(userId)) {
					if (filteredStatements == null) {
						filteredStatements = new ArrayList<Statement>();
					}

					filteredStatements.add(statement);
				}
			}
			return filteredStatements;
		}
		return null;
	}

}
