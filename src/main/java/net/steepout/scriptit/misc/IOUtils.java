package net.steepout.scriptit.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class IOUtils {

    public static String readAllFromResource(String path) {
        return readAllFromStream(IOUtils.class.getResourceAsStream(path));
    }

    public static String readAllFromStream(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
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
