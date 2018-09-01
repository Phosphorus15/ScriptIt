package net.steepout.scriptit.impl;

import net.steepout.scriptit.ScriptEvent;
import net.steepout.scriptit.ScriptPlugin;
import net.steepout.scriptit.ScriptPluginManager;
import net.steepout.scriptit.events.PluginLoadEvent;
import net.steepout.scriptit.misc.Events;
import net.steepout.scriptit.misc.ScriptPluginException;
import org.python.core.Py;
import org.python.core.PyClass;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class PythonPluginManager extends ScriptPluginManager {

    static {
        new PythonInterpreter(); // register interpreter
    }

    ScriptEngine engine;

    String initialBindings;

    Map<String, List<PyObject>> eventBus = new HashMap<>();

    public PythonPluginManager() {
        super("python", new HashMap<>());
        engine = Objects.requireNonNull(new ScriptEngineManager().getEngineByName("python"));
        InputStream in = JSPluginManager.class.getResourceAsStream("/init.py");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String str;
        StringBuilder buffer = new StringBuilder();
        try {
            while ((str = reader.readLine()) != null) buffer.append(str).append('\n');
            reader.close();
        } catch (IOException e) {
            throw new ScriptPluginException(e);
        }
        initialBindings = buffer.toString();
    }

    @Override
    public Optional<Object> getScriptEngine() {
        return Optional.of(engine);
    }

    @Override
    public void setupEngine() {

    }

    @Override
    public void setupEventSystem(Object base) {
        if (base instanceof PyClass) {
            Events.getAliases().forEach((alias, origin) -> {
                ((PyClass) base).__setattr__(alias, Py.newString(origin));
            });
        } else throw new ScriptPluginException("Unable to setup event system, invalid base object provided : " + base);
    }

    public void subscribe(String type, Object callback) {
        if (callback instanceof PyObject && ((PyObject) callback).isCallable()) {
            eventBus.compute(type, (s, list) -> {
                if (list == null) list = new ArrayList<>();
                list.add((PyObject) callback);
                return list;
            });
        }
    }

    public void importJavaClass(String clazz, Bindings bindings) {
        try {
            Class<?> clazzObj = Class.forName(clazz);
            engine.eval(String.format("import %s; %s = %s;", clazz, clazzObj.getSimpleName(), clazz), bindings);
        } catch (ClassNotFoundException | ScriptException e) {
            throw new ScriptPluginException(e);
        }
    }

    @Override
    public String registerPlugin(String source, String script) {
        Bindings bindings = engine.createBindings();
        lobbies.forEach(lobby -> importJavaClass(lobby.getName(), bindings));
        bindings.put("jython_pManager", this);
        //noinspection CollectionAddedToSelf
        bindings.put("jython_bindings", bindings);
        try {
            engine.eval(initialBindings, bindings);
            engine.eval(script, bindings);
        } catch (ScriptException e) {
            throw new ScriptPluginException(e);
        }
        String id = Objects.requireNonNull(bindings.get("jython_id"),
                "a plugin script should always invoke plugin(name) or pluginc(...) for registration").toString();
        ScriptPlugin pluginObject = new JavaxScriptPlugin(bindings, id
                , bindings.get("jython_name").toString(), bindings.get("jython_version").toString(),
                "python", source, script);
        registeredPlugins.put(id, pluginObject);
        bindings.put("plugin", pluginObject);
        handleEvent(new PluginLoadEvent(this, pluginObject));
        return id;
    }

    @Override
    public void handleEvent(ScriptEvent event) {
        Optional<String> type = event.get("eventType");
        if (type.isPresent()) {
            List<PyObject> bus = eventBus.get(type.get());
            if (bus != null)
                for (PyObject mirror : bus) {
                    mirror.__call__(Py.java2py(event));
                }
        }
    }
}
