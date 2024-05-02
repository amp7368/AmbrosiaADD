package com.ambrosia.roulette.table;

import apple.utilities.json.gson.serialize.JsonSerializing;
import com.ambrosia.roulette.Roulette;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;

public class RouletteSpaceTypeAdapter implements JsonSerializing<RouletteSpace> {

    @Override
    public RouletteSpace deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
        throws JsonParseException {
        int digit = jsonElement.getAsInt();
        return Roulette.TABLE.getSpace(digit);
    }

    @Override
    public JsonElement serialize(RouletteSpace space, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(space.digit());
    }
}
