package pro.sky.teamwork.animalsheltertelegrambotv2.controller;


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
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;
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

    @Test
    void testGetFindCarer() throws Exception {

//CAT
        mockMvc.perform(
                        get("/carer/{id}",1)
                                .param("id", "1")
                                .param("petType", "CAT"))
                .andExpect(status().isOk());
        verify(carerService).findCarer(1L, PetType.CAT);

//DOG
        mockMvc.perform(
                        get("/carer/{id}",1)
                                .param("id", "1")
                                .param("petType", "DOG"))
                .andExpect(status().isOk());
        verify(carerService).findCarer(1L, PetType.DOG);
    }

    @Test
    public void testPatchEditCarer() throws Exception {

        JSONObject carerObject = new JSONObject();
        carerObject.put("id", 1);
        carerObject.put("secondName", "Иванов").toString();
        carerObject.put("firstName", "Иван");
        carerObject.put("patronymic", "Иванович");
        carerObject.put("age", 30);
        carerObject.put("phoneNumber", "+79881234567");
        carerObject.put("passportNumber", "1234 123456");

        CarerRecord carerTest = new CarerRecord();
        carerTest.setId(1);
        carerTest.setSecondName("Иванов");
        carerTest.setFirstName("Иван");
        carerTest.setPatronymic("Иванович");
        carerTest.setAge(30);
        carerTest.setPhoneNumber("+79881234567");
        carerTest.setPassportNumber("1234 123456");

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
                .andExpect(jsonPath("$.passportNumber", Matchers.equalTo("1234 123456")));
    }

    @Test
    void testDeleteCarer() throws Exception {

//CAT
        mockMvc.perform(
                        delete("/carer/{id}",1)
                                .param("id", "1")
                                .param("petType", "CAT"))
                .andExpect(status().isOk());
        verify(carerService).deleteCarer(1L, PetType.CAT);

//DOG
        mockMvc.perform(
                        delete("/carer/{id}",1)
                                .param("id", "1")
                                .param("petType", "DOG"))
                .andExpect(status().isOk());
        verify(carerService).deleteCarer(1L, PetType.DOG);
    }

    @Test
    void testGetFindCarerByPhoneNumber() throws Exception {

//CAT
        mockMvc.perform(
                        get("/carer/phone-number")
                                .param("phone","+7(123)1234567")
                                .param("petType", "CAT")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(carerService).findCarerByPhoneNumber("+7(123)1234567",PetType.CAT);

//DOG
        mockMvc.perform(
                        get("/carer/phone-number")
                                .param("phone","+7(123)1234567")
                                .param("petType", "DOG")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(carerService).findCarerByPhoneNumber("+7(123)1234567",PetType.DOG);
    }

    @Test
    void testGetFindAllCarers() throws Exception {

//CAT
        mockMvc.perform(
                        get("/carer")
                                .param("petType","CAT"))
                .andExpect(status().isOk());
        verify(carerService).findAllCarers(PetType.CAT);

//DOG
        mockMvc.perform(
                        get("/carer")
                                .param("petType","DOG"))
                .andExpect(status().isOk());
        verify(carerService).findAllCarers(PetType.DOG);
    }
}
