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
package org.apereo.openlrs.services.xapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apereo.openlrs.exceptions.xapi.StatementStateConflictException;
import org.apereo.openlrs.model.OpenLRSEntity;
import org.apereo.openlrs.model.event.Event;
import org.apereo.openlrs.model.event.EventConversionService;
import org.apereo.openlrs.model.xapi.Statement;
import org.apereo.openlrs.model.xapi.StatementResult;
import org.apereo.openlrs.services.EventService;
import org.apereo.openlrs.utils.TimestampUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author ggilbert
 *
 */
@Service
public class XApiService extends EventService {
  private Logger log = Logger.getLogger(XApiService.class);

  @Autowired
  private EventConversionService eventConversionService;

  public List<String> post(String organizationId, Statement statement) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("New Statement: %s", statement));
    }

    if (StringUtils.isBlank(statement.getId())) {
      statement.setId(UUID.randomUUID().toString());
    } else {
      // TODO - Need to also check tier one
      // check for conflict
      OpenLRSEntity entity = getTierTwoStorage().findById(statement.getId());
      if (entity != null) {
        Statement existingStatement = eventConversionService.toXApi(entity);
        // set the stored timestamps null for comparison purposes
        statement.setStored(null);
        existingStatement.setStored(null);

        String json = statement.toJSON();
        String json_existing = existingStatement.toJSON();
        if (json.equals(json_existing)) {
          throw new StatementStateConflictException(
              String.format("Matching statement for id: %s already exists",
                  statement.getId()));
        }
      }
    }
    statement.setStored(TimestampUtils.getISO8601StringForDate(new Date()));
    Statement savedStatement = (Statement) getTierOneStorage().save(statement);

    if (log.isDebugEnabled()) {
      log.debug(String.format("Saved Statement: %s", savedStatement));
    }

    List<String> statementIds = new ArrayList<String>();
    statementIds.add(savedStatement.getId());

    return statementIds;
  }

  public Statement get(String id) {
    OpenLRSEntity entity = getTierTwoStorage().findById(id);
    return eventConversionService.toXApi(entity);
  }

  public StatementResult get(Map<String, String> filterMap) {

    StatementResult result = null;
    List<OpenLRSEntity> entities = null;

    if (filterMap != null && !filterMap.isEmpty()) {
      entities = getTierTwoStorage().findWithFilters(filterMap);
    } else {
      entities = getTierTwoStorage().findAll();
    }

    result = eventConversionService.toXApiCollection(entities);

    return result;
  }

  public Page<Statement> getByContext(String context, Pageable pageable) {
    Page<OpenLRSEntity> page = getTierTwoStorage().findByContext(context,
        pageable);

    return eventConversionService.toXApiPage(page);
  }

  public Page<Statement> getByUser(String user, Pageable pageable) {
    Page<OpenLRSEntity> page = getTierTwoStorage().findByUser(user, pageable);
    return eventConversionService.toXApiPage(page);
  }

  public Page<Statement> getByContextAndUser(String context, String user,
      Pageable pageable) {
    Page<OpenLRSEntity> page = getTierTwoStorage().findByContextAndUser(
        context, user, pageable);
    return eventConversionService.toXApiPage(page);
  }

}
