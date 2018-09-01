package net.steepout.scriptit.events;

import net.steepout.scriptit.ScriptEvent;
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
public class PluginUnloadEvent extends ScriptEvent {

    static {
        Events.registerEvent(new PluginUnloadEvent(null, null)); // example for events manager
        Events.getAliases().put("Unload", "pluginUnload");
    }

    @SuppressWarnings("unchecked")
    public PluginUnloadEvent(ScriptPluginManager manager, ScriptPlugin pluginObject) {
        super("pluginUnload", createMap(new Pair<>("pluginManager", manager), new Pair<>("plugin", pluginObject)));
    }

}
