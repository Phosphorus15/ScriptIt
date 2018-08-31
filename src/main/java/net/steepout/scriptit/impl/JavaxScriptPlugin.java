package net.steepout.scriptit.impl;

import net.steepout.scriptit.ScriptPlugin;

import javax.script.Bindings;

public class JavaxScriptPlugin extends ScriptPlugin {

    private Bindings currentBindings;

    protected JavaxScriptPlugin(Bindings bindings, String id, String name, String version, String language, String source, String scriptCodes) {
        super(id, name, version, language, source, scriptCodes);
        currentBindings = bindings;
    }

    public Bindings getCurrentBindings() {
        return currentBindings;
    }
}
