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
package org.apereo.openlrs.storage.elasticsearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.model.event.Event;
import org.apereo.openlrs.model.event.EventConversionService;
import org.apereo.openlrs.repositories.event.ElasticsearchEventRepository;
import org.apereo.openlrs.storage.TierTwoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 *
 */
@Component("NormalizedElasticsearchTierTwoStorage")
@Profile("elasticsearch")
public class NormalizedElasticsearchTierTwoStorage implements TierTwoStorage<OpenLRSEntity> {
	
	@Autowired private ElasticsearchEventRepository elasticsearchEventRepository;
	@Autowired private EventConversionService eventConversionService;

	@Override
	public OpenLRSEntity findById(String id) {
		return elasticsearchEventRepository.findBySourceId(id);
	}

	@Override
	public OpenLRSEntity save(OpenLRSEntity entity) {
		Event event = eventConversionService.toEvent(entity);
		return elasticsearchEventRepository.save(event);
	}

	@Override
	public List<OpenLRSEntity> saveAll(Collection<OpenLRSEntity> entities) {
		List<OpenLRSEntity> retval = null;
		
		if (entities != null && !entities.isEmpty()) {
			List<Event> events = new ArrayList<Event>();
			for (OpenLRSEntity openLRSEntity : entities) {
				events.add(eventConversionService.toEvent(openLRSEntity));
			}
			
			retval = (List<OpenLRSEntity>)(List<?>)elasticsearchEventRepository.save(events);
			
		}
		
		return retval;
	}

	@Override
	public Page<OpenLRSEntity> findAll(Pageable pageable) {
		return (Page<OpenLRSEntity>)(Page<?>)elasticsearchEventRepository.findAll(pageable);
	}

	@Override
	public Page<OpenLRSEntity> findWithFilters(Map<String, String> filters,
			Pageable pageable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<OpenLRSEntity> findAll() {
		Iterable<Event> events = elasticsearchEventRepository.findAll();
		return (List<OpenLRSEntity>)(List<?>)IteratorUtils.toList(events.iterator());
	}

	@Override
	public List<OpenLRSEntity> findWithFilters(Map<String, String> filters) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Page<OpenLRSEntity> findByContext(String context, Pageable pageable) {
		return (Page<OpenLRSEntity>)(Page<?>)elasticsearchEventRepository.findByContext(context,pageable);	
	}

	@Override
	public Page<OpenLRSEntity> findByUser(String user, Pageable pageable) {
		return (Page<OpenLRSEntity>)(Page<?>)elasticsearchEventRepository.findByActor(user,pageable);	
	}

	@Override
	public Page<OpenLRSEntity> findByContextAndUser(String context,
			String user, Pageable pageable) {
		return (Page<OpenLRSEntity>)(Page<?>)elasticsearchEventRepository.findByActorAndContext(user,context,pageable);	
	}

}
