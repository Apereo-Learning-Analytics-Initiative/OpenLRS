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
package org.apereo.openlrs.controllers.xapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.exceptions.NotFoundException;
import org.apereo.openlrs.exceptions.xapi.InvalidXAPIRequestException;
import org.apereo.openlrs.model.xapi.Statement;
import org.apereo.openlrs.model.xapi.StatementResult;
import org.apereo.openlrs.services.xapi.XApiService;
import org.apereo.openlrs.utils.StatementUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller to handle GET and POST calls see
 * https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#stmtapi
 * 
 * @author Robert E. Long (rlong @ unicon.net)
 */
@RestController
@RequestMapping("/xAPI/statements")
public class StatementController {

  private final Logger logger = LoggerFactory
      .getLogger(StatementController.class);
  private ObjectMapper objectMapper;
  private Validator validator;
  private final XApiService xapiService;

  @Autowired
  public StatementController(XApiService xapiService,
      ObjectMapper objectMapper, Validator validator) {
    this.xapiService = xapiService;
    this.objectMapper = objectMapper;
    this.validator = validator;
  }

  @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=utf-8", params = "statementId")
  public Statement getStatement(
      @RequestParam(value = "statementId", required = true) String statementId,
      @RequestParam Map<String, String> allRequestParams) {
    logger.debug("Statement GET request received with parameters: {}",
        allRequestParams);
    if (allRequestParams.containsKey("voidedStatementId")) {
      throw new InvalidXAPIRequestException(
          "Cannot submit both 'statementId' and 'voidedStatementId' parameters.");
    }
    Statement result = null;
    try {
      result = xapiService.get(statementId);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new InvalidXAPIRequestException(e.getMessage(), e);
    }
    if (result == null) {
      throw new NotFoundException("Statement for ID [" + statementId
          + "] not found.");
    }
    return result;
  }

  /**
   * Get statement objects for the specified criteria
   * 
   * @param actor
   *          the ID of the actor
   * @param activity
   *          the activity
   * @return JSON string of the statement objects matching the specified filter
   */
  @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=utf-8", params = "!statementId")
  public StatementResult getStatement(
      @RequestParam(value = "actor", required = false) String actor,
      @RequestParam(value = "activity", required = false) String activity,
      @RequestParam(value = "since", required = false) String since,
      @RequestParam(value = "until", required = false) String until,
      @RequestParam(value = "limit", required = false) String limit) {

    logger.debug("getStatement with actor: {} and activity: {}", actor,
        activity);

    try {
      Map<String, String> filterMap = StatementUtils.createStatementFilterMap(
          actor, activity, since, until, limit);
      return xapiService.get(filterMap);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new InvalidXAPIRequestException(e.getMessage(), e);
    }
  }

  /**
   * Post a statement
   * 
   * @param requestBody
   *          the JSON containing the statement data
   * @return JSON string of the statement object with the specified ID
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   * @throws JsonProcessingException
   */
  @RequestMapping(value = { "", "/" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
  public List<String> postStatement(@RequestBody String json)
      throws InvalidXAPIRequestException {

    List<String> ids = null;
    try {
      if (json != null && StringUtils.isNotBlank(json)) {
        ids = new ArrayList<String>();

        List<Statement> statements = null;
        try {
          statements = objectMapper.readValue(json,
              new TypeReference<List<Statement>>() {
              });
        } catch (Exception e) {
          throw new InvalidXAPIRequestException(e);
        }

        for (Statement statement : statements) {
          Set<ConstraintViolation<Statement>> violations = validator
              .validate(statement);
          if (!violations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<Statement> cv : violations) {
              msg.append(cv.getMessage() + ", ");
            }
            throw new InvalidXAPIRequestException(msg.toString());
          }
          logger.debug(
              "Statement POST request received with input statement: {}",
              statement);
          ids.addAll(xapiService.post("test", statement));
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new InvalidXAPIRequestException(e.getMessage(), e);
    }
    return ids;
  }
}
