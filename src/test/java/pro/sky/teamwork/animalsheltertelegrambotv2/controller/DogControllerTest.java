package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.mockito.ArgumentMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.PetRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.PetService;
import pro.sky.teamwork.animalsheltertelegrambotv2.controller.PetController;
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

import java.time.LocalDate;
import java.time.Month;
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

@WebMvcTest(controllers = PetController.class)
public class DogControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PetService petService;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPostDog() throws Exception {

        PetRecord dogTest = new PetRecord();
        dogTest.setId(1);
        dogTest.setPetType(PetType.DOG);
        dogTest.setName("Тузик");
        dogTest.setBreed("Двортерьер");
        dogTest.setCoatColor("Черный");
        dogTest.setAge(15);
        dogTest.setFeatures("Носится шо больной");

        when(petService.addPet(ArgumentMatchers.any()))
                .thenReturn(dogTest);

        String json = mapper.writeValueAsString(dogTest);

        mockMvc.perform(
                post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.petType", Matchers.equalTo("собака")))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Тузик")))
                .andExpect(jsonPath("$.breed", Matchers.equalTo("Двортерьер")))
                .andExpect(jsonPath("$.coatColor", Matchers.equalTo("Черный")))
                .andExpect(jsonPath("$.age", Matchers.equalTo(15)))
                .andExpect(jsonPath("$.features", Matchers.equalTo("Носится шо больной")));
    }

    @Test
    public void testPutDog() throws Exception {
        PetRecord dogTest = new PetRecord();

        dogTest.setId(0);
        dogTest.setPetType(PetType.DOG);
        dogTest.setName("Тузик");
        dogTest.setBreed("Двортерьер");
        dogTest.setCoatColor("Черный");
        dogTest.setAge(15);
        dogTest.setFeatures("Носится шо больной");

        when(petService.editPet(ArgumentMatchers.any()))
                .thenReturn(dogTest);

        String json = mapper.writeValueAsString(dogTest);

        mockMvc.perform(put("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.petType", Matchers.equalTo("собака")))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Тузик")))
                .andExpect(jsonPath("$.breed", Matchers.equalTo("Двортерьер")))
                .andExpect(jsonPath("$.coatColor", Matchers.equalTo("Черный")))
                .andExpect(jsonPath("$.age", Matchers.equalTo(15)))
                .andExpect(jsonPath("$.features", Matchers.equalTo("Носится шо больной")));
    }

    @Test
    void testDeleteDog() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                        delete("/pet/" + 1 + "?petType=DOG" ))
                .andExpect(status().isOk());
        verify(petService).deletePet(1L, PetType.DOG);
    }

    @Test
    void testGetDog() throws Exception {
        mockMvc.perform(
                        get("/pet/" + 1 + "?petType=DOG" ))
                        get("/dog/{id}", 1))
                .andExpect(status().isOk());
        verify(dogService).findDog(1L);
    }

    @Test
    void testGetAllDogs() throws Exception {
        mockMvc.perform(
                        get("/dog"))
                .andExpect(status().isOk());
        verify(petService).findPet(1L, PetType.DOG);
    }
        verify(dogService).findAllDogs();
    }

    @Test
    void testPatchChangeOnProbationStatus() throws Exception {

        mockMvc.perform(
                        patch("/dog/on-probation/{id}",1)
                                .param("id", "1")
                                .param("onProbation", "true")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dogService).changeOnProbationStatus(1L, true);

    }

    @Test
    void testPatchChangeIsTakenStatus() throws Exception {

        mockMvc.perform(
                        patch("/dog/is-taken/{id}",1)
                                .param("id", "1")
                                .param("isTaken", "true")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dogService).changeIsTakenStatus(1L, true);

    }
}
