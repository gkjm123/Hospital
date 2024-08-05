package com.example.hospital.controller.patient;

import com.example.hospital.dto.response.patient.RegistResponse;
import com.example.hospital.service.patient.RegistService;
import com.example.hospital.type.RegisterStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RegistController.class)
class RegistControllerTest {
    @MockBean
    private RegistService registService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerPatient() throws Exception {
        //given
        given(registService.register(any()))
                .willReturn(RegistResponse.builder()
                        .id(1L)
                        .doctorId(1L)
                        .patientId(1L)
                        .patientName("P")
                        .registerStatus(RegisterStatus.REGISTERED)
                        .registrationDate(LocalDateTime.now())
                        .build());

        //when

        //then
        mockMvc.perform(get("get-doctors"))
                .andExpect(jsonPath("$.id").value(1L));
    }

}