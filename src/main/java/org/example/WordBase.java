package org.example;

import java.util.Objects;

/**
 * Класс который описывает пару слов которые нужно пользователю
 */
public class WordBase {
    private final String originalWord;
    private String wordTranslation;

    public WordBase(String originalWord, String wordTranslation) {
        this.originalWord = originalWord;
        this.wordTranslation = wordTranslation;
    }

    /**
     * Метод для получения слова
     *
     * @return originalWord
     */
    public String getWord() {
        return originalWord;
    }

    /**
     * Метод для получения перевода слова
     *
     * @return wordTranslation
     */
    public String getTranslation() {
        return wordTranslation;
    }

    /**
     * Метод который меняет перевод слова
     */
    public void changeTranslation(String newWord) {
        this.wordTranslation = newWord;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WordBase wordBase = (WordBase) o;
        return Objects.equals(originalWord, wordBase.originalWord);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(originalWord);
    }
}