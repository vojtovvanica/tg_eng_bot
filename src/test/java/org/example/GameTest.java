package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {
    private Game game;
    private WordMemory memory;

    @BeforeEach
    void setUp() {
        memory = new WordMemory();
        memory.addDictionary(1L, "animals");
        memory.openDictionary(1L, "animals");
        memory.add(1L, new WordBase("cat", "кот"));
        memory.add(1L, new WordBase("dog", "собака"));

        game = new Game(memory);
    }

    /**
     * Проверяет, что игра запускается по конкретному словарю.
     */
    @Test
    void testStartGame() {
        String result = game.startGame(1L, "animals");
        assertTrue(result.contains("cat") || result.contains("dog"));
        assertTrue(game.isUserPlaying(1L));

    }

    /**
     * Проверяет, что режим игры не доступен без слов в словаре
     */
    @Test
    void startGameWithNoWords() {
        memory.delete(1L, "cat");
        memory.delete(1L, "dog");
        String result = game.startGame(1L, "animals");
        assertTrue(result.contains("нет слов"));
        assertFalse(game.isUserPlaying(1L));
    }

    /**
     * Проверяет как работает команда метод handleAnswer
     * Проверяет, что во время режима игры, в случае если пользователь
     * пытется воспользоваться другими командами, то они блокируются
     */
    @Test
    void handleAnswer() {
        game.startGame(1L, "animals");

        String result = game.handleAnswer(1L, "/get cat");

        assertTrue(result.contains("вы находитесь в режиме игры. Эта команда сейчас недоступна. " +
                "Чтобы завершить игру, отправьте команду /stopGame. "));
    }

    /**
     * Проверяет, что команда /stopGame завершает игру и выводит статистику по игре
     */
    @Test
    void testStopGame() {
        game.startGame(1L, "animals");
        game.handleAnswer(1L, "бла");

        String result = game.handleAnswer(1L, "/stopGame");

        assertTrue(result.contains("Вы завершили игру"));
        assertTrue(result.contains("Правильных ответов 0"));
        assertTrue(result.contains("Неправильных ответов 1"));
        assertFalse(game.isUserPlaying(1L));
    }

}