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

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.log4j.Logger;
import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.model.event.EventConversionService;
import org.apereo.openlrs.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ggilbert
 * @author Lance E Sloan (lsloan at umich dot edu)
 */
@Service
public class CaliperService extends EventService {

    private Logger log = Logger.getLogger(CaliperService.class);

    @Autowired
    private EventConversionService eventConversionService;

    public void post(String organizationId, CaliperEvent caliperEvent) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Caliper event: %s", caliperEvent));
        }

        getTierOneStorage().save(caliperEvent);
    }

    public JsonNode getJsonNode(String id) {
        OpenLRSEntity entity = getTierTwoStorage().findById(id);
        return eventConversionService.toCaliperJson(entity);
    }

    public List<JsonNode> getJsonNodes(Map<String, String> filterMap) {
        List<JsonNode> result = null;
        List<OpenLRSEntity> entities = null;

        if (filterMap != null && !filterMap.isEmpty()) {
            entities = getTierTwoStorage().findWithFilters(filterMap);
        } else {
            entities = getTierTwoStorage().findAll();
        }

        result = eventConversionService.toCaliperJsonList(entities);

        return result;
    }

    public Page<JsonNode> getJsonNodes(Map<String, String> filterMap, Pageable pageable) {
        Page<OpenLRSEntity> entities = getTierTwoStorage().findAll(pageable);
        return eventConversionService.toCaliperJsonPage(entities, pageable);
    }
}
