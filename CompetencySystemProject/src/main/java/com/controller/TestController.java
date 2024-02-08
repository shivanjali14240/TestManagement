package com.controller;

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
			return ResponseEntity.status(HttpStatus.CREATED).body(test);
		} catch (Exception e) {
			log.error("Error adding test: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while adding test");
		}
	}

	@GetMapping("/{testId}")
	public ResponseEntity<?> getTestById(@PathVariable("testId") Long testId) {
		try {
			TestManagement test = service.getTestById(testId);
			log.info("getTestById: Retrieved test with ID {}", testId);
			return ResponseEntity.ok(test);
		} catch (TestIdNotExistException e) {
			log.warn("Test with ID {} not found", testId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test with ID " + testId + " not found");
		} catch (Exception e) {
			log.error("Error getting test by ID {}: {}", testId, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while getting test by ID");
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllTest() {
		try {
			List<TestManagement> tests = service.getTest();
			log.info("getAllTest: Retrieved all tests");
			return ResponseEntity.ok(tests);
		} catch (Exception e) {
			log.error("Error getting all tests: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while getting all tests");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateTest(@PathVariable("id") Long id, @RequestBody TestManagement test) {
		try {
			TestManagement updatedTest = service.updateTest(id, test);
			log.info("updateTest: Test with ID {} updated successfully", id);
			return ResponseEntity.ok(updatedTest);
		} catch (TestIdNotExistException e) {
			log.warn("Test with ID {} not found", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test with ID " + id + " not found");
		} catch (Exception e) {
			log.error("Error updating test with ID {}: {}", id, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while updating test with ID " + id);
		}
	}

	@DeleteMapping("/{testId}")
	public ResponseEntity<?> deleteTest(@PathVariable("testId") Long testId) {
		try {
			service.deleteTestById(testId);
			log.info("deleteTest: Test with ID {} deleted successfully", testId);
			return ResponseEntity.ok("Test with ID " + testId + " deleted successfully");
		} catch (TestIdNotExistException e) {
			log.warn("Test with ID {} not found", testId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test with ID " + testId + " not found");
		} catch (Exception e) {
			log.error("Error deleting test with ID {}: {}", testId, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while deleting test with ID " + testId);
		}
	}
}