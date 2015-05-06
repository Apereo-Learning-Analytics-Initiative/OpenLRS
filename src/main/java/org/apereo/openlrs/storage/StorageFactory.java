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
package org.apereo.openlrs.storage;

import java.util.Map;

import org.apereo.openlrs.model.OpenLRSEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 *
 */
@Component
public class StorageFactory {

	@Value("${openlrs.tierOneStorage:InMemoryStorage}")
	private String tierOneStorage;
	@Autowired private Map<String, TierOneStorage<OpenLRSEntity>> tierOneStorageOptions;
	
	@Value("${openlrs.tierTwoStorage:InMemoryStorage}")
	private String tierTwoStorage;
	@Autowired private Map<String, TierTwoStorage<OpenLRSEntity>> tierTwoStorageOptions;
	
	public TierOneStorage<OpenLRSEntity> getTierOneStorage() {
System.out.println("******************");
System.out.println(tierOneStorage);
System.out.println(tierOneStorageOptions);
System.out.println("**********************");
		return tierOneStorageOptions.get(tierOneStorage);
	}
	
	public TierOneStorage<OpenLRSEntity> getTierOneStorage(final String tierOneStorageType) {
		return tierOneStorageOptions.get(tierOneStorageType);
	}
	
	public TierTwoStorage<OpenLRSEntity> getTierTwoStorage() {
System.out.println("******************");
System.out.println(tierTwoStorage);
System.out.println(tierTwoStorageOptions);
System.out.println("**********************");
		return tierTwoStorageOptions.get(tierTwoStorage);
	}
	
	public TierTwoStorage<OpenLRSEntity> getTierTwoStorage(final String tierTwoStorageType) {
		return tierTwoStorageOptions.get(tierTwoStorageType);
	}

}
