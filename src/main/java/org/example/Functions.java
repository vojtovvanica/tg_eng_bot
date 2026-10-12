package org.example;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
/** Бот, работающий в режиме long polling
 * Класс реализует интерфейс LongPollingSingleThreadUpdateConsumer,
 * Каждое входящее текстовое сообщение бот пересылает обратно пользователю
 */
public class Functions implements LongPollingSingleThreadUpdateConsumer {
    /** Клиент для отправки запросов в Telegram Bot  */
    private final TelegramClient telegramClient;
    private final MainCommands commands = new MainCommands();
    /**
     * Создаёт бота и инициализирует клиента для работы с Telegram Bot
     * @param botToken
     */
    public Functions(String botToken){
        telegramClient = new OkHttpTelegramClient(botToken);

    }
    /**Формирует текст ответа на основе входящего сообщения
     * @param  inputText текст входящего сообщения от пользователя
     * @return текст ответа
     */
    public String writeMessage(String inputText){
        return inputText;
    }
    /*** Разбирает текстовое сообщение и возвращает ответ бота.*/
    public String handle(long chatId, String text) {
        String[] parts = text.trim().split("\\s+", 3);
        String command = parts[0];

        switch (command) {
            case "/start":
                return commands.start(chatId);
            case "/add":
                return handleAdd(chatId, parts);
            case "/view":
                return commands.view(chatId);
            case "/get":
                return handleGet(chatId, parts);
            case "/delete":
                return handleDelete(chatId, parts);
            case "/help":
                return commands.help();
            case "/edit":
                return handleEdit(chatId, parts);
            default:
                return "Неизвестная команда. Напишите /start";
        }
    }

    private String handleAdd(long chatId, String[] parts) {
        if (parts.length < 3) {
            return "Формат: /add word translation";
        }
        return commands.add(chatId, parts[1], parts[2]);
    }

    private String handleGet(long chatId, String[] parts) {
        if (parts.length < 2) {
            return "Формат: /get word";
        }
        return commands.get(chatId, parts[1]);
    }

    private String handleDelete(long chatId, String[] parts) {
        if (parts.length < 2) {
            return "Формат: /delete word";
        }
        return commands.delete(chatId, parts[1]);
    }

    private String handleEdit(long chatId, String[] parts) {
        if (parts.length < 3) {
            return "Формат: /edit word new_translation";
        }
        return commands.edit(chatId, parts[1], parts[2]);
    }

    /**
     * Обрабатывает входящее обновление от Telegram
     * @param update объект обновления, полученный от Telegram Bot
     */
    @Override
    public void consume(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String reply = handle(chatId, text);

        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(reply)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
