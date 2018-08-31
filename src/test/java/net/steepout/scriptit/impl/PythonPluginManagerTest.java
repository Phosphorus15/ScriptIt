package net.steepout.scriptit.impl;

import net.steepout.scriptit.misc.ScriptPluginException;
import org.junit.Test;
import org.python.util.PythonInterpreter;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import static org.junit.Assert.fail;

public class PythonPluginManagerTest {

    static {
        new PythonInterpreter(); // import jython
    }

    ScriptEngine engine = new ScriptEngineManager().getEngineByName("python");

    @Test
    public void scriptEngineNonNull() {
        Objects.requireNonNull(engine);
    }

    @Test
    public void individualBindings() throws ScriptException {
        Bindings bindings = engine.createBindings();
        Bindings bindings1 = engine.createBindings();
        engine.eval("a = 3", bindings);
        try {
            engine.eval("print a", bindings1);
            fail();
        } catch (ScriptException ignored) {

        }
        engine.eval("print a", bindings);
    }

    @Test
    public void registry() {
        new PythonPluginManager().registerPlugin("[string object]", "plugin(\"Test Case Plugin\")");
    }

    @Test
    public void testHandler() {
        PythonPluginManager manager = new PythonPluginManager();
        InputStream in = JSPluginManagerTest.class.getResourceAsStream("/example.py");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String str;
        StringBuilder buffer = new StringBuilder();
        try {
            while ((str = reader.readLine()) != null) buffer.append(str).append('\n');
            reader.close();
        } catch (IOException e) {
            throw new ScriptPluginException(e);
        }
        manager.registerPlugin("[string object]", buffer.toString());
    }

}