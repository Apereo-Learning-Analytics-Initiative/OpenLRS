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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.repositories.Repository;
import org.apereo.openlrs.utils.StatementUtils;

/**
 * TODO Document this file
 * @author steve cody scody@unicon.net
 */
@org.springframework.stereotype.Repository("InMemoryCaliperEventRepository")
public class InMemoryCaliperEventRepository implements Repository<CaliperEvent> {
	
	private static Map<String, CaliperEvent> store = new HashMap<String, CaliperEvent>();

	@Override
	public CaliperEvent post(CaliperEvent entity) {
		store.put(entity.getKey(), entity);
		return entity;
	}

	@Override
	public CaliperEvent get(CaliperEvent key) {
		return store.get(key.getKey());
	}

	@Override
	public List<CaliperEvent> get() {
		return new ArrayList<CaliperEvent>(store.values());
	}

	@Override
	public List<CaliperEvent> get(Map<String, String> filters) {
		List<CaliperEvent> caliperEvents = get();
		if (caliperEvents != null && !caliperEvents.isEmpty()) {
			String actor = filters.get(StatementUtils.ACTOR_FILTER);
			String activity = filters.get(StatementUtils.ACTIVITY_FILTER);
			
			List<CaliperEvent> filteredStatements = null;
			for (CaliperEvent caliperEvent : caliperEvents) {
				if (StringUtils.isNotBlank(actor) && StringUtils.isNotBlank(activity)) {
					if (caliperEvent.getId().equals(activity)) {
						if (filteredStatements == null) {
							filteredStatements = new ArrayList<CaliperEvent>();
						}
						filteredStatements.add(caliperEvent);
					}
				}
				else if (StringUtils.isNotBlank(actor)) {
					
				}
				else if (StringUtils.isNotBlank(activity)) {					
					if (caliperEvent.getId().equals(activity)) {
						if (filteredStatements == null) {
							filteredStatements = new ArrayList<CaliperEvent>();
						}

						filteredStatements.add(caliperEvent);
					}
				}
			}
			return filteredStatements;
		}
		
		
		return caliperEvents;
	}

	
	/**
	 * TODO: MUST IMPLEMENT
	 */
	@Override
	public List<CaliperEvent> getByUser(String userId) {
		
		List<CaliperEvent> caliperEvents = get();
		/*
		if (caliperEvents != null && !caliperEvents.isEmpty()) {			
			List<CaliperEventL> filteredStatements = null;
			for (CaliperEvent caliperEvent : caliperEvents) {
				if (caliperEvent.toJSON().contains(userId)) {
					if (filteredStatements == null) {
						filteredStatements = new ArrayList<CaliperEventL>();
					}

					filteredStatements.add(caliperEvent);
				}
			}
			return filteredStatements;
		}
		return null;
		*/
		return caliperEvents;
	}

	@Override
	public List<CaliperEvent> getByContext(String context) {
		List<CaliperEvent> caliperEvents = get();
		if (caliperEvents != null && !caliperEvents.isEmpty()) {			
			List<CaliperEvent> filteredStatements = null;
			for (CaliperEvent caliperEvent : caliperEvents) {
				if (caliperEvent.toJSON().contains(context)) {
					if (filteredStatements == null) {
						filteredStatements = new ArrayList<CaliperEvent>();
					}

					filteredStatements.add(caliperEvent);
				}
			}
			return filteredStatements;
		}
		return null;
	}

	@Override
	public List<CaliperEvent> getByContextAndUser(String context, String userId) {
		List<CaliperEvent> caliperEvents = get();
		if (caliperEvents != null && !caliperEvents.isEmpty()) {			
			List<CaliperEvent> filteredStatements = null;
			for (CaliperEvent caliperEvent : caliperEvents) {
				if (caliperEvent.toJSON().contains(context) && caliperEvent.toJSON().contains(userId)) {
					if (filteredStatements == null) {
						filteredStatements = new ArrayList<CaliperEvent>();
					}

					filteredStatements.add(caliperEvent);
				}
			}
			return filteredStatements;
		}
		return null;
	}

}
