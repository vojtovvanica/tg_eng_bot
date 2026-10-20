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
    private WordMemory memory;

    @BeforeEach
    void setUp() {
        memory = new WordMemory();
        commands = new MainCommands(memory);

        commands.addDict(1L, "testDict");
        commands.open(1L, "testDict");
    }

    /**
     * Проверяет корректность добавления новой пары слов
     * Тест команды /add
     *
     */
    @Test
    void testAdd() {
        commands.add(1L, "cat", "кот");
        assertTrue(commands.view(1L).contains("cat - кот"));
        commands.delete(1L, "cat");
    }

    /**
     * Проверяет корректность удаления слов из списка
     * Тест команды /delete
     *
     */
    @Test
    void testDelete() {
        commands.add(1L, "cat", "кот");
        commands.delete(1L, "cat");
        assertFalse(commands.view(1L).contains("cat"));
    }

    /**
     * Проверяет корректность получения запрашиваемой пары слов
     * Тест команды /get
     *
     */
    @Test
    void testGet() {
        commands.add(1L, "cat", "кот");
        assertEquals("cat - кот", commands.get(1L, "cat"));
        commands.delete(1L, "cat");
    }

    /**
     * Проверяет корректность изменения перевода слова
     * Тест команды /edit
     *
     */
    @Test
    void testEdit() {
        commands.add(1L, "cat", "кот");
        commands.edit(1L, "cat", "кошка");
        assertEquals("cat - кошка", commands.get(1L, "cat"));
        commands.delete(1L, "cat");
    }

    /**
     * Проверяет корректность получаемого списка слов
     * Тест команды /view
     *
     */
    @Test
    void testView() {
        commands.add(1L, "cat", "кот");
        commands.add(1L, "dog", "собака");
        String result = commands.view(1L);
        assertTrue(result.contains("cat - кот"));
        assertTrue(result.contains("dog - собака"));
        commands.delete(1L, "cat");
        commands.delete(1L, "dog");
    }

    /**
     * Проверяет, что списки пользователей корректны и не пересекаются
     * Тест изоляции данных по chatId
     *
     */
    @Test
    void isolateUsers() {
        commands.addDict(1L, "animals");
        commands.open(1L, "animals");
        commands.add(1L, "cat", "кот");

        commands.addDict(2L, "animals");
        commands.open(2L, "animals");
        commands.add(2L, "dog", "собака");

        assertTrue(commands.view(1L).contains("cat"));
        assertFalse(commands.view(1L).contains("dog"));

        assertTrue(commands.view(2L).contains("dog"));
        assertFalse(commands.view(2L).contains("cat"));

        commands.delete(1L, "cat");
        commands.delete(2L, "dog");

        commands.deleteDict(1L, "animals");
        commands.deleteDict(2L, "animals");
    }

    /**
     * Тестирует функцию добавления нового словаря
     *
     */
    @Test
    void testAddDict() {
        commands.addDict(1L, "animals");
        assertTrue(commands.viewDict(1L).contains("animals"));
        commands.deleteDict(1L, "animals");
    }

    /**
     * Тестирует, что метод addDict выведет,
     * если пользователь захочет создать словарь с уже существующим названием
     *
     */
    @Test
    void testAddDuplicateDict() {
        commands.addDict(1L, "animals");
        String result = commands.addDict(1L, "animals");
        assertTrue(result.contains("У вас уже есть словарь с таким названием"));
        commands.deleteDict(1L, "animals");
    }

    /**
     * Тестирует метод удаления словаря
     *
     */
    @Test
    void testDeleteDict() {
        commands.addDict(1L, "animals");
        commands.deleteDict(1L, "animals");
        assertFalse(commands.viewDict(1L).contains("animals"));
    }

    /**
     * Тестирует, что при смене словаря
     * первод записывается в нужный словарь
     *
     */
    @Test
    void testSwitchDictionaries() {
        commands.addDict(1L, "animals");
        commands.addDict(1L, "food");

        commands.open(1L, "animals");
        commands.add(1L, "cat", "кот");

        commands.open(1L, "food");
        commands.add(1L, "apple", "яблоко");

        assertTrue(commands.view(1L).contains("apple"));
        assertFalse(commands.view(1L).contains("cat"));

        commands.open(1L, "animals");
        assertTrue(commands.view(1L).contains("cat"));
        assertFalse(commands.view(1L).contains("apple"));

        commands.delete(1L, "cat");
        commands.deleteDict(1L, "animals");
        commands.open(1L, "food");
        commands.delete(1L, "apple");
        commands.deleteDict(1L, "food");
    }
    /*
    @Test
    void testSaveAndLoad() {
        WordMemory memory1 = new WordMemory();
        memory1.addDictionary(1L, "family");
        memory1.openDictionary(1L, "family");
        memory1.add(1L, new WordBase("cat", "кот"));
        WorkWithFile fileWorker = new WorkWithFile();
        fileWorker.saveToFile(memory1.getData());
        WordMemory memory2 = new WordMemory();
        fileWorker.readFromFile(memory2.getData());
        assertTrue(memory2.getDictionaryNames(1L).contains("family"));

        memory2.openDictionary(1L, "family");
        assertEquals(1, memory2.size(1L));
        assertEquals("cat", memory2.getAll(1L).get(0).getWord());

        memory1.deleteDictionary(1L, "family");
        fileWorker.saveToFile(memory1.getData());
        memory2.deleteDictionary(2L, "family");
        fileWorker.saveToFile(memory2.getData());

    }*/


}