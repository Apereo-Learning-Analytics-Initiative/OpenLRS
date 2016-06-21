/**
 * 
 */
package org.apereo.openlrs.storage.elasticsearch;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.params.Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apereo.openlrs.model.event.Event;
import org.apereo.openlrs.storage.Reader;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ggilbert
 *
 */
@ConditionalOnProperty(name="openlrs.reader", havingValue="AwsElasticsearchReader")
@Component("AwsElasticsearchReader")
public class AwsElasticsearchReader implements Reader {
  
  private Logger log = LoggerFactory.getLogger(AwsElasticsearchReader.class);
  
  @Value("${aws.es.connectionUrl}")
  private String connectionUrl;
  
  @Autowired private JestClient jestClient;
  @Autowired private ObjectMapper objectMapper;
  
  @Override
  public Page<Event> findByTenantId(String tenantId, Pageable pageable) {
    
    Page<Event> page = null;
    
    int offset = (pageable == null) ? 0 : pageable.getOffset();
    int pagesize = (pageable == null) ? 1000 : pageable.getPageSize();
    
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
    

    Search search = new Search.Builder(searchSourceBuilder.toString())
    .addIndex(tenantId)
    .setParameter("from", offset)
    .setParameter(Parameters.SIZE, pagesize)
    .build();
    
    try {
      SearchResult result = jestClient.execute(search);
      if (result != null) {
        log.info(result.getJsonString());
        
        JsonNode resultsNode = objectMapper.readTree(result.getJsonString());
        if (resultsNode != null) {
          JsonNode hitsNode = resultsNode.get("hits");
          int total = hitsNode.get("total").intValue();
          if (total > 0) {
            JsonNode hitsArrayNode = hitsNode.get("hits");
            if (hitsArrayNode != null && hitsArrayNode.isArray()) {
              List<Event> events = new ArrayList<>();
              Iterator<JsonNode> i = hitsArrayNode.elements();
              while(i.hasNext()) {
                JsonNode hit = i.next();
                JsonNode source = hit.get("_source");
                log.info(source.toString());
                Event event = objectMapper.readValue(source.toString(), Event.class);
                events.add(event);
              }
              page = new PageImpl<Event>(events, pageable, events.size());
            }
          }
        }
        
        
//        List<Event> events = result.getSourceAsObjectList(Event.class);
//        page = new PageImpl<Event>(events, pageable, events.size());
        
//        List<Hit<Event, Void>> hits = result.getHits();
//        if (hits != null && !hits.isEmpty()) {
//          List<Event> events = new LinkedList<Event>();
//          for (Hit<Event,Void> hit : hits) {
//            events.add(hit.source);
//          }
//          
//          page = new PageImpl<Event>(events, pageable, events.size());
//        }
      }
    } 
    catch (IOException e) {
      log.error(e.getMessage(),e);
    }

    return page;
  }

  @Override
  public Event findByTenantIdAndEventId(String tenantId, String eventId) {
    Event event = null;
    
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("id", eventId));
    

    Search search = new Search.Builder(searchSourceBuilder.toString())
    .addIndex(tenantId)
    .build();
    
    try {
      SearchResult result = jestClient.execute(search);
      if (result != null) {
        List<Hit<Event, Void>> hits = result.getHits(Event.class);
        if (hits != null && !hits.isEmpty()) {
          List<Event> events = new LinkedList<Event>();
          for (Hit<Event,Void> hit : hits) {
            events.add(hit.source);
          }
          event = events.get(0);
        }
      }
    } 
    catch (IOException e) {
      log.error(e.getMessage(),e);
    }

    return event;
  }

  @Override
  public Event save(Event event, String tenantId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Event> saveAll(Collection<Event> events, String tenantId) {
    throw new UnsupportedOperationException();
  }

}
