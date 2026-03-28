package bluesea.mc270159;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public class MC270159Config {
    public static final Path configDir = FabricLoader.getInstance().getConfigDir();
    private static final Gson config = new GsonBuilder().setPrettyPrinting().create();

    public static boolean replaceOutputStream;
    public static boolean recordTime;
    public static boolean fullAnalyzeTime;

    static {
        replaceOutputStream = true;
        recordTime = false;
        fullAnalyzeTime = false;

        if (!Files.exists(configDir)) {
            try {
                Files.createDirectory(configDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Path configFile = configDir.resolve("mc270159.json");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("replaceOutputStream", replaceOutputStream);
        jsonObject.addProperty("recordTime", recordTime);
        jsonObject.addProperty("fullAnalyzeTime", fullAnalyzeTime);
        if (!Files.exists(configFile)) {
            MC270159.LOGGER.info("Creating config file");
            try {
                Files.createFile(configFile);

                String json = config.toJson(jsonObject);
                Files.write(configFile, json.getBytes());
            } catch (IOException e) {
                throw new IllegalStateException("Can't create config file", e);
            }
        } else {
            try {
                byte[] bytes = Files.readAllBytes(configFile);
                JsonObject readJsonObject = config.fromJson(new String(bytes, StandardCharsets.UTF_8), JsonObject.class);
                for (String key : jsonObject.keySet()) {
                    if (!readJsonObject.has(key)) {
                        readJsonObject.add(key, jsonObject.get(key));
                    }
                }

                replaceOutputStream = jsonObject.get("replaceOutputStream").getAsBoolean();
                recordTime = jsonObject.get("recordTime").getAsBoolean();
                fullAnalyzeTime = jsonObject.get("fullAnalyzeTime").getAsBoolean();
            } catch (IOException e) {
                throw new IllegalStateException("Can't read config file", e);
            }
        }
    }
}
