package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.mockito.ArgumentMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.PetRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.PetService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PetController.class)
public class PetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PetService petService;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPostAddPet() throws Exception {

//DOG
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

        String jsonDog = mapper.writeValueAsString(dogTest);

        mockMvc.perform(
                        post("/pet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(jsonDog).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.petType", Matchers.equalTo("DOG")))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Тузик")))
                .andExpect(jsonPath("$.breed", Matchers.equalTo("Двортерьер")))
                .andExpect(jsonPath("$.coatColor", Matchers.equalTo("Черный")))
                .andExpect(jsonPath("$.age", Matchers.equalTo(15)))
                .andExpect(jsonPath("$.features", Matchers.equalTo("Носится шо больной")));

//CAT
        PetRecord catTest = new PetRecord();
        catTest.setId(1);
        catTest.setPetType(PetType.CAT);
        catTest.setName("Мурзик");
        catTest.setBreed("Двортерьер");
        catTest.setCoatColor("рыжий");
        catTest.setAge(5);
        catTest.setFeatures("Носится шо тыгыдым");

        when(petService.addPet(ArgumentMatchers.any()))
                .thenReturn(catTest);

        String jsonCat = mapper.writeValueAsString(catTest);

        mockMvc.perform(
                        post("/pet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(jsonCat).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.petType", Matchers.equalTo("CAT")))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Мурзик")))
                .andExpect(jsonPath("$.breed", Matchers.equalTo("Двортерьер")))
                .andExpect(jsonPath("$.coatColor", Matchers.equalTo("рыжий")))
                .andExpect(jsonPath("$.age", Matchers.equalTo(5)))
                .andExpect(jsonPath("$.features", Matchers.equalTo("Носится шо тыгыдым")));
    }

    @Test
    void testGetfindPetCat() throws Exception {

//CAT
        mockMvc.perform(
                        get("/pet/" + 1 + "?petType=CAT")
                                .param("id", "1")
                                .param("petType", "CAT"))
                .andExpect(status().isOk());
        verify(petService).findPet(1L,PetType.CAT);
//DOG
        mockMvc.perform(
                        get("/pet/" + 1 + "?petType=DOG")
                                .param("id", "1")
                                .param("petType", "DOG"))
                .andExpect(status().isOk());
        verify(petService).findPet(1L,PetType.DOG);
    }

    @Test
    public void testPutPet() throws Exception {

//DOG
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

        String jsonDog = mapper.writeValueAsString(dogTest);

        mockMvc.perform(put("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(jsonDog)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.petType", Matchers.equalTo("DOG")))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Тузик")))
                .andExpect(jsonPath("$.breed", Matchers.equalTo("Двортерьер")))
                .andExpect(jsonPath("$.coatColor", Matchers.equalTo("Черный")))
                .andExpect(jsonPath("$.age", Matchers.equalTo(15)))
                .andExpect(jsonPath("$.features", Matchers.equalTo("Носится шо больной")));
//CAT
        PetRecord catTest = new PetRecord();

        catTest.setId(0);
        catTest.setPetType(PetType.CAT);
        catTest.setName("Мурзик");
        catTest.setBreed("Двортерьер");
        catTest.setCoatColor("Рыжий");
        catTest.setAge(5);
        catTest.setFeatures("Носится шо тыгыдым");

        when(petService.editPet(ArgumentMatchers.any()))
                .thenReturn(catTest);

        String jsonCat = mapper.writeValueAsString(catTest);

        mockMvc.perform(put("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(jsonCat)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.petType", Matchers.equalTo("CAT")))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Мурзик")))
                .andExpect(jsonPath("$.breed", Matchers.equalTo("Двортерьер")))
                .andExpect(jsonPath("$.coatColor", Matchers.equalTo("Рыжий")))
                .andExpect(jsonPath("$.age", Matchers.equalTo(5)))
                .andExpect(jsonPath("$.features", Matchers.equalTo("Носится шо тыгыдым")));
    }

    @Test
    void testDeletePet() throws Exception {

//DOG
        mockMvc.perform(MockMvcRequestBuilders.
                        delete("/pet/" + 1 + "?petType=DOG"))
                .andExpect(status().isOk());
        verify(petService).deletePet(1L, PetType.DOG);
//CAT
        mockMvc.perform(MockMvcRequestBuilders.
                        delete("/pet/" + 1 + "?petType=CAT"))
                .andExpect(status().isOk());
        verify(petService).deletePet(1L, PetType.CAT);
    }


    @Test
    void testGetFindAllPets() throws Exception {

//DOG
        mockMvc.perform(
                        get("/pet/" + 1 + "?petType=DOG"))
                .andExpect(status().isOk());
        verify(petService).findPet(1L, PetType.DOG);
//CAT
        mockMvc.perform(
                        get("/pet/" + 1 + "?petType=CAT"))
                .andExpect(status().isOk());
        verify(petService).findPet(1L, PetType.CAT);
    }

    @Test
    void testPatchChangeIsTakenStatus() throws Exception {

//CAT
        mockMvc.perform(
                        patch("/pet/is-taken/{id}",1)
                                .param("id", "1")
                                .param("petType", "CAT")
                                .param("isTaken", "true")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(petService).changeIsTakenStatus(1L,PetType.CAT, true);
//DOG
        mockMvc.perform(
                        patch("/pet/is-taken/{id}",1)
                                .param("id", "1")
                                .param("petType", "DOG")
                                .param("isTaken", "true")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(petService).changeIsTakenStatus(1L,PetType.DOG, true);

    }

    @Test
    void testPatchChangeOnProbationStatus() throws Exception {

//CAT
        mockMvc.perform(
                        patch("/pet/on-probation/{id}",1)
                                .param("id", "1")
                                .param("petType", "CAT")
                                .param("onProbation", "true")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(petService).changeOnProbationStatus(1L,PetType.CAT, true);
//DOG
        mockMvc.perform(
                        patch("/pet/on-probation/{id}",1)
                                .param("id", "1")
                                .param("petType", "DOG")
                                .param("onProbation", "true")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(petService).changeOnProbationStatus(1L,PetType.DOG, true);
    }
}
