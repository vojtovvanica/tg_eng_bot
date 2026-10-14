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
public class Bot implements LongPollingSingleThreadUpdateConsumer {
    /** Клиент для отправки запросов в Telegram Bot  */
    private final TelegramClient telegramClient;
    private final MainCommands commands = new MainCommands();
    /**
     * Создаёт бота и инициализирует клиента для работы с Telegram Bot
     * @param botToken
     */
    public Bot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);

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
        String reply = commands.handle(chatId, text);

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
