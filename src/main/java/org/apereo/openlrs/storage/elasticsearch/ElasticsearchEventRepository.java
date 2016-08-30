/**
 * 
 */
package org.apereo.openlrs.storage.elasticsearch;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 *
 */
@ConditionalOnProperty(name="openlrs.reader", havingValue="ElasticsearchReader")
@Component
public interface ElasticsearchEventRepository extends ElasticsearchRepository<EventElasticsearch, String> {
  Page<EventElasticsearch> findByTenantId(String tenantId, Pageable pageable);
  
  @Query("{ \"bool\":{ \"must\":[ {\"query_string\":{\"query\":\"?0\",\"fields\":[\"tenantId\"]}},{\"query_string\":{\"query\":\"?1\",\"fields\":[\"event.group.@id\"]}}]}}")
  Page<EventElasticsearch> findByTenantIdAndEventGroupIdWhereGroupIdContains(String tenantId, String context, Pageable pageable);
                           
  Page<EventElasticsearch> findByTenantIdAndEventGroupIdAndEventActorId(String tenantId, String context, String user, Pageable pageable);
  Page<EventElasticsearch> findByTenantIdAndEventActorId(String tenantId, String user, Pageable pageable);
  EventElasticsearch findByTenantIdAndEventId(String tenantId, String eventId);
}



