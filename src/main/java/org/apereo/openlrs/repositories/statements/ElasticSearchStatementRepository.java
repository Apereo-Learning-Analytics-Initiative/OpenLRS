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

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.model.statement.XApiAccount;
import org.apereo.openlrs.model.statement.XApiActor;
import org.apereo.openlrs.model.statement.XApiActorTypes;
import org.apereo.openlrs.repositories.Repository;
import org.apereo.openlrs.utils.StatementUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

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

		SearchQuery searchQuery = null;
		
		if (StringUtils.isNotBlank(activity) && xApiActor != null) {
			QueryBuilder actorQuery = buildActorQuery(xApiActor);
			QueryBuilder activityQuery = nestedQuery("object",boolQuery().must(matchQuery("object.id", activity)));
			
			BoolQueryBuilder boolQuery = boolQuery().must(actorQuery).must(activityQuery);
			searchQuery = new NativeSearchQueryBuilder()
			.withQuery(boolQuery)
			.build();
		}
		else if (xApiActor != null) {
			
			QueryBuilder query = buildActorQuery(xApiActor);
			
			if (query != null) {
				searchQuery = new NativeSearchQueryBuilder()
				.withQuery(query)
				.build();
			}
		}
		else if (StringUtils.isNotBlank(activity)) {	
			
			QueryBuilder query = nestedQuery("object",boolQuery().must(matchQuery("object.id", activity)));
			
			searchQuery = new NativeSearchQueryBuilder()
			.withQuery(query)
			.build();
		}
		
		if (searchQuery != null) {
			if (log.isDebugEnabled()) {
				log.debug(String.format("Elasticsearch query %s", searchQuery.getQuery().toString()));
			}
			
			Iterable<Statement> iterableStatements = esSpringDataRepository.search(searchQuery);
			if (iterableStatements != null) {
				return IteratorUtils.toList(iterableStatements.iterator());
			}
		}
		return null;
	}
	
	private QueryBuilder buildActorQuery(XApiActor xApiActor) {
		List<QueryBuilder> queryBuilders = new ArrayList<QueryBuilder>();
		
		XApiActorTypes actorType = xApiActor.getObjectType();
		if (actorType != null) {
			MatchQueryBuilder actorTypeQuery = matchQuery("actor.objectType", actorType);
			queryBuilders.add(actorTypeQuery);
		}
		
		String mbox = xApiActor.getMbox();
		if (StringUtils.isNotBlank(mbox)) {
			if (log.isDebugEnabled()) {
				log.debug(String.format("mbox: %s",mbox));
			}
			MatchQueryBuilder mboxQuery = matchQuery("actor.mbox", mbox);
			queryBuilders.add(mboxQuery);
		}
		
		String mbox_sha1sum = xApiActor.getMbox_sha1sum();
		if (StringUtils.isNotBlank(mbox_sha1sum)) {
			if (log.isDebugEnabled()) {
				log.debug(String.format("mbox_sha1sum: %s",mbox_sha1sum));
			}
			MatchQueryBuilder mbox_sha1sumQuery = matchQuery("actor.mbox_sha1sum", mbox_sha1sum);
			queryBuilders.add(mbox_sha1sumQuery);
		}

		String openid = xApiActor.getOpenid();
		if (StringUtils.isNotBlank(openid)) {
			if (log.isDebugEnabled()) {
				log.debug(String.format("openid: %s",openid));
			}
			MatchQueryBuilder openidQuery = matchQuery("actor.openid", openid);
			queryBuilders.add(openidQuery);
		}
		
		XApiAccount account = xApiActor.getAccount();
		if (account != null) {
			
			String name = account.getName();
			String homepage = account.getHomePage();
			
			if (StringUtils.isNotBlank(name)) {
				MatchQueryBuilder acctNameQuery = matchQuery("actor.account.name", name);
				queryBuilders.add(acctNameQuery);
			}
			
			if (StringUtils.isNotBlank(homepage)) {
				MatchQueryBuilder acctHomepageQuery = matchQuery("actor.account.homePage", homepage);
				queryBuilders.add(acctHomepageQuery);
			}

		}
		
		if (!queryBuilders.isEmpty()) {
			BoolQueryBuilder boolQuery = boolQuery();
			for (QueryBuilder qb : queryBuilders) {
				boolQuery = boolQuery.must(qb);
			}
			return nestedQuery("actor", boolQuery);
		}
		return null;
	}
}
