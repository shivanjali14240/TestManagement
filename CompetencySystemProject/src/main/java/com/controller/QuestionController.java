package com.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.exception.CategoryNotFoundException;
import com.exception.QuestionNotFoundException;
import com.service.QuestionService;

import lombok.extern.slf4j.Slf4j;

@RestController
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
	public ResponseEntity<?> getQuestionById(@PathVariable Long id) {
		try {
			log.info("Fetching question by id: {}", id);
			Question question = questionService.getQuestionById(id);
			return ResponseEntity.ok(question);
		} catch (QuestionNotFoundException ex) {
			log.warn("Question with ID {} not found", id);
			return ResponseEntity.badRequest().body("Id is not exist");
		}
	}

	@PostMapping
	public ResponseEntity<?> createQuestion(@RequestBody Question question, @RequestParam String categoryName) {
		try {
			log.info("Creating question...");
			return ResponseEntity.ok(questionService.saveQuestion(question, categoryName));
		} catch (IllegalArgumentException e) {
			log.error("Error occurred while saving duplicate category by name", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody Question question) {
		try {
			log.info("Updating question with ID {}: {}", id, question);
			Question updatedQuestion = questionService.updateQuestion(id, question);
			return ResponseEntity.ok(updatedQuestion);
		} catch (QuestionNotFoundException ex) {
			log.warn("Question with ID {} not found", id);
			return ResponseEntity.badRequest().body("Id is not found");
		} catch (Exception ex) {
			log.error("Error updating question with ID {}: {}", id, ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
		try {
			questionService.deleteQuestion(id);
			log.info("Question with ID {} deleted successfully", id);
			return ResponseEntity.ok("Question with ID " + id + " deleted successfully");
		} catch (QuestionNotFoundException ex) {
			log.error("Error deleting question with ID {}: {}", id, ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found with ID: " + id);
		} catch (Exception ex) {
			log.error("Error deleting question with ID {}: {}", id, ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to delete question with ID: " + id);
		}
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadQuestions(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("File is empty");
		}

		try {
			List<Question> savedQuestions = questionService.saveQuestionsFromExcel(file);
			log.info("Successfully saved {} questions from Excel file", savedQuestions.size());
			return ResponseEntity.ok(savedQuestions);
		} catch (CategoryNotFoundException ex) {
			String errorMessage = "Category not found: " + ex.getMessage();
			log.error(errorMessage);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		} catch (IOException ex) {
			String errorMessage = "Failed to upload questions. Please try again later.";
			log.error(errorMessage, ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}
}
