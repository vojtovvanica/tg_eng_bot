package org.example;

import java.util.*;

/**
 * Класс в котором реализуется режим игры для проверки слов,
 * которые вводил пользователь
 */
public class Game {
    private final Map<Long, GameSession> activeSession = new HashMap<>();
    private final WordMemory memory;

    public Game(WordMemory memory) {
        this.memory = memory;
    }

    /**
     * Класс для хранения состояния
     */
    private static class GameSession {
        String dictionaryName;
        WordBase currentWord;
        List<WordBase> wordsForGame;
        int correctAnswers = 0;
        int wrongAnswers = 0;

        GameSession(String dictionaryName, List<WordBase> words) {
            this.dictionaryName = dictionaryName;
            this.wordsForGame = words;
        }
    }

    /**
     * Проверяет запустил ли пользователь режим игры
     */
    public boolean isUserPlaying(long chatId) {
        return activeSession.containsKey(chatId);
    }

    /**
     * Метод, реализующий режим игры
     * Еcли пользоаветель выбрал конкретный словарь, то с помощью метода
     * getWordsFromDictionary слова записывваются в лист wordsForGame
     * Если пользователь не вводил конкретного словаря, то метод getWordsFromDictionary
     * собирает со всех словарей пользователя слова
     * И методом getRandomWord отправляет рандомные слова
     *
     */
    public String startGame(long chatId, String dictionaryName) {
        List<WordBase> wordsForGame = new ArrayList<>();
        if (dictionaryName == null || dictionaryName.isBlank()) {
            for (String name : memory.getDictionaryNames(chatId)) {
                wordsForGame.addAll(memory.getWordsFromDictionary(chatId, name));
            }
        } else {
            wordsForGame = memory.getWordsFromDictionary(chatId, dictionaryName);
        }
        if (wordsForGame.isEmpty()) {
            return "В словарях нет слов. Добавьте слова для игры!";
        }
        GameSession session = new GameSession(dictionaryName, wordsForGame);
        session.currentWord = getRandomWord(session.wordsForGame);
        activeSession.put(chatId, session);
        return "Отправьте перевод слова или /stopGame.\n"
                + session.currentWord.getWord() + " - ?";
    }

    /**
     * Метод, который выдает рандомное слово
     */
    private WordBase getRandomWord(List<WordBase> words) {
        return words.get(new Random().nextInt(words.size()));
    }

    /**
     * Реализует завершение игры
     *
     */
    private String stopGame(long chatId) {
        GameSession session = activeSession.remove(chatId);
        if (session == null) {
            return "Вы не находитесь в режиме игра. Если хотите запустить /startGame";
        }

        return "Вы завершили игру.\n" +
                "Правильных ответов " + session.correctAnswers +
                "\nНеправильных ответов " + session.wrongAnswers;
    }

    /**
     * Метод для обработки ответа пользователя
     *
     */
    public String handleAnswer(long chatId, String text) {
        if (text.equals("/stopGame")) {
            return stopGame(chatId);
        }

        if (text.startsWith("/")) {
            GameSession session = activeSession.get(chatId);
            return "вы находитесь в режиме игры. Эта команда сейчас недоступна. " +
                    "Чтобы завершить игру, отправьте команду /stopGame. ";
        }
        GameSession session = activeSession.get(chatId);

        if (text.equalsIgnoreCase(session.currentWord.getTranslation())) {
            session.correctAnswers++;
            session.currentWord = getRandomWord(session.wordsForGame);
            return "Верно. Как переводится " + session.currentWord.getWord() + "?";
        } else {
            session.wrongAnswers++;
            String rightAnswer = session.currentWord.getTranslation();
            session.currentWord = getRandomWord(session.wordsForGame);
            return "Неверный ответ. Правильный перевод " + rightAnswer +
                    "\nСледующее слово " + session.currentWord.getWord();
        }
    }
}