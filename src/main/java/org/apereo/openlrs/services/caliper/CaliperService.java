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
package org.apereo.openlrs.services.caliper;

import org.apache.log4j.Logger;
import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.services.EventService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author ggilbert
 *
 */
@Service
@Profile({"redis","mongo"})
public class CaliperService extends EventService {

	private Logger log = Logger.getLogger(CaliperService.class);
	
	public void post(String organizationId, CaliperEvent caliperEvent) {
    	if (log.isDebugEnabled()) {
    		log.debug(String.format("Caliper event: %s",caliperEvent));
    	}
    	
        getTierOneStorage().save(caliperEvent);
	}
	
}
