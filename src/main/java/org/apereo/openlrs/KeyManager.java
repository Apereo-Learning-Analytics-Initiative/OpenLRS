/**
 * 
 */
package org.apereo.openlrs;

import org.apereo.openlrs.exceptions.NotFoundException;

/**
 * @author ggilbert
 *
 */
public interface KeyManager {
  String getSecretForKey(String key) throws NotFoundException;
  Tenant getTenantForKey(String key) throws NotFoundException;
}
