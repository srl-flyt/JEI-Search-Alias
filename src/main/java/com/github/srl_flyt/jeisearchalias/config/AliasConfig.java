package com.github.srl_flyt.jeisearchalias.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class AliasConfig {

    private static final Path CONFIG_DIR = FMLPaths.CONFIGDIR.get().resolve("jei_search_alias");

    private AliasConfig() {
    }

    public static void init() {
        try {
            Files.createDirectories(CONFIG_DIR);
            System.out.println("[JEI Search Alias] Config directory: " + CONFIG_DIR);
        }
        catch (IOException e) {
            System.err.println("[JEI Search Alias] " + "Failed to create config directory.");
            e.printStackTrace();
        }
    }

    public static Map<String, List<String>> load() {
        Map<String, List<String>> aliases = new LinkedHashMap<>();
        init();
        try (var stream = Files.list(CONFIG_DIR)) {
            stream
                    .filter(Files::isRegularFile)
                    .filter(AliasConfig::isJsonFile)
                    .sorted()
                    .forEach(file -> loadFile(file, aliases));
        }
        catch (IOException e) {
            System.err.println("[JEI Search Alias] " + "Failed to read config directory.");
            e.printStackTrace();
        }
        return aliases;
    }

    private static boolean isJsonFile(Path file) {
        return file.getFileName()
                .toString()
                .toLowerCase(Locale.ROOT)
                .endsWith(".json");
    }

    private static void loadFile(Path file, Map<String, List<String>> aliases) {
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonElement root = com.google.gson.JsonParser.parseReader(reader);
            if (!root.isJsonObject()) {
                System.err.println("[JEI Search Alias] " + file.getFileName() + ": root must be an object.");
                return;
            }
            parseObject(root.getAsJsonObject(), aliases);
        }
        catch (IOException | JsonParseException | IllegalStateException e) {
            System.err.println("[JEI Search Alias] Failed to load: " + file.getFileName());
            e.printStackTrace();
        }
    }

    private static void parseObject(JsonObject object, Map<String, List<String>> aliases) {
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value.isJsonArray()) {
                addAliases(key, value, aliases);
                continue;
            }
            if (value.isJsonObject()) {
                parseNamespace(key, value.getAsJsonObject(), aliases);
                continue;
            }
            System.err.println("[JEI Search Alias] " + key + ": value must be an array or object.");
        }
    }

    private static void parseNamespace(String namespace, JsonObject items, Map<String, List<String>> aliases) {
        if (namespace.isEmpty()) {
            System.err.println("[JEI Search Alias] " + "Namespace cannot be empty.");
            return;
        }

        if (!namespace.endsWith(":")) {
            namespace += ":";
        }
        for (Map.Entry<String, JsonElement> entry : items.entrySet()) {
            String itemName = entry.getKey();
            JsonElement value = entry.getValue();
            String itemId = namespace + itemName;
            if (!value.isJsonArray()) {
                System.err.println("[JEI Search Alias] " + itemId + ": aliases must be an array.");
                continue;
            }
            addAliases(itemId, value, aliases);
        }
    }

    private static void addAliases(String itemId, JsonElement value, Map<String, List<String>> aliases) {
        if (!value.isJsonArray()) {
            return;
        }
        List<String> words = aliases.computeIfAbsent(itemId, key -> new ArrayList<>());
        for (JsonElement element : value.getAsJsonArray()) {
            if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
                System.err.println("[JEI Search Alias] " + itemId + ": alias must be a string.");
                continue;
            }
            String word = element.getAsString().trim();
            if (!word.isEmpty() && !words.contains(word)) {
                words.add(word);
            }
        }
    }
}