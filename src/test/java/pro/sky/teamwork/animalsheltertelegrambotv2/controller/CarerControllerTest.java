package pro.sky.teamwork.animalsheltertelegrambotv2.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.CarerRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.CarerService;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CarerController.class)
public class CarerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarerService carerService;
    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    void testGetCarerById() throws Exception {
        mockMvc.perform(
                        get("/carer/{id}", 1))
                .andExpect(status().isOk());
        verify(carerService).findCarer(1L);
    }

    @Test
    public void testPatchCarer() throws Exception {

        JSONObject carerObject = new JSONObject();
        carerObject.put("id", 1);
        carerObject.put("secondName", "Иванов").toString();
        carerObject.put("firstName", "Иван");
        carerObject.put("patronymic", "Иванович");
        carerObject.put("age", 30);
        carerObject.put("phoneNumber", "+79881234567");
        carerObject.put("passportNumber", "1234 123456");
        carerObject.put("dogId", 1);

        CarerRecord carerTest = new CarerRecord();
        carerTest.setId(1);
        carerTest.setSecondName("Иванов");
        carerTest.setFirstName("Иван");
        carerTest.setPatronymic("Иванович");
        carerTest.setAge(30);
        carerTest.setPhoneNumber("+79881234567");
        carerTest.setPassportNumber("1234 123456");
        carerTest.setDogId(1);

        when(carerService.editCarer(ArgumentMatchers.any())).thenReturn(carerTest);


        mockMvc.perform(patch("/carer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(carerObject.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.secondName", Matchers.equalTo("Иванов")))
                .andExpect(jsonPath("$.firstName", Matchers.equalTo("Иван")))
                .andExpect(jsonPath("$.patronymic", Matchers.equalTo("Иванович")))
                .andExpect(jsonPath("$.age", Matchers.equalTo(30)))
                .andExpect(jsonPath("$.phoneNumber", Matchers.equalTo("+79881234567")))
                .andExpect(jsonPath("$.passportNumber", Matchers.equalTo("1234 123456")))
                .andExpect(jsonPath("$.dogId", Matchers.equalTo(1)));
    }

    @Test
    void testDeleteCarer() throws Exception {

        mockMvc.perform(
                        delete("/carer/{id}", 1))
                .andExpect(status().isOk());
        verify(carerService).deleteCarer(1L);
    }

    @Test
    void testGetCarerByPhoneNumber() throws Exception {

              mockMvc.perform(
                        get("/carer/phone-number?phoneNumber=+7(123)1234567")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(carerService).findCarerByPhoneNumber("+7(123)1234567");
    }

    @Test
    void testGetAllCarer() throws Exception {
        mockMvc.perform(
                        get("/carer"))
                .andExpect(status().isOk());
        verify(carerService).findAllCarers();
    }
}
