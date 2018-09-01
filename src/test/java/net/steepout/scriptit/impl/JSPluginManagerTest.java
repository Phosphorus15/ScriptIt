package net.steepout.scriptit.impl;

import net.steepout.scriptit.misc.ScriptPluginException;
import org.junit.Test;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        InputStream in = JSPluginManagerTest.class.getResourceAsStream("/example.js");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String str;
        StringBuilder buffer = new StringBuilder();
        try {
            while ((str = reader.readLine()) != null) buffer.append(str).append('\n');
            reader.close();
        } catch (IOException e) {
            throw new ScriptPluginException(e);
        }
        manager.registerPlugin(buffer.toString());
    }

}
