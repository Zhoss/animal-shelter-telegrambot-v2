package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DailyReportRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.DailyReportService;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

        JSONObject agreementObject = new JSONObject();
        agreementObject.put("reportDat",LocalDate.of(2023, Month.APRIL, 22));

        DailyReportRecord test = new DailyReportRecord();
        test.setReportDate(LocalDate.of(2023, Month.APRIL, 22));

        when(dailyReportService.findDailyReportsByDate(ArgumentMatchers.any()))
                .thenReturn(Collections.singletonList(test));

        mockMvc.perform(
                        get("/reports/date", 2023-04-22)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
              //  .andExpect(jsonPath("$.reportDat", Matchers.equalTo(LocalDate.of(2023, Month.APRIL, 22))));

        verify(dailyReportService).findDailyReportsByDate(LocalDate.of(2023, Month.APRIL, 22));
    }


}
