/**
 * Copyright 2015 Unicon (R) Licensed under the
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
package org.apereo.openlrs.storage.aws.elasticsearch;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.model.xapi.Statement;
import org.apereo.openlrs.model.xapi.StatementMetadata;
import org.apereo.openlrs.storage.TierTwoStorage;
import org.apereo.openlrs.utils.MetadataUtils;
import org.apereo.openlrs.utils.StatementUtils;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Bulk.Builder;
import io.searchbox.core.Doc;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.MultiGet;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.params.Parameters;

/**
 * @author jasonbrown
 *
 */
@Component("XApiOnlyAwsElasticsearchTierTwoStorage")
@Profile("awselasticsearch")
public class XApiOnlyAwsElasticsearchTierTwoStorage implements TierTwoStorage<OpenLRSEntity> {
    
    private Logger log = LoggerFactory.getLogger(XApiOnlyAwsElasticsearchTierTwoStorage.class);

    @Value("${es.bulkIndexSize:100}")
    private int bulkIndexSize;
    
    @Value("${es.bulkIndexScheduleRateSecond:1}")
    private int bulkIndexScheduleRateSecond;
    @Autowired JestClient jestClient;

    private ScheduledExecutorService executorService = null;
    
    private final String STATEMENT_INDEX = "openlrsstatement";
    private final String STATEMENT_TYPE = "statement";
    private final String METADATA_INDEX = "openlrsstatementmetadata";
    private final String METADATA_TYPE = "statement_metadata";
    
    private LinkedBlockingQueue<Statement> statementQueue = new LinkedBlockingQueue<Statement>();
    private Runnable task = new Runnable() {
        
        @Override
        public void run() {
            int size = statementQueue.size();
            
            if (size > 0) {
                
                if (size > bulkIndexSize) {
                    size = bulkIndexSize;
                }
                List<OpenLRSEntity> statementsToIndex = new ArrayList<OpenLRSEntity>();
                List<StatementMetadata> metadataToIndex = new ArrayList<StatementMetadata>();

                for (int i = 0; i < size; i++) {
                    Statement statement = statementQueue.poll();
                    if (statement != null) {
                        statementsToIndex.add(statement);
                        metadataToIndex.add(MetadataUtils.extractMetadata(statement));
                    }
                }

                if (!statementsToIndex.isEmpty()) {
                    try {
                        saveAll(statementsToIndex);
                        saveAllMetaData(metadataToIndex);
                    }
                    catch (Exception e) {
                        log.error("Unable to index statements");
                    }
                }
            }
        }
    };
    
    public OpenLRSEntity save(OpenLRSEntity entity) {
        if (entity != null && Statement.OBJECT_KEY.equals(entity.getObjectKey())) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("statement to index: {}",entity);
                }
                statementQueue.add((Statement)entity);
                
