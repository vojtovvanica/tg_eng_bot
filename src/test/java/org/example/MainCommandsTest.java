package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс, тестриющий команды бота
 * Проверяет добавление, удаление, поиск и редактирование слов и
 * корректность работы с несколькими пользователями
 *
 */
class MainCommandsTest {
    private MainCommands commands;

    @BeforeEach
    void setUp() {
        commands = new MainCommands();
    }

    /**
     * Проверяет корректность добавления новой пары слов
     * Тест команды /add
     * */
    @Test
    void testAdd() {
        commands.add(1L, "cat", "кот");
        assertTrue(commands.view(1L).contains("cat - кот"));
    }

    /**
     * Проверяет корректность удаления слов из списка
     * Тест команды /delete
     * */
    @Test
    void testDelete() {
        commands.add(1L, "cat", "кот");
        commands.delete(1L, "cat");
        assertFalse(commands.view(1L).contains("cat"));
    }

    /**
     * Проверяет корректность получения запрашиваемой пары слов
     * Тест команды /get
     * */
    @Test
    void testGet() {
        commands.add(1L, "cat", "кот");
        assertEquals("cat - кот", commands.get(1L, "cat"));
    }

    /**
     * Проверяет корректность изменения перевода слова
     * Тест команды /edit
     * */
    @Test
    void testEdit() {
    commands.add(1L, "cat", "кот");
    commands.edit(1L, "cat", "кошка");
    assertEquals("cat - кошка", commands.get(1L, "cat"));
    }

    /**
     * Проверяет корректность получаемого списка слов
     * Тест команды /view
     * */
    @Test
    void testView() {
        commands.add(1L, "cat", "кот");
        commands.add(1L, "dog", "собака");
        assertEquals("Ваш список слов: \ncat - кот\ndog - собака", commands.view(1L));
    }

    /**
     * Проверяет, что списки пользователей корректны и не пересекаются
     * Тест изоляции данных по chatId
     * */
    @Test
    void isolateUsers() {
        commands.add(1L, "cat", "кот");
        commands.add(2L, "dog", "собака");

        assertTrue(commands.view(1L).contains("cat"));
        assertFalse(commands.view(1L).contains("dog"));

        assertTrue(commands.view(2L).contains("dog"));
        assertFalse(commands.view(2L).contains("cat"));
    }
}