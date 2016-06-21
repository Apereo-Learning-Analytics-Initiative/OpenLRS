/**
 * 
 */
package org.apereo.openlrs.storage.elasticsearch;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author ggilbert
 *
 */
@ConditionalOnProperty(name="openlrs.reader", havingValue="ElasticsearchReader")
@Component
public interface ElasticsearchEventRepository extends ElasticsearchRepository<EventElasticsearch, String> {
  Page<EventElasticsearch> findByTenantId(String tenantId, Pageable pageable);
  EventElasticsearch findByTenantIdAndEventId(String tenantId, String eventId);
}
