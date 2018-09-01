package net.steepout.scriptit.impl;

import net.steepout.scriptit.misc.ScriptPluginException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

    public static String readAllFromResource(String path) {
        InputStream in = IOUtils.class.getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String str;
        StringBuilder buffer = new StringBuilder();
        try {
            while ((str = reader.readLine()) != null) buffer.append(str).append('\n');
            reader.close();
        } catch (IOException e) {
            throw new ScriptPluginException(e);
        }
        return buffer.toString();
    }

}
