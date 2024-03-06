package gss.server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class JsonHelper {

    public static String hashmapToJson(HashMap<String, Object> map) {
        return new Gson().toJson(map);
    }

    public static HashMap<String, Object> jsonToHashmap(String json) {
        final GsonBuilder builder = new GsonBuilder();
        builder.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE);
        return builder.create().fromJson(json, new TypeToken<HashMap<String, Object>>(){}.getType());
    }

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }
    public static <T> T fromJson(String json, Class<T> clazz) {

        final GsonBuilder builder = new GsonBuilder();
        builder.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE);

        return builder.create().fromJson(json, clazz);

    }

}
