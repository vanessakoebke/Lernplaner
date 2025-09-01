package lang;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class I18n {
    private static Map<String, String> texte = new HashMap<>();

    //Sprache laden
    public static void load(String sprache) {
        String resource = "/lang/" + sprache + ".json"; // im Classpath, damit Ausführung als .jar funktioniert
        try (InputStream is = I18n.class.getResourceAsStream(resource)) {
            if (is == null) {
                System.err.println("Sprachdatei nicht gefunden: " + resource);
                texte.clear();
                return;
            }

            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<?, ?> tree = new Gson().fromJson(reader, type);

            texte.clear();
            if (tree != null) flatten("", tree);
        } catch (IOException e) {
            System.err.println("Die Sprachdatei " + resource + " konnte nicht geladen werden.");
            texte.clear();
        }
    }

    //Text abrufen
    public static String t(String key) {
        return texte.getOrDefault(key, "???" + key + "???");
    }

    // Rekursives Flatten von verschachtelten Maps 
    private static void flatten(String prefix, Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey().toString() : prefix + "." + entry.getKey();
            Object val = entry.getValue();
            if (val instanceof Map) {
                flatten(key, (Map<?, ?>) val);
            } else {
                texte.put(key, val.toString());
            }
        }
    }
}
