/**
 * 
 */
package org.apereo.openlrs;

import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 *
 */
@ConditionalOnProperty(name="openlrs.keyManager", havingValue="PropertiesFileKeyManager")
@Component
public class PropertiesFileKeyManager implements KeyManager {
  
  @Value("${auth.basic.username}")
  private String username;
  @Value("${auth.basic.password}")
  private String password;
  
  @Value("${auth.oauth.key}")
  private String key;
  @Value("${auth.oauth.secret}")
  private String secret;

  @Override
  public String getSecretForKey(String key) throws NotFoundException {
    if (StringUtils.isNotBlank(key)) {
      if (key.equals(this.username)) {
        return this.password;
      }
      else if (key.equals(this.key)) {
        return this.secret;
      }
      else {
        throw new NotFoundException("Invalid key");
      }
    }
    else {
      throw new IllegalArgumentException("key cannot be null or empty");
    }
  }

  @Override
  public Tenant getTenantForKey(String key) throws NotFoundException {
    // assumes properties file key manager is only used in a single tenant deployment
    
    Tenant tenant = new Tenant();
    tenant.setActive(true);
    tenant.setConsumerKey(key);
    tenant.setSecret(getSecretForKey(key));
    tenant.setName(key);
    tenant.setId(0);
    
    return tenant;
  }

}
