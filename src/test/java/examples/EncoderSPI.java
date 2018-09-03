package examples;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * to find the correspond example scripts, see /src/test/resources/spi1.py , spi2.py , spi3.js
 */
public class EncoderSPI {

    static Map<String, Function<String, String>> encoder = new HashMap<>();

    public static void register(String name, Function<String, String> encoder) {

    }

    public static class Crypto { // Lobby

        public static void register(String name, Object encoderObject) {

        }

    }

}
