package net.steepout.scriptit.misc;

public class ScriptPluginException extends RuntimeException {

    public ScriptPluginException() {
    }

    public ScriptPluginException(String message) {
        super(message);
    }

    public ScriptPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptPluginException(Throwable cause) {
        super(cause);
    }
}
