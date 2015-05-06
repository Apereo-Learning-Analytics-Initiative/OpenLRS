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

import java.util.Collection;
import java.util.List;

import org.apereo.openlrs.model.OpenLRSEntity;

/**
 * @author ggilbert
 *
 */
public interface TierOneStorage<V extends OpenLRSEntity> {
	V save(V entity);
	List<V> saveAll(Collection<V> entities);
}
