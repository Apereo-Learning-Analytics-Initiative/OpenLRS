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
package org.apereo.openlrs.storage.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.storage.TierOneStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 *
 */
@Component("RedisPubSubTierOneStorage")
@Profile("redis")
public class RedisPubSubTierOneStorage implements TierOneStorage<OpenLRSEntity> {
	
	@Autowired private String channelName;	
	@Autowired private StringRedisTemplate redisTemplate;

	@Override
	public OpenLRSEntity save(OpenLRSEntity entity) {
		redisTemplate.convertAndSend(channelName, entity.toJSON());
		return entity;
	}

	@Override
	public List<OpenLRSEntity> saveAll(Collection<OpenLRSEntity> entities) {
		List<OpenLRSEntity> savedEntities = null;
		if (entities != null && !entities.isEmpty()) {
			savedEntities = new ArrayList<OpenLRSEntity>();
			for (OpenLRSEntity entity : entities) {
				OpenLRSEntity saved = save(entity);
				savedEntities.add(saved);
			}
		}
		return savedEntities;
	}

}
