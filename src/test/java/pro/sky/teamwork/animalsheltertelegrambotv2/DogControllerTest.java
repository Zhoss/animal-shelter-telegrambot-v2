package pro.sky.teamwork.animalsheltertelegrambotv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.mockito.ArgumentMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.PetRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.PetService;
import pro.sky.teamwork.animalsheltertelegrambotv2.controller.PetController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PetController.class)
public class DogControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PetService petService;
    private static ObjectMapper mapper = new ObjectMapper();


    /*@Test
    public void testGetDog() throws Exception {  // метод GET #1
        List<DogRecord> dogs = new ArrayList();

        DogRecord dogTest = new DogRecord();

        dogTest.setId(1);
        dogTest.setName("тузик");
        dogTest.setBreed("Двортерьер");
        dogTest.setCoatColor("Черный");
        dogTest.setAge(15);
        dogTest.setFeatures("Носится шо больной");

        dogs.add(dogTest);

        when(dogService.findDog(dogTest.getId()))
                .thenReturn(dogTest);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dog/{id}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("тузик")))
                .andExpect(jsonPath("$.breed", Matchers.equalTo("Двортерьер")))
                .andExpect(jsonPath("$.coatColor", Matchers.equalTo("Черный")))
                .andExpect(jsonPath("$.age", Matchers.equalTo(15)))
                .andExpect(jsonPath("$.features", Matchers.equalTo("Носится шо больной")));
    }*/
    @Test
    public void testPostDog() throws Exception {

        PetRecord dogTest = new PetRecord();
        dogTest.setId(1);
        dogTest.setPetType("собака");
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
        dogTest.setPetType("собака");
        dogTest.setName("Тузик");
        dogTest.setBreed("Двортерьер");
        dogTest.setCoatColor("Черный");
        dogTest.setAge(15);
        dogTest.setFeatures("Носится шо больной");

        when(petService.editDog(ArgumentMatchers.any()))
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
                        delete("/pet/" + 1 + "?petType=собака" ))
                .andExpect(status().isOk());
        verify(petService).deletePet(1L, "собака");
    }

    @Test
    void testGetDog() throws Exception { // метод GET #4 валидный
        mockMvc.perform(
                        get("/pet/" + 1 + "?petType=собака" ))
                .andExpect(status().isOk());
        verify(petService).findPet(1L, "собака");
    }

    /*@Test // метод GET #2
    void testGetDog2() throws Exception {
        DogRecord dogTest = new DogRecord();
        when(dogService.getAllDogs()).thenReturn(List.of(new Dog()));

        mockMvc.perform(
                        get("/dog"))
                .andExpect(status().isOk());
    }*/

   /* @Test // метод GET #3
    public void testGetDog3() throws Exception {
        List dogs = new ArrayList<>(Arrays.asList());

        Mockito.when(dogService.getAllDogs()).thenReturn(dogs);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dog")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[1].id", Matchers.equalTo(0)))
                .andExpect(jsonPath("$[2].name", Matchers.equalTo("Тузик")))
                .andExpect(jsonPath("$[3].breed", Matchers.equalTo("Двортерьер")))
                .andExpect(jsonPath("$[4].coatColor", Matchers.equalTo("Черный")))
                .andExpect(jsonPath("$[5].age", Matchers.equalTo(15)))
                .andExpect(jsonPath("$[6].features", Matchers.equalTo("Носится шо больной")));
    }*/
}
