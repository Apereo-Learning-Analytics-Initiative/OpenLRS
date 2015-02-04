package org.apereo.openlrs.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apereo.openlrs.exceptions.CaliperEventStateConflictException;
//import org.apereo.openlrs.exceptions.CaliperEventStateConflictException;
import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.model.caliper.CaliperEventResults;
import org.apereo.openlrs.repositories.caliperevents.CaliperEventRepositoryFactory;
import org.apereo.openlrs.utils.TimestampUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaliperEventService {
	private Logger log = Logger.getLogger(CaliperEventService.class);
	 
	@Autowired CaliperEventRepositoryFactory factory;
	
	public CaliperEvent getCaliperEvent(String id) {
		
		CaliperEvent key = new CaliperEvent();
		key.setId(id);
		
		return factory.getRepository().get(key);
	}

    /**
     * Gets statement objects for the specified criteria
     * 
     * @param filterMap a hashmap of the filtering criteria
     * @return JSON string of the statement objects
     */
    public CaliperEventResults getCaliperEvent(Map<String, String> filterMap) {
    	
    	CaliperEventResults result = null;
    	if (filterMap != null && !filterMap.isEmpty()) {
     		result = new CaliperEventResults(factory.getRepository().get(filterMap));
    	}
    	else {
    		result = new CaliperEventResults(factory.getRepository().get());
    	}
    	
        return result;
    }

    /**
     * Post a statement to the database
     * 
     * @param newCaliperEvent a statement object 
     * @return a collection of statement ids
     */
    public List<String> postCaliperEvent(CaliperEvent newCaliperEvent) {
    	
    	System.out.println("inside postCaliperEvent");
    	log.debug("from logger");
    	
    	if (log.isDebugEnabled()) {
    		log.debug(String.format("New CaliperEvent: %s",newCaliperEvent));
    	}
    	
    	if (StringUtils.isBlank(newCaliperEvent.getId())) {
    		newCaliperEvent.setId(UUID.randomUUID().toString());
    	}
    	else {
    		// check for conflict
    		CaliperEvent caliperEvent = factory.getRepository().get(newCaliperEvent);
    		if (caliperEvent != null) {
    			// set the stored timestamps null for comparison purposes
    			caliperEvent.setStored(null);
        		newCaliperEvent.setStored(null);

    			String newCaliperEventJSON = newCaliperEvent.toJSON();
    			String statementJSON = caliperEvent.toJSON();
    			if (newCaliperEventJSON.equals(statementJSON)) {
    				throw new CaliperEventStateConflictException(String.format("Matching statement for id: %s already exists", newCaliperEvent.getId()));
    			}
    		}
    	}
    	newCaliperEvent.setStored(TimestampUtils.getISO8601StringForDate(new Date()));
    	
    	CaliperEvent savedCaliperEvent = factory.getRepository().post(newCaliperEvent);
        
        if (log.isDebugEnabled()) {
        	log.debug(String.format("Saved CaliperEvent: %s",savedCaliperEvent));
        }
        
        List<String> statementIds = new ArrayList<String>();
        statementIds.add(savedCaliperEvent.getId());
        
        return statementIds;
    }
    
    public List<CaliperEvent> getByContext(String context) {
    	return factory.getRepository().getByContext(context);
    }
    
    public List<CaliperEvent> getByUser(String user) {
    	return factory.getRepository().getByUser(user);
    }
    
    public List<CaliperEvent> getByContextAndUser(String context,String user) {
    	return factory.getRepository().getByContextAndUser(context,user);
    }

}
