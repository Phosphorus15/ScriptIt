package net.steepout.scriptit;

import net.steepout.scriptit.misc.PluginUtils;

import java.util.*;

public abstract class ScriptPluginManager {

    static {
        try {
            Class.forName("net.steepout.scriptit.events.PluginLoadEvent");
            Class.forName("net.steepout.scriptit.events.PluginUnloadEvent");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String language;

    protected Map<String, ScriptPlugin> registeredPlugins;

    protected List<Class<?>> lobbies = new ArrayList<>();

    protected ScriptPluginManager(String language, Map<String, ScriptPlugin> registeredPlugins) {
        this.language = language;
        this.registeredPlugins = registeredPlugins;
        lobbies.addAll(Arrays.asList(System.class, PluginUtils.class));
    }

    public String getLanguage() {
        return language;
    }

    public Map<String, ScriptPlugin> getRegisteredPlugins() {
        return registeredPlugins;
    }

    public abstract Optional<Object> getScriptEngine();

    public abstract void setupEngine();

    /**
     * To register a 'lobby', which usually performed as an function pool in script language.
     * <p>
     * (e.g. set java.lang.System as lobby, then in jjs, we could invoke 'System.exit(0)' without importing it)
     * <p>
     * note that lobby could only be added before a plugin was loaded, and is invalid for some script languages
     *
     * @param lobby the class of lobby
     */
    public void putLobby(Class<?> lobby) {
        lobbies.add(lobby);
    }

    public void putStartupScript(String script) {
        // TODO implement
    }

    /**
     * Setup the event system, which ought to be fully-implemented by any plugin manager
     * <p>
     * it was left as a callback for 'init' scripts, and the param, as usual, should be a operative class object
     * <p>
     * (most commonly, the 'Events' class in global scope)
     *
     * @param base the events class object
     */
    public abstract void setupEventSystem(Object base);

    /**
     * @param source source of plugin , default '[object string]'
     * @param script code of script
     * @return plugin's id (determined & maintained by plugin manager)
     */
    public abstract String registerPlugin(String source, String script);

    public String registerPlugin(String script) {
        return registerPlugin("[object string]", script);
    }

    public abstract void handleEvent(ScriptEvent event);

}
