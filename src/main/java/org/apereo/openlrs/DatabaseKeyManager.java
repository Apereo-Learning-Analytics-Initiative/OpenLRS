package org.apereo.openlrs;

import org.apereo.openlrs.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 * @author scody
 *
 */
@ConditionalOnProperty(name="openlrs.keyManager", havingValue="DatabaseKeyManager")
@Component
public class DatabaseKeyManager implements KeyManager {
  
  @Autowired
  private TenantRepository tenantRepository;

  @Override
  public String getSecretForKey(String key) throws NotFoundException {
    Tenant tenant = tenantRepository.findByConsumerKey(key);
    
    if (tenant == null) {
      throw new NotFoundException(String.format("No tenant found for key: %s", key));
    }
        
    return tenant.getSecret();
  }
  
  

  @Override
  public Tenant getTenantForKey(String key) throws NotFoundException {
    Tenant tenant = tenantRepository.findByConsumerKey(key);
    
    if (tenant == null) {
      throw new NotFoundException(String.format("No tenant found for key: %s", key));
    }
    
    return tenant;
  }

}
