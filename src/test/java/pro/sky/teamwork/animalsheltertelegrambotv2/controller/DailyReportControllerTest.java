package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.DailyReportService;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DailyReportController.class)
public class DailyReportControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DailyReportService dailyReportService;
    private static ObjectMapper mapper = new ObjectMapper();


    @Test
    void testDeleteDailyRepor() throws Exception {
        mockMvc.perform(
                        delete("/reports/{id}", 1))
                .andExpect(status().isOk());
        verify(dailyReportService).deleteDailyReport(1L);
    }

    @Test
    void testGetDailyReportsByDate() throws Exception {

        mockMvc.perform(
                        get("/reports/date?Дата отчета=2023-04-22")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dailyReportService).findDailyReportsByDate(LocalDate.of(2023, Month.APRIL, 22));
    }

    @Test
    void testGetDailyReportsCarerById() throws Exception {

        mockMvc.perform(
                        get("/reports/carer?Идентификатор опекуна=1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dailyReportService).findDailyReportsByCarer(1L);
    }

    @Test
    void testGetFindDailyReportsByCarerAndDate() throws Exception {

        mockMvc.perform(
                        get("/reports/carer-date")
                                .param("carerId", "1")
                                .param("reportDate", "2023-04-22")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dailyReportService).findDailyReportByCarerAndDate(1L, LocalDate.of(2023, Month.APRIL, 22));

    }

}

    /*@Test
    void testGetDownloadPhotoByByCarerIdAndDate() throws Exception {

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        PhotoSize photoSize = mock(PhotoSize.class);
        when(photoSize.fileId()).thenReturn("123456");
        when(chat.id()).thenReturn(123L);
        when(message.chat()).thenReturn(chat);
        when(message.photo()).thenReturn(new PhotoSize[]{photoSize});
        when(update.message()).thenReturn(message);

        GetFileResponse getFileResponse = mock(GetFileResponse.class);
        File file = mock(File.class);
        when(getFileResponse.file()).thenReturn(file);
        when(file.fileSize()).thenReturn(111111L);
        AtomicReference<SendMessage> atomicReference = new AtomicReference<>();
        when(telegramBot.execute(any()))
                .thenAnswer(
                        invocation -> {
                            Object request = invocation.getArgument(0);
                            if(request instanceof GetFile) {
                                return getFileResponse;
                            }else if (request instanceof SendMessage) {
                                atomicReference.set((SendMessage) request);
                                return null;
                            }
                            return null;
                        });

        botComponent.process(List.of(update));
        Assertion.assertEquals(
              atomicReference.get().getParameters().get("test"),"PHOTO ACCEPTED");*/













