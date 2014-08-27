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

import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.repositories.Repository;

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

}
