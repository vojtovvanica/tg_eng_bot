package org.example;

import java.util.*;

/**
 * Класс который реализует хранение слов в памяти
 */
public class WordMemory {
    private final Map<Long, Map<String, List<WordBase>>> data = new HashMap<>();
    private final Map<Long, String> openDictionary = new HashMap<>();
    private final WorkWithFile fileWorker;

    public WordMemory() {
        this.fileWorker = new WorkWithFile();
        this.fileWorker.readFromFile(this.data);
    }

    public WordMemory(WorkWithFile fileWorker) {
        this.fileWorker = fileWorker;
        this.fileWorker.readFromFile(this.data);
    }

    public WorkWithFile getFileWorker() {
        return this.fileWorker;
    }

    /**
     * Метод для получения всех словарей пользователя
     * Возвращает список созданных словарей
     *
     */
    private Map<String, List<WordBase>> dictionariesForUser(long chatId) {
        Map<String, List<WordBase>> userDicts = data.get(chatId);
        if (userDicts == null) {
            userDicts = new HashMap<>();
            data.put(chatId, userDicts);
        }
        return userDicts;
    }

    /**
     * Метод для получения слов из открытого словаря пользователя
     *
     */

    private List<WordBase> listForUser(long chatId) {
        String openDictName = openDictionary.get(chatId);
        if (openDictName == null) {
            return new ArrayList<>();
        }
        Map<String, List<WordBase>> userDicts = dictionariesForUser(chatId);
        List<WordBase> words = userDicts.get(openDictName);
        if (words == null) {
            words = new ArrayList<>();
            userDicts.put(openDictName, words);
        }
        return words;
    }

    /**
     * Добавляет пару слов в словарь
     */
    public void add(long chatId, WordBase pair) {
        listForUser(chatId).add(pair);
    }

    /**
     * Возвращает все слова которые пользователь добавил в словарб
     * С помощью копирования из общего словаря
     *
     * @return new ArrayList<>
     */
    public List<WordBase> getAll(long chatId) {
        return new ArrayList<>(listForUser(chatId));
    }

    /**
     * Метод который удаляет пару слов из списка
     */
    public boolean delete(long chatId, String word) {
        List<WordBase> list = listForUser(chatId);
        return list.remove(new WordBase(word, ""));

    }

    public Map<Long, Map<String, List<WordBase>>> getData() {
        return this.data;
    }

    /**
     * Метод для поиска слова в списке пользователя
     */
    public WordBase findByWord(long chatId, String word) {
        List<WordBase> list = listForUser(chatId);
        Iterator<WordBase> iterator = list.iterator();
        while (iterator.hasNext()) {
            WordBase pair = iterator.next();
            String pairWord = pair.getWord();
            boolean matches = pairWord.equals(word);
            if (matches) {
                return pair;
            }
        }
        return null;
    }


    /**
     * Количество слов у пользователя.
     */
    public int size(long chatId) {
        return listForUser(chatId).size();
    }

    /**
     * Метод, который возвращает список словарей,
     * созданыых пользователем
     *
     */
    public List<String> getDictionaryNames(long chatId) {
        return new ArrayList<>(dictionariesForUser(chatId).keySet());
    }

    /**
     * Метод, для открытия словаря с нужными словами
     *
     */

    public boolean openDictionary(long chatId, String dictName) {
        if (dictionariesForUser(chatId).containsKey(dictName)) {
            openDictionary.put(chatId, dictName);
            return true;
        }
        return false;
    }

    /**
     * Метод, который возвращает название открытого словаря
     */
    public String getOpenDictionary(long chatId) {
        return openDictionary.get(chatId);
    }

    /**
     * Метод для реализации команды добавления нового словаря
     * Проверяет существует ли в списке словарей пользователя словарь
     * с таким значением, если нет создает его
     */
    public boolean addDictionary(long chatId, String dictName) {
        Map<String, List<WordBase>> userDicts = dictionariesForUser(chatId);
        if (userDicts.containsKey(dictName)) {
            return false;
        }
        userDicts.put(dictName, new ArrayList<>());
        return true;
    }


    /**
     * Метод для реализации команды удаления словаря
     */
    public boolean deleteDictionary(long chatId, String dictName) {
        Map<String, List<WordBase>> userDicts = dictionariesForUser(chatId);
        boolean removed = userDicts.remove(dictName) != null;
        if (removed && dictName.equals(openDictionary.get(chatId))) {
            openDictionary.remove(chatId);
        }

        return removed;
    }

    /**
     * Метод, который получает слова из словаря
     */
    public List<WordBase> getWordsFromDictionary(long chatId, String dictionaryName) {
        Map<String, List<WordBase>> userDicts = dictionariesForUser(chatId);
        List<WordBase> words = userDicts.get(dictionaryName);
        List<WordBase> result;
        if (words == null) {
            result = new ArrayList<>();
        } else {
            result = new ArrayList<>(words);
        }
        return result;
    }


}
