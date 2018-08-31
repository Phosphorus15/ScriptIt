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
public class PluginUnloadEvent extends ScriptEvent {

    @SuppressWarnings("unchecked")
    public PluginUnloadEvent(ScriptPluginManager manager, ScriptPlugin pluginObject) {
        super("pluginUnload", createMap(new Pair<>("pluginManager", manager), new Pair<>("plugin", pluginObject)));
    }

}
