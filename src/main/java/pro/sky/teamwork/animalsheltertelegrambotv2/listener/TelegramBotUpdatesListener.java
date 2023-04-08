package pro.sky.teamwork.animalsheltertelegrambotv2.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.CarerService;

//import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author VitaliyK (commit)
 * <br>
 * Класс описывающий константы и работу класса
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final CarerService carerService;
    /**
     * Константа указывающая ID волонтера
     */
    private final static long VOLUNTEER_CHAT_ID = 1517311315; //указать id чата волонтеров, сейчас это мой личный ID
    public final static BotCommand START_COMMAND = new BotCommand("/start",
            "Основное меню");
    public final static BotCommand SHELTER_INFO_COMMAND = new BotCommand("/shelter_info",
            "Меню с информацией о приюте");
    public final static BotCommand SHELTER_MAIN_INFO_COMMAND = new BotCommand(
            "/shelter_main_info", "Основная информация о приюте");
    public final static BotCommand SHELTER_WORK_SCHEDULE_COMMAND = new BotCommand(
            "/shelter_work_schedule", "Информация о расписании работы приюта,\n" +
            " адресе, схеме проезда, контактной информации");
    public final static BotCommand SHELTER_SAFETY_RECOMMENDATIONS_COMMAND = new BotCommand(
            "/shelter_safety_recommendations", "Рекомендации о технике\n" +
            " безопасности на территории приюта");
    public final static BotCommand WRITE_CLIENT_CONTACT_COMMAND = new BotCommand(
            "/write_contact_information", "Записать контактные данные для\n" +
            " связи с волонтерами");
    public final static BotCommand CALL_VOLUNTEER_COMMAND = new BotCommand(
            "/call_volunteer", "Позвать волонтера");
    public final static BotCommand BACK_COMMAND = new BotCommand("/back",
            "Вернуться назад");

    public final static BotCommand TAKE_A_DOG_COMMAND = new BotCommand("/take_dog",
            "Как взять собаку из приюта");
    public final static BotCommand INTRODUCTION_TO_DOG_COMMAND = new BotCommand(
            "/intro_dog", "Узнать правила знакомства с собакой");
    public final static BotCommand TAKE_DOCUMENTS_LIST_COMMAND = new BotCommand(
            "/take_doc_list", "Получить список документов");

    public final static BotCommand TRANSFER_A_DOG_COMMAND = new BotCommand("/transfer_dog",
            "Транспортировка животного");
    public final static BotCommand ENVIRONMENT_FOR_PUPPY_COMMAND = new BotCommand(
            "/puppy_environment", "Обустройство" +
            " дома для щенка");
    public final static BotCommand ENVIRONMENT_FOR_DOG_COMMAND = new BotCommand(
            "/dog_environment", "Обустройство дома для взрослой собаки");

    public final static BotCommand ENVIRONMENT_FOR_LIMITED_DOG_COMMAND = new BotCommand(
            "/limited_dog_environment", "Обустройство дома для собаки\n" +
            " с ограниченными возможностями");
    public final static BotCommand CYNOLOGIST_ADVICES_COMMAND = new BotCommand(
            "/cynologist_advices", "советы кинолога" +
            " с ограниченными возможностями");
    public final static BotCommand CYNOLOGIST_CONTACTS_COMMAND = new BotCommand(
            "/cynologist_contacts", "Контакты проверенных кинологов");
    public final static BotCommand USUAL_REFUSALS_COMMAND = new BotCommand(
            "/usual_refusals",
            "Частые причины отказов в выдаче собаки кандидату");
    public final static BotCommand SEND_REPORT_COMMAND = new BotCommand("/send_report",
            "Прислать отчет о питомце");


    public TelegramBotUpdatesListener(TelegramBot telegramBot, CarerService carerService) {
        this.telegramBot = telegramBot;
        this.carerService = carerService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Метод описывающий логику работы меню приветсвия и рабу с константами
     *
     * @param updates расширение на класс {@link Update} телеграммбота
     * @return возврат к началу списка
     */
    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Processing update: {}", update);
                if (update.message() != null) {
                    String message = update.message().text();
                    long chatId = update.message().chat().id();
                    Pattern clientContactPattern = Pattern.compile(
                            "([А-я\\s]+)(\\s)(\\+\\d{1,7}\\(\\d{3}\\)\\d{7})");
                    Matcher matcher = clientContactPattern.matcher(message);

                    if (message.equals(START_COMMAND.command())) {
                        SendMessage response = new SendMessage(chatId,
                                "Добрый день! Меня зовут AnimalShelterBot. Я отвечаю на\n" +
                                        " популярные\n" +
                                        " вопросы о том, что нужно знать и уметь, чтобы забрать\n" +
                                        " собаку\n" +
                                        " из приюта.");
                        telegramBot.execute(response);
                        startCommandMenu(chatId);
                    } else if (message.equals(SHELTER_INFO_COMMAND.command())) {
                        shelterInfoCommandMenu(chatId);
                    } else if (message.equals(SHELTER_MAIN_INFO_COMMAND.command())) {
                        SendMessage sendMessage = new SendMessage(chatId,
                                "Заполнить основной инфой о приюте"); //заполнить по факту
                        telegramBot.execute(sendMessage);
                    } else if (message.equals(SHELTER_WORK_SCHEDULE_COMMAND.command())) {
                        sendShelterContacts(chatId);
                    } else if (message.equals(SHELTER_SAFETY_RECOMMENDATIONS_COMMAND.command())) {
                        SendMessage sendMessage = new SendMessage(chatId,
                                "Рекомендации о технике безопасности на территории приюта");
                        //заполнить по факту
                        telegramBot.execute(sendMessage);
                    } else if (message.equals(WRITE_CLIENT_CONTACT_COMMAND.command())) {
                        SendMessage sendMessage = new SendMessage(chatId,
                                "Прошу написать Ваши ФИО и контактный телефон в формате" +
                                        "+7(ХХХ)ХХХХХХХ.");
                        telegramBot.execute(sendMessage);
                    } else if (message.equals(CALL_VOLUNTEER_COMMAND.command())) {
                        long clientId = update.message().from().id();
                        String clientFirsName = update.message().from().firstName();
                        String clientLastName = update.message().from().lastName();
                        sendCallVolunteerCommand(chatId, clientId, clientFirsName, clientLastName);
                    } else if (message.equals(BACK_COMMAND.command())) {
                        startCommandMenu(chatId);
                    } else if (message.equals(TAKE_A_DOG_COMMAND.command())) {
                        takeDogCommandMenu(chatId);
                    } else if (message.equals(INTRODUCTION_TO_DOG_COMMAND.command())) {
                        sendPlainText(chatId);
                    } else if (message.equals(TAKE_DOCUMENTS_LIST_COMMAND.command())) {
                        sendPlainText(chatId);
                    } else if (message.equals(TRANSFER_A_DOG_COMMAND.command())) {
                        sendPlainText(chatId);
                    } else if (message.equals(ENVIRONMENT_FOR_PUPPY_COMMAND.command())) {
                        sendPlainText(chatId);
                    } else if (message.equals(ENVIRONMENT_FOR_DOG_COMMAND.command())) {
                        sendPlainText(chatId);
                    } else if (message.equals(ENVIRONMENT_FOR_LIMITED_DOG_COMMAND.command())) {
                        sendPlainText(chatId);
                    } else if (message.equals(CYNOLOGIST_ADVICES_COMMAND.command())) {
                        sendPlainText(chatId);
                    } else if (message.equals(CYNOLOGIST_CONTACTS_COMMAND.command())) {
                        sendPlainText(chatId);
                    } else if (message.equals(USUAL_REFUSALS_COMMAND.command())) {
                        sendPlainText(chatId);
                    } else if (message.equals(SEND_REPORT_COMMAND.command())) {
                        sendReportCommandMenu(chatId);


                    } else if (matcher.matches()) {
                        String clientName = matcher.group(1);
                        String clientPhoneNumber = matcher.group(3);

                        carerService.addCarer(clientName, 20, clientPhoneNumber);
                        SendMessage sendMessageForVolunteer = new SendMessage(VOLUNTEER_CHAT_ID,
                                "Прошу связаться с клиентом " + clientName + " по телефону "
                                        + clientPhoneNumber);
                        SendMessage sendMessageForClient = new SendMessage(chatId,
                                "Ваши контактные данные записаны. Волонтеры свяжутся с Вами\n" +
                                        " в ближайшее время.");
                        telegramBot.execute(sendMessageForVolunteer);
                        telegramBot.execute(sendMessageForClient);
                    } else {
                        SendMessage sendMessage = new SendMessage(chatId, "Неизвестная комманда");
                        telegramBot.execute(sendMessage);
                    }


                } else if (update.message() == null && update.callbackQuery() != null) {
                    String message = update.callbackQuery().data();
                    String callBackQueryId = update.callbackQuery().id();
                    long chatId = update.callbackQuery().message().chat().id();
                    AnswerCallbackQuery callbackQuery = new AnswerCallbackQuery(callBackQueryId)
                            .showAlert(false);
                    telegramBot.execute(callbackQuery);

                    if (message.equals(SHELTER_INFO_COMMAND.command())) {
                        shelterInfoCommandMenu(chatId);
                    } else if (message.equals(TAKE_A_DOG_COMMAND.command())) {
                        takeDogCommandMenu(chatId);
                    } else if (message.equals(INTRODUCTION_TO_DOG_COMMAND.command())) {
                        sendPlainText(chatId);
                    } else if (message.equals(TAKE_DOCUMENTS_LIST_COMMAND.command())) {
                        sendPlainText(chatId);
                    }else if (message.equals(TRANSFER_A_DOG_COMMAND.command())) {
                        sendPlainText(chatId);
                    }else if (message.equals(ENVIRONMENT_FOR_PUPPY_COMMAND.command())) {
                        sendPlainText(chatId);
                    }else if (message.equals(ENVIRONMENT_FOR_DOG_COMMAND.command())) {
                        sendPlainText(chatId);
                    }else if (message.equals(ENVIRONMENT_FOR_LIMITED_DOG_COMMAND.command())) {
                        sendPlainText(chatId);
                    }else if (message.equals(CYNOLOGIST_ADVICES_COMMAND.command())) {
                        sendPlainText(chatId);
                    }else if (message.equals(CYNOLOGIST_CONTACTS_COMMAND.command())) {
                        sendPlainText(chatId);
                    }else if (message.equals(USUAL_REFUSALS_COMMAND.command())) {
                        sendPlainText(chatId);
                    } else if (message.equals(SEND_REPORT_COMMAND.command())) {
                        sendReportCommandMenu(chatId);
                    } else if (message.equals(SHELTER_MAIN_INFO_COMMAND.command())) {
                        SendMessage sendMessage = new SendMessage(chatId,
                                "Заполнить основной инфой о приюте"); //заполнить по факту
                        telegramBot.execute(sendMessage);
                    } else if (message.equals(SHELTER_WORK_SCHEDULE_COMMAND.command())) {
                        sendShelterContacts(chatId);
                    } else if (message.equals(SHELTER_SAFETY_RECOMMENDATIONS_COMMAND.command())) {
                        SendMessage sendMessage = new SendMessage(chatId,
                                "Рекомендации о технике безопасности на территории приюта");
                        //заполнить по факту
                        telegramBot.execute(sendMessage);
                    } else if (message.equals(WRITE_CLIENT_CONTACT_COMMAND.command())) {
                        SendMessage sendMessage = new SendMessage(chatId, "Прошу написать Ваши ФИО и \n" +
                                "контактный телефон в формате" +
                                "+7(ХХХ)ХХХХХХХ.");
                        telegramBot.execute(sendMessage);
                    } else if (message.equals(CALL_VOLUNTEER_COMMAND.command())) {
                        long clientId = update.callbackQuery().from().id();
                        String clientFirstName = update.callbackQuery().from().firstName();
                        String clientLastName = update.callbackQuery().from().lastName();
                        sendCallVolunteerCommand(chatId, clientId, clientFirstName,
                                clientLastName);

                    } else if (message.equals(BACK_COMMAND.command())) {
                        startCommandMenu(chatId);
                    } else {
                        SendMessage sendMessage = new SendMessage(chatId, "Неизвестная комманда");
                        telegramBot.execute(sendMessage);
                    }
                }
            });
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * выводит сообщение с контактами и схемой проезда к приюту
     * @param chatId идентификатор чата, в который выводится информация
     * @see SendMessage
     * @see SendPhoto
     */
    public void sendShelterContacts(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, //заполнить по факту
                """
                        Расписание работы приюта:
                        номер телефона:
                        e-mail:
                        """);
        SendPhoto sendPhoto = new SendPhoto(chatId,
                new File("src/redaktirovat-kartu.png"));
        telegramBot.execute(sendMessage);
        telegramBot.execute(sendPhoto);
    }

    /**
     * Метод описывающий кнопки в начальном окне бота.
     *
     * <br> при обращении выдается сообщение через <b>SendMessage</b>, которое ссылается
     * на класс {@link SendMessage}
     * <br> если текст вводится в строке команды, то бот инициализирует его и переводит
     * запрос по <b>callbackData</b>,
     * в ином случае прожимается кнопка соответствующей команды.
     *
     * @param chatId
     */
    private void startCommandMenu(long chatId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Узнать информацию о приюте").callbackData("/shelter_info"),
                new InlineKeyboardButton("Как взять собаку из приюта")
                        .callbackData("/take_dog"),
                new InlineKeyboardButton("Прислать отчет о питомце").callbackData("/send_report"),
                new InlineKeyboardButton("Позвать волонтера").callbackData("/call_volunteer")
        ));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        SendMessage response = new SendMessage(chatId, "Что бы Вы хотели узнать?");
        response.replyMarkup(keyboard);
        telegramBot.execute(response);
    }

    /**
     * Метод описывающий кнопки в начальном окне бота.
     * <br> если текст вводится в строке команды, то бот инициализирует его и переводит запрос
     * по <b>callbackData</b>,
     * в ином случае прожимается кнопка соответствующей команды.
     *
     * @param chatId
     */
    private void shelterInfoCommandMenu(long chatId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Основная информация").callbackData("/shelter_main_info"),
                new InlineKeyboardButton("Расписание работы, адрес, схема проезда, контактная\n" +
                        " информация").callbackData("/shelter_work_schedule"),
                new InlineKeyboardButton("Общие рекомендации о технике безопасности на территории\n"
                        +
                        " приюта").callbackData("/shelter_safety_recommendations"),
                new InlineKeyboardButton("Записать Ваши контактные данные для связи")
                        .callbackData("/write_contact_information"),
                new InlineKeyboardButton("Позвать волонтера").callbackData("/call_volunteer"),
                new InlineKeyboardButton("Вернуться назад").callbackData("/back")
        ));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        SendMessage response = new SendMessage(chatId, "Добрый день! Здесь Вы можете узнать\n" +
                " основную информацию о нашем приюте.");
        response.replyMarkup(keyboard);
        telegramBot.execute(response);
    }

    /**
     * вывод кнопок меню "Как взять собаку из приюта" и отправка соответствующих
     * данных {@link InlineKeyboardButton#callbackData()} при нажатии кнопки
     * @param chatId идентификатор чата, в котором выводятся кнопки
     */
    private void takeDogCommandMenu(long chatId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Узнать правила знакомства с собакой")
                        .callbackData("/intro_dog"),

                new InlineKeyboardButton("Получить список документов")
                        .callbackData("/take_doc_list"),

                new InlineKeyboardButton("Транспортировка животного")
                        .callbackData("/transfer_dog"),

                new InlineKeyboardButton("Обустройство дома для щенка")
                        .callbackData("/puppy_environment"),

                new InlineKeyboardButton("Обустройство дома для взрослой собаки")
                        .callbackData("/dog_environment"),

                new InlineKeyboardButton("Обустройство дома для собаки с ограниченными возможностями")
                        .callbackData("/limited_dog_environment"),

                new InlineKeyboardButton("советы кинолога")
                        .callbackData("/cynologist_advices"),

                new InlineKeyboardButton("Контакты проверенных кинологов")
                        .callbackData("/cynologist_contacts"),

                new InlineKeyboardButton("Частые причины отказов в выдаче собаки кандидату")
                        .callbackData("/usual_refusals"),

                new InlineKeyboardButton("Записать Ваши контактные данные для связи")
                        .callbackData("/write_contact_information"),

                new InlineKeyboardButton("Позвать волонтера").callbackData("/call_volunteer"),
                new InlineKeyboardButton("Вернуться назад").callbackData("/back")
        ));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        SendMessage response = new SendMessage(chatId, "Добрый день! Здесь Вы можете узнать\n" +
                " как взять собаку из приюта.");
        response.replyMarkup(keyboard);
        telegramBot.execute(response);
    }

    /**
     * вывод в чат кнопок с командами меню и отправка данных при нажатии на кнопку
     * @param chatId идентификатор чата, в котором выводятся кнопки
     */
    private void sendReportCommandMenu(long chatId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Сфотайте на телефон и пришлите фото питомца.")
                        .callbackData("/"),

                new InlineKeyboardButton("Пришлите сегодняшний рацион питомца")
                        .callbackData("/"),

                new InlineKeyboardButton("Опишите общее самочуствие и привыкание к новому\n" +
                        " месту питомца")
                        .callbackData("/"),

                new InlineKeyboardButton("Опишите изменение в поведении питомца: отказ от старых\n" +
                        " привычек, приобретение новых")
                        .callbackData("/"),

                new InlineKeyboardButton("Позвать волонтера").callbackData("/call_volunteer"),
                new InlineKeyboardButton("Вернуться назад").callbackData("/back")
        ));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        SendMessage response = new SendMessage(chatId, "Здесь Вы можете узнать\n" +
                " как отправить отчёт о питомце.");
        response.replyMarkup(keyboard);
        telegramBot.execute(response);
    }

    /**
     * обработчик команды "Позвать волонтёра"
     * @param chatId идентификатор чата, в который отсылается сообщение
     * @param clientId идентификатор клиента, с которым должен связаться волонтёр
     * @param clientFirstName имя клиента, с которым должен связаться волонтёр
     * @param clientLastName фамилия клиента, с которым должен связаться волонтёр
     */
    public void sendCallVolunteerCommand(long chatId, long clientId, String clientFirstName,
                                         String clientLastName) {
        SendMessage sendMessageForClient = new SendMessage(chatId,
                "Волонтер свяжется с Вами в ближайшее время");
        SendMessage sendMessageForVolunteer = new SendMessage(VOLUNTEER_CHAT_ID,
                "Необходимо связаться с клиентом " + clientFirstName + " "
                        + clientLastName + " " + "[User link](tg://user?id="
                        + clientId + " )");
        sendMessageForVolunteer.parseMode(ParseMode.Markdown);
        telegramBot.execute(sendMessageForClient);
        telegramBot.execute(sendMessageForVolunteer);
    }

    /**
     * Временный метод, который выводит замещающий текст при выборе некоторых
     * пунктов меню
     * @param chatId идентификатор чата, в который отсылается сообщение
     * @see TelegramBot#execute(BaseRequest)
     */
    public void sendPlainText(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Заполнить необходимой инфой"));
    }

}
