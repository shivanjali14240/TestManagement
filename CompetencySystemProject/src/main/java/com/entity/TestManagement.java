package com.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tests")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestManagement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long testId;

	private String title;

	private String description;

	private int maxMarks;

	private int numberofQuestions;

	private boolean active = false;

	@ManyToMany(mappedBy = "tests",cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Question> questions;

}
