/**
 * Copyright 2015 Unicon (R) Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package org.apereo.openlrs.storage.kinesis;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apereo.openlrs.model.event.Event;
import org.apereo.openlrs.storage.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.PutRecordResult;

/**
 * @author ggilbert
 *
 */
@ConditionalOnProperty(name="openlrs.writer", havingValue="AwsKinesisWriter")
@Component("AwsKinesisWriter")
public class AwsKinesisWriter implements Writer {
  
  private static Logger log = Logger.getLogger(AwsKinesisWriter.class);
  
  @Autowired private AmazonKinesisClient kinesisClient;

  @Value("${aws.kinesis.stream}") 
  private String stream;

  @Override
  public Event save(Event event, String tenantId) {
    if (event == null || tenantId == null) {
      log.error(event.toString());
      throw new IllegalArgumentException("Event or Tenant cannot be null");
    }
    
    PutRecordRequest putRecordRequest = new PutRecordRequest();
    putRecordRequest.setStreamName(stream);
    putRecordRequest.setPartitionKey(tenantId);
    putRecordRequest.setData(ByteBuffer.wrap(event.toJSON().getBytes()));
    PutRecordResult result = kinesisClient.putRecord(putRecordRequest);
    
    log.debug(String.format("Successfully putrecord, partition key : %s, ShardID: %s, Sequence Number: %s",
        putRecordRequest.getPartitionKey(),result.getShardId(),result.getSequenceNumber()));

    return event;
  }

  @Override
  public List<Event> saveAll(Collection<Event> events, String tenantId) {
    List<Event> savedEvents = null;
    
    if (events != null && !events.isEmpty()) {
      savedEvents = new ArrayList<Event>(events.size());
      for (Event e : events) {
        savedEvents.add(this.save(e, tenantId));
      }
    }
    
    return savedEvents;
  }

}
