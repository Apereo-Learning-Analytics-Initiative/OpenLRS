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
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author ggilbert
 *
 */
@org.springframework.stereotype.Repository("RedisStatementRepository")
@Profile("redisElasticsearch")
public class RedisStatementRepository implements Repository<Statement> {
	
	@Autowired private String channelName;
	
	@Autowired private StringRedisTemplate redisTemplate;

	@Override
	public Statement post(Statement entity) {
		redisTemplate.convertAndSend(channelName, entity.toJSON());
		return entity;
	}

	@Override
	public Statement get(Statement key) {
		throw new UnsupportedOperationException("RedisStatementRepository does not support get operations");
	}

	@Override
	public List<Statement> get() {
		throw new UnsupportedOperationException("RedisStatementRepository does not support get operations");	
	}

	@Override
	public List<Statement> get(Map<String, String> filters) {
		throw new UnsupportedOperationException("RedisStatementRepository does not support get operations");	
	}

	@Override
	public List<Statement> getByUser(String userId) {
		throw new UnsupportedOperationException("RedisStatementRepository does not support get operations");
	}

	@Override
	public List<Statement> getByContext(String context) {
		throw new UnsupportedOperationException("RedisStatementRepository does not support get operations");
	}

	@Override
	public List<Statement> getByContextAndUser(String context, String userId) {
		throw new UnsupportedOperationException("RedisStatementRepository does not support get operations");
	}

}
