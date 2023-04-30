package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.mockito.ArgumentMatchers;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.AgreementRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.AgreementService;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AgreementController.class)
public class AgreementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AgreementService agreementService;

    @Test
    void testGetAgreement() throws Exception {

//CAT
        mockMvc.perform(
                        get("/agreement/{id}",1)
                                .param("id", "1")
                                .param("petType", "CAT"))
                .andExpect(status().isOk());
        verify(agreementService).findAgreementById(1L,PetType.CAT);
//DOG
        mockMvc.perform(
                        get("/agreement/{id}",1)
                                .param("id", "1")
                                .param("petType", "DOG"))
                .andExpect(status().isOk());
        verify(agreementService).findAgreementById(1L,PetType.DOG);
    }

    @Test
    void testGetAllAgreements() throws Exception {

//CAT
        mockMvc.perform(get("/agreement?petType=CAT"))
                .andExpect(status().isOk());
        verify(agreementService).findAllAgreements(PetType.CAT);

//DOG
        mockMvc.perform(get("/agreement?petType=DOG"))
                .andExpect(status().isOk());
        verify(agreementService).findAllAgreements(PetType.DOG);
    }

    @Test
    public void testPostAgreement() throws Exception {

        JSONObject agreementObject = new JSONObject();
        agreementObject.put("id", 1);
        agreementObject.put("number", "00000001");
        agreementObject.put("сonclusionDate", "2023-04-19");
        agreementObject.put("carerId", 1);

        AgreementRecord AgreementTest = new AgreementRecord();
        AgreementTest.setId(1);
        AgreementTest.setNumber("000000001");
        AgreementTest.setConclusionDate(LocalDate.of(2023, Month.APRIL, 19));
        AgreementTest.setCarerId(1);

        when(agreementService.addAgreement(ArgumentMatchers.any()))
                .thenReturn(AgreementTest);

        mockMvc.perform(
                        post("/agreement")

                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                                .content(agreementObject.toString())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.number", Matchers.equalTo("000000001")))
                .andExpect(jsonPath("$.conclusionDate", Matchers.equalTo("2023-04-19")))
                .andExpect(jsonPath("$.carerId", Matchers.equalTo(1)));
    }

    @Test
    public void testPutAgreement() throws Exception {

        JSONObject agreementObject = new JSONObject();
        agreementObject.put("id", 1);
        agreementObject.put("number", "00000001");
        agreementObject.put("сonclusionDate", "2023-04-19");
        agreementObject.put("carerId", 1);

        AgreementRecord AgreementTest = new AgreementRecord();
        AgreementTest.setId(1);
        AgreementTest.setNumber("000000001");
        AgreementTest.setConclusionDate(LocalDate.of(2023, Month.APRIL, 19));
        AgreementTest.setCarerId(1);

        when(agreementService.editAgreement(ArgumentMatchers.any()))
                .thenReturn(AgreementTest);


        mockMvc.perform(put("/agreement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(agreementObject.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.number", Matchers.equalTo("000000001")))
                .andExpect(jsonPath("$.conclusionDate", Matchers.equalTo("2023-04-19")))
                .andExpect(jsonPath("$.carerId", Matchers.equalTo(1)));
    }

    @Test
    void testDeleteAgreement() throws Exception {

//CAT
        mockMvc.perform(
                        delete("/agreement/{id}",1)
                .param("id", "1")
                .param("petType", "CAT"))
                .andExpect(status().isOk());
        verify(agreementService).deleteAgreement(1L, PetType.CAT);

//DOG
        mockMvc.perform(
                        delete("/agreement/{id}",1)
                                .param("id", "1")
                                .param("petType", "DOG"))
                .andExpect(status().isOk());
        verify(agreementService).deleteAgreement(1L, PetType.DOG);
    }
    @Test
    void testPatchChangeProbationEndData() throws Exception {

//CAT
        mockMvc.perform(
                        patch("/agreement/{id}",1)
                .param("id", "1")
                .param("localDate", "2023-04-22")
                .param("petType", "CAT"))
                .andExpect(status().isOk());
        verify(agreementService).changeProbationEndData(1L,LocalDate.of(2023, Month.APRIL, 22), PetType.CAT);

//DOG
        mockMvc.perform(
                        patch("/agreement/{id}",1)
                                .param("id", "1")
                                .param("localDate", "2023-04-22")
                                .param("petType", "DOG"))
                .andExpect(status().isOk());
        verify(agreementService).changeProbationEndData(1L,LocalDate.of(2023, Month.APRIL, 22), PetType.DOG);
    }
}
