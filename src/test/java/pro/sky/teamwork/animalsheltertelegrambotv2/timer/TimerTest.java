package pro.sky.teamwork.animalsheltertelegrambotv2.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatAgreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Command;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.VolunteerChat;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.VolunteerChatRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.AgreementService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TimerTest {
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private VolunteerChatRepository volunteerChatRepository;
    @Mock
    private AgreementService agreementService;
    @InjectMocks
    private Timer timer;

    @Test
    void handleCatCarerNotSendDailyReportFor1DaysTest() {
        CatCarer catCarer = new CatCarer();
        CatDailyReport catDailyReport = new CatDailyReport();
        catDailyReport.setReportDate(LocalDate.now().minusDays(2));
        catCarer.setCatDailyReports(List.of(catDailyReport));
        catCarer.setChatId(12345L);
        catCarer.setFullName("Иванов Иван Иванович");
        catDailyReport.setCarer(catCarer);

        CatAgreement catAgreement = new CatAgreement();
        catAgreement.setCarer(catCarer);
        List<Agreement> catAgreements = List.of(catAgreement);

        when(agreementService.findAllAgreementsWithProbationByDate(any(LocalDate.class), eq(PetType.CAT))).thenReturn(catAgreements);

        timer.sendNotificationAboutNotSendingDailyReport();

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Добрый день! Напоминаю, что " +
                "Вы не отправили отчет за прошлый день. Прошу прислать отчет!");
    }

    @Test
    void handleCatCarerNotSendDailyReportFor2DaysTest() {
        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setTelegramChatId(98765L);
        volunteerChat.setPetType(PetType.CAT);
        when(volunteerChatRepository.findByPetType(any(PetType.class))).thenReturn(Optional.of(volunteerChat));

        CatCarer catCarer = new CatCarer();
        CatDailyReport catDailyReport = new CatDailyReport();
        catDailyReport.setReportDate(LocalDate.now().minusDays(3));
        catCarer.setCatDailyReports(List.of(catDailyReport));
        catCarer.setChatId(12345L);
        catCarer.setFullName("Иванов Иван Иванович");
        catDailyReport.setCarer(catCarer);

        CatAgreement catAgreement = new CatAgreement();
        catAgreement.setCarer(catCarer);
        List<Agreement> catAgreements = List.of(catAgreement);

        when(agreementService.findAllAgreementsWithProbationByDate(any(LocalDate.class), eq(PetType.CAT))).thenReturn(catAgreements);

        timer.sendNotificationAboutNotSendingDailyReport();

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actual = argumentCaptor.getAllValues();

        Assertions.assertThat(actual.get(0).getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.get(0).getParameters().get("text")).isEqualTo("Добрый день! Напоминаю, что " +
                "Вы не отправляли отчет больше двух дней. Прошу прислать отчет!");

        Assertions.assertThat(actual.get(1).getParameters().get("chat_id")).isEqualTo(98765L);
        Assertions.assertThat(actual.get(1).getParameters().get("text")).isEqualTo("Опекун Иванов Иван Иванович не отправлял отчет более двух дней." +
                "[Информация об опекуне](tg://user?id=12345 )");
        Assertions.assertThat(actual.get(1).getParameters().get("parse_mode")).isEqualTo(ParseMode.Markdown.name());
    }

    @Test
    void handleVolunteerNotifiedThatProbationIsEndedTest() {
        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setTelegramChatId(98765L);
        volunteerChat.setPetType(PetType.CAT);
        when(volunteerChatRepository.findByPetType(any(PetType.class))).thenReturn(Optional.of(volunteerChat));

        CatCarer catCarer = new CatCarer();
        catCarer.setId(1L);
        catCarer.setChatId(12345L);
        catCarer.setFullName("Иванов Иван Иванович");
        catCarer.setPhoneNumber("+7(111)1234567");

        CatAgreement catAgreement = new CatAgreement();
        catAgreement.setCarer(catCarer);
        catAgreement.setNumber("K-2023/1");
        catAgreement.setConclusionDate(LocalDate.now());
        List<Agreement> catAgreements = List.of(catAgreement);

        when(agreementService.findAgreementsWithEndingProbation(eq(PetType.CAT))).thenReturn(catAgreements);

        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Продлить испытательный срок на 14 дней")
                        .callbackData(Command.EXTEND_PROBATION_14_COMMAND.getCommand() + "/1"),
                new InlineKeyboardButton("Продлить испытательный срок на 30 дней")
                        .callbackData(Command.EXTEND_PROBATION_30_COMMAND.getCommand() + "/1"),
                new InlineKeyboardButton("Испытательный срок пройден")
                        .callbackData(Command.PROBATION_PASSED_COMMAND.getCommand() + "/1"),
                new InlineKeyboardButton("Испытательный срок НЕ пройден")
                        .callbackData(Command.PROBATION_NOT_PASSED_COMMAND.getCommand() + "/1")
        ));

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        timer.notifyForVolunteerThatProbationIsEnded();

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(98765L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("У опекуна Иванов Иван Иванович, телефон +7(111)1234567" +
                ", договор K-2023/1 от " + LocalDate.now() + " испытательный срок заканчивается через 2 дня. " +
                "Прошу принять решение о продлении испытательного срока");
        Assertions.assertThat(actual.getParameters().get("reply_markup")).isEqualTo(keyboard);
    }

    @Test
    void handleVolunteerNotifiedForCheckDailyReports() {
        VolunteerChat volunteerChat = new VolunteerChat();
        volunteerChat.setTelegramChatId(98765L);
        volunteerChat.setPetType(PetType.CAT);
        when(volunteerChatRepository.findByPetType(any(PetType.class))).thenReturn(Optional.of(volunteerChat));

        InlineKeyboardButton button = new InlineKeyboardButton("Уведомить опекунов")
                .callbackData(Command.NOTIFY_CARERS_COMMAND.getCommand());

        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(button));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);

        timer.sendNotificationForCheckDailyReports();

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(3)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(98765L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Уважаемые волонтеры! Пожалуйста, просмотрите все присланные отчеты за сегодняшнее число. " +
                "При необходимости уведомить опекунов о более ответственном заполнение отчета, нажмите на кнопку.");
        Assertions.assertThat(actual.getParameters().get("reply_markup")).isEqualTo(keyboard);
    }
}