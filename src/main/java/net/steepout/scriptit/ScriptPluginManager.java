package net.steepout.scriptit;

import net.steepout.scriptit.events.PluginUnloadEvent;
import net.steepout.scriptit.misc.IOUtils;
import net.steepout.scriptit.misc.PluginUtils;
import net.steepout.scriptit.misc.ScriptPluginException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

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

    protected List<String> startup = new ArrayList<>();

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
        startup.add(script);
    }

    public void putStartupScript(InputStream in) {
        putStartupScript(IOUtils.readAllFromStream(in));
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
    public abstract ScriptPlugin registerPlugin(String source, String script);

    public ScriptPlugin registerPlugin(String source, InputStream in) {
        return registerPlugin(source, IOUtils.readAllFromStream(in));
    }

    public ScriptPlugin registerPlugin(InputStream in) {
        return registerPlugin("[object stream]", in);
    }

    public ScriptPlugin registerPlugin(File file) {
        try {
            return registerPlugin(file.getAbsolutePath(), new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new ScriptPluginException(e);
        }
    }

    public ScriptPlugin registerPlugin(String script) {
        return registerPlugin("[object string]", script);
    }

    /**
     * Trigger an event manually (the most common way for user-custom event)
     * <p>
     * any registered plugin which have subscribed the event would process it
     *
     * @param event the event to trigger
     */
    public abstract void handleEvent(ScriptEvent event);

    /**
     * Trigger a event sequence (sequentially) in a async way
     *
     * @param events the events to trigger
     * @param exceptionConsumer the consumer which to process any exception caused during the event
     * @see ScriptPluginManager#handleEvent
     */
    public void handleEventsAsync(Consumer<Exception> exceptionConsumer, ScriptEvent... events) {
        new Thread(() -> {
            for (ScriptEvent event : events) {
                try {
                    handleEvent(event);
                } catch (Exception e) {
                    exceptionConsumer.accept(e);
                }
            }
        }).start(); // bring up the thread
    }

    private AtomicBoolean hasUnloadHook = new AtomicBoolean();

    /**
     * By default, the event 'PluginUnload' would not be triggered when jvm shutdown, invoking this
     * method can enable the function
     */
    public void registerUnloadHook() {
        if (!hasUnloadHook.getAndSet(true))
            Runtime.getRuntime().addShutdownHook(new Thread(this::unloadHook));
    }

    private void unloadHook() {
        handleEvent(new PluginUnloadEvent(this, null)); // FIXME do ... unload
    }

}
