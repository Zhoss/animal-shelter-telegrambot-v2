package pro.sky.teamwork.animalsheltertelegrambotv2.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.CarerService;

import javax.annotation.PostConstruct;
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
    private final static long VOLUNTEER_CHAT_ID = 1140085807; //указать id чата волонтеров, сейчас это мой личный ID
    public final static BotCommand START_COMMAND = new BotCommand("/start", "Основное меню");
    public final static BotCommand SHELTER_INFO_COMMAND = new BotCommand("/shelter_info", "Меню с информацией о приюте");
    public final static BotCommand SHELTER_MAIN_INFO_COMMAND = new BotCommand("/shelter_main_info", "Основная информация о приюте");
    public final static BotCommand SHELTER_WORK_SCHEDULE_COMMAND = new BotCommand("/shelter_work_schedule", "Информация о расписании работы приюта, адресе, схеме проезда, контактной информации");
    public final static BotCommand SHELTER_SAFETY_RECOMMENDATIONS_COMMAND = new BotCommand("/shelter_safety_recommendations", "Рекомендации о технике безопасности на территории приюта");
    public final static BotCommand WRITE_CLIENT_CONTACT_COMMAND = new BotCommand("/write_contact_information", "Записать контактные данные для связи с волонтерами");
    public final static BotCommand CALL_VOLUNTEER_COMMAND = new BotCommand("/call_volunteer", "Позвать волонтера");
    public final static BotCommand BACK_COMMAND = new BotCommand("/back", "Вернуться назад");

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
                    Pattern clientContactPattern = Pattern.compile("([А-я\\s]+)(\\s)(\\+\\d{1,7}\\(\\d{3}\\)\\d{7})");
                    Matcher matcher = clientContactPattern.matcher(message);

                    if (message.equals(START_COMMAND.command())) {
                        SendMessage response = new SendMessage(chatId, "Добрый день! Меня зовут AnimalShelterBot. Я отвечаю на популярные вопросы о том, что нужно знать и уметь, чтобы забрать собаку из приюта.");
                        telegramBot.execute(response);
                        startCommandMenu(chatId);
                    } else if (matcher.matches()) {
                        String clientName = matcher.group(1);
                        String clientPhoneNumber = matcher.group(3);

                        carerService.addCarer(clientName, 20, clientPhoneNumber);
                        SendMessage sendMessageForVolunteer = new SendMessage(VOLUNTEER_CHAT_ID, "Прошу связаться с клиентом " + clientName + " по телефону " + clientPhoneNumber);
                        SendMessage sendMessageForClient = new SendMessage(chatId, "Ваши контактные данные записаны. Волонтеры свяжутся с Вами в ближайшее время.");
                        telegramBot.execute(sendMessageForVolunteer);
                        telegramBot.execute(sendMessageForClient);
                    }
                } else if (update.message() == null && update.callbackQuery() != null) {
                    String message = update.callbackQuery().data();
                    String callBackQueryId = update.callbackQuery().id();
                    long chatId = update.callbackQuery().message().chat().id();
                    AnswerCallbackQuery callbackQuery = new AnswerCallbackQuery(callBackQueryId).showAlert(false);
                    telegramBot.execute(callbackQuery);

                    if (message.equals(SHELTER_INFO_COMMAND.command())) {
                        shelterInfoCommandMenu(chatId);
                    } else if (message.equals(SHELTER_MAIN_INFO_COMMAND.command())) {
                        SendMessage sendMessage = new SendMessage(chatId, "Основная информация о приюте"); //заполнить по факту
                        telegramBot.execute(sendMessage);
                    } else if (message.equals(SHELTER_WORK_SCHEDULE_COMMAND.command())) {
                        SendMessage sendMessage = new SendMessage(chatId, //заполнить по факту
                                """
                                        Расписание работы приюта:
                                        номер телефона:
                                        e-mail:
                                        """);
                        SendPhoto sendPhoto = new SendPhoto(chatId, new File("src/redaktirovat-kartu.png"));
                        telegramBot.execute(sendMessage);
                        telegramBot.execute(sendPhoto);
                    } else if (message.equals(SHELTER_SAFETY_RECOMMENDATIONS_COMMAND.command())) {
                        SendMessage sendMessage = new SendMessage(chatId, "Рекомендации о технике безопасности на территории приюта"); //заполнить по факту
                        telegramBot.execute(sendMessage);
                    } else if (message.equals(WRITE_CLIENT_CONTACT_COMMAND.command())) {

                        SendMessage sendMessage = new SendMessage(chatId, "Прошу написать Ваши ФИО и контактный телефон в формате" +
                                "+7(ХХХ)ХХХХХХХ.");
                        telegramBot.execute(sendMessage);
                    } else if (message.equals(CALL_VOLUNTEER_COMMAND.command())) {
                        long clientId = update.callbackQuery().from().id();
                        String clientFirstName = update.callbackQuery().from().firstName();
                        String clientLastName = update.callbackQuery().from().lastName();
                        SendMessage sendMessageForClient = new SendMessage(chatId, "Волонтер свяжется с Вами в ближайшее время");
                        SendMessage sendMessageForVolunteer = new SendMessage(VOLUNTEER_CHAT_ID, "Необходимо связаться с клиентом " + clientFirstName + " " + clientLastName + " " + "[User link](tg://user?id=" + clientId + " )");
                        sendMessageForVolunteer.parseMode(ParseMode.Markdown);
                        telegramBot.execute(sendMessageForClient);
                        telegramBot.execute(sendMessageForVolunteer);
                    } else if (message.equals(BACK_COMMAND.command())) {
                        startCommandMenu(chatId);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Метод описывающий кнопки в начальном окне бота.
     *
     * <br> при обращении выдается сообщение через <b>SendMessage</b>, которое ссылается на класс {@link SendMessage}
     * <br> если текст вводится в строке команды, то бот инициализирует его и переводит запрос по <b>callbackData</b>,
     * в ином случае прожимается кнопка соответствующей команды.
     * @param chatId
     */
    private void startCommandMenu(long chatId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Узнать информацию о приюте").callbackData("/shelter_info"),
                new InlineKeyboardButton("Как взять собаку из приюта").callbackData("/how_to_adopt_the_dog"),
                new InlineKeyboardButton("Прислать отчет о питомце").callbackData("/send_a_report"),
                new InlineKeyboardButton("Позвать волонтера").callbackData("/call_volunteer")
        ));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(button -> keyboard.addRow(button));

        SendMessage response = new SendMessage(chatId, "Что Вы хотите узнать?");
        response.replyMarkup(keyboard);
        telegramBot.execute(response);
    }

    /**
     * Метод описывающий кнопки в начальном окне бота.
     * <br> если текст вводится в строке команды, то бот инициализирует его и переводит запрос по <b>callbackData</b>,
     * в ином случае прожимается кнопка соответствующей команды.
     * @param chatId
     */
    private void shelterInfoCommandMenu(long chatId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Основная информация").callbackData("/shelter_main_info"),
                new InlineKeyboardButton("Расписание работы, адрес, схема проезда, контактная информация").callbackData("/shelter_work_schedule"),
                new InlineKeyboardButton("Общие рекомендации о технике безопасности на территории приюта").callbackData("/shelter_safety_recommendations"),
                new InlineKeyboardButton("Записать Ваши контактные данные для связи").callbackData("/write_contact_information"),
                new InlineKeyboardButton("Позвать волонтера").callbackData("/call_volunteer"),
                new InlineKeyboardButton("Вернуться назад").callbackData("/back")
        ));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(button -> keyboard.addRow(button));

        SendMessage response = new SendMessage(chatId, "Добрый день! Здесь Вы можете узнать основную информацию о нашем приюте.");
        response.replyMarkup(keyboard);
        telegramBot.execute(response);
    }
}
