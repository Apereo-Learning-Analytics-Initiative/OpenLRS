package org.apereo.openlrs.model.statement;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseActorObjectType {

    protected Map<String, String> inverseFunctionalIdentifier = new HashMap<String, String>(1);
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getInverseFunctionalIdentifier() {
        return inverseFunctionalIdentifier;
    }

    public void setInverseFunctionalIdentifier(String inverseFunctionalIdentifier, String ifiValue) {
        this.inverseFunctionalIdentifier.put(inverseFunctionalIdentifier, ifiValue);
    }

    /*public void setInverseFunctionalIdentifier(String inverseFunctionalIdentifier, Map<String, String> ifiValue) {
        Account account = new Account(ifiValue.get("homepage"), ifiValue.get("name"));
        this.inverseFunctionalIdentifier.put(inverseFunctionalIdentifier, account);
    }*/

}
