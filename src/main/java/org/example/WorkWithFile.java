package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для реализации работы с файлом
 * Реализует сохранение пользовательских словарей со словами
 * в виде chatId dictionary originalWord translation
 */

public class WorkWithFile {
    private final Path filePath;

    /**
     * Конструктор для бота
     * Создает путь к папке data в корне проекта
     */
    public WorkWithFile() {
        this.filePath = Paths.get("data", "englishWords.txt");
    }

    /**
     * Конструктор для тестов
     * Принимает путь к временной папке, которую создал JUnit.
     */
    public WorkWithFile(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Метод, который проверяет существование файла
     * Если файла не существует создает его
     */
    public void existenceOfTheFile() {
        try {
            Path dir = filePath.getParent();
            if (dir != null && !Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            System.err.println("Ошибка создания файла " + e.getMessage());
        }
    }

    /**
     * Метод для сохранения слов для последующего использования
     * При выходе программа записывает все слова и словари пользователя в файл
     */
    public void saveToFile(Map<Long, Map<String, List<WordBase>>> data) {
        List<String> File = new ArrayList<>();

        for (Map.Entry<Long, Map<String, List<WordBase>>> userInf : data.entrySet()) {
            long chatId = userInf.getKey();

            Map<String, List<WordBase>> userDictionaries = userInf.getValue();

            for (Map.Entry<String, List<WordBase>> dictionaries : userDictionaries.entrySet()) {
                String dicrionary = dictionaries.getKey();

                List<WordBase> words = dictionaries.getValue();
                for (WordBase word : words) {
                    File.add(chatId + "|" + dicrionary + "|" + word.getWord() + "|" + word.getTranslation());
                }
            }
        }
        try {
            Files.write(this.filePath, File);
            System.out.println("Слова сохранены в файл");
        } catch (IOException e) {
            System.err.println("Произошла ошибка " + e.getMessage());
        }

    }

    /**
     * Метод который при запуске кода читает слова и словари,
     * которые были записаны пользователями до этого и записывает их в Map
     * для дальнейшего использования
     */
    public void readFromFile(Map<Long, Map<String, List<WordBase>>> data) {
        existenceOfTheFile();
        data.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(this.filePath.toFile()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;
                long chatId = Long.parseLong(parts[0]);
                String dictName = parts[1];
                String originalWord = parts[2];
                String translation = parts[3];

                Map<String, List<WordBase>> userDicts = data.get(chatId);
                if (userDicts == null) {
                    userDicts = new HashMap<>();
                    data.put(chatId, userDicts);
                }
                List<WordBase> words = userDicts.get(dictName);
                if (words == null) {
                    words = new ArrayList<>();
                    userDicts.put(dictName, words);
                }
                words.add(new WordBase(originalWord, translation));
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла " + e.getMessage());
        }
    }


}
