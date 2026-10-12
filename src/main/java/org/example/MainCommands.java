package org.example;
 /**Логика работы основных команд бота*/
public class MainCommands {

    private final WordMemory instance = new WordMemory();
    /**Реализация команды help
     * @return вспомогательный список с командами*/
    public String help(){
      return """
              Доступные команды:
              /add word translation - добавить пару слов
              /delete word - удалить слово
              /get word - посмотреть перевод слова
              /edit word newTranslation - редактировать перевод слова
              /view - посмотреть слова, которые уже добавлены
              """;
    }
     /**Реализация команды add
      * добавляет новое слово в словарь*/
    public String add(long chatId, String word, String translation){
        if (word == null || word.isBlank() || translation == null || translation.isBlank()){
            return "Пожалуйства введите пару слов корректно" +
                    "example: add word translation";
        }
        if (instance.findByWord(chatId, word) != null){
            return "Слово " + word + " уже есть в словаре";
        }
        instance.add(chatId, new WordBase(word, translation));
        return "Слово успешно добавлено";

    }
     /**Реализация команды delete*/
    public String delete(long chatId, String word){
        if(instance.delete(chatId, word)){
            return "Слово удалено из списка";
        }
        return "Слово не найдено в словаре";
    }
     /**Реализация команды get
      * @return перевод запрашиваемого слова*/
    public String get(long chatId,String word){
        WordBase p = instance.findByWord(chatId, word);
        if (p == null){
            return "Вы еще не добавляли это слово";
        }
        return word + " - " + p.getTranslation();

    }
     /**Реализация команды edit
      * редактирует перевод запрашиваемого слова*/
    public String edit(long chatId, String word, String newTranslation){
      WordBase p = instance.findByWord(chatId, word);
      if (p == null){
          return "Вы еще не добавляли это слово в словарь";
      }
      p.changeTranslation(newTranslation);
      return "Перевод слова изменен";
    }
     /**Реализация команды view
      * @return спиисок слов которые пользователь добавил в словарь*/
    public String view(long chatId){
        if(instance.size(chatId) == 0){
            return "Ваш список пуст";

        }
        StringBuilder newMessage = new StringBuilder("Ваш список слов: \n");
        for (WordBase p : instance.getAll(chatId)) {
            newMessage.append(p.getWord()).append(" - ").append(p.getTranslation()).append("\n");
        }
        return newMessage.toString().trim();
    }

    public String start(long chatId){
        if (instance.size(chatId) == 0){
            return "Привет, я бот, который поможет тебе запомнить английские слова. \n" +
                    "У вас пока нет слов для обучения, хотите добавить слово? " +
                    "Напишите какие слова вы хотите добавить в формате /add word translation\n";
        }
        return help();
    }

}
