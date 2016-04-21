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
package org.apereo.openlrs.services;

import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.model.event.Event;
import org.apereo.openlrs.model.event.EventConversionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author ggilbert
 * @author Lance E Sloan (lsloan at umich dot edu)
 */
@Service
public class NormalizedEventService extends EventService {
	private final Logger logger = LoggerFactory.getLogger(NormalizedEventService.class);

	@Autowired private EventConversionService eventConversionService;

    public Page<Event> getByContext(String context, Pageable pageable) {
    	Page<OpenLRSEntity> page = getTierTwoStorage().findByContext(context,pageable);   	
    	return eventConversionService.toEventPage(page);
    }
    
    public Event getBySourceId(String sourceId) {
    	OpenLRSEntity entity = getTierTwoStorage().findBySourceId(sourceId);
    	return eventConversionService.toEvent(entity);
    }

    public Page<Event> getByUser(String user, Pageable pageable) {
    	Page<OpenLRSEntity> page = getTierTwoStorage().findByUser(user,pageable);
    	return eventConversionService.toEventPage(page);
    }
    
    public Page<Event> getByContextAndUser(String context,String user, Pageable pageable) {
    	Page<OpenLRSEntity> page = getTierTwoStorage().findByContextAndUser(context,user,pageable);
    	return eventConversionService.toEventPage(page);
    }
}
