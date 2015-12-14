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
package org.apereo.openlrs.repositories.event;

import org.apereo.openlrs.model.event.Event;
import org.apereo.openlrs.model.xapi.StatementMetadata;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ggilbert
 *
 */
@Repository
@Profile("elasticsearch")
public interface ElasticsearchEventRepository extends ElasticsearchRepository<Event,String> {
	Event findBySourceId(String sourceId);
	Page<Event> findByActor(String actor, Pageable pageable);
	Page<Event> findByContext(String context, Pageable pageable);
	Page<Event> findByActorAndContext(String actor, String context, Pageable pageable);

}
