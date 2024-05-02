package com.ambrosia.roulette.table;

import apple.utilities.json.gson.serialize.JsonSerializing;
import com.ambrosia.roulette.Roulette;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;

public class RouletteStreetTypeAdapter implements JsonSerializing<RouletteStreet> {

    @Override
    public RouletteStreet deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext)
        throws JsonParseException {
        int street = json.getAsInt();
        return Roulette.TABLE.getStreet(street);
    }

    @Override
    public JsonElement serialize(RouletteStreet street, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(street.getStreetNum());
    }
}
