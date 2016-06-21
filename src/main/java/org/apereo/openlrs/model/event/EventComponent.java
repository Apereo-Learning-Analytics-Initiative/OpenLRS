/**
 * 
 */
package org.apereo.openlrs.model.event;

import java.util.Map;

/**
 * @author ggilbert
 *
 */
public interface EventComponent {
  String getId();
  String getContext();
  String getType();
  
  String getName();
  String getDescription();
  
  Map<String, String> getExtensions();
}
