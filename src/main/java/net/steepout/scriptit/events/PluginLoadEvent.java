package net.steepout.scriptit.events;

import net.steepout.scriptit.ScriptEventImpl;
import net.steepout.scriptit.ScriptPlugin;
import net.steepout.scriptit.ScriptPluginManager;
import net.steepout.scriptit.misc.Events;

/**
 * Attribute List :
 * <p>
 * - scriptManager :: ScriptPluginManager
 * <p>
 * - plugin :: ScriptPlugin
 */
public class PluginLoadEvent extends ScriptEventImpl {

    static {
        Events.registerEvent(new PluginLoadEvent(null, null)); // example for events manager
        Events.getAliases().put("Load", "pluginLoad");
    }

    @SuppressWarnings("unchecked")
    public PluginLoadEvent(ScriptPluginManager manager, ScriptPlugin pluginObject) {
        super("pluginLoad", createMap(new Pair<>("pluginManager", manager), new Pair<>("plugin", pluginObject)));
    }

}
