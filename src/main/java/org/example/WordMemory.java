package org.example;
import java.util.*;

/**Класс который реализует хранение слов в памяти*/
public class WordMemory {
    private final Map<Long, List<WordBase>> data = new HashMap<>();
    /**Метод который проверяет существует ли лист для конкретного пользователя по ID чата
     * @return list*/
    private List<WordBase> listForUser(long chatId){
        List<WordBase> list = data.get(chatId);
        if (list == null){
            list = new ArrayList<>();
            data.put(chatId, list);
        }
        return list;
    }
    /**Добавляет пару слов в словарь*/
    public void add(long chatId, WordBase pair){
        listForUser(chatId).add(pair);
    }
    /**Возвращает все слова которые пользователь добавил в словарб
     * С помощью копирования из общего словаря
     * @return new ArrayList<>*/
    public List<WordBase> getAll(long chatId){
        return new ArrayList<>(listForUser(chatId));
    }
    /**Метод который удаляет пару слов из списка*/
    public boolean delete(long chatId, String word){
        List<WordBase> list = listForUser(chatId);
        boolean deleted = false;
        Iterator<WordBase> currentWord = list.iterator();
        while(currentWord.hasNext()){
            WordBase p = currentWord.next();
            if(p.getWord().equals(word)){
                currentWord.remove();
                deleted = true;
            }
        }
        return deleted;

    }
    /**Метод для поиска слова в списке пользователя*/
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
    /** Количество слов у пользователя. */
    public int size(long chatId) {
        return listForUser(chatId).size();
    }
}
