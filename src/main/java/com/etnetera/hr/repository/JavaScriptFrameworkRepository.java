package com.etnetera.hr.repository;

import com.etnetera.hr.domain.JavaScriptFramework;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JavaScriptFrameworkRepository extends CrudRepository<JavaScriptFramework, Long> {

    List<JavaScriptFramework> findByNameContainsIgnoreCase(String name);
}
