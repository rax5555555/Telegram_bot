import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Bot extends TelegramLongPollingBot {
    final private String BOT_NAME = "super_calculator_bot";

    byte[] arr1 = {-30, -101, -123, -17, -72, -113};
    byte[] arr2 = {-16, -97, -116, -95};

    String BOT_TOKEN;
    Storage storage;

    Uart uart;
    ReplyKeyboardMarkup replyKeyboardMarkup;

    Bot()
    {
        uart = new Uart("COM9");

        File file = new File("./src/main/resources/token.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BOT_TOKEN = sc.nextLine();
        sc.close();

        storage = new Storage();
        initKeyboard();
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {

        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try{
            if(update.hasMessage() && update.getMessage().hasText())
            {
                //Извлекаем из объекта сообщение пользователя
                Message inMess = update.getMessage();
                //Достаем из inMess id чата пользователя
                String chatId = inMess.getChatId().toString();
                //Получаем текст сообщения пользователя, отправляем в написанный нами обработчик
                String response = parseMessage(inMess.getText());

                //Создаем объект класса SendMessage - наш будущий ответ пользователю
                SendMessage outMess = new SendMessage();

                //Добавляем в наше сообщение id чата а также наш ответ
                outMess.setChatId(chatId);
                outMess.setText(response);

                outMess.setReplyMarkup(replyKeyboardMarkup);

                //Отправка в чат
                execute(outMess);

            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public String parseMessage(String textMsg) {
        String response;

        //Сравниваем текст пользователя с нашими командами, на основе этого формируем ответ
        if(textMsg.equals("/start"))
            //response = "Приветствую, бот знает много цитат. Жми /get, чтобы получить случайную из них";
            response = "Hello, this bot remote control smart home";
        else if(textMsg.equals("/get"))
            response = storage.getRandQuote();
        else if(textMsg.equals("Blink")) {
            uart.uartwrite('1');
            response = "LED Blink";
        }
        else if(textMsg.equals("On")) {
            uart.uartwrite('2');
            response = "LED On";
        }
        else if(textMsg.equals("Off")) {
            uart.uartwrite('3');
            response = "LED Off";
        }
        else
            // = "Сообщение не распознано";
            //response = textMsg;
        {
            try {
                uart.uartwrite('1');
                String infostr = "Error, please try again";
                String[] word = uart.uartread().split(" ");
                if (word.length == 2) {
                    System.out.println(word[0]);
                    System.out.println(word[1]);

                    String str = new String(arr2, "UTF-8");
                    String str2 = new String(arr1, "UTF-8");
                    infostr = str + " temperature = " + word[0] + " C" + "\n" +
                              str2 + " pressure = " + (int)(Double.parseDouble(word[1])/133.322) + " mm Hg";
                }

                response = infostr;

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }



        System.out.println(Arrays.toString(textMsg.getBytes()));

        return response;
    }

    void initKeyboard()
    {
        //Создаем объект будущей клавиатуры и выставляем нужные настройки
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true); //подгоняем размер
        replyKeyboardMarkup.setOneTimeKeyboard(false); //скрываем после использования

        //Создаем список с рядами кнопок
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        //Создаем один ряд кнопок и добавляем его в список
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        //Добавляем одну кнопку с текстом "Просвяти" наш ряд

        try {
            String str = new String(arr2, "UTF-8");

            keyboardRow.add(new KeyboardButton(str + " Info"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


        keyboardRow.add(new KeyboardButton("On"));
        keyboardRow.add(new KeyboardButton("Off"));
        //добавляем лист с одним рядом кнопок в главный объект
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }
}
