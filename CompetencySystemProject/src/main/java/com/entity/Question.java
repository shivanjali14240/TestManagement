package com.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long questionId;

	private String content;
	private String option1;
	private String option2;
	private String option3;
	private String option4;
	private String answer;
	private int marks;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToMany  
	@JoinTable(name = "question_test",joinColumns = @JoinColumn(name = "question_id"),inverseJoinColumns = @JoinColumn(name = "test_id"))   
	private List<TestManagement> tests;

}
