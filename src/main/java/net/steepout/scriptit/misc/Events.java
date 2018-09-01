package net.steepout.scriptit.misc;

import net.steepout.scriptit.ScriptEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Events {

    private static Map<String, ScriptEvent> registeredEvents = new HashMap<>();

    private static Map<String, String> aliases = Collections.synchronizedMap(new HashMap<>());

    @SuppressWarnings("all")
    public static boolean registerEvent(ScriptEvent emptyEventObj) {
        return registeredEvents.put(emptyEventObj.getEventType().toLowerCase(), emptyEventObj) == null;
    }

    public static ScriptEvent getEventTypeByName(String name) {
        return registeredEvents.get(name.toLowerCase());
    }

    /**
     * In the order of quickly access the 'Events' object within script languages, aliases was made
     * <p>
     * for example, adding alias 'Load' -> 'pluginLoad', thus, subscribing the load event could be simplified
     * <p>
     * (e.g. in js, subscribe(Events.Load, ...) instead of subscribe("pluginLoad", ...))
     *
     * @return a modifiable map of all aliases
     */
    public static Map<String, String> getAliases() {
        return aliases;
    }

}
