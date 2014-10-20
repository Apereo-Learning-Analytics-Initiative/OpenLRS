package org.apereo.openlrs.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apereo.openlrs.exceptions.StatementStateConflictException;
import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.model.StatementResult;
import org.apereo.openlrs.repositories.statements.StatementRepositoryFactory;
import org.apereo.openlrs.utils.TimestampUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatementService {
	private Logger log = Logger.getLogger(StatementService.class);
	
	@Autowired StatementRepositoryFactory factory;
	
	public Statement getStatement(String id) {
		
		Statement key = new Statement();
		key.setId(id);
		
		return factory.getRepository().get(key);
	}

    /**
     * Gets statement objects for the specified criteria
     * 
     * @param filterMap a hashmap of the filtering criteria
     * @return JSON string of the statement objects
     */
    public StatementResult getStatement(Map<String, String> filterMap) {
    	
    	StatementResult result = null;
    	if (filterMap != null && !filterMap.isEmpty()) {
     		result = new StatementResult(factory.getRepository().get(filterMap));
    	}
    	else {
    		result = new StatementResult(factory.getRepository().get());
    	}
    	
        return result;
    }

    /**
     * Post a statement to the database
     * 
     * @param newStatement a statement object 
     * @return a collection of statement ids
     */
    public List<String> postStatement(Statement newStatement) {
    	
    	if (log.isDebugEnabled()) {
    		log.debug(String.format("New Statement: %s",newStatement));
    	}
    	
    	if (StringUtils.isBlank(newStatement.getId())) {
    		newStatement.setId(UUID.randomUUID().toString());
    	}
    	else {
    		// check for conflict
    		Statement statement = factory.getRepository().get(newStatement);
    		if (statement != null) {
    			// set the stored timestamps null for comparison purposes
        		statement.setStored(null);
        		newStatement.setStored(null);

    			String newStatementJSON = newStatement.toJSON();
    			String statementJSON = statement.toJSON();
    			if (newStatementJSON.equals(statementJSON)) {
    				throw new StatementStateConflictException(String.format("Matching statement for id: %s already exists", newStatement.getId()));
    			}
    		}
    	}
    	newStatement.setStored(TimestampUtils.getISO8601StringForDate(new Date()));
    	
        Statement savedStatement = factory.getRepository().post(newStatement);
        
        if (log.isDebugEnabled()) {
        	log.debug(String.format("Saved Statement: %s",savedStatement));
        }
        
        List<String> statementIds = new ArrayList<String>();
        statementIds.add(savedStatement.getId());
        
        return statementIds;
    }
    
    public List<Statement> getByContext(String context) {
    	return factory.getRepository().getByContext(context);
    }
    
    public List<Statement> getByUser(String user) {
    	return factory.getRepository().getByUser(user);
    }
    
    public List<Statement> getByContextAndUser(String context,String user) {
    	return factory.getRepository().getByContextAndUser(context,user);
    }

}
