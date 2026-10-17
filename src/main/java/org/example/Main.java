package org.example;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

/**
 * Точка входа в телеграм бот
 */
public class Main {
    public static void main(String[] args) {
        String botToken = System.getenv("TELEGRAM_BOT_TOKEN");


        if (botToken == null || botToken.isEmpty()) {
            System.err.println("переменная окружения TELEGRAM_BOT_TOKEN не установлена");
            return;
        }
        try (TelegramBotsLongPollingApplication botsAplication = new TelegramBotsLongPollingApplication()) {
            botsAplication.registerBot(botToken, new Bot(botToken));
            System.out.println("The bot is running");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
