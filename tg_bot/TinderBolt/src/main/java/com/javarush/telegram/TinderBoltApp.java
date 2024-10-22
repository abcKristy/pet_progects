package com.javarush.telegram;

import com.javarush.telegram.ChatGPTService;
import com.javarush.telegram.DialogMode;
import com.javarush.telegram.MultiSessionTelegramBot;
import com.javarush.telegram.UserInfo;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = "TestAiTinerBot"; //TODO: добавь имя бота в кавычках
    public static final String TELEGRAM_BOT_TOKEN = "7808269150:AAF31QPn2lWBVBZc-dbNdUYbXRc0rVT7Em0"; //TODO: добавь токен бота в кавычках
    public static final String OPEN_AI_TOKEN = "gpt:4dws6NYyD0BDK2ufp71ZJFkblB3TCC3tppbmX6OYmhSFydbM"; //TODO: добавь токен ChatGPT в кавычках

    private ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode currentMode = null;
    private ArrayList<String> list = new ArrayList<>();
    private UserInfo me;
    private UserInfo opponent;
    private int questionCount;
    public TinderBoltApp() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }

    @Override
    public void onUpdateEventReceived(Update update) {

        String message = getMessageText();

        if(message.equals("/start")){
            currentMode =DialogMode.MAIN;
            sendPhotoMessage("main");
            String text = loadMessage("main");
            sendTextMessage(text);
            showMainMenu("главное меню бота","/start",
                    "генерация Tinder-профля","/profile",
                    "сообщение для знакомства","/opener",
                    "переписка от вашего имени","/message",
                    "переписка со звездами","/date",
                    "адать вопрос чату GPT","/gpt");
            return;
        }

        if (message.equals("/gpt")) {
            currentMode = DialogMode.GPT;
            sendPhotoMessage("gpt");
            String text = loadMessage("gpt");
            sendTextMessage(text);
            return;
        }

        if(currentMode == DialogMode.GPT && !isMessageCommand()){
            String promt = loadPrompt("gpt");
            Message msg = sendTextMessage("Подождите, запрос отправлен в космоc . . .");
            String answer = chatGPT.sendMessage(promt,message);
            updateTextMessage(msg,answer);
            return;
        }

        if(message.equals("/date")){
            currentMode =DialogMode.DATE;
            sendPhotoMessage("date");
            String text = loadMessage("date");
            sendTextButtonsMessage(text,
                    "Ариана Гране","date_grande",
                    "Марго Робби","date_robbie",
                    "Зендея","date_zendaya",
                    "Райн Гослинг","date_gosling",
                    "Том Харди","date_hardy");
            return;
        }
        if(currentMode == DialogMode.DATE && !isMessageCommand()){
            String query = getCallbackQueryButtonKey();
            if(query.startsWith("date_")){
                sendPhotoMessage(query);
                sendTextMessage("Отличный выбор");
                String promt = loadPrompt(query);
                chatGPT.setPrompt(promt);
                return;
            }
            Message msg = sendTextMessage("Подождите, вам отвечает лапочка . . .");
            String answer = chatGPT.addMessage(message);
            updateTextMessage(msg,answer);
            return;
        }

        if(message.equals("/message")){
            currentMode = DialogMode.MESSAGE;
            sendPhotoMessage("message");
            sendTextButtonsMessage("Отправьте в чат вашу переписку",
                    "Следующее сообщение","message_next",
                    "Пригласить на свидание","message_date");
            return;
        }
        if(currentMode == DialogMode.MESSAGE && !isMessageCommand()){
            String query = getCallbackQueryButtonKey();
            if(query.startsWith("message_")){
                String promt = loadPrompt(query);
                String userChartHistory = String.join("\n\n",list);
                Message msg = sendTextMessage("Подождите, запрос отправлен в космоc . . .");
                String answer = chatGPT.sendMessage(promt,userChartHistory);
                updateTextMessage(msg,answer);
                return;
            }
            list.add(message);
            return;
        }

        if(message.equals("/profile")){
            currentMode = DialogMode.PROFILE;
            sendPhotoMessage("profile");

            me = new UserInfo();
            questionCount = 1;
            sendTextMessage(loadMessage("profile"));
            sendTextMessage("Сколько вaм лет?");
            return;
        }

        if(currentMode == DialogMode.PROFILE && !isMessageCommand()){
            switch (questionCount){
                case 1:
                    me.age = message;
                    questionCount=2;
                    sendTextMessage("Кем вы работаете?");
                    return;
                case 2:
                    me.occupation = message;
                    questionCount=3;
                    sendTextMessage("У вас есть хобби?");
                    return;
                case 3:
                    me.hobby = message;
                    questionCount=4;
                    sendTextMessage("Вы из какого города?");
                    return;
                case 4:
                    me.city = message;
                    questionCount=5;
                    sendTextMessage("Что вам не нравится в людях?");
                    return;
                case 5:
                    me.annoys = message;
                    String adout_myself = me.toString();
                    Message msg = sendTextMessage("Подождите, запрос отправлен в космоc . . .");
                    String answer = chatGPT.sendMessage(loadPrompt("profile"),adout_myself);
                    updateTextMessage(msg,answer);
                    return;
            }
            return;
        }

        if(message.equals("/opener")){
            currentMode = DialogMode.OPENER;
            sendPhotoMessage("opener");
            sendTextMessage(loadMessage("opener"));
            opponent = new UserInfo();
            questionCount =1;
            sendTextMessage("Пол собеседника");
            return;
        }
        if(currentMode==DialogMode.OPENER && !isMessageCommand()){
            switch (questionCount){
                case 1:
                    opponent.sex = message;
                    questionCount=2;
                    sendTextMessage("Кем работает?");
                    return;
                case 2:
                    opponent.occupation = message;
                    questionCount=3;
                    sendTextMessage("Есть хобби?");
                    return;
                case 3:
                    opponent.hobby = message;
                    questionCount=4;
                    sendTextMessage("Из какого города?");
                    return;
                case 4:
                    opponent.city = message;
                    questionCount=5;
                    sendTextMessage("Сколько лет?");
                    return;
                case 5:
                    opponent.age = message;
                    String about_friend = opponent.toString();
                    Message msg = sendTextMessage("Подождите, запрос отправлен в космоc . . .");
                    String answer = chatGPT.sendMessage(loadPrompt("opener"),about_friend);
                    updateTextMessage(msg,answer);
                    return;
            }
            return;
        }


        sendTextMessage("*Привет!*");
        sendTextMessage("_Привет!_");

        sendTextMessage("Вы написали "+message);
        sendTextButtonsMessage("Выберите режим работы: ",
                "Старт","start",
                "Стоп","stop");

    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}

























