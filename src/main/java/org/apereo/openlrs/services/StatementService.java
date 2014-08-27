package org.apereo.openlrs.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.model.StatementResult;
import org.apereo.openlrs.repositories.Repository;
import org.apereo.openlrs.repositories.statements.StatementRepositoryFactory;
import org.apereo.openlrs.utils.JsonUtils;
import org.apereo.openlrs.utils.StatementUtils;
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
     * @param statementData JSON string containing the statement properties
     * @return JSON string of the response
     */
    public List<String> postStatement(String statementData) {
    	
        Map<String, Object> statementProperties = JsonUtils.parseJsonStringToMap(statementData);
        if (!StatementUtils.hasAllRequiredProperties(statementProperties)) {
            throw new IllegalArgumentException("Statement properties must contain all required properties.");
        }
        
        if (log.isDebugEnabled()) {
        	log.debug(String.format("Statement Properties: %s", statementProperties));
        }

        // prepare the statement for storage & save
        Statement statement = factory.getRepository().post(StatementUtils.prepareStatement(statementProperties));
        
        if (log.isDebugEnabled()) {
        	log.debug(statement);
        }
        
        List<String> statementIds = new ArrayList<String>();
        statementIds.add(statement.getId());
        
        return statementIds;
    }

}
