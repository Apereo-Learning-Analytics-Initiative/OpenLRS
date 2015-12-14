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
import org.apereo.openlrs.storage.StorageFactory;
import org.apereo.openlrs.storage.TierOneStorage;
import org.apereo.openlrs.storage.TierTwoStorage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ggilbert
 *
 */
public abstract class EventService {
	
	@Autowired protected StorageFactory storageFactory;	
	private TierOneStorage<OpenLRSEntity> tierOneStorage;
	private TierTwoStorage<OpenLRSEntity> tierTwoStorage;
	
	protected TierOneStorage<OpenLRSEntity> getTierOneStorage() {
		if (tierOneStorage == null) {
			tierOneStorage = storageFactory.getTierOneStorage();
		}
		return tierOneStorage;
	}
	
	protected TierTwoStorage<OpenLRSEntity> getTierTwoStorage() {
		if (tierTwoStorage == null) {
			tierTwoStorage = storageFactory.getTierTwoStorage();
		}
		return tierTwoStorage;
	}


}
