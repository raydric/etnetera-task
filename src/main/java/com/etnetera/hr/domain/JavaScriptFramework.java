package com.etnetera.hr.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class JavaScriptFramework {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@ElementCollection
	private List<String> versions;

	@Column(name = "deprecation_date")
	private LocalDate deprecationDate;

	@Column(name = "hype_level")
	private Integer hypeLevel;

}
