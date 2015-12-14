package org.apereo.openlrs.utils;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apereo.openlrs.model.xapi.Statement;
import org.apereo.openlrs.model.xapi.StatementMetadata;
import org.apereo.openlrs.model.xapi.XApiActor;
import org.apereo.openlrs.model.xapi.XApiContext;
import org.apereo.openlrs.model.xapi.XApiContextActivities;
import org.apereo.openlrs.model.xapi.XApiObject;

public class MetadataUtils {

    public static StatementMetadata extractMetadata(Statement statement) {
        StatementMetadata statementMetadata = new StatementMetadata();
        statementMetadata.setId(UUID.randomUUID().toString());
        statementMetadata.setStatementId(statement.getId());
        
        XApiContext xApiContext = statement.getContext();
        if (xApiContext != null) {
            XApiContextActivities xApiContextActivities = xApiContext.getContextActivities();
            if (xApiContextActivities != null) {
                List<XApiObject> parentContext = xApiContextActivities.getParent();
                if (parentContext != null && !parentContext.isEmpty()) {
                    for (XApiObject object : parentContext) {
                        String id = object.getId();
                        if (StringUtils.contains(id, "portal/site/")) {
                            statementMetadata.setContext(StringUtils.substringAfterLast(id, "/"));
                        }
                    }
                }
            }
        }
        
        XApiActor xApiActor = statement.getActor();
        if (xApiActor != null) {
            String mbox = xApiActor.getMbox();
            if (StringUtils.isNotBlank(mbox)) {
                statementMetadata.setUser(StringUtils.substringBetween(mbox, "mailto:", "@"));
            }
        }
        
        return statementMetadata;
    }
}
