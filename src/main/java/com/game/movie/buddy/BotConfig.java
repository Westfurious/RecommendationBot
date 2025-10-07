package com.game.movie.buddy;

import java.util.HashMap;
import java.util.Map;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class BotConfig {
    private static final Map<String, String> config = new HashMap<String, String>();

    static {
        loadConfigFromEnv();
    }

    private static void loadConfigFromEnv() {
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split("=");
                if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                config.put(key, value);}
            }

            validateRequired("BOT_USERNAME", "BOT_TOKEN");

        }   catch (IOException e) {
            System.err.println("Ошибка чтения .env файла: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void validateRequired (String... keys)
    {
        for (String key : keys) {
            if (!config.containsKey(key) || config.get(key) == null) {
                throw new IllegalArgumentException(key + " is required");
            }
        }
    }

    private static String get(String key){
        return config.get(key);
    }

    private static String get(String key, String defaultValue){
        return config.getOrDefault(key, defaultValue);
    }

    public static String getBotUsername() {
        return get("BOT_USERNAME");
    }

    public static String getBotToken() {
        return get("BOT_TOKEN");
    }
    public static String getBaseUrl() {
        return get("BASE_URL");
    }
    public static String getGameApiKey() {
        return get("GAME_API_KEY");
    }
}
