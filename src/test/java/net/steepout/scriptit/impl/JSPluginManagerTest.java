package net.steepout.scriptit.impl;

import net.steepout.scriptit.misc.IOUtils;
import org.junit.Test;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Objects;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class JSPluginManagerTest {

    ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");

    @Test
    public void scriptEngineNonNull() {
        Objects.requireNonNull(engine);
    }

    @Test
    public void setupBindings() throws ScriptException {
        Bindings bd1 = engine.createBindings();
        engine.eval("ScriptEvent = Java.type('net.steepout.scriptit.ScriptEvent')", bd1);
        assertTrue(engine.eval("ScriptEvent", bd1).getClass().getName().contains("Class"));
    }

    @Test
    public void registry() {
        new JSPluginManager().registerPlugin("[string object]", "plugin('test')");
    }

    @Test
    public void registry2() {
        try {
            new JSPluginManager().registerPlugin("[string object]", "");
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testIntegrity() {
        JSPluginManager manager = new JSPluginManager();
        manager.registerPlugin(IOUtils.readAllFromResource("/example.js"));
    }

}
