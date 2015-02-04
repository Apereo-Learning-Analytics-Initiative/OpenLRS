/**
 * Copyright 2014 Unicon (R) Licensed under the
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
package org.apereo.openlrs.repositories.caliperevents;

import java.util.List;
import java.util.Map;

import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.repositories.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * TODO Document this file
 * @author steve cody scody@unicon.net
 */
@org.springframework.stereotype.Repository("TwoTierCaliperEventRepository")
public class TwoTierCaliperEventRepository implements Repository<CaliperEvent> {
	
	@Value("${caliper.events.repository.twotier.write}") private String writeRepositoryType;
	@Value("${caliper.events.repository.twotier.read}") private String readRepositoryType;
	
	@Autowired private CaliperEventRepositoryFactory caliperEventRepositoryFactory;
	
	private Repository<CaliperEvent> writeRepository;
	private Repository<CaliperEvent> readRepository;

	@Override
	public CaliperEvent post(CaliperEvent entity) {
		return getWriteRepository().post(entity);
	}

	@Override
	public CaliperEvent get(CaliperEvent key) {
		return getReadRepository().get(key);
	}

	@Override
	public List<CaliperEvent> get() {
		return getReadRepository().get();
	}
	
	private Repository<CaliperEvent> getWriteRepository() {
		if (writeRepository == null) {
			writeRepository = caliperEventRepositoryFactory.getRepository(writeRepositoryType);
		}
		return writeRepository;
	}
	
	private Repository<CaliperEvent> getReadRepository() {
		if (readRepository == null) {
			readRepository = caliperEventRepositoryFactory.getRepository(readRepositoryType);
		}
		return readRepository;
	}

	@Override
	public List<CaliperEvent> get(Map<String, String> filters) {
		return getReadRepository().get(filters);
	}

	@Override
	public List<CaliperEvent> getByUser(String userId) {
		return getReadRepository().getByUser(userId);
	}

	@Override
	public List<CaliperEvent> getByContext(String context) {
		return getReadRepository().getByContext(context);
	}

	@Override
	public List<CaliperEvent> getByContextAndUser(String context, String userId) {
		return getReadRepository().getByContextAndUser(context, userId);
	}

}