                if (executorService == null) {
                    log.debug("Init executorService with rate "+bulkIndexScheduleRateSecond);
                    executorService = Executors.newSingleThreadScheduledExecutor();
                    executorService.scheduleAtFixedRate(task, 0, bulkIndexScheduleRateSecond, TimeUnit.SECONDS);
                }
            } 
            catch (Exception e) {
                log.error(e.getMessage(),e);
                e.printStackTrace();
            } 
        }
        else if (entity != null) {
            log.warn("XApiOnlyAwsElasticsearchTierTwoStorage does not support "+entity.getObjectKey());
        }
        
        return entity;
    }

    @Override
    public List<OpenLRSEntity> findAll() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        List<OpenLRSEntity> openLRSentities = new ArrayList<OpenLRSEntity>();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                                        .addIndex(STATEMENT_INDEX)
                                        .addType(STATEMENT_TYPE)
                                        .setParameter("from", 0)
                                        .setParameter(Parameters.SIZE, 100) // default elasticsearch returns only 10 results.
                                        .build();
        try {
            SearchResult result = jestClient.execute(search);
            //TODO remove deprecated.
            Iterable<Statement> iterableStatements = result.getSourceAsObjectList(Statement.class);
            if (iterableStatements != null) {
                openLRSentities = new ArrayList<OpenLRSEntity>(IteratorUtils.toList(iterableStatements.iterator()));
            }
   
        } catch (IOException e) {
            log.error("Could not findAll AWS Statements");
            e.printStackTrace();
        }
        
        return openLRSentities;
    }

    @Override
    public Page<OpenLRSEntity> findAll(Pageable pageable) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                                        .addIndex(STATEMENT_INDEX)
                                        .addType(STATEMENT_TYPE)
                                        .setParameter("from", pageable.getOffset())
                                        .setParameter(Parameters.SIZE, pageable.getPageSize())
                                        .build();

        try {
            SearchResult result = jestClient.execute(search);
            //TODO remove deprecated.
            Iterable<Statement> iterableStatements = result.getSourceAsObjectList(Statement.class);
            return createPage(iterableStatements);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public OpenLRSEntity findById(String id) {
        Statement statement = new Statement();
        Get get = new Get.Builder(STATEMENT_INDEX, id).type(STATEMENT_TYPE).build();
        try {
            JestResult result = jestClient.execute(get);
            statement = result.getSourceAsObject(Statement.class);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return (OpenLRSEntity) statement;
    }

    @Override
    public List<OpenLRSEntity> findWithFilters(Map<String, String> filters) {
        Page<OpenLRSEntity> page = findWithFilters(filters, null);
        if (page != null) {
            return page.getContent();
        }
        return null;
    }

    @Override
    public Page<OpenLRSEntity> findWithFilters(Map<String, String> filters, Pageable pageable) {
        int offset = (pageable == null) ? 0 : pageable.getOffset();
        int pagesize = (pageable == null) ? 100 : pageable.getPageSize();
        
        String actor = filters.get(StatementUtils.ACTOR_FILTER);
        String activity = filters.get(StatementUtils.ACTIVITY_FILTER);
        String since = filters.get(StatementUtils.SINCE_FILTER);
        String until = filters.get(StatementUtils.UNTIL_FILTER);
        int limit = getLimit(filters.get(StatementUtils.LIMIT_FILTER));
        
        FilteredQueryBuilder filterQueryBuilder = null;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (StringUtils.isNotBlank(actor)) {
            String mailbox = String.format("mailto:%s@adlnet.gov", actor);
            filterQueryBuilder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.nestedFilter("actor", FilterBuilders.termFilter("actor.mbox", mailbox)));
        }
        
        if(StringUtils.isNotBlank(activity)) {
            if(filterQueryBuilder != null){
                filterQueryBuilder = QueryBuilders.filteredQuery(filterQueryBuilder, FilterBuilders.nestedFilter("object", FilterBuilders.termFilter("object.id", activity)));
            }else{
                filterQueryBuilder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.nestedFilter("object", FilterBuilders.termFilter("object.id", activity)));
            }
        }
        
        if (StringUtils.isNotBlank(since) || StringUtils.isNotBlank(until)) {
            FilterBuilder rangeFilter = null;
            if(StringUtils.isNotBlank(since) && StringUtils.isNotBlank(until))
            {
                rangeFilter = FilterBuilders.rangeFilter("stored").from(since).to(until);
            }
            else
            {
                if(StringUtils.isNotBlank(since))
                {
                    rangeFilter = FilterBuilders.rangeFilter("stored").from(since).to("now");
                }
                
                if(StringUtils.isNotBlank(until))
                {
                    try {
                        
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        TimeZone tz = TimeZone.getTimeZone("UTC");
                        formatter.setTimeZone(tz);
                        Date date = (Date)formatter.parse(until);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date); 
                        calendar.add(Calendar.YEAR,-1);
                        rangeFilter = FilterBuilders.rangeFilter("stored").from(formatter.format(calendar.getTime())).to(until);
                    } catch (ParseException e) {
                        log.error(e.getMessage(),e);
                        return null;
                    }
                }
            }

            if(filterQueryBuilder != null){
                filterQueryBuilder = QueryBuilders.filteredQuery(filterQueryBuilder, rangeFilter);
            }else{
                filterQueryBuilder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), rangeFilter);
            }
        }

        if(limit > 0){
            pagesize = limit;
        }

        searchSourceBuilder.query(filterQueryBuilder).sort("stored", SortOrder.DESC);
        Search search = new Search.Builder(searchSourceBuilder.toString())
                                        .addIndex(STATEMENT_INDEX)
                                        .addType(STATEMENT_TYPE)
                                        .setParameter("from", offset)
                                        .setParameter(Parameters.SIZE, pagesize)
                                        .build();
        
        try {
            SearchResult result = jestClient.execute(search);
            //TODO remove deprecated.
            Iterable<Statement> statements = result.getSourceAsObjectList(Statement.class);
            return createPage(statements);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return null;
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
    @Override
    public List<OpenLRSEntity> saveAll(Collection<OpenLRSEntity> entities) {
        if (entities != null && !entities.isEmpty()) {
            Builder builder = new Bulk.Builder().defaultIndex(STATEMENT_INDEX).defaultType(STATEMENT_TYPE);
            
            for (OpenLRSEntity entity : entities) {
                Statement statement = (Statement) entity;
                builder.addAction(new Index.Builder(statement.toJSON()).id(statement.getId()).build());
            }
            Bulk bulk = builder.build();
            try {
                jestClient.execute(bulk);
            } catch (IOException e) {
                log.error(e.getMessage(),e);
                e.printStackTrace();
            }
        }
        
        
        return new ArrayList<OpenLRSEntity>(entities);
    }

    public ArrayList<StatementMetadata> saveAllMetaData(Collection<StatementMetadata> metaDataEntries) {
        if (metaDataEntries != null && !metaDataEntries.isEmpty()) {
            Builder builder = new Bulk.Builder().defaultIndex(METADATA_INDEX).defaultType(METADATA_TYPE);
            
            for (StatementMetadata entry : metaDataEntries) {
                StatementMetadata statementMetadata = (StatementMetadata) entry;
                builder.addAction(new Index.Builder(statementMetadata).id(statementMetadata.getId()).build());
            }
            Bulk bulk = builder.build();
            try {
                jestClient.execute(bulk);
            } catch (IOException e) {
                log.error(e.getMessage(),e);
                e.printStackTrace();
            }
        }
        
        return new ArrayList<StatementMetadata>(metaDataEntries);
    }

    @Override
    public Page<OpenLRSEntity> findByContext(String context, Pageable pageable) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("context", context));

        Iterable<Statement> statements = queryMetadataForIdThenGetAllDocsWithIds(searchSourceBuilder, pageable);
        return createPage(statements);
    }


    @Override
    public Page<OpenLRSEntity> findByUser(String user, Pageable pageable) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("user", user));

        Iterable<Statement> statements = queryMetadataForIdThenGetAllDocsWithIds(searchSourceBuilder, pageable);
        return createPage(statements);
    }

    @Override
    public Page<OpenLRSEntity> findByContextAndUser(String context, String user, Pageable pageable) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("user", user)).must(QueryBuilders.matchQuery("context", context)));

        Iterable<Statement> statements = queryMetadataForIdThenGetAllDocsWithIds(searchSourceBuilder, pageable);
        return createPage(statements);
    }
    
    private Iterable<Statement> queryMetadataForIdThenGetAllDocsWithIds(SearchSourceBuilder searchSourceBuilder, Pageable pageable){
        try {
            //First query the metadata
            if(log.isDebugEnabled()){
                log.debug(searchSourceBuilder.toString());
            }
            Search search = new Search.Builder(searchSourceBuilder.toString())
                                        .addIndex(METADATA_INDEX)
                                        .setParameter("from", pageable.getOffset())
                                        .setParameter(Parameters.SIZE, pageable.getPageSize())
                                        .build();
            SearchResult result = jestClient.execute(search);
            List<StatementMetadata> metadata = result.getSourceAsObjectList(StatementMetadata.class);

            //Second get Doc with IDs
            List<Doc> docs = new ArrayList<Doc>();
            for(StatementMetadata d: metadata){
                docs.add(new Doc(STATEMENT_INDEX, STATEMENT_TYPE, d.getStatementId()));
            }
            
            MultiGet multiget = new MultiGet.Builder.ByDoc(docs).build();
            JestResult results = jestClient.execute(multiget);
            Iterable<Statement> statements = results.getSourceAsObjectList(Statement.class);
            return statements;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return null;
    }

    private Page<OpenLRSEntity> createPage(Iterable<Statement> statements){
        if (statements != null) {
            return new PageImpl<OpenLRSEntity>(IteratorUtils.toList(statements.iterator()));
        }
        return null;
    }
}
