package com.amsamu.mymedialists.util;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class JsonOperations {

    public static JsonObject readJsonFile(InputStream is) throws JsonIOException, JsonSyntaxException {
        return (JsonObject) JsonParser.parseReader(new JsonReader(new InputStreamReader(is)));
    }

    public static void writeJsonFile(OutputStream out, JsonObject object) throws IOException {
        JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        jsonWriter.setIndent("  ");
        new Gson().toJson(object, jsonWriter);
        jsonWriter.close();
    }

}
