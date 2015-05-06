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
package org.apereo.openlrs.storage.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.storage.TierOneStorage;
import org.apereo.openlrs.storage.TierTwoStorage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 *
 */
@Component("InMemoryStorage")
public class InMemoryStorage implements TierOneStorage<OpenLRSEntity>, TierTwoStorage<OpenLRSEntity> {
	private Logger log = Logger.getLogger(InMemoryStorage.class);
	private static Map<String, OpenLRSEntity> store = new HashMap<String, OpenLRSEntity>();

	@Override
	public OpenLRSEntity save(OpenLRSEntity entity) {
		store.put(entity.getKey(), entity);
		return entity;
	}

	@Override
	public List<OpenLRSEntity> saveAll(Collection<OpenLRSEntity> entities) {
		if (entities != null && !entities.isEmpty()) {
			for (OpenLRSEntity entity : entities) {
				save(entity);
			}
		}
		return new ArrayList<OpenLRSEntity>(entities);
	}

	@Override
	public OpenLRSEntity findById(String id) {
		return store.get(id);
	}
	
	@Override
	public List<OpenLRSEntity> findAll() {
		return new ArrayList<OpenLRSEntity>(store.values());
	}

	@Override
	public List<OpenLRSEntity> findWithFilters(Map<String, String> filters) {
		log.warn("InMemoryStorage does not support filters. Return all.");
		return new ArrayList<OpenLRSEntity>(store.values());
	}


	@Override
	public Page<OpenLRSEntity> findAll(Pageable pageable) {
		return new PageImpl<OpenLRSEntity>(new ArrayList<OpenLRSEntity>(store.values()));
	}

	@Override
	public Page<OpenLRSEntity> findWithFilters(Map<String, String> filters,
			Pageable pageable) {
		log.warn("InMemoryStorage does not support filters. Return all.");
		return new PageImpl<OpenLRSEntity>(new ArrayList<OpenLRSEntity>(store.values()));
	}
	
	@Override
	public Page<OpenLRSEntity> findByUser(String userId, Pageable pageable) {
		Collection<OpenLRSEntity> values = store.values();
		if (values != null && !values.isEmpty()) {			
			List<OpenLRSEntity> filtered = null;
			for (OpenLRSEntity entity : values) {
				if (entity.toJSON().contains(userId)) {
					if (filtered == null) {
						filtered = new ArrayList<OpenLRSEntity>();
					}

					filtered.add(entity);
				}
			}
			return new PageImpl<OpenLRSEntity>(filtered);
		}
		return null;
	}

	@Override
	public Page<OpenLRSEntity> findByContext(String context, Pageable pageable) {
		Collection<OpenLRSEntity> values = store.values();
		if (values != null && !values.isEmpty()) {			
			List<OpenLRSEntity> filtered = null;
			for (OpenLRSEntity entity : values) {
				if (entity.toJSON().contains(context)) {
					if (filtered == null) {
						filtered = new ArrayList<OpenLRSEntity>();
					}

					filtered.add(entity);
				}
			}
			return new PageImpl<OpenLRSEntity>(filtered);
		}
		return null;
	}

	@Override
	public Page<OpenLRSEntity> findByContextAndUser(String context, String userId, Pageable pageable) {
		Collection<OpenLRSEntity> values = store.values();
		if (values != null && !values.isEmpty()) {			
			List<OpenLRSEntity> filtered = null;
			for (OpenLRSEntity entity : values) {
				if (entity.toJSON().contains(context) && entity.toJSON().contains(userId)) {
					if (filtered == null) {
						filtered = new ArrayList<OpenLRSEntity>();
					}

					filtered.add(entity);
				}
			}
			return new PageImpl<OpenLRSEntity>(filtered);
		}
		return null;
	}
}
