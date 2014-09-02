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
package org.apereo.openlrs.model.statement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author ggilbert
 *
 */
@JsonInclude(Include.NON_NULL)
public class XApiContextActivities {
	private XApiObject parent;
	private XApiObject grouping;
	private XApiObject category;
	private XApiObject other;
	/**
	 * @return the parent
	 */
	public XApiObject getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(XApiObject parent) {
		this.parent = parent;
	}
	/**
	 * @return the grouping
	 */
	public XApiObject getGrouping() {
		return grouping;
	}
	/**
	 * @param grouping the grouping to set
	 */
	public void setGrouping(XApiObject grouping) {
		this.grouping = grouping;
	}
	/**
	 * @return the category
	 */
	public XApiObject getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(XApiObject category) {
		this.category = category;
	}
	/**
	 * @return the other
	 */
	public XApiObject getOther() {
		return other;
	}
	/**
	 * @param other the other to set
	 */
	public void setOther(XApiObject other) {
		this.other = other;
	}
}
