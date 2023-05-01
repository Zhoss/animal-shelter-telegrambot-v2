package pro.sky.teamwork.animalsheltertelegrambotv2.listener;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatCarerRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogAgreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogAgreementRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.*;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.ClientRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.VolunteerChatRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.CarerService;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.DailyReportService;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {
    private static final String MESSAGE_JSON = "message_update.json";
    private static final String CALLBACK_QUERY_JSON = "callback_query_update.json";
    private static final String NO_TEXT_JSON = "no_text_update.json";
    private static final String PRIVATE_CHAT = "Private";
    private static final String GROUP_CHAT = "group";
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private CarerService carerService;
    @Mock
    private DailyReportService dailyReportService;
    @Mock
    private VolunteerChatRepository volunteerChatRepository;
    @Mock
    private DogAgreementRepository dogAgreementRepository;
    @Mock
    private DogRepository dogRepository;
    @Mock
    private CatCarerRepository catCarerRepository;
    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private TelegramBotUpdatesListener listener;

    @Test
    void handleCarerStartMenuTest() throws URISyntaxException, IOException {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Приют для кошек").
                        callbackData(Command.SELECT_CAT_SHELTER_COMMAND.getCommand()),
                new InlineKeyboardButton("Приют для собак")
                        .callbackData(Command.SELECT_DOG_SHELTER_COMMAND.getCommand())
        ));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        createUpdateFromJson(MESSAGE_JSON, PRIVATE_CHAT, "/start");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actual = argumentCaptor.getAllValues();

        Assertions.assertThat(actual.get(0).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(0).getParameters().get("text")).isEqualTo("""
                Добрый день! Меня зовут AnimalShelterBot. Я отвечаю на
                популярные вопросы о том, что нужно знать и уметь,
                чтобы забрать собаку из приюта.
                """);
        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("Пожалуйста, выберите приют для животных. Для получения информации о другом приюте " +
                "нажмите на кнопку другого приюта или вызовите меню выбора через /start с последующим выбором приюта");
        Assertions.assertThat(actual.get(1).getParameters().get("reply_markup")).isEqualTo(keyboard);

        Client client = new Client();
        client.setTelegramChatId(12345L);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        Assertions.assertThat(clientRepository.save(client)).isEqualTo(client);
    }

    @Test
    void handleSelectCatShelterMenuTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setTelegramChatId(12345L);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);
        client.setPetType(PetType.CAT);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        Assertions.assertThat(clientRepository.save(client)).isEqualTo(client);

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

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/select_cat_shelter");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Что бы Вы хотели узнать?");
        Assertions.assertThat(actual.getParameters().get("reply_markup")).isEqualTo(keyboard);
    }

    @Test
    void handleCatShelterInfoMenuTest() throws URISyntaxException, IOException {
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

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/shelter_info");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Добрый день! Здесь Вы можете узнать " +
                "основную информацию о нашем приюте.");
        Assertions.assertThat(actual.getParameters().get("reply_markup")).isEqualTo(keyboard);
    }

    @Test
    void handleTakeCatMenuTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
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
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/take_pet");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Добрый день! Здесь Вы можете узнать как взять кошку из приюта.");
        Assertions.assertThat(actual.getParameters().get("reply_markup")).isEqualTo(keyboard);
    }

    @Test
    void handleSendCatReportMenuTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

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

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/send_report_menu");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Здесь Вы можете узнать как отправить отчёт о кошке.");
        Assertions.assertThat(actual.getParameters().get("reply_markup")).isEqualTo(keyboard);
    }

    @Test
    void callCatVolunteerTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setTelegramChatId(12345L);
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setTelegramChatId(98765L);
        volunteerChat.setPetType(PetType.CAT);
        when(volunteerChatRepository.findByPetType(any(PetType.class))).thenReturn(Optional.of(volunteerChat));

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/call_volunteer");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(3)).execute(argumentCaptor.capture());
        List<SendMessage> actual = argumentCaptor.getAllValues();

        InlineKeyboardButton button = new InlineKeyboardButton("Подтвердить")
                .callbackData(Command.VOLUNTEER_CONFIRM_COMMAND.getCommand());

        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(button));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("Волонтер свяжется с Вами в ближайшее время");

        Assertions.assertThat(actual.get(2).getParameters().get("chat_id")).isEqualTo(98765L);
        Assertions.assertThat(actual.get(2).getParameters().get("text")).isEqualTo("Необходимо связаться с клиентом Ivan Ivanov [Информация о клиенте](tg://user?id=12345 )");
        Assertions.assertThat(actual.get(2).getParameters().get("parse_mode")).isEqualTo(ParseMode.Markdown.name());
        Assertions.assertThat(actual.get(2).getParameters().get("reply_markup")).isEqualTo(keyboard);
    }

    @Test
    void handleCatShelterMainInfoTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/shelter_main_info");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Основная информация о приюте для кошек");
    }

    @Test
    void handleCatShelterWorkScheduleTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/shelter_work_schedule");

        ArgumentCaptor<BaseRequest> argumentCaptor = ArgumentCaptor.forClass(BaseRequest.class);
        Mockito.verify(telegramBot, times(3)).execute(argumentCaptor.capture());
        List<BaseRequest> actual = argumentCaptor.getAllValues();

        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("""
                Расписание работы приюта для кошек:
                номер телефона:
                e-mail:
                """);

        Assertions.assertThat(actual.get(2).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(2).getParameters().get("photo")).isEqualTo(new File("src/redaktirovat-kartu.png"));
    }

    @Test
    void handleCatShelterSecurityContact() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/security_contact");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Контактные данные охраны для оформления пропуска на машину для проезда к приюту для кошек");
    }

    @Test
    void handleCatShelterSafetyRecommendations() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/shelter_safety_recommendations");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Общие рекомендации о технике безопасности на территории приюта для кошек");
    }

    @Test
    void handleWriteClientContactTest() throws URISyntaxException, IOException {
        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/write_contact_information");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                Прошу написать Ваши Фамилию Имя Отчество
                (напр., Иванов Иван Иванович)
                и номер телефона в формате +7(ХХХ)ХХХХХХХ
                """);
    }

    @Test
    void handleBackToStartMenuTest() throws URISyntaxException, IOException {
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

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/back");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Что бы Вы хотели узнать?");
        Assertions.assertThat(actual.getParameters().get("reply_markup")).isEqualTo(keyboard);
    }

    @Test
    void handleIntroductionToCatTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/intro_pet");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Правила знакомства с кошкой до того, как можно забрать ее из приюта");
    }

    @Test
    void handleTakeDocumentTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/take_pet_list");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Список документов, необходимых для того, чтобы взять кошку из приюта");
    }

    @Test
    void handleTransferCatTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/transfer_pet");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Список рекомендаций по транспортировке кошки");
    }

    @Test
    void handleEnvironmentForKittyTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/young_pet_environment");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Список рекомендаций по обустройству дома для котенка");
    }

    @Test
    void handleEnvironmentForCatTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/pet_environment");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Список рекомендаций по обустройству дома для взрослой кошки");
    }

    @Test
    void handleEnvironmentForLimitedCatTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/limited_pet_environment");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Список рекомендаций по обустройству дома для кошки с ограниченными " +
                "возможностями (зрение, передвижение)");
    }

    @Test
    void handleCynologistAdvicesForDogTest() throws URISyntaxException, IOException {
        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/cynologist_advices");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Советы кинолога по первичному общению с собакой");
    }

    @Test
    void handleCynologistContactsForDogTest() throws URISyntaxException, IOException {
        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/cynologist_contacts");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Рекомендации по проверенным кинологам для дальнейшего обращения к ним");
    }

    @Test
    void handleUsualRefusalsTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/usual_refusals");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Список причин, почему могут отказать и не дать забрать кошку из приюта");
    }

    @Test
    void handleSendCatReportTest() throws URISyntaxException, IOException {
        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/send_report");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(3)).execute(argumentCaptor.capture());
        List<SendMessage> actual = argumentCaptor.getAllValues();

        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("""
                Уважаемый опекун! В качестве отчета пошагово направляются следующие данные:
                1) Фото животного.
                2) Рацион животного.
                3) Общее самочувствие и привыкание к новому месту.
                4) Изменение в поведении: отказ от старых привычек, приобретение новых.
                """);
        Assertions.assertThat(actual.get(2).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(2).getParameters().get("text")).isEqualTo("Пожалуйста, пришлите фото животного (1 шт.)");
    }

    @Test
    void handleUnknownCommandTest() throws URISyntaxException, IOException {
        createUpdateFromJson(CALLBACK_QUERY_JSON, PRIVATE_CHAT, "/any_command");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Неизвестная команда");
    }

    @Test
    void handleUpdateWithAudioTest() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        Audio audio = mock(Audio.class);
        User user = mock(User.class);

        when(update.message()).thenReturn(message);
        when(chat.id()).thenReturn(12345L);
        when(message.chat()).thenReturn(chat);
        when(message.from()).thenReturn(user);
        when(message.from().id()).thenReturn(12345L);
        when(message.from().firstName()).thenReturn("Ivan");
        when(message.from().lastName()).thenReturn("Ivanov");

        when(message.audio()).thenReturn(audio);

        listener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Извините, но я могу работать только с текстом или фото");
    }

    @Test
    void handleUpdateWithDocumentTest() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        Document document = mock(Document.class);
        User user = mock(User.class);

        when(update.message()).thenReturn(message);
        when(chat.id()).thenReturn(12345L);
        when(message.chat()).thenReturn(chat);
        when(message.from()).thenReturn(user);
        when(message.from().id()).thenReturn(12345L);
        when(message.from().firstName()).thenReturn("Ivan");
        when(message.from().lastName()).thenReturn("Ivanov");

        when(message.document()).thenReturn(document);

        listener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Извините, но я могу работать только с текстом или фото");
    }

    @Test
    void handleUpdateWithStickerTest() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        Sticker sticker = mock(Sticker.class);
        User user = mock(User.class);

        when(update.message()).thenReturn(message);
        when(chat.id()).thenReturn(12345L);
        when(message.chat()).thenReturn(chat);
        when(message.from()).thenReturn(user);
        when(message.from().id()).thenReturn(12345L);
        when(message.from().firstName()).thenReturn("Ivan");
        when(message.from().lastName()).thenReturn("Ivanov");

        when(message.sticker()).thenReturn(sticker);

        listener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Извините, но я могу работать только с текстом или фото");
    }

    @Test
    void handleUpdateWithVideoTest() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        Video video = mock(Video.class);
        User user = mock(User.class);

        when(update.message()).thenReturn(message);
        when(chat.id()).thenReturn(12345L);
        when(message.chat()).thenReturn(chat);
        when(message.from()).thenReturn(user);
        when(message.from().id()).thenReturn(12345L);
        when(message.from().firstName()).thenReturn("Ivan");
        when(message.from().lastName()).thenReturn("Ivanov");

        when(message.video()).thenReturn(video);

        listener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Извините, но я могу работать только с текстом или фото");
    }

    @Test
    void handleVolunteerChatSelectShelter() throws URISyntaxException, IOException {
        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Приют для кошек").
                        callbackData(Command.SELECT_CAT_SHELTER_COMMAND.getCommand()),
                new InlineKeyboardButton("Приют для собак")
                        .callbackData(Command.SELECT_DOG_SHELTER_COMMAND.getCommand())
        ));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        String oJson = Files.readString(
                Paths.get(TelegramBotUpdatesListener.class.getResource(NO_TEXT_JSON).toURI()));
        String nJson = oJson.replace("%chat_type%", GROUP_CHAT);
        Update update = BotUtils.fromJson(nJson, Update.class);
        listener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actual = argumentCaptor.getAllValues();

        Assertions.assertThat(actual.get(0).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(0).getParameters().get("text")).isEqualTo("Добрый день, уважаемые волонтеры!");

        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("Пожалуйста, выберите к какому приюту для животных вы относитесь.");
        Assertions.assertThat(actual.get(1).getParameters().get("reply_markup")).isEqualTo(keyboard);
    }

    @Test
    void handleWriteClientContactInfoTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.DOG);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        Carer carer = new Carer();
        carer.setFullName("Иванов Иван Иванович");
        carer.setPhoneNumber("+7(111)1234567");
        when(carerService.findCarerByChatId(12345L, PetType.DOG)).thenReturn(null);
        when(carerService.addCarer("Иванов Иван Иванович", 20, "+7(111)1234567", 12345L, PetType.DOG)).thenReturn(carer);

        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setTelegramChatId(98765L);
        volunteerChat.setPetType(PetType.DOG);
        when(volunteerChatRepository.findByPetType(any(PetType.class))).thenReturn(Optional.of(volunteerChat));

        createUpdateFromJson(MESSAGE_JSON, PRIVATE_CHAT, "иванов иван иванович +7(111)1234567");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actual = argumentCaptor.getAllValues();

        Assertions.assertThat(actual.get(0).getParameters().get("chat_id")).isEqualTo(98765L);
        Assertions.assertThat(actual.get(0).getParameters().get("text")).isEqualTo("Прошу связаться с клиентом Иванов Иван Иванович по телефону +7(111)1234567");

        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("Ваши контактные данные записаны. Волонтеры свяжутся с Вами в ближайшее время.");
    }

    @Test
    void handleUpdateClientContactInfoTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.DOG);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        Carer carer = new Carer();
        when(carerService.findCarerByChatId(12345L, PetType.DOG)).thenReturn(carer);
        when(carerService.saveCarer(carer, PetType.DOG)).thenReturn(carer);

        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setTelegramChatId(98765L);
        volunteerChat.setPetType(PetType.DOG);
        when(volunteerChatRepository.findByPetType(any(PetType.class))).thenReturn(Optional.of(volunteerChat));

        createUpdateFromJson(MESSAGE_JSON, PRIVATE_CHAT, "иванов иван иванович +7(111)1234567");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actual = argumentCaptor.getAllValues();

        Assertions.assertThat(actual.get(0).getParameters().get("chat_id")).isEqualTo(98765L);
        Assertions.assertThat(actual.get(0).getParameters().get("text")).isEqualTo("Прошу связаться с клиентом Иванов Иван Иванович по телефону +7(111)1234567");

        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("""
                        Ваши контактные данные перезаписаны.
                        Волонтеры свяжутся с Вами в ближайшее время.
                        """);
    }

    @Test
    void handleCatDietReportTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        Carer carer = new Carer();
        carer.setId(1L);
        when(carerService.findCarerByChatId(12345L, PetType.CAT)).thenReturn(carer);

        CatDailyReport catDailyReport = new CatDailyReport();
        when(dailyReportService.findDailyReportByCarerIdAndDate(1L, LocalDate.now(), PetType.CAT)).thenReturn(catDailyReport);

        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setPetType(PetType.CAT);
        when(volunteerChatRepository.findByPetType(any(PetType.class))).thenReturn(Optional.of(volunteerChat));

        createUpdateFromJson(MESSAGE_JSON, PRIVATE_CHAT, "2) Рацион кота");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                    Спасибо! Информация сохранена.
                    Пожалуйста, пришлите информация об
                    общем самочувствии и привыкании к новому месту.
                    ВАЖНО! Сообщение должно начинаться с "3)"!
                    """);
    }

    @Test
    void handleDogHealthReportTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.DOG);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        Carer carer = new Carer();
        carer.setId(1L);
        when(carerService.findCarerByChatId(12345L, PetType.DOG)).thenReturn(carer);

        DogDailyReport dogDailyReport = new DogDailyReport();
        when(dailyReportService.findDailyReportByCarerIdAndDate(1L, LocalDate.now(), PetType.DOG)).thenReturn(dogDailyReport);

        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setPetType(PetType.DOG);
        when(volunteerChatRepository.findByPetType(any(PetType.class))).thenReturn(Optional.of(volunteerChat));

        createUpdateFromJson(MESSAGE_JSON, PRIVATE_CHAT, "3) Рацион собаки");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                    Спасибо! Информация сохранена.
                    Пожалуйста, пришлите информацию об
                    изменении в поведении: отказ от старых привычек,
                    приобретение новых.
                    ВАЖНО! Сообщение должно начинаться с "4)"!
                    """);
    }

    @Test
    void handleCatHealthReportTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        Carer carer = new Carer();
        carer.setId(1L);
        when(carerService.findCarerByChatId(12345L, PetType.CAT)).thenReturn(carer);

        CatDailyReport catDailyReport = new CatDailyReport();
        when(dailyReportService.findDailyReportByCarerIdAndDate(1L, LocalDate.now(), PetType.CAT)).thenReturn(catDailyReport);

        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setPetType(PetType.CAT);
        when(volunteerChatRepository.findByPetType(any(PetType.class))).thenReturn(Optional.of(volunteerChat));

        createUpdateFromJson(MESSAGE_JSON, PRIVATE_CHAT, "4) Поведение кота");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Спасибо! Отчет за " +
                LocalDate.now() + " сохранен!");
    }

    @Test
    void handleNotCorrectMessageTest() throws URISyntaxException, IOException {
        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setPetType(PetType.CAT);
        when(volunteerChatRepository.findByPetType(any(PetType.class))).thenReturn(Optional.of(volunteerChat));

        createUpdateFromJson(MESSAGE_JSON, PRIVATE_CHAT, "Любое сообщение");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Некорректное сообщение");
    }

    @Test
    void handleVolunteerSelectCatShelterTest() throws URISyntaxException, IOException {
        createUpdateFromJson(MESSAGE_JSON, GROUP_CHAT, "/select_cat_shelter");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Теперь вы волонтеры приюта для кошек");
    }

    @Test
    void handleVolunteerConfirmRequestTest() throws URISyntaxException, IOException {
        createUpdateFromJson(MESSAGE_JSON, GROUP_CHAT, "/volunteer_confirm");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Спасибо за подтверждение заявки");
    }

    @Test
    void handleVolunteerConfirmNotifyCarersTest() throws URISyntaxException, IOException {
        createUpdateFromJson(MESSAGE_JSON, GROUP_CHAT, "/notify_carers");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Пришлите, пожалуйста, id опекуна в формате \"id X\", " +
                "где X - идентификатор опекуна. Помните, нужно именно ответить (Reply) на данное сообщение");
    }

    @Test
    void handleVolunteerNotifyCarersTest() throws URISyntaxException, IOException {
        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setTelegramChatId(12345L);
        volunteerChat.setPetType(PetType.CAT);
        when(volunteerChatRepository.findByTelegramChatId(any(Long.class))).thenReturn(volunteerChat);

        CatCarer carer = new CatCarer();
        carer.setId(1L);
        carer.setChatId(98765L);
        carer.setFullName("Иванов Иван Иванович");
        when(catCarerRepository.findById(any(Long.class))).thenReturn(Optional.of(carer));

        createUpdateFromJson(MESSAGE_JSON, GROUP_CHAT, "id 1");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actual = argumentCaptor.getAllValues();

        Assertions.assertThat(actual.get(0).getParameters().get("chat_id")).isEqualTo(98765L);
        Assertions.assertThat(actual.get(0).getParameters().get("text")).isEqualTo("Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                "Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут " +
                "обязаны самолично проверять условия содержания животного");

        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("Опекун кота/кошки Иванов Иван Иванович уведомлен");
    }

    @Test
    void handleVolunteerSendAnyMessageTest() throws URISyntaxException, IOException {
        createUpdateFromJson(MESSAGE_JSON, GROUP_CHAT, "любое сообщение");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Неизвестная команда");
    }

    @Test
    void handleVolunteerDecidesExtendProbationFor14DaysTest() throws URISyntaxException, IOException {
        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setTelegramChatId(12345L);
        volunteerChat.setPetType(PetType.DOG);
        when(volunteerChatRepository.findByTelegramChatId(any(Long.class))).thenReturn(volunteerChat);

        DogCarer carer = new DogCarer();
        carer.setId(1L);
        carer.setChatId(98765L);
        carer.setFullName("Иванов Иван Иванович");

        DogAgreement agreement = new DogAgreement();
        agreement.setCarer(carer);
        agreement.setProbationEndData(LocalDate.parse("2023-01-01"));
        when(dogAgreementRepository.findDogAgreementByDogCarer_Id(any(Long.class))).thenReturn(Optional.of(agreement));
        when(dogAgreementRepository.save(any(DogAgreement.class))).thenReturn(agreement);

        createUpdateFromJson(MESSAGE_JSON, GROUP_CHAT, "/extend_probation_14/1");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actual = argumentCaptor.getAllValues();

        Assertions.assertThat(actual.get(0).getParameters().get("chat_id")).isEqualTo(98765L);
        Assertions.assertThat(actual.get(0).getParameters().get("text")).isEqualTo("Добрый день! Ваш испытательный срок был продлен на 14 дней.");

        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("Опекун Иванов Иван Иванович уведомлен о продлении испытательного срока на 14 дней.");
    }

    @Test
    void handleVolunteerDecidesExtendProbationFor30DaysTest() throws URISyntaxException, IOException {
        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setTelegramChatId(12345L);
        volunteerChat.setPetType(PetType.DOG);
        when(volunteerChatRepository.findByTelegramChatId(any(Long.class))).thenReturn(volunteerChat);

        DogCarer carer = new DogCarer();
        carer.setId(1L);
        carer.setChatId(98765L);
        carer.setFullName("Иванов Иван Иванович");

        DogAgreement agreement = new DogAgreement();
        agreement.setCarer(carer);
        agreement.setProbationEndData(LocalDate.parse("2023-01-01"));
        when(dogAgreementRepository.findDogAgreementByDogCarer_Id(any(Long.class))).thenReturn(Optional.of(agreement));
        when(dogAgreementRepository.save(any(DogAgreement.class))).thenReturn(agreement);

        createUpdateFromJson(MESSAGE_JSON, GROUP_CHAT, "/extend_probation_30/1");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actual = argumentCaptor.getAllValues();

        Assertions.assertThat(actual.get(0).getParameters().get("chat_id")).isEqualTo(98765L);
        Assertions.assertThat(actual.get(0).getParameters().get("text")).isEqualTo("Добрый день! Ваш испытательный срок был продлен на 30 дней.");

        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("Опекун Иванов Иван Иванович уведомлен о продлении испытательного срока на 30 дней.");
    }

    @Test
    void handleVolunteerDecidesProbationNotPassedTest() throws URISyntaxException, IOException {
        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setTelegramChatId(12345L);
        volunteerChat.setPetType(PetType.DOG);
        when(volunteerChatRepository.findByTelegramChatId(any(Long.class))).thenReturn(volunteerChat);

        Dog dog = new Dog();
        dog.setOnProbation(true);

        DogCarer carer = new DogCarer();
        carer.setId(1L);
        carer.setChatId(98765L);
        carer.setDog(dog);
        carer.setFullName("Иванов Иван Иванович");

        DogAgreement agreement = new DogAgreement();
        agreement.setCarer(carer);
        agreement.setProbationEndData(LocalDate.parse("2023-01-01"));
        when(dogAgreementRepository.findDogAgreementByDogCarer_Id(any(Long.class))).thenReturn(Optional.of(agreement));
        when(dogAgreementRepository.save(any(DogAgreement.class))).thenReturn(agreement);

        createUpdateFromJson(MESSAGE_JSON, GROUP_CHAT, "/probation_not_passed/1");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actual = argumentCaptor.getAllValues();

        Assertions.assertThat(actual.get(0).getParameters().get("chat_id")).isEqualTo(98765L);
        Assertions.assertThat(actual.get(0).getParameters().get("text")).isEqualTo("Добрый день! К сожалению, Вы не прошли испытательный срок. " +
                "Просим Вас привезти собаку обратно в приют и уточнить всю необходимую информацию");

        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("Опекун Иванов Иван Иванович уведомлен о не прохождении испытательного срока.");
    }

    @Test
    void handleVolunteerDecidesProbationPassedTest() throws URISyntaxException, IOException {
        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setTelegramChatId(12345L);
        volunteerChat.setPetType(PetType.DOG);
        when(volunteerChatRepository.findByTelegramChatId(any(Long.class))).thenReturn(volunteerChat);

        Dog dog = new Dog();
        dog.setOnProbation(true);

        DogCarer carer = new DogCarer();
        carer.setId(1L);
        carer.setChatId(98765L);
        carer.setDog(dog);
        carer.setFullName("Иванов Иван Иванович");

        DogAgreement agreement = new DogAgreement();
        agreement.setCarer(carer);
        agreement.setProbationEndData(LocalDate.parse("2023-01-01"));
        when(dogAgreementRepository.findDogAgreementByDogCarer_Id(any(Long.class))).thenReturn(Optional.of(agreement));
        when(dogAgreementRepository.save(any(DogAgreement.class))).thenReturn(agreement);

        createUpdateFromJson(MESSAGE_JSON, GROUP_CHAT, "/probation_passed/1");

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actual = argumentCaptor.getAllValues();

        Assertions.assertThat(actual.get(0).getParameters().get("chat_id")).isEqualTo(98765L);
        Assertions.assertThat(actual.get(0).getParameters().get("text")).isEqualTo("Добрый день! Поздравляем, Вы прошли испытательный срок!");

        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("Опекун Иванов Иван Иванович уведомлен о прохождении испытательного срока.");
    }

    @Test
    void handleCarerSendPhotoTest() throws IOException {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        PhotoSize photo = mock(PhotoSize.class);
        User user = mock(User.class);

        when(update.message()).thenReturn(message);
        when(chat.id()).thenReturn(12345L);
        when(message.chat()).thenReturn(chat);
        when(message.from()).thenReturn(user);
        when(message.from().id()).thenReturn(12345L);
        when(photo.fileId()).thenReturn("123456");
        when(message.photo()).thenReturn(new PhotoSize[] {photo});

        GetFileResponse getFileResponse = mock(GetFileResponse.class);
        com.pengrad.telegrambot.model.File file = mock(com.pengrad.telegrambot.model.File.class);
        when(telegramBot.execute(any(GetFile.class))).thenReturn(getFileResponse);
        when(getFileResponse.file()).thenReturn(file);
        when(file.fileSize()).thenReturn(111111L);
        when(getFileResponse.isOk()).thenReturn(true);

        String pet = "Pet";
        byte[] bytes = pet.getBytes();
        when(telegramBot.getFileContent(any(com.pengrad.telegrambot.model.File.class))).thenReturn(bytes);

        Client client = new Client();
        client.setPetType(PetType.CAT);
        when(clientRepository.findByTelegramChatId(any(Long.class))).thenReturn(client);

        CatCarer carer = new CatCarer();
        carer.setId(1L);
        carer.setPassportNumber("1234 123456");
        when(carerService.findCarerByChatId(12345L, PetType.CAT)).thenReturn(carer);

        when(dailyReportService.findDailyReportByCarerIdAndDate(1L, LocalDate.now(), PetType.CAT)).thenReturn(null);

        listener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                            Спасибо! Информация сохранена.
                            Пожалуйста, пришлите информацию о
                            рационе животного.
                            ВАЖНО! Сообщение должно начинаться с "2)"!
                            """);
    }

    private void createUpdateFromJson(String jsonType, String chatType, String text) throws IOException, URISyntaxException {
        String oJson = Files.readString(
                Paths.get(TelegramBotUpdatesListener.class.getResource(jsonType).toURI()));
        String nJson = oJson.replace("%chat_type%", chatType);
        Update update = BotUtils.fromJson(nJson.replace("%command%", text), Update.class);
        listener.process(Collections.singletonList(update));
    }
}