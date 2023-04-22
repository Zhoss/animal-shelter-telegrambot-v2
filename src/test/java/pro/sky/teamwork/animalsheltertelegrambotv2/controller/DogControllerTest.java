package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.mockito.ArgumentMatchers;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.AgreementRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.DogService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.ModelMapper;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.postgresql.hostchooser.HostRequirement.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DogController.class)
public class DogControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DogService dogService;
    private static ObjectMapper mapper = new ObjectMapper();


    @Test
    public void testPostDog() throws Exception {

        DogRecord dogTest = new DogRecord();
        dogTest.setId(1);
        dogTest.setName("Тузик");
        dogTest.setBreed("Двортерьер");
        dogTest.setCoatColor("Черный");
        dogTest.setAge(15);
        dogTest.setFeatures("Носится шо больной");

        when(dogService.addDog(ArgumentMatchers.any()))
                .thenReturn(dogTest);

        String json = mapper.writeValueAsString(dogTest);

        mockMvc.perform(
                        post("/dog")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(json)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Тузик")))
                .andExpect(jsonPath("$.breed", Matchers.equalTo("Двортерьер")))
                .andExpect(jsonPath("$.coatColor", Matchers.equalTo("Черный")))
                .andExpect(jsonPath("$.age", Matchers.equalTo(15)))
                .andExpect(jsonPath("$.features", Matchers.equalTo("Носится шо больной")));
    }

    @Test
    public void testPutDog() throws Exception {
        DogRecord dogTest = new DogRecord();

        dogTest.setId(0);
        dogTest.setName("Тузик");
        dogTest.setBreed("Двортерьер");
        dogTest.setCoatColor("Черный");
        dogTest.setAge(15);
        dogTest.setFeatures("Носится шо больной");

        when(dogService.editDog(ArgumentMatchers.any()))
                .thenReturn(dogTest);

        String json = mapper.writeValueAsString(dogTest);

        mockMvc.perform(put("/dog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Тузик")))
                .andExpect(jsonPath("$.breed", Matchers.equalTo("Двортерьер")))
                .andExpect(jsonPath("$.coatColor", Matchers.equalTo("Черный")))
                .andExpect(jsonPath("$.age", Matchers.equalTo(15)))
                .andExpect(jsonPath("$.features", Matchers.equalTo("Носится шо больной")));
    }

    @Test
    void testDeleteDog() throws Exception {
        mockMvc.perform(
                        delete("/dog/{id}", 1))
                .andExpect(status().isOk());
        verify(dogService).deleteDog(1L);
    }

    @Test
    void testGetDog() throws Exception {
        mockMvc.perform(
                        get("/dog/{id}", 1))
                .andExpect(status().isOk());
        verify(dogService).findDog(1L);
    }

    @Test
    void testGetAllDogs() throws Exception {
        mockMvc.perform(
                        get("/dog"))
                .andExpect(status().isOk());
        verify(dogService).findAllDogs();
    }

    /*@Test
    void testPatchChangeIsTakenStatusTrue() throws Exception {
       /*HashMap<Long, Boolean> updates = new HashMap<>();

        updates.put(1L,false);

        mockMvc.perform(
                        patch("/is-taken/{id}",1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .characterEncoding("utf-8")
                                .content(mapper.writeValueAsString(updates))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.isTaken", Matchers.equalTo(true)));
        verify(dogService).changeIsTakenStatus(1L, true);*/
       /* boolean oldIsTaken = true;
        boolean newIsTaken ;

        Dog testDog = new Dog();

        newIsTaken = testDog.isTaken();

        when(dogService.changeIsTakenStatus(ArgumentMatchers.eq()))
                .thenReturn(testDog);
        assertEquals(newIsTaken, oldIsTaken);

        mockMvc.perform(
                        patch("/dog/is-taken/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(0)));
        verify(dogService).changeOnProbationStatus(1, true);*/
//бооооль!!!
       /* JSONObject dogObject = new JSONObject();
        dogObject.put("id", 1);
        dogObject.put("isTaken", true);

        when(dogService.changeIsTakenStatus(ArgumentMatchers.any()).thenReturn(dogObject);

        mockMvc.perform(
                        patch("/dog/is-taken/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(dogObject.toString())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(dogService).changeIsTakenStatus(1, true);*/
      /*  Dog dogTest = new Dog();

        dogTest.setId(1);
        dogTest.setTaken(true);

        when(dogService.changeIsTakenStatus()).thenReturn(dogTest);


        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/dog/is-taken/{id}")
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .accept(MediaType.APPLICATION_XML)
                        .characterEncoding("UTF-8")
                        .content(dogTest);
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Article created."))
                .andDo(MockMvcResultHandlers.print());


        mockMvc.perform(patch("/heavyrecource/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(dogService.changeIsTakenStatus(dogTest.getId(), dogTest.isTaken())
    (dogTest))).andExpect(status().isOk());



    }*/

}
