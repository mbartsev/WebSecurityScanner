package webscanner.setup;

import java.net.MalformedURLException;
import java.net.URL;

public class Config {
    public static String BASE_URL = "https://kazanposad.ru/";
    public static URL URL;

    static {
        try {
            URL = new URL("https://kazanposad.ru/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
