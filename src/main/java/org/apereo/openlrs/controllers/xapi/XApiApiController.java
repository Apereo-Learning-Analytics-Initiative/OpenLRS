/**
 * 
 */
package org.apereo.openlrs.controllers.xapi;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.KeyManager;
import org.apereo.openlrs.Tenant;
import org.apereo.openlrs.exceptions.xapi.InvalidXAPIRequestException;
import org.apereo.openlrs.model.event.Event;
import org.apereo.openlrs.model.xapi.Statement;
import org.apereo.openlrs.model.xapi.StatementResult;
import org.apereo.openlrs.storage.Reader;
import org.apereo.openlrs.storage.Writer;
import org.apereo.openlrs.utils.AuthorizationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ggilbert
 *
 */
@RestController
@RequestMapping("/v1/xAPI/statements")
public class XApiApiController {
  private final Logger logger = LoggerFactory.getLogger(XApiApiController.class);
  
  @Autowired private ObjectMapper objectMapper;
  @Autowired private Validator validator;
  @Autowired private XApiToCaliperConversionService xapiToCaliperConversionService;
  @Autowired private Writer writer;
  @Autowired private Reader reader;
  @Autowired KeyManager keyManager;
  
  @RequestMapping(value = { "", "/" }, 
      method = RequestMethod.POST, 
      consumes = "application/json", produces = "application/json;charset=utf-8")
  public List<String> postStatement(@RequestBody String json, @RequestHeader(value="Authorization") String authorizationHeader)
      throws InvalidXAPIRequestException {
    List<String> ids = null;
    String key = AuthorizationUtils.getKeyFromHeader(authorizationHeader);
    
    if (StringUtils.isNotBlank(key)) {
      Tenant tenant = keyManager.getTenantForKey(key);  
      if (tenant != null) { 
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
              Event event = xapiToCaliperConversionService.fromXapi(statement);
              logger.debug("{}",event);
              if (StringUtils.isBlank(event.getId())) {
                event.setId(UUID.randomUUID().toString());
              }
              else {
                Event existingEvent = reader.findByTenantIdAndEventId(String.valueOf(tenant.getId()), event.getId());
                if (existingEvent != null) {
                  throw new InvalidXAPIRequestException(String.format("Event with ID %s already exists", event.getId()));
                }
              }

              ids.add(writer.save(event, String.valueOf(tenant.getId())).getId());
            }
          }
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
          throw new InvalidXAPIRequestException(e.getMessage(), e);
        }
      }
      else {
        throw new InvalidXAPIRequestException(String.format("Unknown Tenant %s",key));
      }
    }
    else {
      throw new InvalidXAPIRequestException("Missing Authorization Header");
    }

    return ids;
  }
  
  @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public StatementResult getStatements(
      @RequestHeader(value="Authorization") String authorizationHeader,
      @RequestParam(value = "statementId", required = false) String statementId,
      @RequestParam(value = "page", required = false, defaultValue = "0") String page,
      @RequestParam(value = "limit", required = false, defaultValue = "1000") String limit) throws URISyntaxException {
    StatementResult statementResult = null;
    String key = AuthorizationUtils.getKeyFromHeader(authorizationHeader);
    
    if (StringUtils.isNotBlank(key)) {
      Tenant tenant = keyManager.getTenantForKey(key);       

      if (tenant != null) {
        if (StringUtils.isNotBlank(statementId)) {
          Event event = reader.findByTenantIdAndEventId(String.valueOf(tenant.getId()), statementId);
          if (event == null) {
            throw new InvalidXAPIRequestException(String.format("No statement with id %s",statementId));
          }
          Statement statement = xapiToCaliperConversionService.toXapi(event);
          statementResult = new StatementResult(Collections.singletonList(statement));
        }
        else {
          Page<Event> eventPage = reader.findByTenantId(String.valueOf(tenant.getId()), new PageRequest(Integer.valueOf(page), Integer.valueOf(limit)));
          if (eventPage != null && eventPage.hasContent()) {
            List<Event> events = eventPage.getContent();
            if (events != null && !events.isEmpty()) {
              List<Statement> statements = new ArrayList<>();
              for (Event e : events) {
                statements.add(xapiToCaliperConversionService.toXapi(e));
              }
              
              statementResult = new StatementResult(statements);
            }
          }
        }
      }
      else {
        throw new InvalidXAPIRequestException(String.format("Unknown Tenant %s",key));
      }
    }
    else {
      throw new InvalidXAPIRequestException("Missing Authorization Header");
    }
    return statementResult;
  }
}
