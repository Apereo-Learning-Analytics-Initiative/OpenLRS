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

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.model.StatementMetadata;
import org.apereo.openlrs.model.statement.XApiAccount;
import org.apereo.openlrs.model.statement.XApiActor;
import org.apereo.openlrs.model.statement.XApiActorTypes;
import org.apereo.openlrs.model.statement.XApiContext;
import org.apereo.openlrs.model.statement.XApiContextActivities;
import org.apereo.openlrs.model.statement.XApiObject;
import org.apereo.openlrs.repositories.Repository;
import org.apereo.openlrs.utils.StatementUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ggilbert
 *
 */
@Profile("redisElasticsearch")
@Component("ElasticSearchStatementRepository")
public class ElasticSearchStatementRepository implements Repository<Statement> {
	
	private Logger log = LoggerFactory.getLogger(ElasticSearchStatementRepository.class);
	
	@Value("${es.bulkIndexSize:100}")
	private int bulkIndexSize;
	
	@Value("${es.bulkIndexScheduleRateSecond:1}")
	private int bulkIndexScheduleRateSecond;
	
	@Autowired private ElasticSearchStatementSpringDataRepository esSpringDataRepository;
	@Autowired private ElasticSearchStatementMetadataSDRepository esStatementMetadataRepository;
	@Autowired private ObjectMapper objectMapper;
	
	ScheduledExecutorService executorService = null;
	private LinkedBlockingQueue<Statement> statementQueue = new LinkedBlockingQueue<Statement>();
	Runnable task = new Runnable() {
		
		@Override
		public void run() {
			int size = statementQueue.size();
			if (size > 0) {
				if (size > bulkIndexSize) {
					size = bulkIndexSize;
				}
				List<Statement> statementsToIndex = new ArrayList<Statement>();
				List<StatementMetadata> metadataToIndex = new ArrayList<StatementMetadata>();
				for (int i = 0; i < size; i++) {
					Statement statement = statementQueue.poll();
					if (statement != null) {
						statementsToIndex.add(statement);
						metadataToIndex.add(extractMetadata(statement));
					}
				}

				if (!statementsToIndex.isEmpty()) {
					log.debug("Indexing records: "+statementsToIndex.size());
					try {
						esSpringDataRepository.save(statementsToIndex);
						esStatementMetadataRepository.save(metadataToIndex);
					}
					catch (Exception e) {
						log.error("Unable to index statements");
						//TODO
					}
				}
			}
		}
	};
	
	public void onMessage(String statementJSON) {
		
		try {
			Statement statement = objectMapper.readValue(statementJSON.getBytes(), Statement.class);
			if (log.isDebugEnabled()) {
				log.debug("statement to index: {}",statement);
			}
			statementQueue.add(statement);
			
			if (executorService == null) {
				log.debug("Init executorService with rate "+bulkIndexScheduleRateSecond);
				executorService = Executors.newSingleThreadScheduledExecutor();
				executorService.scheduleAtFixedRate(task, 0, bulkIndexScheduleRateSecond, TimeUnit.SECONDS);
			}
		} 
		catch (Exception e) {
			log.error(e.getMessage(),e);
			//TODO - what else?
		} 
	}
	

