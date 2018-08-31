package net.steepout.scriptit;

import java.util.Map;
import java.util.Optional;

public abstract class ScriptPluginManager {

    private String language;

    protected Map<String, ScriptPlugin> registeredPlugins;

    protected ScriptPluginManager(String language, Map<String, ScriptPlugin> registeredPlugins) {
        this.language = language;
        this.registeredPlugins = registeredPlugins;
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
