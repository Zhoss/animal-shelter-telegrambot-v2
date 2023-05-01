package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.DailyReportService;

import java.time.LocalDate;
import java.time.Month;

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


    @Test
    void testGetFindDailyReportsByCarerId() throws Exception {

//CAT
        mockMvc.perform(
                        get("/reports/carer?petType=CAT")
                                .param("Идентификатор опекуна","1")
                                .param("petType", "CAT")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dailyReportService).findDailyReportsByCarer(1L, PetType.CAT);

//DOG
        mockMvc.perform(
                        get("/reports/carer?petType=DOG")
                                .param("Идентификатор опекуна","1")
                                .param("petType", "DOG")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dailyReportService).findDailyReportsByCarer(1L, PetType.DOG);
    }

    @Test
    void testGetFindDailyReportsByCarerAndDate() throws Exception {

//CAT
        mockMvc.perform(
                        get("/reports/carer-date?petType=CAT")
                                .param("carerId", "1")
                                .param("reportDate", "2023-04-22")
                                .param("petType","CAT")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dailyReportService).findDailyReportByCarerAndDate(1L, LocalDate.of(2023, Month.APRIL, 22),PetType.CAT);

//DOG
        mockMvc.perform(
                        get("/reports/carer-date?petType=DOG")
                                .param("carerId", "1")
                                .param("reportDate", "2023-04-22")
                                .param("petType","DOG")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dailyReportService).findDailyReportByCarerAndDate(1L, LocalDate.of(2023, Month.APRIL, 22),PetType.DOG);

    }

    @Test
    void testGetDailyReportsByDate() throws Exception {

//CAT
        mockMvc.perform(
                        get("/reports/date?petType=CAT")
                                .param("Дата отчета","2023-04-22")
                                .param("petType","CAT")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dailyReportService).findDailyReportsByDate(LocalDate.of(2023, Month.APRIL, 22),PetType.CAT);

//DOG
        mockMvc.perform(
                        get("/reports/date?petType=DOG")
                                .param("Дата отчета","2023-04-22")
                                .param("petType","DOG")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dailyReportService).findDailyReportsByDate(LocalDate.of(2023, Month.APRIL, 22),PetType.DOG);
    }

    @Test
    void testDeleteDailyRepor() throws Exception {

//CAT
        mockMvc.perform(
                        delete("/reports/{id}",1)
                                .param("petType", "CAT"))
                .andExpect(status().isOk());
        verify(dailyReportService).deleteDailyReport(1L,PetType.CAT);

//DOG
        mockMvc.perform(
                        delete("/reports/{id}",1)
                                .param("petType", "DOG"))
                .andExpect(status().isOk());
        verify(dailyReportService).deleteDailyReport(1L,PetType.DOG);
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













