package com.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.entity.Question;
import com.exception.QuestionNotFoundException;
import com.service.QuestionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin("*")
@RequestMapping("/testmanagement/api/v1/questions")
@Slf4j
public class QuestionController {

	@Autowired
	private QuestionService questionService;

	@GetMapping
	public List<Question> getAllQuestions() {
		log.info("Fetching all questions");
		return questionService.getAllQuestions();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
		try {
			log.info("Fetching question by id: {}", id);
			Question question = questionService.getQuestionById(id);
			return ResponseEntity.ok(question);
		} catch (QuestionNotFoundException ex) {
			return ResponseEntity.status(404).body(null); 
		}
	}

	@PostMapping
	public Question createQuestion(@RequestBody Question question) {
		log.info("Creating question: {}", question);
		return questionService.saveQuestion(question);
	}

	@PutMapping("/{id}")
	public Question updateQuestion(@PathVariable Long id, @RequestBody Question question) {
		log.info("Updating question with id {}: {}", id, question);
		question.setQuestionId(id);
		return questionService.saveQuestion(question);
	}

	@DeleteMapping("/{id}")
	public void deleteQuestion(@PathVariable Long id) {
		try {
			log.info("Deleting question with id: {}", id);
			questionService.deleteQuestion(id);
		} catch (QuestionNotFoundException ex) {
			log.error("Error deleting question with id {}: {}", id, ex.getMessage());
		}
	}
	

	 @PostMapping("/import")
	    public ResponseEntity<String> importQuestions(@RequestParam("file") MultipartFile file) {
	        if (file.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide an Excel file");
	        }
	        try (InputStream excelInputStream = file.getInputStream()) {
	            questionService.importQuestionsFromExcel(excelInputStream);
	            return ResponseEntity.ok("Questions imported successfully");
	        } catch (IOException e) {
	            log.error("Error importing questions from Excel", e);
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error importing questions");
	        }
	    }
	 
	 
	 
	}
