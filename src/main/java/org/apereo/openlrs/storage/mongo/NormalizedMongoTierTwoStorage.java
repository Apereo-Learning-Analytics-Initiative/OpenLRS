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
package org.apereo.openlrs.storage.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.model.event.Event;
import org.apereo.openlrs.model.event.EventConversionService;
import org.apereo.openlrs.repositories.event.MongoEventRepository;
import org.apereo.openlrs.storage.TierTwoStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 * @author Lance E Sloan (lsloan at umich dot edu)
 */
@Component("NormalizedMongoTierTwoStorage")
@Profile("mongo")
public class NormalizedMongoTierTwoStorage implements TierTwoStorage<OpenLRSEntity> {
    private Logger logger = LoggerFactory.getLogger(NormalizedMongoTierTwoStorage.class);

    @Autowired private MongoEventRepository mongoEventRepository;
    @Autowired private EventConversionService eventConversionService;

    @Override
    public OpenLRSEntity findById(String id) {
        return mongoEventRepository.findById(id);
    }

    @Override
    public OpenLRSEntity findBySourceId(String sourceId) {
        return mongoEventRepository.findBySourceId(sourceId);
    }

    @Override
    public OpenLRSEntity save(OpenLRSEntity entity) {
        Event olrsEvent = eventConversionService.toEvent(entity);
        return mongoEventRepository.save(olrsEvent);
    }

    @Override
    public List<OpenLRSEntity> saveAll(Collection<OpenLRSEntity> entities) {
        List<OpenLRSEntity> retval = null;

        if (entities != null && !entities.isEmpty()) {
            List<Event> events = new ArrayList<Event>();
            for (OpenLRSEntity openLRSEntity : entities) {
                events.add(eventConversionService.toEvent(openLRSEntity));
            }

            retval = (List<OpenLRSEntity>) (List<?>) mongoEventRepository.save(events);

        }

        return retval;
    }

    @Override
    public Page<OpenLRSEntity> findAll(Pageable pageable) {
        return (Page<OpenLRSEntity>) (Page<?>) mongoEventRepository.findAll(pageable);
    }

    @Override
    public Page<OpenLRSEntity> findWithFilters(Map<String, String> filters, Pageable pageable) {
        throw new UnsupportedOperationException("OpenLRS searching by filters not yet implemented for MongoDB");
    }

    @Override
    public List<OpenLRSEntity> findAll() {
        return (List<OpenLRSEntity>) (List<?>) mongoEventRepository.findAll();
    }

    @Override
    @Query
    public List<OpenLRSEntity> findWithFilters(Map<String, String> filters) {
        Sort sortSpec = new Sort(Sort.Direction.DESC, "id"); // Most recent events first
        return (List<OpenLRSEntity>) (List<?>) mongoEventRepository.findAll(sortSpec);
    }

    @Override
    public Page<OpenLRSEntity> findByContext(String context, Pageable pageable) {
        return (Page<OpenLRSEntity>) (Page<?>) mongoEventRepository.findBySourceId(context, pageable);
    }

    @Override
    public Page<OpenLRSEntity> findByUser(String user, Pageable pageable) {
        return (Page<OpenLRSEntity>) (Page<?>) mongoEventRepository.findByActor(user, pageable);
    }

    @Override
    public Page<OpenLRSEntity> findByContextAndUser(String context,
                                                    String user, Pageable pageable) {
        return (Page<OpenLRSEntity>) (Page<?>) mongoEventRepository.findByActorAndContext(user, context, pageable);
    }

}
