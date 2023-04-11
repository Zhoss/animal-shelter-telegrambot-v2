package pro.sky.teamwork.animalsheltertelegrambotv2.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.GetFileResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.VolunteerChat;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.VolunteerChatRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.CarerService;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * @author VitaliyK (commit)
 * <br>
 * Класс описывающий константы и работу класса
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    /**
     * Константа указывающая ID чата волонтеров
     */
    private final static long VOLUNTEER_CHAT_ID = 1517311315; //указать id чата волонтеров, сейчас это мой личный ID
    public final static BotCommand START_COMMAND = new BotCommand("/start",
            "Основное меню");
    public final static BotCommand SHELTER_INFO_COMMAND = new BotCommand("/shelter_info",
            "Меню с информацией о приюте");
    public final static BotCommand SHELTER_MAIN_INFO_COMMAND = new BotCommand("/shelter_main_info",
            "Основная информация о приюте");
    public final static BotCommand SHELTER_WORK_SCHEDULE_COMMAND = new BotCommand("/shelter_work_schedule",
            "Информация о расписании работы приюта, адресе, схеме проезда, контактной информации");
    public final static BotCommand SHELTER_SAFETY_RECOMMENDATIONS_COMMAND = new BotCommand("/shelter_safety_recommendations",
            "Рекомендации о технике безопасности на территории приюта");
    public final static BotCommand WRITE_CLIENT_CONTACT_COMMAND = new BotCommand("/write_contact_information",
            "Записать контактные данные для связи с волонтерами");
    public final static BotCommand CALL_VOLUNTEER_COMMAND = new BotCommand("/call_volunteer",
            "Позвать волонтера");
    public final static BotCommand BACK_COMMAND = new BotCommand("/back",
            "Вернуться назад");
    public final static BotCommand TAKE_A_DOG_COMMAND = new BotCommand("/take_dog",
            "Как взять собаку из приюта");
    public final static BotCommand INTRODUCTION_TO_DOG_COMMAND = new BotCommand("/intro_dog",
            "Узнать правила знакомства с собакой");
    public final static BotCommand TAKE_DOCUMENTS_LIST_COMMAND = new BotCommand("/take_doc_list",
            "Получить список документов");
    public final static BotCommand TRANSFER_A_DOG_COMMAND = new BotCommand("/transfer_dog",
            "Транспортировка животного");
    public final static BotCommand ENVIRONMENT_FOR_PUPPY_COMMAND = new BotCommand("/puppy_environment",
            "Обустройство дома для щенка");
    public final static BotCommand ENVIRONMENT_FOR_DOG_COMMAND = new BotCommand("/dog_environment",
            "Обустройство дома для взрослой собаки");
    public final static BotCommand ENVIRONMENT_FOR_LIMITED_DOG_COMMAND = new BotCommand("/limited_dog_environment",
            "Обустройство дома для собаки с ограниченными возможностями");
    public final static BotCommand CYNOLOGIST_ADVICES_COMMAND = new BotCommand("/cynologist_advices",
            "Советы кинолога");
    public final static BotCommand CYNOLOGIST_CONTACTS_COMMAND = new BotCommand("/cynologist_contacts",
            "Контакты проверенных кинологов");
    public final static BotCommand USUAL_REFUSALS_COMMAND = new BotCommand("/usual_refusals",
            "Частые причины отказов в выдаче собаки кандидату");
    public final static BotCommand SEND_REPORT_MENU_COMMAND = new BotCommand("/send_report_menu",
            "Прислать отчет о питомце");
    public final static BotCommand SEND_REPORT_COMMAND = new BotCommand("/send_report",
            "Прислать отчет");

    @Value("${dailyReports.photo.dir.path}")
    private String photosDir;
    private final TelegramBot telegramBot;
    private final CarerService carerService;
    private final VolunteerChatRepository volunteerChatRepository;
    private String agreementNumber;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, CarerService carerService, VolunteerChatRepository volunteerChatRepository) {
        this.telegramBot = telegramBot;
        this.carerService = carerService;
        this.volunteerChatRepository = volunteerChatRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Метод описывающий логику работы меню приветствия и рабу с константами
     *
     * @param updates расширение на класс {@link Update} телеграм бота
     * @return возврат к началу списка
     */
    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                LOGGER.info("Processing update: {}", update);
                long volunteerChatId = this.volunteerChatRepository.findById(1L).orElse(new VolunteerChat()).getTelegramChatId();
                String message = "";
                long chatId = 0L;
                long clientId = 0L;
                String clientFirstName = "";
                String clientLastName = "";
                if (update.message() != null) {
                    message = update.message().text();
                    chatId = update.message().chat().id();
                    clientId = update.message().from().id();
                    clientFirstName = update.message().from().firstName();
                    clientLastName = update.message().from().lastName();
                } else if (update.message() == null && update.callbackQuery() != null) {
                    message = update.callbackQuery().data();
                    String callBackQueryId = update.callbackQuery().id();
                    chatId = update.callbackQuery().message().chat().id();
                    clientId = update.callbackQuery().from().id();
                    clientFirstName = update.callbackQuery().from().firstName();
                    clientLastName = update.callbackQuery().from().lastName();
                    AnswerCallbackQuery callbackQuery = new AnswerCallbackQuery(callBackQueryId)
                            .showAlert(false);
                    this.telegramBot.execute(callbackQuery);
                }

                if (update.message() != null && message == null) {
                    if (update.message().sticker() != null ||
                            update.message().video() != null ||
                            update.message().audio() != null) {
                        String errorMessage = "Извините, но я могу работать только с текстом или фото";
                        sendPlainText(chatId, errorMessage);
                    } else if (update.message().chat().type().toString().equals("group")) {
                        long newVolunteerChatId = update.message().chat().id();
                        if (!this.volunteerChatRepository.existsByTelegramChatId(newVolunteerChatId)) {
                            VolunteerChat volunteerChat = new VolunteerChat();
                            volunteerChat.setId(1);
                            volunteerChat.setName("Чат волонтеров " + LocalDate.now().getMonthValue() + "." + LocalDate.now().getYear());
                            volunteerChat.setTelegramChatId(newVolunteerChatId);
                            this.volunteerChatRepository.save(volunteerChat);
                        }
                    } else if (update.message().photo() != null) {
                        savePhotoFromCarer(update, chatId);
                    }
                } else if (message != null) {
                    if (message.startsWith("/")) {
                        handleCommand(message, chatId, clientId, clientFirstName, clientLastName, volunteerChatId);
                    } else {
                        handleTextMessage(message, chatId, volunteerChatId);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void savePhotoFromCarer(Update update, long chatId) {
        PhotoSize[] photo = update.message().photo();
        GetFile request = new GetFile(photo[photo.length - 1].fileId());
        GetFileResponse getFileResponse = this.telegramBot.execute(request);
        com.pengrad.telegrambot.model.File file = getFileResponse.file();
        String fullPath = this.telegramBot.getFullFilePath(file);

        Carer carer = this.carerService.findCarer(this.agreementNumber);

        Path filePath = Path.of(photosDir + "/" + carer.getFullName(), LocalDate.now() + "." + getExtensions(Objects.requireNonNull(fullPath)));
        try {
            URL url = new URL(fullPath);
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (InputStream is = url.openStream();
                 OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                 BufferedInputStream bis = new BufferedInputStream(is, 1024);
                 BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
            ) {
                bis.transferTo(bos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = """
                Спасибо! Информация сохранена.
                Пожалуйста, пришлите информацию о
                рационе животного.
                ВАЖНО! Сообщение должно начинаться с "2)"!
                """;
        sendPlainText(chatId, text);
    }

    //метод по обработке только команд (начинающихся с "/")
    private void handleCommand(String command, long chatId, long clientId, String clientFirstName, String clientLastName, long volunteerChatId) {
        if (command.equals(START_COMMAND.command())) {
            String text = """
                    Добрый день! Меня зовут AnimalShelterBot. Я отвечаю на
                    популярные вопросы о том, что нужно знать и уметь,
                    чтобы забрать собаку из приюта.
                    """;
            sendPlainText(chatId, text);
            startCommandMenu(chatId);
        } else if (command.equals(SHELTER_INFO_COMMAND.command())) {
            shelterInfoCommandMenu(chatId);
        } else if (command.equals(TAKE_A_DOG_COMMAND.command())) {
            takeDogCommandMenu(chatId);
        } else if (command.equals(SEND_REPORT_MENU_COMMAND.command())) {
            sendReportCommandMenu(chatId);
        } else if (command.equals(CALL_VOLUNTEER_COMMAND.command())) {
            sendCallVolunteerCommand(chatId, clientId, clientFirstName, clientLastName, volunteerChatId);
        } else if (command.equals(SHELTER_MAIN_INFO_COMMAND.command())) {
            String text = "Основная информация о приюте"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(SHELTER_WORK_SCHEDULE_COMMAND.command())) {
            String text = """
                    Расписание работы приюта:
                    номер телефона:
                    e-mail:
                    """;
            sendPlainText(chatId, text);
            SendPhoto sendPhoto = new SendPhoto(chatId,
                    new File("src/redaktirovat-kartu.png"));
            telegramBot.execute(sendPhoto);
        } else if (command.equals(SHELTER_SAFETY_RECOMMENDATIONS_COMMAND.command())) {
            String text = "Общие рекомендации о технике безопасности на территории приюта"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(WRITE_CLIENT_CONTACT_COMMAND.command())) {
            String text = """
                    Прошу написать Ваши Фамилию Имя Отчество
                    (напр., Иванов Иван Иванович)
                    и номер телефона в формате +7(ХХХ)ХХХХХХХ
                    """;
            sendPlainText(chatId, text);
        } else if (command.equals(BACK_COMMAND.command())) {
            startCommandMenu(chatId);
        } else if (command.equals(INTRODUCTION_TO_DOG_COMMAND.command())) {
            String text = "Правила знакомства с собакой до того, как можно забрать ее из приюта"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(TAKE_DOCUMENTS_LIST_COMMAND.command())) {
            String text = "Список документов, необходимых для того, чтобы взять собаку из приюта"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(TRANSFER_A_DOG_COMMAND.command())) {
            String text = "Список рекомендаций по транспортировке животного"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(ENVIRONMENT_FOR_PUPPY_COMMAND.command())) {
            String text = "Список рекомендаций по обустройству дома для щенка"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(ENVIRONMENT_FOR_DOG_COMMAND.command())) {
            String text = "Список рекомендаций по обустройству дома для взрослой собаки"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(ENVIRONMENT_FOR_LIMITED_DOG_COMMAND.command())) {
            String text = "Список рекомендаций по обустройству дома для собаки с\n" +
                    "ограниченными возможностями (зрение, передвижение)"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(CYNOLOGIST_ADVICES_COMMAND.command())) {
            String text = "Советы кинолога по первичному общению с собакой"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(CYNOLOGIST_CONTACTS_COMMAND.command())) {
            String text = "Рекомендации по проверенным кинологам для дальнейшего обращения к ним"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(USUAL_REFUSALS_COMMAND.command())) {
            String text = "Список причин, почему могут отказать и не дать забрать собаку из приюта"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(SEND_REPORT_COMMAND.command())) {
            String reportFormText = """
                    Уважаемый опекун! В качестве отчета пошагово направляются следующие данные:
                    1) Фото животного.
                    2) Рацион животного.
                    3) Общее самочувствие и привыкание к новому месту.
                    4) Изменение в поведении: отказ от старых привычек, приобретение новых.
                    """; //требуемая информация
            String requestForAgreementNumber = "Для сохранения отчета, пожалуйста, пришлите номер договора";
            sendPlainText(chatId, reportFormText);
            sendPlainText(chatId, requestForAgreementNumber);
        } else {
            String text = "Неизвестная команда";
            sendPlainText(chatId, text);
        }
    }

    //метод по обработке обычных текстовых сообщений
    private void handleTextMessage(String message, long chatId, long volunteerChatId) {
        Pattern clientContactPattern = Pattern.compile(
                "^(([А-я]+\\s){2}[А-я]+)(\\s)(\\+\\d{1,7}\\(\\d{3}\\)\\d{7})$"); //паттерн на ФИО и телефон клиента для записи
        Matcher matcherClientContact = clientContactPattern.matcher(message);
        Pattern agreementNumberPattern = Pattern.compile(
                "^С-2023/\\d+$"); //паттерн на номер договора(соглашения)
        Matcher matcherAgreementNumber = agreementNumberPattern.matcher(message);
        String clientName;
        String clientPhoneNumber;
        if (matcherClientContact.matches()) {
            clientName = matcherClientContact.group(1);
            clientPhoneNumber = matcherClientContact.group(4);
            if (!this.carerService.existsCarerByFullNameAndPhoneNumber(clientName, clientPhoneNumber)) {
                Carer carer = this.carerService.addCarer(clientName, 20, clientPhoneNumber);
                String textForVolunteer = "Прошу связаться с клиентом " + carer.getFullName() + " по телефону "
                        + carer.getPhoneNumber();
                String textForClient = "Ваши контактные данные записаны. Волонтеры свяжутся с Вами в ближайшее время.";
                sendPlainText(volunteerChatId, textForVolunteer);
                sendPlainText(chatId, textForClient);
            } else {
                String text = """
                        Ваши контактные данные уже были записаны.
                        Присылать контактные данные необходимо только
                        в случае их изменения.
                        """;
                sendPlainText(chatId, text);
            }
        }  else if (matcherAgreementNumber.matches()) {
            this.agreementNumber = matcherAgreementNumber.group(0);
            String text = """
                        Спасибо! Пришлите, пожалуйста, фотографию
                        животного (1 шт.).
                        """;
            sendPlainText(chatId, text);
        } else if (message.startsWith("2)")) {
            String text = """
                        Спасибо! Информация сохранена.
                        Пожалуйста, пришлите информация об
                        общем самочувствии и привыкании к новому месту.
                        ВАЖНО! Сообщение должно начинаться с "3)"!
                        """;
            sendPlainText(chatId, text);
        } else if (message.startsWith("3)")) {
            String text = """
                        Спасибо! Информация сохранена.
                        Пожалуйста, пришлите информацию об
                        изменении в поведении: отказ от старых привычек,
                        приобретение новых.
                        ВАЖНО! Сообщение должно начинаться с "4)"!
                        """;
            sendPlainText(chatId, text);
        } else if (message.startsWith("4)")) {
            String text = "Спасибо! Отчет за " +
                    LocalDate.now() + " сохранен!";
            sendPlainText(chatId, text);
        } else {
            String text = "Некорректное сообщение";
            sendPlainText(chatId, text);
        }
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
                new InlineKeyboardButton("Узнать информацию о приюте").
                        callbackData(SHELTER_INFO_COMMAND.command()),
                new InlineKeyboardButton("Как взять собаку из приюта")
                        .callbackData(TAKE_A_DOG_COMMAND.command()),
                new InlineKeyboardButton("Прислать отчет о питомце").
                        callbackData(SEND_REPORT_MENU_COMMAND.command()),
                new InlineKeyboardButton("Позвать волонтера").
                        callbackData(CALL_VOLUNTEER_COMMAND.command())
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
                new InlineKeyboardButton("Основная информация")
                        .callbackData(SHELTER_MAIN_INFO_COMMAND.command()),
                new InlineKeyboardButton("Расписание работы, адрес, " +
                        "схема проезда, контактная информация")
                        .callbackData(SHELTER_WORK_SCHEDULE_COMMAND.command()),
                new InlineKeyboardButton("Общие рекомендации о технике " +
                        "безопасности на территории приюта")
                        .callbackData(SHELTER_SAFETY_RECOMMENDATIONS_COMMAND.command()),
                new InlineKeyboardButton("Записать Ваши контактные " +
                        "данные для связи")
                        .callbackData(WRITE_CLIENT_CONTACT_COMMAND.command()),
                new InlineKeyboardButton("Позвать волонтера")
                        .callbackData(CALL_VOLUNTEER_COMMAND.command()),
                new InlineKeyboardButton("Вернуться назад")
                        .callbackData(BACK_COMMAND.command())
        ));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        SendMessage response = new SendMessage(chatId, "Добрый день! Здесь Вы можете узнать " +
                "основную информацию о нашем приюте.");
        response.replyMarkup(keyboard);
        telegramBot.execute(response);
    }

    /**
     * Вывод кнопок меню "Как взять собаку из приюта" и отправка соответствующих
     * данных {@link InlineKeyboardButton#callbackData()} при нажатии кнопки
     *
     * @param chatId идентификатор чата, в котором выводятся кнопки
     */
    private void takeDogCommandMenu(long chatId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Узнать правила знакомства с собакой")
                        .callbackData(INTRODUCTION_TO_DOG_COMMAND.command()),
                new InlineKeyboardButton("Получить список документов")
                        .callbackData(TAKE_DOCUMENTS_LIST_COMMAND.command()),
                new InlineKeyboardButton("Транспортировка животного")
                        .callbackData(TRANSFER_A_DOG_COMMAND.command()),
                new InlineKeyboardButton("Обустройство дома для щенка")
                        .callbackData(ENVIRONMENT_FOR_PUPPY_COMMAND.command()),
                new InlineKeyboardButton("Обустройство дома для взрослой собаки")
                        .callbackData(ENVIRONMENT_FOR_DOG_COMMAND.command()),
                new InlineKeyboardButton("Обустройство дома для собаки с ограниченными возможностями")
                        .callbackData(ENVIRONMENT_FOR_LIMITED_DOG_COMMAND.command()),
                new InlineKeyboardButton("советы кинолога")
                        .callbackData(CYNOLOGIST_ADVICES_COMMAND.command()),
                new InlineKeyboardButton("Контакты проверенных кинологов")
                        .callbackData(CYNOLOGIST_CONTACTS_COMMAND.command()),
                new InlineKeyboardButton("Частые причины отказов в выдаче собаки кандидату")
                        .callbackData(USUAL_REFUSALS_COMMAND.command()),
                new InlineKeyboardButton("Записать Ваши контактные данные для связи")
                        .callbackData(WRITE_CLIENT_CONTACT_COMMAND.command()),
                new InlineKeyboardButton("Позвать волонтера")
                        .callbackData(CALL_VOLUNTEER_COMMAND.command()),
                new InlineKeyboardButton("Вернуться назад")
                        .callbackData(BACK_COMMAND.command())
        ));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        SendMessage response = new SendMessage(chatId, "Добрый день! Здесь Вы можете узнать " +
                "как взять собаку из приюта.");
        response.replyMarkup(keyboard);
        telegramBot.execute(response);
    }

    /**
     * Вывод в чат кнопок с командами меню и отправка данных при нажатии на кнопку
     *
     * @param chatId идентификатор чата, в котором выводятся кнопки
     */
    private void sendReportCommandMenu(long chatId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Прислать отчет")
                        .callbackData(SEND_REPORT_COMMAND.command()),
                new InlineKeyboardButton("Позвать волонтера")
                        .callbackData(CALL_VOLUNTEER_COMMAND.command()),
                new InlineKeyboardButton("Вернуться назад")
                        .callbackData(BACK_COMMAND.command())
        ));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        SendMessage response = new SendMessage(chatId, "Здесь Вы можете узнать " +
                "как отправить отчёт о питомце.");
        response.replyMarkup(keyboard);
        telegramBot.execute(response);
    }

    /**
     * Обработчик команды "Позвать волонтёра"
     *
     * @param chatId          идентификатор чата, в который отсылается сообщение
     * @param clientId        идентификатор клиента, с которым должен связаться волонтёр
     * @param clientFirstName имя клиента, с которым должен связаться волонтёр
     * @param clientLastName  фамилия клиента, с которым должен связаться волонтёр
     */
    public void sendCallVolunteerCommand(long chatId,
                                         long clientId,
                                         String clientFirstName,
                                         String clientLastName,
                                         long volunteerChatId) {
        SendMessage sendMessageForClient = new SendMessage(chatId,
                "Волонтер свяжется с Вами в ближайшее время");
        SendMessage sendMessageForVolunteer = new SendMessage(volunteerChatId,
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
     *
     * @param chatId идентификатор чата, в который отсылается сообщение
     * @see TelegramBot#execute(BaseRequest)
     */
    public void sendPlainText(long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
