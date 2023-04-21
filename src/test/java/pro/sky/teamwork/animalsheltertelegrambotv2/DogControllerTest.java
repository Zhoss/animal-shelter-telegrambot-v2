//package pro.sky.teamwork.animalsheltertelegrambotv2;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.hamcrest.Matchers;
//import org.mockito.ArgumentMatchers;
//import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord;
//import pro.sky.teamwork.animalsheltertelegrambotv2.service.DogService;
//import pro.sky.teamwork.animalsheltertelegrambotv2.controller.DogController;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = DogController.class)
//public class DogControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private DogService dogService;
//    private static ObjectMapper mapper = new ObjectMapper();
//
//
//    /*@Test
//    public void testGetDog() throws Exception {  // метод GET #1
//        List<DogRecord> dogs = new ArrayList();
//
//        DogRecord dogTest = new DogRecord();
//
//        dogTest.setId(1);
//        dogTest.setName("тузик");
//        dogTest.setBreed("Двортерьер");
//        dogTest.setCoatColor("Черный");
//        dogTest.setAge(15);
//        dogTest.setFeatures("Носится шо больной");
//
//        dogs.add(dogTest);
//
//        when(dogService.findDog(dogTest.getId()))
//                .thenReturn(dogTest);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/dog/{id}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
//                .andExpect(jsonPath("$.name", Matchers.equalTo("тузик")))
//                .andExpect(jsonPath("$.breed", Matchers.equalTo("Двортерьер")))
//                .andExpect(jsonPath("$.coatColor", Matchers.equalTo("Черный")))
//                .andExpect(jsonPath("$.age", Matchers.equalTo(15)))
//                .andExpect(jsonPath("$.features", Matchers.equalTo("Носится шо больной")));
//    }*/
//    @Test
//    public void testPostDog() throws Exception {
//
//        DogRecord dogTest = new DogRecord();
//        dogTest.setId(1);
//        dogTest.setName("Тузик");
//        dogTest.setBreed("Двортерьер");
//        dogTest.setCoatColor("Черный");
//        dogTest.setAge(15);
//        dogTest.setFeatures("Носится шо больной");
//
//        when(dogService.addDog(ArgumentMatchers.any()))
//                .thenReturn(dogTest);
//
//        String json = mapper.writeValueAsString(dogTest);
//
//        mockMvc.perform(
//                post("/dog")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("utf-8")
//                        .content(json).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
//                .andExpect(jsonPath("$.name", Matchers.equalTo("Тузик")))
//                .andExpect(jsonPath("$.breed", Matchers.equalTo("Двортерьер")))
//                .andExpect(jsonPath("$.coatColor", Matchers.equalTo("Черный")))
//                .andExpect(jsonPath("$.age", Matchers.equalTo(15)))
//                .andExpect(jsonPath("$.features", Matchers.equalTo("Носится шо больной")));
//    }
//
//    @Test
//    public void testPutDog() throws Exception {
//        DogRecord dogTest = new DogRecord();
//
//        dogTest.setId(0);
//        dogTest.setName("Тузик");
//        dogTest.setBreed("Двортерьер");
//        dogTest.setCoatColor("Черный");
//        dogTest.setAge(15);
//        dogTest.setFeatures("Носится шо больной");
//
//        when(dogService.editDog(ArgumentMatchers.any()))
//                .thenReturn(dogTest);
//
//        String json = mapper.writeValueAsString(dogTest);
//
//        mockMvc.perform(put("/dog")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("utf-8")
//                        .content(json)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", Matchers.equalTo(0)))
//                .andExpect(jsonPath("$.name", Matchers.equalTo("Тузик")))
//                .andExpect(jsonPath("$.breed", Matchers.equalTo("Двортерьер")))
//                .andExpect(jsonPath("$.coatColor", Matchers.equalTo("Черный")))
//                .andExpect(jsonPath("$.age", Matchers.equalTo(15)))
//                .andExpect(jsonPath("$.features", Matchers.equalTo("Носится шо больной")));
//    }
//    @Test
//    void testDeleteDog() throws Exception {
//        mockMvc.perform(
//                        delete("/dog/{id}", 1))
//                .andExpect(status().isOk());
//        verify(dogService).deleteDog(1L);
//    }
//
//    @Test
//    void testGetDog() throws Exception { // метод GET #4 валидный
//        mockMvc.perform(
//                        get("/dog/{id}", 1))
//                .andExpect(status().isOk());
//        verify(dogService).findDog(1L);
//    }
//
//    /*@Test // метод GET #2
//    void testGetDog2() throws Exception {
//        DogRecord dogTest = new DogRecord();
//        when(dogService.getAllDogs()).thenReturn(List.of(new Dog()));
//
//        mockMvc.perform(
//                        get("/dog"))
//                .andExpect(status().isOk());
//    }*/
//
//   /* @Test // метод GET #3
//    public void testGetDog3() throws Exception {
//        List dogs = new ArrayList<>(Arrays.asList());
//
//        Mockito.when(dogService.getAllDogs()).thenReturn(dogs);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/dog")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(6)))
//                .andExpect(jsonPath("$[1].id", Matchers.equalTo(0)))
//                .andExpect(jsonPath("$[2].name", Matchers.equalTo("Тузик")))
//                .andExpect(jsonPath("$[3].breed", Matchers.equalTo("Двортерьер")))
//                .andExpect(jsonPath("$[4].coatColor", Matchers.equalTo("Черный")))
//                .andExpect(jsonPath("$[5].age", Matchers.equalTo(15)))
//                .andExpect(jsonPath("$[6].features", Matchers.equalTo("Носится шо больной")));
//    }*/
//}
