package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Класс, тестриющий команды бота
 * Проверяет добавление, удаление, поиск и редактирование слов и
 * корректность работы с несколькими пользователями
 *
 */
class MainCommandsTest {
    @TempDir
    Path tempDir;

    private MainCommands commands;
    private Path testFile;
    private WordMemory memory;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("test_words.txt");
        WorkWithFile testFileWorker = new WorkWithFile(testFile);
        memory = new WordMemory(testFileWorker);
        commands = new MainCommands(memory);

        commands.handle(1L, "/addDict testDict");
        commands.handle(1L, "/open testDict");
    }

    /**
     * Проверяет корректность добавления новой пары слов
     * Тест команды /add
     *
     */
    @Test
    void testAdd() {
        commands.handle(1L, "/add cat кот");
        String result = commands.handle(1L, "/view");
        Assertions.assertTrue(result.contains("cat - кот"));
    }

    /**
     * Проверяет корректность удаления слов из списка
     * Тест команды /delete
     *
     */
    @Test
    void testDelete() {
        commands.handle(1L, "/add cat кот");
        commands.handle(1L, "/delete cat");
        String result = commands.handle(1L, "/view");
        Assertions.assertFalse(result.contains("cat"));
    }

    /**
     * Проверяет корректность получения запрашиваемой пары слов
     * Тест команды /get
     *
     */
    @Test
    void testGet() {
        commands.handle(1L, "/add cat кот");
        Assertions.assertEquals("cat - кот", commands.handle(1L, "/get cat"));
    }

    /**
     * Проверяет корректность изменения перевода слова
     * Тест команды /edit
     *
     */
    @Test
    void testEdit() {
        commands.handle(1L, "/add cat кот");
        commands.handle(1L, "/edit cat кошка");
        Assertions.assertEquals("cat - кошка", commands.handle(1L, "/get cat"));
    }

    /**
     * Проверяет корректность получаемого списка слов
     * Тест команды /view
     *
     */
    @Test
    void testView() {
        commands.handle(1L, "/add cat кот");
        commands.handle(1L, "/add dog собака");
        String result = commands.handle(1L, "/view");
        Assertions.assertTrue(result.contains("cat - кот"));
        Assertions.assertTrue(result.contains("dog - собака"));
    }

    /**
     * Проверяет, что списки пользователей корректны и не пересекаются
     * Тест изоляции данных по chatId
     *
     */
    @Test
    void isolateUsers() {
        commands.handle(1L, "/addDict animals");
        commands.handle(1L, "/open animals");
        commands.handle(1L, "/add cat кот");

        commands.handle(2L, "/addDict animals");
        commands.handle(2L, "/open animals");
        commands.handle(2L, "/add dog собака");

        String result = commands.handle(1L, "/view");
        Assertions.assertTrue(result.contains("cat"));
        Assertions.assertFalse(result.contains("dog"));

        String result_2 = commands.handle(2L, "/view");
        Assertions.assertFalse(result_2.contains("cat"));
        Assertions.assertTrue(result_2.contains("dog"));

    }

    /**
     * Тестирует функцию добавления нового словаря
     *
     */
    @Test
    void testAddDict() {
        commands.handle(1L, "/addDict animals");
        String result = commands.handle(1L, "/viewDict");
        Assertions.assertTrue(result.contains("animals"));
    }

    /**
     * Тестирует, что метод addDict выведет,
     * если пользователь захочет создать словарь с уже существующим названием
     *
     */
    @Test
    void testAddDuplicateDict() {
        commands.handle(1L, "/addDict animals");
        String result = commands.handle(1L, "/addDict animals");
        Assertions.assertTrue(result.contains("У вас уже есть словарь с таким названием"));
    }

    /**
     * Тестирует метод удаления словаря
     *
     */
    @Test
    void testDeleteDict() {
        commands.handle(1L, "/addDict animals");
        commands.handle(1L, "/deleteDict animals");
        String result = commands.handle(1L, "/viewDict");
        Assertions.assertFalse(result.contains("animals"));
    }

    /**
     * Тестирует, что при смене словаря
     * первод записывается в нужный словарь
     *
     */
    @Test
    void testSwitchDictionaries() {
        commands.handle(1L, "/addDict animals");
        commands.handle(1L, "/addDict food");

        commands.handle(1L, "/open animals");
        commands.handle(1L, "/add cat кот");

        commands.handle(1L, "/open food");
        commands.handle(1L, "/add apple яблоко");

        String result_1 = commands.handle(1L, "/view");

        Assertions.assertTrue(result_1.contains("apple"));
        Assertions.assertFalse(result_1.contains("cat"));

        commands.handle(1L, "/open animals");
        String result_2 = commands.handle(1L, "/view");
        Assertions.assertTrue(result_2.contains("cat"));
        Assertions.assertFalse(result_2.contains("apple"));
    }

    /**
     * Тестирует сохранение слова в файл и чтение из него
     */
    @Test
    void testFileSaving() throws IOException {
        commands.handle(1L, "/add cat кот");
        List<String> lines = Files.readAllLines(testFile);

        Assertions.assertTrue(lines.get(0).contains("cat"));
        Assertions.assertTrue(lines.get(0).contains("кот"));
    }


}