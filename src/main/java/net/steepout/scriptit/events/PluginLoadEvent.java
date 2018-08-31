package net.steepout.scriptit.events;

import net.steepout.scriptit.ScriptEvent;
import net.steepout.scriptit.ScriptPlugin;
import net.steepout.scriptit.ScriptPluginManager;

/**
 * Attribute List :
 * <p>
 * - scriptManager :: ScriptPluginManager
 * <p>
 * - plugin :: ScriptPlugin
 */
public class PluginLoadEvent extends ScriptEvent {

    @SuppressWarnings("unchecked")
    public PluginLoadEvent(ScriptPluginManager manager, ScriptPlugin pluginObject) {
        super("pluginLoad", createMap(new Pair<>("pluginManager", manager), new Pair<>("plugin", pluginObject)));
    }

}
