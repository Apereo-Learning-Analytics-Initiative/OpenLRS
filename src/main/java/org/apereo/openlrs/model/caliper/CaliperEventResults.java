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
package org.apereo.openlrs.model.caliper;

import java.util.List;

/**
 * TODO Document this file
 * @author steve cody scody@unicon.net
 */
public class CaliperEventResults {
	
	private List<CaliperEvent> caliperEvents;
	
	public CaliperEventResults(List<CaliperEvent> caliperEvents) {
		this.caliperEvents = caliperEvents;
	}

	public List<CaliperEvent> getEvents() {
		return caliperEvents;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StatementResult [statements=" + caliperEvents + "]";
	}

}