package net.steepout.scriptit;

public abstract class ScriptPlugin {

    private String id;

    private String name;

    private String version;

    private String language;

    private String source;

    private String scriptCodes;

    protected ScriptPlugin(String id, String name, String version, String language, String source, String scriptCodes) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.language = language;
        this.source = source;
        this.scriptCodes = scriptCodes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getLanguage() {
        return language;
    }

    public String getSource() {
        return source;
    }

    public String getScriptCodes() {
        return scriptCodes;
    }
}
