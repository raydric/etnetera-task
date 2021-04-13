package com.etnetera.hr.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FrameworkDTO {

    private Long id;
    private String name;
    private List<String> versions;
    private LocalDate deprecationDate;
    private Integer hypeLevel;
}
