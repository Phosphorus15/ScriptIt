package net.steepout.scriptit.impl;

import net.steepout.scriptit.misc.IOUtils;
import org.junit.Test;
import org.python.util.PythonInterpreter;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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
        manager.registerPlugin(IOUtils.readAllFromResource("/example.py"));
    }

}