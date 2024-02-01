package com.controller;

import java.util.NoSuchElementException;


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
import org.springframework.web.bind.annotation.RestController;

import com.entity.TestManagement;
import com.exception.TestIdNotExistException;
import com.service.TestService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/testmanagement/api/v1/tests")
@Slf4j
public class TestController {

	@Autowired
	TestService service;

	@PostMapping
	public ResponseEntity<?> addTest(@RequestBody TestManagement exam) {
		try {
			TestManagement test = service.addTest(exam);
			log.info("addTest: Test added successfully with ID {}", test.getTestId());
			return ResponseEntity.ok(test);
		} catch (Exception e) {
			log.error("Error adding test: {}", e.getMessage());
			return ResponseEntity.status(500).body("Error Occuerd while adding test");
		}
	}

	@GetMapping("/{testId}")
	public ResponseEntity<?> getTestById(@PathVariable("testId") Long testId) {
		if (testId == null) {
			log.error("Invalid request: Test ID is null");
			return ResponseEntity.badRequest().body("Test ID cannot be null");
		}

		try {
			TestManagement test = service.getTestById(testId);
			if (test != null) {
				log.info("getTestById: Retrieved test with ID {}", testId);
				return ResponseEntity.ok(test);
			} else {
				log.warn("getTestById: Test with ID {} not found", testId);
				return ResponseEntity.notFound().build();
			}
		} catch (TestIdNotExistException e) {
			log.error("Error getting test by ID {}: {}", testId, e.getMessage());
			return ResponseEntity.status(404).body("Test id is not found");
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id is not avilable");
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllTest() {
		try {
			log.info("getAllTest: Retrieving all tests");
			return ResponseEntity.ok(service.getTest());
		} catch (Exception e) {
			log.error("Error getting all tests: {}", e.getMessage());
			return ResponseEntity.status(500).body("Error getting all tests");
		}
	}

	@PutMapping("/{testId}")
	public ResponseEntity<?> updateTest(@PathVariable Long testId, @RequestBody TestManagement updatedTest) {
		if (testId == null) {
			log.error("Invalid request: Test ID is null");
			return ResponseEntity.badRequest().body("Test ID cannot be null");
		}

		try {
			TestManagement test = service.getTestById(testId);
			if (test != null) {
				test.setTitle(updatedTest.getTitle());
				test.setDescription(updatedTest.getDescription());
				test.setMaxMarks(updatedTest.getMaxMarks());
				test.setNumberofQuestions(updatedTest.getNumberofQuestions());
				service.updateTest(test);
				log.info("updateTest: Test with ID {} updated successfully", testId);
				return ResponseEntity.ok(service.updateTest(test));
			} else {
				log.warn("updateTest: Test with ID {} not found", testId);
				return ResponseEntity.notFound().build();
			}
		} catch (TestIdNotExistException e) {
			log.error("Error updating test with ID {}: {}", testId, e.getMessage());
			return ResponseEntity.status(500).body("Id is not available");
		}
	}

	@DeleteMapping("/{testId}")
	public ResponseEntity<String> deleteTest(@PathVariable("testId") Long testId) {
		if (testId == null) {
			log.error("Invalid request: Test ID is null");
			return ResponseEntity.badRequest().body("Test ID cannot be null");
		}

		try {
			service.deleteTestById(testId);
			log.info("deleteTest: Test with ID {} deleted successfully", testId);
			return ResponseEntity.ok("Test with ID " + testId + " deleted successfully");
		} catch (TestIdNotExistException e) {
			log.error("Error deleting test with ID {}: {}", testId, e.getMessage());
			return ResponseEntity.status(500).body("Id is not available");
		}
	}
}
