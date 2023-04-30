package pro.sky.teamwork.animalsheltertelegrambotv2.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.Cat;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatAgreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatAgreementRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatCarerRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogAgreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogAgreementRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogCarerRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Command;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.VolunteerChat;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Client;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.CarerService;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.DailyReportService;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.CarerNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.ClientRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.VolunteerChatRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Класс описывающий константы и работу класса
 */

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static final Pattern CLIENT_CONTACT_PATTERN = Pattern.compile(
            "^(([А-я]+\\s){2}[А-я]+)(\\s)(\\+\\d{1,7}\\(\\d{3}\\)\\d{7})$"); //паттерн на ФИО и телефон клиента для записи
    private static final Pattern PROBATION_PATTERN = Pattern.compile(
            "^(/\\w+)(/)(\\d+)$"); //паттерн на команду из чата волонтеров с id опекуна
    private static final Pattern CARER_ID_PATTERN = Pattern.compile(
            "^(id)(\\s)(\\d+)$"); //паттерн на id опекуна

    @Value("${dailyReports.photo.dir.path}")
    private String photosDir;
    private final TelegramBot telegramBot;
    private final CarerService carerService;
    private final DailyReportService dailyReportService;
    private final VolunteerChatRepository volunteerChatRepository;
    private final DogAgreementRepository dogAgreementRepository;
    private final DogRepository dogRepository;
    private final DogCarerRepository dogCarerRepository;
    private final CatCarerRepository catCarerRepository;
    private final ClientRepository clientRepository;
    private final CatRepository catRepository;
    private final CatAgreementRepository catAgreementRepository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      CarerService carerService,
                                      DailyReportService dailyReportService,
                                      VolunteerChatRepository volunteerChatRepository,
                                      DogAgreementRepository dogAgreementRepository,
                                      DogRepository dogRepository,
                                      DogCarerRepository dogCarerRepository,
                                      CatCarerRepository catCarerRepository, ClientRepository clientRepository, CatRepository catRepository, CatAgreementRepository catAgreementRepository) {
        this.telegramBot = telegramBot;
        this.carerService = carerService;
        this.dailyReportService = dailyReportService;
        this.volunteerChatRepository = volunteerChatRepository;
        this.dogAgreementRepository = dogAgreementRepository;
        this.dogRepository = dogRepository;
        this.dogCarerRepository = dogCarerRepository;
        this.catCarerRepository = catCarerRepository;
        this.clientRepository = clientRepository;
        this.catRepository = catRepository;
        this.catAgreementRepository = catAgreementRepository;
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
                String message = "";
                long chatId = 0L;
                long clientId = 0L;
                String clientFirstName = "";
                String clientLastName = "";
                Chat.Type chatType = null;
                if (update.message() != null) {
                    message = update.message().text();
                    chatId = update.message().chat().id();
                    clientId = update.message().from().id();
                    clientFirstName = update.message().from().firstName();
                    clientLastName = update.message().from().lastName();
                    chatType = update.message().chat().type();
                } else if (update.message() == null && update.callbackQuery() != null) {
                    message = update.callbackQuery().data();
                    String callBackQueryId = update.callbackQuery().id();
                    chatId = update.callbackQuery().message().chat().id();
                    clientId = update.callbackQuery().from().id();
                    clientFirstName = update.callbackQuery().from().firstName();
                    clientLastName = update.callbackQuery().from().lastName();
                    chatType = update.callbackQuery().message().chat().type();
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
                    } else if (Chat.Type.group == chatType && update.message() != null && update.callbackQuery() == null) {
                        sendPlainText(chatId, "Добрый день, уважаемые волонтеры!");
                        selectShelterCommand(chatId, chatType);
                    } else if (update.message().photo() != null) {
                        savePhotoFromCarer(update, chatId);
                    }
                } else if (message != null && Chat.Type.group == chatType) {
                    handleVolunteersCommand(message, chatId);
                } else if (message != null && Chat.Type.Private == chatType) {
                    if (message.startsWith("/")) {
                        handleCarerCommand(message,
                                chatId,
                                clientId,
                                clientFirstName,
                                clientLastName
                        );
                    } else {
                        handleTextMessage(message, chatId);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void updateVolunteerChat(long newVolunteerChatId, VolunteerChat volunteerChat, PetType petType) {
        if (petType == PetType.CAT) {
            volunteerChat.setName("Чат волонтеров приюта для кошек " + LocalDate.now());
            volunteerChat.setPetType(PetType.CAT);
        } else if (petType == PetType.DOG) {
            volunteerChat.setName("Чат волонтеров приюта для собак " + LocalDate.now());
            volunteerChat.setPetType(PetType.DOG);
        }
        volunteerChat.setTelegramChatId(newVolunteerChatId);
        this.volunteerChatRepository.save(volunteerChat);
    }

    /**
     * Метод для сохранения фотографии
     *
     * @param update расширение на класс {@link Update} телеграм бота
     * @param chatId идентификатор чата, в котором выводятся кнопки
     */
    private void savePhotoFromCarer(Update update, long chatId) {
        Client client = this.clientRepository.findByTelegramChatId(chatId);
        Carer carer = this.carerService.findCarerByChatId(chatId, client.getPetType());
        if (carer != null && carer.getPassportNumber() != null) {
            DailyReport foundDailyReport = this.dailyReportService.findDailyReportByCarerIdAndDate(carer.getId(), LocalDate.now(), client.getPetType());
            if (foundDailyReport == null) {
                PhotoSize photo = update.message().photo()[update.message().photo().length - 1];
                GetFile request = new GetFile(photo.fileId());
                GetFileResponse getFileResponse = this.telegramBot.execute(request);
                if (getFileResponse.isOk()) {
                    com.pengrad.telegrambot.model.File file = getFileResponse.file();
                    String extension = org.springframework.util.StringUtils.getFilenameExtension(file.filePath());
                    String path = "";
                    if (client.getPetType() == PetType.CAT) {
                        path = "/cats/";
                    } else if (client.getPetType() == PetType.DOG) {
                        path = "/dogs/";
                    }
                    Path filePath = Path.of(photosDir + path + carer.getFullName(),
                            LocalDate.now() + "." + extension);

                    try {
                        Files.deleteIfExists(filePath);
                        Files.createDirectories(filePath.getParent());
                        Files.write(filePath, telegramBot.getFileContent(file), CREATE_NEW);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (client.getPetType() == PetType.CAT) {
                        CatDailyReport catDailyReport = new CatDailyReport();
                        catDailyReport.setCarer((CatCarer) carer);
                        catDailyReport.setFilePath(filePath.toString());
                        catDailyReport.setFileSize(file.fileSize());
                        catDailyReport.setMediaType("image/" + extension);
                        catDailyReport.setReportDate(LocalDate.now());
                        this.dailyReportService.addDailyReport(catDailyReport, client.getPetType());
                    } else if (client.getPetType() == PetType.DOG) {
                        DogDailyReport dogDailyReport = new DogDailyReport();
                        dogDailyReport.setCarer((DogCarer) carer);
                        dogDailyReport.setFilePath(filePath.toString());
                        dogDailyReport.setFileSize(file.fileSize());
                        dogDailyReport.setMediaType("image/" + extension);
                        dogDailyReport.setReportDate(LocalDate.now());
                        this.dailyReportService.addDailyReport(dogDailyReport, client.getPetType());
                    }

                    String text = """
                            Спасибо! Информация сохранена.
                            Пожалуйста, пришлите информацию о
                            рационе животного.
                            ВАЖНО! Сообщение должно начинаться с "2)"!
                            """;
                    sendPlainText(chatId, text);
                }
            } else {
                String text = "Отчет за сегодняшнюю дату " + LocalDate.now() + " уже сохранен";
                sendPlainText(chatId, text);
            }
        } else {
            String text = "Опекун не найден. Если Вы являетесь опекуном, пожалуйста, " +
                    "пришлите фото с телефона, с которого Вы оставляли свои контактные данные. Также прошу проверить " +
                    "для какого приюта Вы отправляете отчет";
            sendPlainText(chatId, text);
        }
    }

    /**
     * Метод по обработке только команд клиентов/опекунов (начинающихся с "/").
     */
    private void handleCarerCommand(String command,
                                    long chatId,
                                    long clientId,
                                    String clientFirstName,
                                    String clientLastName) {
        if (Command.commandExists(command)) {
            switch (Objects.requireNonNull(Command.findByStringCommand(command))) {
                case START_COMMAND -> {
                    sendPlainText(chatId, """
                            Добрый день! Меня зовут AnimalShelterBot. Я отвечаю на
                            популярные вопросы о том, что нужно знать и уметь,
                            чтобы забрать собаку из приюта.
                            """);
                    selectShelterCommand(chatId, Chat.Type.Private);
                    if (this.clientRepository.existsByTelegramChatId(chatId)) {
                        Client client = this.clientRepository.findByTelegramChatId(chatId);
                        client.setTelegramChatId(chatId);
                        this.clientRepository.save(client);
                    } else {
                        Client client = new Client();
                        client.setTelegramChatId(chatId);
                        this.clientRepository.save(client);
                    }
                }
                case SELECT_CAT_SHELTER_COMMAND -> {
                    Client client = this.clientRepository.findByTelegramChatId(chatId);
                    client.setPetType(PetType.CAT);
                    this.clientRepository.save(client);
                    startCommandMenu(chatId);
                }
                case SELECT_DOG_SHELTER_COMMAND -> {
                    Client client = this.clientRepository.findByTelegramChatId(chatId);
                    client.setPetType(PetType.DOG);
                    this.clientRepository.save(client);
                    startCommandMenu(chatId);
                }
                case SHELTER_INFO_COMMAND -> shelterInfoCommandMenu(chatId);
                case TAKE_A_PET_COMMAND -> {
                    PetType petType = this.clientRepository.findByTelegramChatId(chatId).getPetType();
                    takePetCommandMenu(chatId, petType);
                }
                case SEND_REPORT_MENU_COMMAND -> {
                    PetType petType = this.clientRepository.findByTelegramChatId(chatId).getPetType();
                    sendReportCommandMenu(chatId, petType);
                }
                case CALL_VOLUNTEER_COMMAND -> {
                    long volunteerChatId = selectVolunteerChatId(chatId);
                    sendCallVolunteerCommand(chatId, clientId, clientFirstName, clientLastName, volunteerChatId);
                }
                case SHELTER_MAIN_INFO_COMMAND -> {
                    if (this.clientRepository.findByTelegramChatId(chatId).getPetType() == PetType.CAT) {
                        sendPlainText(chatId, "Основная информация о приюте для кошек");
                    } else {
                        sendPlainText(chatId, "Основная информация о приюте для собак");
                    }
                }
                case SHELTER_WORK_SCHEDULE_COMMAND -> {
                    if (this.clientRepository.findByTelegramChatId(chatId).getPetType() == PetType.CAT) {
                        sendPlainText(chatId, """
                                Расписание работы приюта для кошек:
                                номер телефона:
                                e-mail:
                                """);
                        SendPhoto sendPhoto = new SendPhoto(chatId,
                                new File("src/redaktirovat-kartu.png"));
                        telegramBot.execute(sendPhoto);
                    } else {
                        sendPlainText(chatId, """
                                Расписание работы приюта для собак:
                                номер телефона:
                                e-mail:
                                """);
                        SendPhoto sendPhoto = new SendPhoto(chatId,
                                new File("src/dog_shelter.jpg"));
                        telegramBot.execute(sendPhoto);
                    }
                }
                case SECURITY_CONTATCT_COMMAND -> {
                    if (this.clientRepository.findByTelegramChatId(chatId).getPetType() == PetType.CAT) {
                        sendPlainText(chatId, "Контактные данные охраны для оформления пропуска на машину для проезда к приюту для кошек");
                    } else {
                        sendPlainText(chatId, "Контактные данные охраны для оформления пропуска на машину для проезда к приюту для собак");
                    }
                }
                case SHELTER_SAFETY_RECOMMENDATIONS_COMMAND -> {
                    if (this.clientRepository.findByTelegramChatId(chatId).getPetType() == PetType.CAT) {
                        sendPlainText(chatId, "Общие рекомендации о технике безопасности на территории приюта для кошек");
                    } else {
                        sendPlainText(chatId, "Общие рекомендации о технике безопасности на территории приюта для собак");
                    }
                }
                case WRITE_CLIENT_CONTACT_COMMAND -> sendPlainText(chatId, """
                        Прошу написать Ваши Фамилию Имя Отчество
                        (напр., Иванов Иван Иванович)
                        и номер телефона в формате +7(ХХХ)ХХХХХХХ
                        """);
                case BACK_COMMAND -> startCommandMenu(chatId);
                case INTRODUCTION_TO_PET_COMMAND -> {
                    if (this.clientRepository.findByTelegramChatId(chatId).getPetType() == PetType.CAT) {
                        sendPlainText(chatId, "Правила знакомства с кошкой до того, как можно забрать ее из приюта");
                    } else {
                        sendPlainText(chatId, "Правила знакомства с собакой до того, как можно забрать ее из приюта");
                    }
                }
                case TAKE_DOCUMENTS_LIST_COMMAND -> {
                    if (this.clientRepository.findByTelegramChatId(chatId).getPetType() == PetType.CAT) {
                        sendPlainText(chatId, "Список документов, необходимых для того, чтобы взять кошку из приюта");
                    } else {
                        sendPlainText(chatId, "Список документов, необходимых для того, чтобы взять собаку из приюта");
                    }
                }
                case TRANSFER_A_PET_COMMAND -> {
                    if (this.clientRepository.findByTelegramChatId(chatId).getPetType() == PetType.CAT) {
                        sendPlainText(chatId, "Список рекомендаций по транспортировке кошки");
                    } else {
                        sendPlainText(chatId, "Список рекомендаций по транспортировке собаки");
                    }
                }
                case ENVIRONMENT_FOR_YOUNG_PET_COMMAND -> {
                    if (this.clientRepository.findByTelegramChatId(chatId).getPetType() == PetType.CAT) {
                        sendPlainText(chatId, "Список рекомендаций по обустройству дома для котенка");
                    } else {
                        sendPlainText(chatId, "Список рекомендаций по обустройству дома для щенка");
                    }
                }
                case ENVIRONMENT_FOR_PET_COMMAND -> {
                    if (this.clientRepository.findByTelegramChatId(chatId).getPetType() == PetType.CAT) {
                        sendPlainText(chatId, "Список рекомендаций по обустройству дома для взрослой кошки");
                    } else {
                        sendPlainText(chatId, "Список рекомендаций по обустройству дома для взрослой собаки");
                    }
                }
                case ENVIRONMENT_FOR_LIMITED_PET_COMMAND -> {
                    if (this.clientRepository.findByTelegramChatId(chatId).getPetType() == PetType.CAT) {
                        sendPlainText(chatId, "Список рекомендаций по обустройству дома для кошки с ограниченными " +
                                "возможностями (зрение, передвижение)");
                    } else {
                        sendPlainText(chatId, "Список рекомендаций по обустройству дома для собаки с ограниченными " +
                                "возможностями (зрение, передвижение)");
                    }
                }
                case CYNOLOGIST_ADVICES_COMMAND ->
                        sendPlainText(chatId, "Советы кинолога по первичному общению с собакой");
                case CYNOLOGIST_CONTACTS_COMMAND ->
                        sendPlainText(chatId, "Рекомендации по проверенным кинологам для дальнейшего обращения к ним");
                case USUAL_REFUSALS_COMMAND -> {
                    if (this.clientRepository.findByTelegramChatId(chatId).getPetType() == PetType.CAT) {
                        sendPlainText(chatId, "Список причин, почему могут отказать и не дать забрать кошку из приюта");
                    } else {
                        sendPlainText(chatId, "Список причин, почему могут отказать и не дать забрать собаку из приюта");
                    }
                }
                case SEND_REPORT_COMMAND -> {
                    sendPlainText(chatId, """
                            Уважаемый опекун! В качестве отчета пошагово направляются следующие данные:
                            1) Фото животного.
                            2) Рацион животного.
                            3) Общее самочувствие и привыкание к новому месту.
                            4) Изменение в поведении: отказ от старых привычек, приобретение новых.
                            """);
                    sendPlainText(chatId, "Пожалуйста, пришлите фото животного (1 шт.)");
                }
            }
        } else {
            sendPlainText(chatId, "Неизвестная команда");
        }
    }

    private long selectVolunteerChatId(long chatId) {
        long volunteerChatId;
        if (this.clientRepository.findByTelegramChatId(chatId).getPetType() == PetType.CAT) {
            volunteerChatId = this.volunteerChatRepository.findByPetType(PetType.CAT)
                    .orElseThrow(() -> new IllegalArgumentException("Чат волонтеров не найден"))
                    .getTelegramChatId();
        } else {
            volunteerChatId = this.volunteerChatRepository.findByPetType(PetType.DOG)
                    .orElseThrow(() -> new IllegalArgumentException("Чат волонтеров не найден"))
                    .getTelegramChatId();
        }
        return volunteerChatId;
    }

    /**
     * Метод по обработке сообщений из чата волонтеров.
     */
    private void handleVolunteersCommand(String message, long chatId) {
        Matcher matcherProbation = PROBATION_PATTERN.matcher(message);
        Matcher matcherCarerId = CARER_ID_PATTERN.matcher(message);
        if (Command.commandExists(message)) {
            switch (Objects.requireNonNull(Command.findByStringCommand(message))) {
                case SELECT_CAT_SHELTER_COMMAND -> {
                    sendPlainText(chatId, "Теперь вы волонтеры приюта для кошек");
                    if (!this.volunteerChatRepository.existsByTelegramChatId(chatId)) {
                        VolunteerChat volunteerChat = new VolunteerChat();
                        updateVolunteerChat(chatId, volunteerChat, PetType.CAT);
                    } else {
                        VolunteerChat volunteerChat = this.volunteerChatRepository
                                .findByTelegramChatId(chatId);
                        updateVolunteerChat(chatId, volunteerChat, PetType.CAT);
                    }
                }
                case SELECT_DOG_SHELTER_COMMAND -> {
                    sendPlainText(chatId, "Теперь вы волонтеры приюта для собак");
                    if (!this.volunteerChatRepository.existsByTelegramChatId(chatId)) {
                        VolunteerChat volunteerChat = new VolunteerChat();
                        updateVolunteerChat(chatId, volunteerChat, PetType.DOG);
                    } else {
                        VolunteerChat volunteerChat = this.volunteerChatRepository
                                .findByTelegramChatId(chatId);
                        updateVolunteerChat(chatId, volunteerChat, PetType.DOG);
                    }
                }
                case VOLUNTEER_CONFIRM_COMMAND -> sendPlainText(chatId, "Спасибо за подтверждение заявки");
                case NOTIFY_CARERS_COMMAND ->
                        sendPlainText(chatId, "Пришлите, пожалуйста, id опекуна в формате \"id X\", " +
                                "где X - идентификатор опекуна. Помните, нужно именно ответить (Reply) на данное сообщение");
            }
        } else if (matcherProbation.matches()) {
            decideOnProbation(matcherProbation, chatId);
        } else if (matcherCarerId.matches()) {
            VolunteerChat volunteerChat = this.volunteerChatRepository.findByTelegramChatId(chatId);
            long carerId = Long.parseLong(matcherCarerId.group(3));
            String text = "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                    "Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут " +
                    "обязаны самолично проверять условия содержания животного";
            if (volunteerChat.getPetType() == PetType.CAT) {
                CatCarer catCarer = this.catCarerRepository.findById(carerId)
                        .orElseThrow(() -> new CarerNotFoundException("Опекун не найден"));
                sendPlainText(catCarer.getChatId(), text);
                sendPlainText(chatId, "Опекун кота/кошки " + catCarer.getFullName() + " уведомлен");
            } else if (volunteerChat.getPetType() == PetType.DOG) {
                DogCarer dogCarer = this.dogCarerRepository.findById(carerId)
                        .orElseThrow(() -> new CarerNotFoundException("Опекун не найден"));
                sendPlainText(dogCarer.getChatId(), text);
                sendPlainText(chatId, "Опекун собаки " + dogCarer.getFullName() + " уведомлен");
            }
        } else {
            sendPlainText(chatId, "Неизвестная команда");
        }
    }

    /**
     * Метод по обработке сообщений из чата волонтеров, связанных с решением о продлении испытательного срока.
     */
    private void decideOnProbation(Matcher matcher, long volunteerChatId) {
        PetType petType = this.volunteerChatRepository.findByTelegramChatId(volunteerChatId).getPetType();
        long carerId = Long.parseLong(matcher.group(3));
        if (petType == PetType.CAT) {
            CatAgreement catAgreement = this.catAgreementRepository.findCatAgreementByCatCarer_Id(carerId)
                    .orElseThrow(() -> new IllegalArgumentException("Договор не найден"));
            String command = matcher.group(1);
            if (Command.commandExists(command)) {
                switch (Objects.requireNonNull(Command.findByStringCommand(command))) {
                    case EXTEND_PROBATION_14_COMMAND -> {
                        catAgreement.setProbationEndData(catAgreement.getProbationEndData().plusDays(14));
                        this.catAgreementRepository.save(catAgreement);
                        sendPlainText(catAgreement.getCarer().getChatId(), "Добрый день! Ваш испытательный срок был продлен на 14 дней.");
                        sendPlainText(volunteerChatId, "Опекун " + catAgreement.getCarer().getFullName() + " уведомлен о продлении испытательного срока на 14 дней.");
                    }
                    case EXTEND_PROBATION_30_COMMAND -> {
                        catAgreement.setProbationEndData(catAgreement.getProbationEndData().plusDays(30));
                        this.catAgreementRepository.save(catAgreement);
                        sendPlainText(catAgreement.getCarer().getChatId(), "Добрый день! Ваш испытательный срок был продлен на 30 дней.");
                        sendPlainText(volunteerChatId, "Опекун " + catAgreement.getCarer().getFullName() + " уведомлен о продлении испытательного срока на 30 дней.");
                    }
                    case PROBATION_NOT_PASSED_COMMAND -> {
                        catAgreement.setProbationEndData(LocalDate.now());
                        this.catAgreementRepository.save(catAgreement);
                        Cat cat = catAgreement.getCarer().getCat();
                        cat.setOnProbation(false);
                        this.catRepository.save(cat);
                        sendPlainText(catAgreement.getCarer().getChatId(), "Добрый день! К сожалению, Вы не прошли испытательный срок. " +
                                "Просим Вас привезти собаку обратно в приют и уточнить всю необходимую информацию");
                        sendPlainText(volunteerChatId, "Опекун " + catAgreement.getCarer().getFullName() + " уведомлен о не прохождении испытательного срока.");
                    }
                    case PROBATION_PASSED_COMMAND -> {
                        catAgreement.setProbationEndData(LocalDate.now());
                        this.catAgreementRepository.save(catAgreement);
                        Cat cat = catAgreement.getCarer().getCat();
                        cat.setOnProbation(false);
                        this.catRepository.save(cat);
                        sendPlainText(catAgreement.getCarer().getChatId(), "Добрый день! Поздравляем, Вы прошли испытательный срок!");
                        sendPlainText(volunteerChatId, "Опекун " + catAgreement.getCarer().getFullName() + " уведомлен о прохождении испытательного срока.");
                    }
                }
            }
        } else if (petType == PetType.DOG) {
            DogAgreement dogAgreement = this.dogAgreementRepository.findDogAgreementByDogCarer_Id(carerId)
                    .orElseThrow(() -> new IllegalArgumentException("Договор не найден"));
            String command = matcher.group(1);
            if (Command.commandExists(command)) {
                switch (Objects.requireNonNull(Command.findByStringCommand(command))) {
                    case EXTEND_PROBATION_14_COMMAND -> {
                        dogAgreement.setProbationEndData(dogAgreement.getProbationEndData().plusDays(14));
                        this.dogAgreementRepository.save(dogAgreement);
                        sendPlainText(dogAgreement.getCarer().getChatId(), "Добрый день! Ваш испытательный срок был продлен на 14 дней.");
                        sendPlainText(volunteerChatId, "Опекун " + dogAgreement.getCarer().getFullName() + " уведомлен о продлении испытательного срока на 14 дней.");
                    }
                    case EXTEND_PROBATION_30_COMMAND -> {
                        dogAgreement.setProbationEndData(dogAgreement.getProbationEndData().plusDays(30));
                        this.dogAgreementRepository.save(dogAgreement);
                        sendPlainText(dogAgreement.getCarer().getChatId(), "Добрый день! Ваш испытательный срок был продлен на 30 дней.");
                        sendPlainText(volunteerChatId, "Опекун " + dogAgreement.getCarer().getFullName() + " уведомлен о продлении испытательного срока на 30 дней.");
                    }
                    case PROBATION_NOT_PASSED_COMMAND -> {
                        dogAgreement.setProbationEndData(LocalDate.now());
                        this.dogAgreementRepository.save(dogAgreement);
                        Dog dog = dogAgreement.getCarer().getDog();
                        dog.setOnProbation(false);
                        this.dogRepository.save(dog);
                        sendPlainText(dogAgreement.getCarer().getChatId(), "Добрый день! К сожалению, Вы не прошли испытательный срок. " +
                                "Просим Вас привезти собаку обратно в приют и уточнить всю необходимую информацию");
                        sendPlainText(volunteerChatId, "Опекун " + dogAgreement.getCarer().getFullName() + " уведомлен о не прохождении испытательного срока.");
                    }
                    case PROBATION_PASSED_COMMAND -> {
                        dogAgreement.setProbationEndData(LocalDate.now());
                        this.dogAgreementRepository.save(dogAgreement);
                        Dog dog = dogAgreement.getCarer().getDog();
                        dog.setOnProbation(false);
                        this.dogRepository.save(dog);
                        sendPlainText(dogAgreement.getCarer().getChatId(), "Добрый день! Поздравляем, Вы прошли испытательный срок!");
                        sendPlainText(volunteerChatId, "Опекун " + dogAgreement.getCarer().getFullName() + " уведомлен о прохождении испытательного срока.");
                    }
                }
            }
        }
    }

    /**
     * Метод по обработке обычных текстовых сообщений от клиентов/опекунов.
     */
    private void handleTextMessage(String message, long chatId) {
        long volunteerChatId = selectVolunteerChatId(chatId);
        Matcher matcherClientContact = CLIENT_CONTACT_PATTERN.matcher(message);
        String clientName;
        String clientPhoneNumber;
        Client client = this.clientRepository.findByTelegramChatId(chatId);
        if (matcherClientContact.matches()) {
            String[] words = matcherClientContact.group(1).split(" ");
            StringBuilder stringBuilder = new StringBuilder();
            for (String word : words) {
                stringBuilder.append(StringUtils.capitalize(word.toLowerCase())).append(" ");
            }
            clientName = stringBuilder.toString().trim();
            clientPhoneNumber = matcherClientContact.group(4);

            Carer carer = this.carerService.findCarerByChatId(chatId, client.getPetType());

            if (carer == null) {
                Carer newCarer = this.carerService.addCarer(clientName, 20, clientPhoneNumber, chatId, client.getPetType());
                String textForVolunteer = "Прошу связаться с клиентом " + newCarer.getFullName() + " по телефону "
                        + newCarer.getPhoneNumber();
                String textForClient = "Ваши контактные данные записаны. Волонтеры свяжутся с Вами в ближайшее время.";

                sendPlainText(volunteerChatId, textForVolunteer);
                sendPlainText(chatId, textForClient);
            } else {
                carer.setFullName(clientName);
                carer.setPhoneNumber(clientPhoneNumber);
                Carer updatedCarer = this.carerService.saveCarer(carer, client.getPetType());
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
            Carer carer = this.carerService.findCarerByChatId(chatId, client.getPetType());
            DailyReport dailyReport = this.dailyReportService.findDailyReportByCarerIdAndDate(carer.getId(), LocalDate.now(), client.getPetType());
            if (client.getPetType() == PetType.CAT) {
                CatDailyReport catDailyReport = (CatDailyReport) dailyReport;
                catDailyReport.setCatDiet(message.substring(2).trim());
                this.dailyReportService.addDailyReport(catDailyReport, client.getPetType());
            } else if (client.getPetType() == PetType.DOG) {
                DogDailyReport dogDailyReport = (DogDailyReport) dailyReport;
                dogDailyReport.setDogDiet(message.substring(2).trim());
                this.dailyReportService.addDailyReport(dogDailyReport, client.getPetType());
            }
            String text = """
                    Спасибо! Информация сохранена.
                    Пожалуйста, пришлите информация об
                    общем самочувствии и привыкании к новому месту.
                    ВАЖНО! Сообщение должно начинаться с "3)"!
                    """;
            sendPlainText(chatId, text);
        } else if (message.startsWith("3)")) {
            Carer carer = this.carerService.findCarerByChatId(chatId, client.getPetType());
            DailyReport dailyReport = this.dailyReportService.findDailyReportByCarerIdAndDate(carer.getId(), LocalDate.now(), client.getPetType());
            if (client.getPetType() == PetType.CAT) {
                CatDailyReport catDailyReport = (CatDailyReport) dailyReport;
                catDailyReport.setCatHealth(message.substring(2).trim());
                this.dailyReportService.addDailyReport(catDailyReport, client.getPetType());
            } else if (client.getPetType() == PetType.DOG) {
                DogDailyReport dogDailyReport = (DogDailyReport) dailyReport;
                dogDailyReport.setDogHealth(message.substring(2).trim());
                this.dailyReportService.addDailyReport(dogDailyReport, client.getPetType());
            }
            String text = """
                    Спасибо! Информация сохранена.
                    Пожалуйста, пришлите информацию об
                    изменении в поведении: отказ от старых привычек,
                    приобретение новых.
                    ВАЖНО! Сообщение должно начинаться с "4)"!
                    """;
            sendPlainText(chatId, text);
        } else if (message.startsWith("4)")) {
            Carer carer = this.carerService.findCarerByChatId(chatId, client.getPetType());
            DailyReport dailyReport = this.dailyReportService.findDailyReportByCarerIdAndDate(carer.getId(), LocalDate.now(), client.getPetType());
            if (client.getPetType() == PetType.CAT) {
                CatDailyReport catDailyReport = (CatDailyReport) dailyReport;
                catDailyReport.setCatBehavior(message.substring(2).trim());
                this.dailyReportService.addDailyReport(catDailyReport, client.getPetType());
            } else if (client.getPetType() == PetType.DOG) {
                DogDailyReport dogDailyReport = (DogDailyReport) dailyReport;
                dogDailyReport.setDogBehavior(message.substring(2).trim());
                this.dailyReportService.addDailyReport(dogDailyReport, client.getPetType());
            }
            String text = "Спасибо! Отчет за " +
                    LocalDate.now() + " сохранен!";
            sendPlainText(chatId, text);
        } else {
            String text = "Некорректное сообщение";
            sendPlainText(chatId, text);
        }
    }

    private void selectShelterCommand(long chatId, Chat.Type chatType) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Приют для кошек").
                        callbackData(Command.SELECT_CAT_SHELTER_COMMAND.getCommand()),
                new InlineKeyboardButton("Приют для собак")
                        .callbackData(Command.SELECT_DOG_SHELTER_COMMAND.getCommand())
        ));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        String text = "";
        if (chatType == Chat.Type.Private) {
            text = "Пожалуйста, выберите приют для животных. Для получения информации о другом приюте " +
                    "нажмите на кнопку другого приюта или вызовите меню выбора через /start с последующим выбором приюта";
        } else if (chatType == Chat.Type.group) {
            text = "Пожалуйста, выберите к какому приюту для животных вы относитесь.";
        }
        SendMessage response = new SendMessage(chatId, text);
        response.replyMarkup(keyboard);
        telegramBot.execute(response);
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
                new InlineKeyboardButton("Как взять питомца из приюта")
                        .callbackData(Command.TAKE_A_PET_COMMAND.getCommand()),
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
                new InlineKeyboardButton("Контактные данные охраны для оформления пропуска на машину")
                        .callbackData(Command.SECURITY_CONTATCT_COMMAND.getCommand()),
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
    private void takePetCommandMenu(long chatId, PetType petType) {
        List<InlineKeyboardButton> buttons = null;
        String text = "";
        if (petType == PetType.CAT) {
            buttons = new ArrayList<>(List.of(
                    new InlineKeyboardButton("Узнать правила знакомства с кошкой")
                            .callbackData(Command.INTRODUCTION_TO_PET_COMMAND.getCommand()),
                    new InlineKeyboardButton("Получить список документов")
                            .callbackData(Command.TAKE_DOCUMENTS_LIST_COMMAND.getCommand()),
                    new InlineKeyboardButton("Транспортировка животного")
                            .callbackData(Command.TRANSFER_A_PET_COMMAND.getCommand()),
                    new InlineKeyboardButton("Обустройство дома для котенка")
                            .callbackData(Command.ENVIRONMENT_FOR_YOUNG_PET_COMMAND.getCommand()),
                    new InlineKeyboardButton("Обустройство дома для взрослой кошки")
                            .callbackData(Command.ENVIRONMENT_FOR_PET_COMMAND.getCommand()),
                    new InlineKeyboardButton("Обустройство дома для кошки с ограниченными возможностями")
                            .callbackData(Command.ENVIRONMENT_FOR_LIMITED_PET_COMMAND.getCommand()),
                    new InlineKeyboardButton("Частые причины отказов в выдаче кошки кандидату")
                            .callbackData(Command.USUAL_REFUSALS_COMMAND.getCommand()),
                    new InlineKeyboardButton("Записать Ваши контактные данные для связи")
                            .callbackData(Command.WRITE_CLIENT_CONTACT_COMMAND.getCommand()),
                    new InlineKeyboardButton("Позвать волонтера")
                            .callbackData(Command.CALL_VOLUNTEER_COMMAND.getCommand()),
                    new InlineKeyboardButton("Вернуться назад")
                            .callbackData(Command.BACK_COMMAND.getCommand())
            ));
            text = "Добрый день! Здесь Вы можете узнать как взять кошку из приюта.";
        } else if (petType == PetType.DOG) {
            buttons = new ArrayList<>(List.of(
                    new InlineKeyboardButton("Узнать правила знакомства с собакой")
                            .callbackData(Command.INTRODUCTION_TO_PET_COMMAND.getCommand()),
                    new InlineKeyboardButton("Получить список документов")
                            .callbackData(Command.TAKE_DOCUMENTS_LIST_COMMAND.getCommand()),
                    new InlineKeyboardButton("Транспортировка животного")
                            .callbackData(Command.TRANSFER_A_PET_COMMAND.getCommand()),
                    new InlineKeyboardButton("Обустройство дома для щенка")
                            .callbackData(Command.ENVIRONMENT_FOR_YOUNG_PET_COMMAND.getCommand()),
                    new InlineKeyboardButton("Обустройство дома для взрослой собаки")
                            .callbackData(Command.ENVIRONMENT_FOR_PET_COMMAND.getCommand()),
                    new InlineKeyboardButton("Обустройство дома для собаки с ограниченными возможностями")
                            .callbackData(Command.ENVIRONMENT_FOR_LIMITED_PET_COMMAND.getCommand()),
                    new InlineKeyboardButton("Советы кинолога")
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
            text = "Добрый день! Здесь Вы можете узнать как взять собаку из приюта.";
        }
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        assert buttons != null;
        buttons.forEach(keyboard::addRow);

        SendMessage response = new SendMessage(chatId, text);
        response.replyMarkup(keyboard);
        telegramBot.execute(response);
    }

    /**
     * Вывод в чат кнопок с командами меню и отправка данных при нажатии на кнопку
     *
     * @param chatId идентификатор чата, в котором выводятся кнопки
     */
    private void sendReportCommandMenu(long chatId, PetType petType) {
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

        String text = "";
        if (petType == PetType.CAT) {
            text = "Здесь Вы можете узнать как отправить отчёт о кошке.";
        } else if (petType == PetType.DOG) {
            text = "Здесь Вы можете узнать как отправить отчёт о собаке.";
        }

        SendMessage response = new SendMessage(chatId, text);
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
    private void sendCallVolunteerCommand(long chatId,
                                         long clientId,
                                         String clientFirstName,
                                         String clientLastName,
                                         long volunteerChatId) {
        SendMessage sendMessageForClient = new SendMessage(chatId,
                "Волонтер свяжется с Вами в ближайшее время");
        SendMessage sendMessageForVolunteer = new SendMessage(volunteerChatId,
                "Необходимо связаться с клиентом " + clientFirstName + " "
                        + clientLastName + " " + "[Информация о клиенте](tg://user?id=" + clientId + " )");
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
    private void sendPlainText(long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }
}
