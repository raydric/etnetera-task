package com.etnetera.hr.controller;

import com.etnetera.hr.dto.FrameworkDTO;
import com.etnetera.hr.dto.FrameworkRequestDTO;
import com.etnetera.hr.exception.FrameworkNotFoundException;
import com.etnetera.hr.exception.InvalidRequestException;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(JavaScriptFrameworkController.class)
public class JavaScriptFrameworkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaScriptFrameworkService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static FrameworkRequestDTO requestDTO;
    private static FrameworkDTO frameworkDTO;
    private static FrameworkDTO frameworkDTO2;

    @BeforeClass
    public static void beforeClass() {
        requestDTO = new FrameworkRequestDTO();
        requestDTO.setName("Test framework");

        frameworkDTO = new FrameworkDTO();
        frameworkDTO.setId(1L);
        frameworkDTO.setName("Test framework");

        frameworkDTO2 = new FrameworkDTO();
        frameworkDTO2.setId(2L);
        frameworkDTO2.setName("Another framework");
    }

    @Test
    public void insert_CreateFramework_Created() throws Exception {
        when(service.insert(any(FrameworkRequestDTO.class))).thenReturn(frameworkDTO);

        mockMvc.perform(post("/frameworks")
                .content(objectMapper.writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(frameworkDTO.getName()));
    }

    @Test
    public void insert_MissingName_BadRequestStatus() throws Exception {
        when(service.insert(any(FrameworkRequestDTO.class))).thenThrow(new InvalidRequestException("Invalid request"));

        mockMvc.perform(post("/frameworks"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void edit_ExistingId_Edited() throws Exception {
        when(service.edit(any(FrameworkRequestDTO.class), eq(1L))).thenReturn(frameworkDTO);

        mockMvc.perform(put("/frameworks/1")
                .content(objectMapper.writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value(frameworkDTO.getName()));
    }

    @Test
    public void edit_NonExistingId_NotFoundStatus() throws Exception {
        FrameworkRequestDTO requestDTO = new FrameworkRequestDTO();
        requestDTO.setName("Test framework");

        when(service.edit(any(FrameworkRequestDTO.class), eq(1L))).thenThrow(new FrameworkNotFoundException(1L));

        mockMvc.perform(put("/frameworks/1")
                .content(objectMapper.writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void edit_MissingName_BadRequestStatus() throws Exception {
        when(service.edit(any(FrameworkRequestDTO.class), eq(1L)))
                .thenThrow(new InvalidRequestException("Invalid request"));

        mockMvc.perform(put("/frameworks/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteById_ExistingId_Deleted() throws Exception {
        doNothing().when(service).deleteById(eq(1L));

        mockMvc.perform(delete("/frameworks/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    public void deleteById_NonExistingId_NotFoundStatus() throws Exception {
        doThrow(new FrameworkNotFoundException(1L)).when(service).deleteById(1L);

        mockMvc.perform(delete("/frameworks/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAll_GetAll_GetFrameworks() throws Exception {
        List<FrameworkDTO> frameworks = new ArrayList<>();
        frameworks.add(frameworkDTO);
        frameworks.add(frameworkDTO2);

        when(service.getAll()).thenReturn(frameworks);

        mockMvc.perform(get("/frameworks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getAll_Search_GetFilteredFrameworks() throws Exception {
        List<FrameworkDTO> frameworks = new ArrayList<>();
        frameworks.add(frameworkDTO);

        when(service.search("test")).thenReturn(frameworks);

        mockMvc.perform(get("/frameworks?search=test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(frameworkDTO.getName()));
    }

    @Test
    public void getById_ExistingId_GetFramework() throws Exception {
        when(service.getById(eq(1L))).thenReturn(frameworkDTO);

        mockMvc.perform(get("/frameworks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(frameworkDTO.getName()));
    }

    @Test
    public void getById_NonExistingId_NotFoundStatus() throws Exception {
        when(service.getById(eq(1L))).thenThrow(new FrameworkNotFoundException(1L));

        mockMvc.perform(get("/frameworks/1"))
                .andExpect(status().isNotFound());
    }
}
