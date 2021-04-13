package com.etnetera.hr.service;

import com.etnetera.hr.dto.FrameworkDTO;
import com.etnetera.hr.dto.FrameworkRequestDTO;
import com.etnetera.hr.exception.FrameworkNotFoundException;
import com.etnetera.hr.domain.JavaScriptFramework;
import com.etnetera.hr.exception.InvalidRequestException;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class JavaScriptFrameworkService {

    private final JavaScriptFrameworkRepository jsFrameworkRepository;
    private final ModelMapper modelMapper;

    public List<FrameworkDTO> getAll() {
        Iterable<JavaScriptFramework> frameworks = jsFrameworkRepository.findAll();

        return StreamSupport.stream(frameworks.spliterator(), false)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FrameworkDTO> search(final String name) {
        Iterable<JavaScriptFramework> frameworks = jsFrameworkRepository.findByNameContainsIgnoreCase(name);

        return StreamSupport.stream(frameworks.spliterator(), false)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FrameworkDTO getById(final long id) {
        Optional<JavaScriptFramework> jsFramework = jsFrameworkRepository.findById(id);

        if (jsFramework.isPresent()) {
            return convertToDTO(jsFramework.get());
        }

        throw new FrameworkNotFoundException(id);
    }

    public FrameworkDTO insert(final FrameworkRequestDTO frameworkRequestDTO) {
        if (frameworkRequestDTO == null || frameworkRequestDTO.getName() == null || frameworkRequestDTO.getName().isEmpty()) {
            throw new InvalidRequestException("Invalid request");
        }

        JavaScriptFramework framework = jsFrameworkRepository.save(convertToEntity(frameworkRequestDTO));

        log.info("Inserted " + framework.toString());

        return convertToDTO(framework);
    }

    public FrameworkDTO edit(final FrameworkRequestDTO frameworkRequestDTO, final long id) {
        if (frameworkRequestDTO == null || frameworkRequestDTO.getName() == null || frameworkRequestDTO.getName().isEmpty()) {
            throw new InvalidRequestException("Invalid request");
        }

        jsFrameworkRepository.findById(id)
                .orElseThrow(() -> new FrameworkNotFoundException(id));

        JavaScriptFramework framework = convertToEntity(frameworkRequestDTO);
        framework.setId(id);
        jsFrameworkRepository.save(framework);

        log.info("Updated " + framework.toString());

        return convertToDTO(framework);
    }

    public void deleteById(final long id) {
        if (jsFrameworkRepository.existsById(id)) {
            jsFrameworkRepository.deleteById(id);

            log.info("Deleted JavaScriptFramework with id " + id);
        } else {
            throw new FrameworkNotFoundException(id);
        }
    }

    private FrameworkDTO convertToDTO(final JavaScriptFramework jsFramework) {
        return modelMapper.map(jsFramework, FrameworkDTO.class);
    }

    private JavaScriptFramework convertToEntity(final FrameworkRequestDTO frameworkRequestDTO) {
        return modelMapper.map(frameworkRequestDTO, JavaScriptFramework.class);
    }
}
