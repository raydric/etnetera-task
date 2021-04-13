package com.etnetera.hr.controller;

import com.etnetera.hr.dto.FrameworkDTO;
import com.etnetera.hr.dto.FrameworkRequestDTO;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/frameworks")
public class JavaScriptFrameworkController {

	private final JavaScriptFrameworkService jsFrameworkService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<FrameworkDTO> getAll(@RequestParam(required = false) final String search) {
		if (search != null) {
			return jsFrameworkService.search(search);
		}

		return jsFrameworkService.getAll();
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public FrameworkDTO getById(@PathVariable final long id) {
		return jsFrameworkService.getById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FrameworkDTO insert(@RequestBody final FrameworkRequestDTO frameworkRequestDTO) {
		return jsFrameworkService.insert(frameworkRequestDTO);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public FrameworkDTO edit(@RequestBody final FrameworkRequestDTO frameworkRequestDTO, @PathVariable final long id) {
		return jsFrameworkService.edit(frameworkRequestDTO, id);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteById(@PathVariable final long id) {
		jsFrameworkService.deleteById(id);
	}

}
