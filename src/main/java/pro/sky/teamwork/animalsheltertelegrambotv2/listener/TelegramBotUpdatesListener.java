package pro.sky.teamwork.animalsheltertelegrambotv2.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.*;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.CarerRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.VolunteerChatRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.AgreementService;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.CarerService;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.DailyReportService;

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
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Класс описывающий константы и работу класса
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Value("${dailyReports.photo.dir.path}")
    private String photosDir;
    private final TelegramBot telegramBot;
    private final CarerService carerService;
    private final DailyReportService dailyReportService;
    private final AgreementService agreementService;
    private final VolunteerChatRepository volunteerChatRepository;
    private final CarerRepository carerRepository;
    private Timer timer;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, CarerService carerService, DailyReportService dailyReportService, AgreementService agreementService, VolunteerChatRepository volunteerChatRepository, CarerRepository carerRepository) {
        this.telegramBot = telegramBot;
        this.carerService = carerService;
        this.dailyReportService = dailyReportService;
        this.agreementService = agreementService;
        this.volunteerChatRepository = volunteerChatRepository;
        this.carerRepository = carerRepository;
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
                long volunteerChatId = this.volunteerChatRepository.findById(1L)
                        .orElse(new VolunteerChat()).getTelegramChatId();
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
                            update.message().audio() != null ||
                            update.message().document() != null) {
                        String errorMessage = "Извините, но я могу работать только с текстом или фото";
                        sendPlainText(chatId, errorMessage);
                    } else if (update.message().chat().type().toString().equals("group")) {
                        long newVolunteerChatId = update.message().chat().id();
                        if (!this.volunteerChatRepository.existsByTelegramChatId(newVolunteerChatId)) {
                            VolunteerChat volunteerChat = new VolunteerChat();
                            volunteerChat.setId(1);
                            volunteerChat.setName("Чат волонтеров " + LocalDate.now());
                            volunteerChat.setTelegramChatId(newVolunteerChatId);
                            this.volunteerChatRepository.save(volunteerChat);
                        } else {
                            VolunteerChat volunteerChat = this.volunteerChatRepository
                                    .findByTelegramChatId(volunteerChatId);
                            volunteerChat.setId(1);
                            volunteerChat.setName("Чат волонтеров " + LocalDate.now());
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

    /**
     * Метод для сохранения фотографии
     * @param update расширение на класс {@link Update} телеграм бота
     * @param chatId идентификатор чата, в котором выводятся кнопки
     */
    private void savePhotoFromCarer(Update update, long chatId) {
        Carer carer = this.carerService.findCarerByChatId(chatId);
        if (carer != null) {
            PhotoSize[] photo = update.message().photo();
            GetFile request = new GetFile(photo[photo.length - 1].fileId());
            GetFileResponse getFileResponse = this.telegramBot.execute(request);
            com.pengrad.telegrambot.model.File file = getFileResponse.file();
            String fullPath = this.telegramBot.getFullFilePath(file);

            String mediaType = getExtensions(Objects.requireNonNull(fullPath));

            Path filePath = Path.of(photosDir + "/" + carer.getFullName(),
                    LocalDate.now() + "." + mediaType);
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

            DailyReport dailyReport = new DailyReport();
            dailyReport.setCarer(carer);
            dailyReport.setFilePath(filePath.toString());
            dailyReport.setFileSize(file.fileSize());
            dailyReport.setMediaType("image/" + mediaType);
            dailyReport.setReportDate(LocalDate.now());
            this.dailyReportService.addDailyReport(dailyReport);

            String text = """
                    Спасибо! Информация сохранена.
                    Пожалуйста, пришлите информацию о
                    рационе животного.
                    ВАЖНО! Сообщение должно начинаться с "2)"!
                    """;
            sendPlainText(chatId, text);
        } else {
            String text = "Опекун не найден. Если Вы являетесь опекуном, пожалуйста, " +
                    "пришлите фото с телефона, с которого Вы оставляли свои контактные данные";
            sendPlainText(chatId, text);
        }
    }

    /**
     * Метод по обработке только команд (начинающихся с "/").
     */
    private void handleCommand(String command,
                               long chatId,
                               long clientId,
                               String clientFirstName,
                               String clientLastName,
                               long volunteerChatId) {
        if (command.equals(Command.START_COMMAND.getCommand())) {
            String text = """
                    Добрый день! Меня зовут AnimalShelterBot. Я отвечаю на
                    популярные вопросы о том, что нужно знать и уметь,
                    чтобы забрать собаку из приюта.
                    """;
            sendPlainText(chatId, text);
            startCommandMenu(chatId);
        } else if (command.equals(Command.SHELTER_INFO_COMMAND.getCommand())) {
            shelterInfoCommandMenu(chatId);
        } else if (command.equals(Command.TAKE_A_DOG_COMMAND.getCommand())) {
            takeDogCommandMenu(chatId);
        } else if (command.equals(Command.SEND_REPORT_MENU_COMMAND.getCommand())) {
            sendReportCommandMenu(chatId);
        } else if (command.equals(Command.CALL_VOLUNTEER_COMMAND.getCommand())) {
            sendCallVolunteerCommand(chatId, clientId, clientFirstName, clientLastName, volunteerChatId);
        } else if (command.equals(Command.SHELTER_MAIN_INFO_COMMAND.getCommand())) {
            String text = "Основная информация о приюте"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(Command.SHELTER_WORK_SCHEDULE_COMMAND.getCommand())) {
            String text = """
                    Расписание работы приюта:
                    номер телефона:
                    e-mail:
                    """;
            sendPlainText(chatId, text);
            SendPhoto sendPhoto = new SendPhoto(chatId,
                    new File("src/redaktirovat-kartu.png"));
            telegramBot.execute(sendPhoto);
        } else if (command.equals(Command.SHELTER_SAFETY_RECOMMENDATIONS_COMMAND.getCommand())) {
            String text = "Общие рекомендации о технике безопасности на территории приюта"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(Command.WRITE_CLIENT_CONTACT_COMMAND.getCommand())) {
            String text = """
                    Прошу написать Ваши Фамилию Имя Отчество
                    (напр., Иванов Иван Иванович)
                    и номер телефона в формате +7(ХХХ)ХХХХХХХ
                    """;
            sendPlainText(chatId, text);
        } else if (command.equals(Command.BACK_COMMAND.getCommand())) {
            startCommandMenu(chatId);
        } else if (command.equals(Command.INTRODUCTION_TO_DOG_COMMAND.getCommand())) {
            String text = "Правила знакомства с собакой до того, как можно забрать ее из приюта"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(Command.TAKE_DOCUMENTS_LIST_COMMAND.getCommand())) {
            String text = "Список документов, необходимых для того, чтобы взять собаку из приюта"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(Command.TRANSFER_A_DOG_COMMAND.getCommand())) {
            String text = "Список рекомендаций по транспортировке животного"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(Command.ENVIRONMENT_FOR_PUPPY_COMMAND.getCommand())) {
            String text = "Список рекомендаций по обустройству дома для щенка"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(Command.ENVIRONMENT_FOR_DOG_COMMAND.getCommand())) {
            String text = "Список рекомендаций по обустройству дома для взрослой собаки"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(Command.ENVIRONMENT_FOR_LIMITED_DOG_COMMAND.getCommand())) {
            String text = "Список рекомендаций по обустройству дома для собаки с ограниченными " +
                    "возможностями (зрение, передвижение)"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(Command.CYNOLOGIST_ADVICES_COMMAND.getCommand())) {
            String text = "Советы кинолога по первичному общению с собакой"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(Command.CYNOLOGIST_CONTACTS_COMMAND.getCommand())) {
            String text = "Рекомендации по проверенным кинологам для дальнейшего обращения к ним"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(Command.USUAL_REFUSALS_COMMAND.getCommand())) {
            String text = "Список причин, почему могут отказать и не дать забрать собаку из приюта"; //требуемая информация
            sendPlainText(chatId, text);
        } else if (command.equals(Command.SEND_REPORT_COMMAND.getCommand())) {
            String reportFormText = """
                    Уважаемый опекун! В качестве отчета пошагово направляются следующие данные:
                    1) Фото животного.
                    2) Рацион животного.
                    3) Общее самочувствие и привыкание к новому месту.
                    4) Изменение в поведении: отказ от старых привычек, приобретение новых.
                    """; //требуемая информация
            String requestForPhoto = "Пожалуйста, пришлите фото животного (1 шт.)";
            sendPlainText(chatId, reportFormText);
            sendPlainText(chatId, requestForPhoto);
        } else if (command.equals(Command.VOLUNTEER_CONFIRM_COMMAND.getCommand())) {
            String text = "Спасибо за подтверждение заявки";
            sendPlainText(chatId, text);
        } else {
            String text = "Неизвестная команда";
            sendPlainText(chatId, text);
        }
    }

    /**
     * Метод по обработке обычных текстовых сообщений.
     *
     */

    private void handleTextMessage(String message, long chatId, long volunteerChatId) {
        Pattern clientContactPattern = Pattern.compile(
                "^(([А-я]+\\s){2}[А-я]+)(\\s)(\\+\\d{1,7}\\(\\d{3}\\)\\d{7})$"); //паттерн на ФИО и телефон клиента для записи
        Matcher matcherClientContact = clientContactPattern.matcher(message);
        String clientName;
        String clientPhoneNumber;
        if (matcherClientContact.matches()) {
            String[] words = matcherClientContact.group(1).split(" ");
            StringBuilder stringBuilder = new StringBuilder();
            for (String word : words) {
                stringBuilder.append(StringUtils.capitalize(word.toLowerCase())).append(" ");
            }
            clientName = stringBuilder.toString().trim();
            clientPhoneNumber = matcherClientContact.group(4);
            Carer carer = this.carerService.findCarerByChatId(chatId);
            if (carer == null) {
                Carer newCarer = this.carerService.addCarer(clientName, 20, clientPhoneNumber, chatId);
                String textForVolunteer = "Прошу связаться с клиентом " + newCarer.getFullName() + " по телефону "
                        + newCarer.getPhoneNumber();
                String textForClient = "Ваши контактные данные записаны. Волонтеры свяжутся с Вами в ближайшее время.";
                sendPlainText(volunteerChatId, textForVolunteer);
                sendPlainText(chatId, textForClient);
            } else {
                carer.setFullName(clientName);
                carer.setPhoneNumber(clientPhoneNumber);
                Carer updatedCarer = this.carerService.saveCarer(carer);
                String textForVolunteer = "Прошу связаться с клиентом " + updatedCarer.getFullName() + " по телефону "
                        + updatedCarer.getPhoneNumber();
                String textForClient = """
                        Ваши контактные данные перезаписаны.
                        Волонтеры свяжутся с Вами в ближайшее время.
                        """;
                sendPlainText(volunteerChatId, textForVolunteer);
                sendPlainText(chatId, textForClient);
            }
        } else if (message.startsWith("2)")) {
            Carer carer = this.carerService.findCarerByChatId(chatId);
            DailyReport dailyReport = this.dailyReportService.findDailyReportByCarerIdAndDate(carer.getId(), LocalDate.now());
            dailyReport.setDogDiet(message);
            this.dailyReportService.addDailyReport(dailyReport);
            String text = """
                    Спасибо! Информация сохранена.
                    Пожалуйста, пришлите информация об
                    общем самочувствии и привыкании к новому месту.
                    ВАЖНО! Сообщение должно начинаться с "3)"!
                    """;
            sendPlainText(chatId, text);
        } else if (message.startsWith("3)")) {
            Carer carer = this.carerService.findCarerByChatId(chatId);
            DailyReport dailyReport = this.dailyReportService.findDailyReportByCarerIdAndDate(carer.getId(), LocalDate.now());
            dailyReport.setDogHealth(message);
            this.dailyReportService.addDailyReport(dailyReport);
            String text = """
                    Спасибо! Информация сохранена.
                    Пожалуйста, пришлите информацию об
                    изменении в поведении: отказ от старых привычек,
                    приобретение новых.
                    ВАЖНО! Сообщение должно начинаться с "4)"!
                    """;
            sendPlainText(chatId, text);
        } else if (message.startsWith("4)")) {
            Carer carer = this.carerService.findCarerByChatId(chatId);
            DailyReport dailyReport = this.dailyReportService.findDailyReportByCarerIdAndDate(carer.getId(), LocalDate.now());
            dailyReport.setDogBehavior(message);
            this.dailyReportService.addDailyReport(dailyReport);
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
     * @param chatId идентификатор чата, в котором выводятся кнопки
     */
    private void startCommandMenu(long chatId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Узнать информацию о приюте").
                        callbackData(Command.SHELTER_INFO_COMMAND.getCommand()),
                new InlineKeyboardButton("Как взять собаку из приюта")
                        .callbackData(Command.TAKE_A_DOG_COMMAND.getCommand()),
                new InlineKeyboardButton("Прислать отчет о питомце").
                        callbackData(Command.SEND_REPORT_MENU_COMMAND.getCommand()),
                new InlineKeyboardButton("Позвать волонтера").
                        callbackData(Command.CALL_VOLUNTEER_COMMAND.getCommand())
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
     * @param chatId идентификатор чата, в котором выводятся кнопки
     */
    private void shelterInfoCommandMenu(long chatId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Основная информация")
                        .callbackData(Command.SHELTER_MAIN_INFO_COMMAND.getCommand()),
                new InlineKeyboardButton("Расписание работы, адрес, " +
                        "схема проезда, контактная информация")
                        .callbackData(Command.SHELTER_WORK_SCHEDULE_COMMAND.getCommand()),
                new InlineKeyboardButton("Общие рекомендации о технике " +
                        "безопасности на территории приюта")
                        .callbackData(Command.SHELTER_SAFETY_RECOMMENDATIONS_COMMAND.getCommand()),
                new InlineKeyboardButton("Записать Ваши контактные " +
                        "данные для связи")
                        .callbackData(Command.WRITE_CLIENT_CONTACT_COMMAND.getCommand()),
                new InlineKeyboardButton("Позвать волонтера")
                        .callbackData(Command.CALL_VOLUNTEER_COMMAND.getCommand()),
                new InlineKeyboardButton("Вернуться назад")
                        .callbackData(Command.BACK_COMMAND.getCommand())
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
                        .callbackData(Command.INTRODUCTION_TO_DOG_COMMAND.getCommand()),
                new InlineKeyboardButton("Получить список документов")
                        .callbackData(Command.TAKE_DOCUMENTS_LIST_COMMAND.getCommand()),
                new InlineKeyboardButton("Транспортировка животного")
                        .callbackData(Command.TRANSFER_A_DOG_COMMAND.getCommand()),
                new InlineKeyboardButton("Обустройство дома для щенка")
                        .callbackData(Command.ENVIRONMENT_FOR_PUPPY_COMMAND.getCommand()),
                new InlineKeyboardButton("Обустройство дома для взрослой собаки")
                        .callbackData(Command.ENVIRONMENT_FOR_DOG_COMMAND.getCommand()),
                new InlineKeyboardButton("Обустройство дома для собаки с ограниченными возможностями")
                        .callbackData(Command.ENVIRONMENT_FOR_LIMITED_DOG_COMMAND.getCommand()),
                new InlineKeyboardButton("советы кинолога")
                        .callbackData(Command.CYNOLOGIST_ADVICES_COMMAND.getCommand()),
                new InlineKeyboardButton("Контакты проверенных кинологов")
                        .callbackData(Command.CYNOLOGIST_CONTACTS_COMMAND.getCommand()),
                new InlineKeyboardButton("Частые причины отказов в выдаче собаки кандидату")
                        .callbackData(Command.USUAL_REFUSALS_COMMAND.getCommand()),
                new InlineKeyboardButton("Записать Ваши контактные данные для связи")
                        .callbackData(Command.WRITE_CLIENT_CONTACT_COMMAND.getCommand()),
                new InlineKeyboardButton("Позвать волонтера")
                        .callbackData(Command.CALL_VOLUNTEER_COMMAND.getCommand()),
                new InlineKeyboardButton("Вернуться назад")
                        .callbackData(Command.BACK_COMMAND.getCommand())
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
                        .callbackData(Command.SEND_REPORT_COMMAND.getCommand()),
                new InlineKeyboardButton("Позвать волонтера")
                        .callbackData(Command.CALL_VOLUNTEER_COMMAND.getCommand()),
                new InlineKeyboardButton("Вернуться назад")
                        .callbackData(Command.BACK_COMMAND.getCommand())
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
                        + clientLastName + " " + "[User link](tg://user?id=" + clientId + " )");
        InlineKeyboardButton button = new InlineKeyboardButton("Подтвердить")
                .callbackData(Command.VOLUNTEER_CONFIRM_COMMAND.getCommand());

        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(button));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        sendMessageForVolunteer.parseMode(ParseMode.Markdown);
        sendMessageForVolunteer.replyMarkup(keyboard);
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

    @Scheduled(cron = "0 0 14 * * ?")
    public void sendNotification() {
        List<Agreement> agreements = this.agreementService.findAllAgreementsByDate(LocalDate.now());
        agreements.forEach(agreement -> {
            this.timer = new Timer();
            this.timer.scheduleAtFixedRate(new TimerTask() {
                @Autowired
                private VolunteerChatRepository volunteerChatRepository;
                @Autowired
                private CarerRepository carerRepository;

                private void sendMessage(long chatId, String text) {
                    telegramBot.execute(new SendMessage(chatId, text));
                }

                @Override
                public void run() {
                    List<Carer> carers = this.carerRepository.findAll();
                    carers.forEach(carer -> {
                        List<DailyReport> dailyReports = carer.getDailyReports();
                        int dailyReportSize = dailyReports.size();
                        DailyReport lastReport = dailyReports.get(dailyReportSize - 1);

                        if (lastReport.getReportDate().isBefore(LocalDate.now().minusDays(1))) {
                            sendMessage(carer.getChatId(), "Добрый день! Напоминаю, что " +
                                    "Вы не отправили отчет за прошлый день. Прошу прислать отчет!");
                        }
                        if (lastReport.getReportDate().isBefore(LocalDate.now().minusDays(2))) {
                            sendMessage(carer.getChatId(), "Добрый день! Напоминаю, что " +
                                    "Вы не отправляли отчет больше двух дней. Прошу прислать отчет!");
                            SendMessage sendMessageForVolunteer = new SendMessage(this.volunteerChatRepository.findById(1L)
                                    .orElseThrow(() -> new RuntimeException("Чат волонтеров не найден"))
                                    .getTelegramChatId(),
                                    "Опекун " + carer.getFullName() + " не отправлял отчет более двух дней." +
                                            "[User link](tg://user?id=" + carer.getChatId() + " )");
                            sendMessageForVolunteer.parseMode(ParseMode.Markdown);
                            telegramBot.execute(sendMessageForVolunteer);
                        }
                    });
                }
            }, Date.from(agreement.getConclusionDate()
                    .plusDays(1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()),
                    1000 * 60 * 60 * 24L);
        });
        List<Carer> carers = this.carerRepository.findAll();
        carers.forEach(carer -> {
            if (carer.getDog().isTaken() && !carer.getDog().isOnProbation()) {
                this.timer.cancel();
            }
        });
    }
}
