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
import java.util.Map;

import org.apereo.openlrs.model.OpenLRSEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author ggilbert
 * @author Lance E Sloan (lsloan at umich dot edu)
 */
public interface TierTwoStorage<V extends OpenLRSEntity> {
    V findById(String id);

    V findBySourceId(String sourceId);

    List<V> findAll();

    Page<V> findAll(Pageable pageable);

    List<V> findWithFilters(Map<String, String> filters);

    Page<V> findWithFilters(Map<String, String> filters, Pageable pageable);

    Page<V> findByContext(String context, Pageable pageable);

    Page<V> findByUser(String user, Pageable pageable);

    Page<V> findByContextAndUser(String context, String user, Pageable pageable);

    V save(OpenLRSEntity entity);

    List<V> saveAll(Collection<V> entities);
}
