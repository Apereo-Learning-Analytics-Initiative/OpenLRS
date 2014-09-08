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
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.model.statement.XApiActor;
import org.apereo.openlrs.model.statement.XApiActorTypes;
import org.apereo.openlrs.repositories.Repository;
import org.apereo.openlrs.utils.StatementUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ggilbert
 *
 */
@org.springframework.stereotype.Repository("ElasticSearchStatementRepository")
public class ElasticSearchStatementRepository implements Repository<Statement> {
	
	private Logger log = LoggerFactory.getLogger(ElasticSearchStatementRepository.class);
	
	@Autowired private ElasticSearchStatementSpringDataRepository esSpringDataRepository;
	
	public void onMessage(String statementJSON) {
		
		if (log.isDebugEnabled()) {
			log.debug(statementJSON);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Statement statement = mapper.readValue(statementJSON.getBytes(), Statement.class);
			if (log.isDebugEnabled()) {
				log.debug("statement: {}",statement);
			}

			post(statement);
		} 
		catch (Exception e) {
			log.error(e.getMessage(),e);
			//TODO - what else?
		} 
	}
	

	@Override
	public Statement post(Statement entity) {
		return esSpringDataRepository.save(entity);
	}

	@Override
	public Statement get(Statement key) {
		return esSpringDataRepository.findOne(key.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Statement> get() {
		Iterable<Statement> iterableStatements = esSpringDataRepository.findAll();
		if (iterableStatements != null) {
			return IteratorUtils.toList(iterableStatements.iterator());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Statement> get(Map<String, String> filters) {
		String actor = filters.get(StatementUtils.ACTOR_FILTER);
		String activity = filters.get(StatementUtils.ACTIVITY_FILTER);
		
		XApiActor xApiActor = null;
		
		if (StringUtils.isNotBlank(actor)) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				xApiActor = objectMapper.readValue(actor.getBytes(), XApiActor.class);
			} 
			catch (Exception e) {
				log.error(e.getMessage(),e);
			} 
		}

		QueryBuilder queryBuilder = null;
		QueryBuilder query = null;
		
		if (StringUtils.isNotBlank(actor) && xApiActor != null) {
			query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("id", activity));
			queryBuilder = QueryBuilders.nestedQuery("object", query);
		}
		else if (xApiActor != null) {
			MatchQueryBuilder mboxQuery = null;
			MatchQueryBuilder actorTypeQuery = null;
			List<QueryBuilder> queryBuilders = new ArrayList<QueryBuilder>();
			
			XApiActorTypes actorType = xApiActor.getObjectType();
			if (actorType != null) {
				actorTypeQuery = QueryBuilders.matchQuery("objectType", actorType);
				queryBuilders.add(actorTypeQuery);
			}
			
			String mbox = xApiActor.getMbox();
			if (StringUtils.isNotBlank(mbox)) {
				mboxQuery = QueryBuilders.matchQuery("mbox", mbox);
				queryBuilders.add(mboxQuery);
			}
			
			if (!queryBuilders.isEmpty()) {
				query = QueryBuilders.boolQuery();
				for (QueryBuilder qb : queryBuilders) {
					query = ((BoolQueryBuilder)query).must(qb);
				}
				queryBuilder = QueryBuilders.nestedQuery("actor", query);
			}
		}
		else if (StringUtils.isNotBlank(activity)) {
			query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("id", activity));
			queryBuilder = QueryBuilders.nestedQuery("object", query);
		}
		
		Iterable<Statement> iterableStatements = esSpringDataRepository.search(queryBuilder);
		if (iterableStatements != null) {
			return IteratorUtils.toList(iterableStatements.iterator());
		}
		return null;
	}
}
