/**
 * 
 */
package org.apereo.openlrs.model.event;

import java.io.Serializable;
import java.util.Map;

import org.joda.time.LocalDate;

/**
 * @author ggilbert
 *
 */
public class Stats implements Serializable {
  private static final long serialVersionUID = 1L;
  private long total;
  private Map<LocalDate,Long> totalByDate;

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public Map<LocalDate, Long> getTotalByDate() {
    return totalByDate;
  }

  public void setTotalByDate(Map<LocalDate, Long> totalByDate) {
    this.totalByDate = totalByDate;
  }
  
}
