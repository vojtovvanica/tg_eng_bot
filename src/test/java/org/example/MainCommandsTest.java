package org.example;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class MainCommandsTest {
    private MainCommands commands;

    @BeforeEach
    void setUp() {
        commands = new MainCommands();
    }

    @Test
    void add() {
        commands.add(1L, "cat", "кот");
        assertTrue(commands.view(1L).contains("cat - кот"));
    }

    @Test
    void delete() {
        commands.add(1L, "cat", "кот");
        commands.delete(1L, "cat");
        assertFalse(commands.view(1L).contains("cat"));
    }

    @Test
    void get() {
        commands.add(1L, "cat", "кот");
        assertEquals("cat - кот", commands.get(1L, "cat"));
    }

    @Test
    void edit() {
    commands.add(1L, "cat", "кот");
    commands.edit(1L, "cat", "кошка");
    assertEquals("cat - кошка", commands.get(1L, "cat"));
    }

    @Test
    void view() {
        commands.add(1L, "cat", "кот");
        commands.add(1L, "dog", "собака");
        assertEquals("Ваш список слов: \ncat - кот\ndog - собака", commands.view(1L));
    }
    @Test
    void IsolateUsers() {
        commands.add(1L, "cat", "кот");
        commands.add(2L, "dog", "собака");

        assertTrue(commands.view(1L).contains("cat"));
        assertFalse(commands.view(1L).contains("dog"));

        assertTrue(commands.view(2L).contains("dog"));
        assertFalse(commands.view(2L).contains("cat"));
    }
}