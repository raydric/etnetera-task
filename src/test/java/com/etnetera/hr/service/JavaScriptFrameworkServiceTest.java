package com.etnetera.hr.service;

import com.etnetera.hr.domain.JavaScriptFramework;
import com.etnetera.hr.dto.FrameworkDTO;
import com.etnetera.hr.dto.FrameworkRequestDTO;
import com.etnetera.hr.exception.FrameworkNotFoundException;
import com.etnetera.hr.exception.InvalidRequestException;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaScriptFrameworkServiceTest {

    @Mock
    private JavaScriptFrameworkRepository repository;

    private JavaScriptFrameworkService service;

    private static JavaScriptFramework framework;
    private static JavaScriptFramework framework2;
    private static FrameworkRequestDTO requestDTO;

    @Before
    public void setup() {
        ModelMapper modelMapper = new ModelMapper();
        service = new JavaScriptFrameworkService(repository, modelMapper);
    }

    @BeforeClass
    public static void beforeClass() {
        framework = new JavaScriptFramework();
        framework.setId(1L);
        framework.setName("Test framework");

        framework2 = new JavaScriptFramework();
        framework2.setId(2L);
        framework2.setName("Another framework");

        requestDTO = new FrameworkRequestDTO();
        requestDTO.setName("Test framework");
    }

    @Test
    public void getAll_GetAll_GetFrameworks() {
        List<JavaScriptFramework> frameworks = new ArrayList<>();
        frameworks.add(framework);
        frameworks.add(framework2);

        when(repository.findAll()).thenReturn(frameworks);

        List<FrameworkDTO> results = service.getAll();
        assertEquals(2, results.size());

        verify(repository, times(1)).findAll();
    }

    @Test
    public void search_Search_FilteredResults() {
        List<JavaScriptFramework> frameworks = new ArrayList<>();
        frameworks.add(framework);

        when(repository.findByNameContainsIgnoreCase("test")).thenReturn(frameworks);

        List<FrameworkDTO> results = service.search("test");
        assertEquals(1, results.size());
        assertEquals(framework.getName(), results.get(0).getName());

        verify(repository, times(1)).findByNameContainsIgnoreCase("test");
    }

    @Test
    public void getById_ExistingId_GetFramework() {
        when(repository.findById(1L)).thenReturn(Optional.of(framework));

        FrameworkDTO frameworkDTO = service.getById(1L);
        assertNotNull(frameworkDTO);
        assertEquals(framework.getName(), frameworkDTO.getName());

        verify(repository, times(1)).findById(1L);
    }

    @Test(expected = FrameworkNotFoundException.class)
    public void getById_NonExistingId_FrameworkNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        service.getById(1L);
    }

    @Test
    public void insert_CreateFramework_GetNewFramework() {
        when(repository.save(any(JavaScriptFramework.class))).thenReturn(framework);

        FrameworkDTO result = service.insert(requestDTO);
        assertNotNull(result);
        assertEquals(result.getName(), framework.getName());
        assertEquals(Long.valueOf(1L), framework.getId());

        verify(repository, times(1)).save(any(JavaScriptFramework.class));
    }

    @Test(expected = InvalidRequestException.class)
    public void insert_MissingName_InvalidRequestException() {
        service.insert(new FrameworkRequestDTO());
    }

    @Test
    public void edit_ExistingId_Edited() {
        when(repository.findById(1L)).thenReturn(Optional.of(framework));
        when(repository.save(any(JavaScriptFramework.class))).thenReturn(framework);

        FrameworkDTO result = service.edit(requestDTO, 1L);
        assertNotNull(result);
        assertEquals(framework.getName(), result.getName());

        verify(repository, times(1)).save(any(JavaScriptFramework.class));
    }

    @Test(expected = FrameworkNotFoundException.class)
    public void edit_NonExistingId_FrameworkNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        service.edit(requestDTO, 1L);
    }

    @Test(expected = InvalidRequestException.class)
    public void edit_MissingName_InvalidRequestException() {
        service.edit(new FrameworkRequestDTO(),1L);
    }

    @Test
    public void deleteById_ExistingId_Deleted() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test(expected = FrameworkNotFoundException.class)
    public void deleteById_NonExistingId_FrameworkNotFoundException() {
        when(repository.existsById(1L)).thenReturn(false);

        service.deleteById(1L);
    }
}