	@Override
	public Statement post(Statement entity) {
		
		try {
			return esSpringDataRepository.save(entity);
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Statement get(Statement key) {
		return esSpringDataRepository.findOne(key.getId());
	}
	
	private List<Statement> map(List<StatementMetadata> metadata) {
		List<String> ids = new ArrayList<String>();
		for (StatementMetadata sm: metadata) {
			ids.add(sm.getStatementId());
		}
		
		if (log.isDebugEnabled()) {
			log.debug("statement ids: "+ids);
		}
		
		Page<Statement> pagedStatements = esSpringDataRepository.findByIdInOrderByTimestampDesc(ids, new PageRequest(0, 1000));
		
		return pagedStatements.getContent();
	}
	
	@Override
	public List<Statement> getByContext(String context) {
		
		Page<StatementMetadata> metadata = esStatementMetadataRepository.findByContext(context, new PageRequest(0, 1000));
		if (metadata != null && metadata.getContent() != null && !metadata.getContent().isEmpty()) {
			return map(metadata.getContent());
		}
		return null;
	}
	
	@Override
	public List<Statement> getByUser(String user) {
		
		Page<StatementMetadata> metadata = esStatementMetadataRepository.findByUser(user, new PageRequest(0, 1000));
		if (metadata != null && metadata.getContent() != null && !metadata.getContent().isEmpty()) {
			return map(metadata.getContent());
		}
		return null;
	}
	
	@Override
	public List<Statement> getByContextAndUser(String context, String user) {
		
		Page<StatementMetadata> metadata = esStatementMetadataRepository.findByUserAndContext(user,context,new PageRequest(0, 1000));
		if (metadata != null && metadata.getContent() != null && !metadata.getContent().isEmpty()) {
			return map(metadata.getContent());
		}
		return null;
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
		String since = filters.get(StatementUtils.SINCE_FILTER);
		String until = filters.get(StatementUtils.UNTIL_FILTER);
		int limit = getLimit(filters.get(StatementUtils.LIMIT_FILTER));;
		
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
			
			searchQuery = startQuery(limit, boolQuery).build();
		}
		else if (xApiActor != null) {
			
			QueryBuilder query = buildActorQuery(xApiActor);
			
			if (query != null) {
				searchQuery = startQuery(limit, query).build();
			}
		}
		else if (StringUtils.isNotBlank(activity)) {	
			QueryBuilder query = nestedQuery("object",boolQuery().must(matchQuery("object.id", activity)));
			searchQuery = startQuery(limit, query).build();
		}
		else if (StringUtils.isNotBlank(since) || StringUtils.isNotBlank(until)) {
			QueryBuilder query = null;
			
			if(StringUtils.isNotBlank(since) && StringUtils.isNotBlank(until))
			{
				query = new RangeQueryBuilder("stored").from(since).to(until);
			}
			else
			{
				if(StringUtils.isNotBlank(since))
				{
					query = new RangeQueryBuilder("stored").from(since).to("now");
				}
				
				if(StringUtils.isNotBlank(until))
				{
					try {
						
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
						TimeZone tz = TimeZone.getTimeZone("UTC");
						formatter.setTimeZone( tz );
						Date date = (Date)formatter.parse(until);
						Calendar calendar = Calendar.getInstance();  
						calendar.setTime(date); 
						calendar.add(Calendar.YEAR,-1);  
						
						query = new RangeQueryBuilder("stored").from(formatter.format(calendar.getTime())).to(until);
					} catch (ParseException e) {
						log.error(e.getMessage(),e);
						return null;
					}
				}
			}
			
			NativeSearchQueryBuilder searchQueryBuilder = startQuery(limit, query);
		
			searchQuery = searchQueryBuilder.withSort(new FieldSortBuilder("stored").order(SortOrder.DESC)).build();
		}
		else if(limit > 0) {
			searchQuery  = startQuery(limit, null).build();
		}
		
		if (searchQuery != null) {
			if (log.isDebugEnabled()) 
			{
				if(searchQuery.getQuery() != null)
				{
					log.debug(String.format("Elasticsearch query %s", searchQuery.getQuery().toString()));
				}
			}
			
			Iterable<Statement> iterableStatements = esSpringDataRepository.search(searchQuery);
			if (iterableStatements != null) {
				return IteratorUtils.toList(iterableStatements.iterator());
			}
		}
		return null;
	}


	private NativeSearchQueryBuilder startQuery(int limit, QueryBuilder query) {
		NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();

		if(query != null)
		{
			searchQueryBuilder = searchQueryBuilder.withQuery(query);
		}
		
		searchQueryBuilder = searchQueryBuilder.withPageable(new PageRequest(0, limit > 0 ? limit : 500));
		
		return searchQueryBuilder;
	}

	private int getLimit(String limit) {
		
		if(StringUtils.isNotBlank(limit))
		{
			try{
    		  return Integer.parseInt(limit);
    		} catch (NumberFormatException e) {
    			log.debug("Limit not a number");
    		}
		}
		
		return 0;
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
	
	private StatementMetadata extractMetadata(Statement statement) {
		StatementMetadata statementMetadata = new StatementMetadata();
		statementMetadata.setId(UUID.randomUUID().toString());
		statementMetadata.setStatementId(statement.getId());
		
		XApiContext xApiContext = statement.getContext();
		if (xApiContext != null) {
			XApiContextActivities xApiContextActivities = xApiContext.getContextActivities();
			if (xApiContextActivities != null) {
				List<XApiObject> parentContext = xApiContextActivities.getParent();
				if (parentContext != null && !parentContext.isEmpty()) {
					for (XApiObject object : parentContext) {
						String id = object.getId();
						if (StringUtils.contains(id, "portal/site/")) {
							statementMetadata.setContext(StringUtils.substringAfterLast(id, "/"));
						}
					}
				}
			}
		}
		
		XApiActor xApiActor = statement.getActor();
		if (xApiActor != null) {
			String mbox = xApiActor.getMbox();
			if (StringUtils.isNotBlank(mbox)) {
				statementMetadata.setUser(StringUtils.substringBetween(mbox, "mailto:", "@"));
			}
		}
		
		return statementMetadata;
	}
}
