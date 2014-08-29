package org.apereo.openlrs.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
    	List<Statement> allStatements = factory.getRepository().get();
    	
    	if (filterMap != null && !filterMap.isEmpty()) {
    		String actor = filterMap.get("actor"); // TODO actor filter
    		String activity = filterMap.get("activity");
    		
    		List<Statement> filteredStatements = new ArrayList<Statement>();
    		for (Statement statement: allStatements) {
    			if (StringUtils.isNotBlank(activity)) {
    				if (activity.equals(statement.getObject().getId())) {
    					filteredStatements.add(statement);
    				}
    			}
    		}
    		
    		result = new StatementResult(filteredStatements);
    	}
    	else {
    		result = new StatementResult(allStatements);
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
    	newStatement.setStored(TimestampUtils.getISO8601StringForDate(new Date()));
    	
        Statement savedStatement = factory.getRepository().post(newStatement);
        
        if (log.isDebugEnabled()) {
        	log.debug(String.format("Saved Statement: %s",savedStatement));
        }
        
        List<String> statementIds = new ArrayList<String>();
        statementIds.add(savedStatement.getId());
        
        return statementIds;
    }

}
