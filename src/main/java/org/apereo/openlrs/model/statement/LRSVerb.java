package org.apereo.openlrs.model.statement;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds a representation of a statement verb
 *
 * @author Robert E. Long (rlong @ unicon.net)
 */
public class LRSVerb {

    /**
     * Corresponds to a Verb definition. Each Verb definition corresponds to the meaning of a Verb, not the word. 
     * The IRI should be human-readable and contain the Verb meaning.
     */
    private String id;

    /**
     * The human readable representation of the Verb in one or more languages. This does not have any impact on 
     * the meaning of the Statement, but serves to give a human-readable display of the meaning already determined 
     * by the chosen Verb.
     * 
     * e.g.
     * "display":{
     *   "en-US":"ran",
     *   "es" : "corrió" 
     * }
     */
    private Map<String, String> display;

    /**
     * Constructor to create an empty verb
     * If used, an id and (optional) display language map must be set in order to use this object
     */
    public LRSVerb() {
        this("", new HashMap<String, String>());
    }

    /**
     * Constructor to create a full verb object with an id and (optional) display language map
     * 
     * @param id
     * @param display
     */
    public LRSVerb(String id) {
        this(id, new HashMap<String, String>());
    }

    /**
     * Constructor to create a full verb object with an id and (optional) display language map
     * 
     * @param id
     * @param display
     */
    public LRSVerb(String id, Map<String, String> display) {
        this.id = id;
        this.display = display;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getDisplay() {
        return display;
    }

    public void setDisplay(Map<String, String> display) {
        this.display = display;
    }

}
