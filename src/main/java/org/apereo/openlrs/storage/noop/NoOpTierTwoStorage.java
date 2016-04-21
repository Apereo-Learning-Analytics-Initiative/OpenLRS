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
package org.apereo.openlrs.storage.noop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.storage.TierTwoStorage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 *
 */
@Component("NoOpTierTwoStorage")
public class NoOpTierTwoStorage implements TierTwoStorage<OpenLRSEntity> {
	private Logger log = Logger.getLogger(NoOpTierTwoStorage.class);

	/* (non-Javadoc)
	 * @see org.apereo.openlrs.storage.TierTwoStorage#findById(java.lang.String)
	 */
	@Override
	public OpenLRSEntity findById(String id) {
		log.warn("Using NoOp Tier Two Storage");
		return null;
	}

	@Override
	public OpenLRSEntity findBySourceId(String sourceId) {
		log.warn("Using NoOp Tier Two Storage");
		return null;
	}

	@Override
	public OpenLRSEntity save(OpenLRSEntity entity) {
		log.warn("Using NoOp Tier Two Storage");
		return entity;
	}

	@Override
	public List<OpenLRSEntity> saveAll(Collection<OpenLRSEntity> entities) {
		log.warn("Using NoOp Tier Two Storage");
		return new ArrayList<OpenLRSEntity>(entities);
	}

	@Override
	public Page<OpenLRSEntity> findAll(Pageable pageable) {
		log.warn("Using NoOp Tier Two Storage");
		return null;
	}

	@Override
	public Page<OpenLRSEntity> findWithFilters(Map<String, String> filters,Pageable pageable) {
		log.warn("Using NoOp Tier Two Storage");
		return null;
	}

	@Override
	public List<OpenLRSEntity> findAll() {
		log.warn("Using NoOp Tier Two Storage");
		return null;
	}

	@Override
	public List<OpenLRSEntity> findWithFilters(Map<String, String> filters) {
		log.warn("Using NoOp Tier Two Storage");
		return null;
	}

	@Override
	public Page<OpenLRSEntity> findByContext(String context, Pageable pageable) {
		log.warn("Using NoOp Tier Two Storage");
		return null;
	}

	@Override
	public Page<OpenLRSEntity> findByUser(String user, Pageable pageable) {
		log.warn("Using NoOp Tier Two Storage");
		return null;
	}

	@Override
	public Page<OpenLRSEntity> findByContextAndUser(String context,
			String user, Pageable pageable) {
		log.warn("Using NoOp Tier Two Storage");
		return null;
	}

}
