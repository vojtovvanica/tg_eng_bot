package org.example;

/**
 * Логика работы команд бота
 */
public class MainCommands {

    private final WordMemory instance = new WordMemory();

    /*** Разбирает текстовое сообщение и возвращает ответ бота.*/
    public String handle(long chatId, String text) {
        String[] parts = text.trim().split("\\s+", 3);
        String command = parts[0];

        switch (command) {
            case "/start":
                return start(chatId);
            case "/add":
                return handleAdd(chatId, parts);
            case "/view":
                return view(chatId);
            case "/get":
                return handleGet(chatId, parts);
            case "/delete":
                return handleDelete(chatId, parts);
            case "/help":
                return help();
            case "/edit":
                return handleEdit(chatId, parts);
            default:
                return "Неизвестная команда. Напишите /start";
        }
    }

    /**
     * Метод проверяет формат сообщения от пользователя
     * В случае ошибки возвращает сообщение с правильным вводом команды
     * Если все правильно, то вызывает команду add
     */
    private String handleAdd(long chatId, String[] parts) {
        if (parts.length < 3) {
            return "Формат: /add word translation";
        }
        return add(chatId, parts[1], parts[2]);
    }

    /**
     * Метод проверяет формат сообщения от пользователя
     * В случае ошибки возвращает сообщение с правильным вводом команды
     * Если все правильно, то вызывает команду get
     */
    private String handleGet(long chatId, String[] parts) {
        if (parts.length < 2) {
            return "Формат: /get word";
        }
        return get(chatId, parts[1]);
    }

    /**
     * Метод проверяет формат сообщения от пользователя
     * В случае ошибки возвращает сообщение с правильным вводом команды
     * Если все правильно, то вызывает команду delete
     */
    private String handleDelete(long chatId, String[] parts) {
        if (parts.length < 2) {
            return "Формат: /delete word";
        }
        return delete(chatId, parts[1]);
    }

    /**
     * Метод проверяет формат сообщения от пользователя
     * В случае ошибки возвращает сообщение с правильным вводом команды
     * Если все правильно, то вызывает команду edit
     */
    private String handleEdit(long chatId, String[] parts) {
        if (parts.length < 3) {
            return "Формат: /edit word new_translation";
        }
        return edit(chatId, parts[1], parts[2]);
    }

    /**
     * Реализация команды help
     *
     * @return вспомогательный список с командами
     */
    public String help() {
        return """
                Доступные команды:
                /add word translation - добавить пару слов
                /delete word - удалить слово
                /get word - посмотреть перевод слова
                /edit word newTranslation - редактировать перевод слова
                /view - посмотреть слова, которые уже добавлены
                """;
    }

    /**
     * Реализация команды add
     * добавляет новое слово в словарь
     */
    public String add(long chatId, String word, String translation) {
        if (word == null || word.isBlank() || translation == null || translation.isBlank()) {
            return "Пожалуйства введите пару слов корректно" +
                    "example: add word translation";
        }
        if (instance.findByWord(chatId, word) != null) {
            return "Слово " + word + " уже есть в словаре";
        }
        instance.add(chatId, new WordBase(word, translation));
        return "Слово успешно добавлено";

    }

    /**
     * Реализация команды delete
     */
    public String delete(long chatId, String word) {
        if (instance.delete(chatId, word)) {
            return "Слово удалено из списка";
        }
        return "Слово не найдено в словаре";
    }

    /**
     * Реализация команды get
     *
     * @return перевод запрашиваемого слова
     */
    public String get(long chatId, String word) {
        WordBase p = instance.findByWord(chatId, word);
        if (p == null) {
            return "Вы еще не добавляли это слово";
        }
        return word + " - " + p.getTranslation();

    }

    /**
     * Реализация команды edit
     * редактирует перевод запрашиваемого слова
     */
    public String edit(long chatId, String word, String newTranslation) {
        WordBase p = instance.findByWord(chatId, word);
        if (p == null) {
            return "Вы еще не добавляли это слово в словарь";
        }
        p.changeTranslation(newTranslation);
        return "Перевод слова изменен";
    }

    /**
     * Реализация команды view
     *
     * @return список слов которые пользователь добавил в словарь
     */
    public String view(long chatId) {
        if (instance.size(chatId) == 0) {
            return "Ваш список пуст";

        }
        StringBuilder newMessage = new StringBuilder("Ваш список слов: \n");
        for (WordBase p : instance.getAll(chatId)) {
            newMessage.append(p.getWord()).append(" - ").append(p.getTranslation()).append("\n");
        }
        return newMessage.toString().trim();
    }

    /**
     * Реализация приветсвенной команды бота
     */
    public String start(long chatId) {
        if (instance.size(chatId) == 0) {
            return "Привет, я бот, который поможет тебе запомнить английские слова. \n" +
                    "У вас пока нет слов для обучения, хотите добавить слово? " +
                    "Напишите какие слова вы хотите добавить в формате /add word translation\n";
        }
        return help();
    }

}
