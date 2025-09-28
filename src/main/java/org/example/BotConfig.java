package org.example;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class BotConfig {
    private static String botUsername;
    private static String botToken;

    static {
        loadConfigFromEnv();
    }

    private static void loadConfigFromEnv() {
        try {
            Scanner scanner = new Scanner(new File(".env"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("BOT_USERNAME=")) {
                    botUsername = line.substring(13);
                } else if (line.startsWith("BOT_TOKEN=")) {
                    botToken = line.substring(10);
                }
            }
            scanner.close();

            if (botUsername == null || botToken == null) {
                throw new RuntimeException("Не удалось загрузить конфиг из .env файла");
            }

        } catch (FileNotFoundException e) {
            System.err.println("Файл .env не найден");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Ошибка загрузки конфига: " + e.getMessage());
            System.exit(1);
        }
    }

    public static String getBotUsername() {
        return botUsername;
    }

    public static String getBotToken() {
        return botToken;
    }
}
