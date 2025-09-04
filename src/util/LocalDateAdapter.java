package util;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    // Serialisierung: immer als String "yyyy-MM-dd"
    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(FORMATTER.format(src));
    }

    // Deserialisierung: entweder String oder altes Objekt
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            // Neue Serialisierung als String
            return LocalDate.parse(json.getAsString(), FORMATTER);
        } else if (json.isJsonObject()) {
            // Alte Serialisierung als Objekt
            JsonObject obj = json.getAsJsonObject();
            int year = obj.get("year").getAsInt();
            int month = obj.has("monthValue") ? obj.get("monthValue").getAsInt() : obj.get("month").getAsInt();
            int day = obj.has("dayOfMonth") ? obj.get("dayOfMonth").getAsInt() : obj.get("day").getAsInt();
            return LocalDate.of(year, month, day);
        } else {
            throw new JsonParseException("Cannot parse LocalDate from: " + json);
        }
    }
}

