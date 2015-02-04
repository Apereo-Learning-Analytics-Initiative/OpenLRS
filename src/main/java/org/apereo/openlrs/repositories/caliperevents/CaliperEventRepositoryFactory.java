package org.apereo.openlrs.repositories.caliperevents;

import java.util.Map;

import org.apereo.openlrs.model.caliper.CaliperEvent;
import org.apereo.openlrs.repositories.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TODO Document this file
 * @author steve cody scody@unicon.net
 */
@Component
public class CaliperEventRepositoryFactory {
	
	@Value("${caliper.events.repository}") String repository;
	@Autowired private Map<String, Repository<CaliperEvent>> eventRepositories;
	
	public Repository<CaliperEvent> getRepository() {
		return eventRepositories.get(repository);
	}	
	public Repository<CaliperEvent> getRepository(String repositoryType) {
		return eventRepositories.get(repositoryType);
	}
}
