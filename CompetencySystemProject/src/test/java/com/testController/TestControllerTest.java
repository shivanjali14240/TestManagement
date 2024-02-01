package com.testController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.controller.TestController;
import com.entity.TestManagement;
import com.exception.TestIdNotExistException;
import com.service.TestService;

@SpringBootTest
public class TestControllerTest {
	@Mock
	private TestService testService;

	@InjectMocks
	private TestController testController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddTestPositive() {
		TestManagement testToAdd = new TestManagement();

		when(testService.addTest(testToAdd)).thenReturn(testToAdd);

		ResponseEntity<?> responseEntity = testController.addTest(testToAdd);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	void testAddTestNegative() {
		TestManagement testToAdd = new TestManagement();

		when(testService.addTest(testToAdd)).thenThrow(new RuntimeException("Error adding test"));

		ResponseEntity<?> responseEntity = testController.addTest(testToAdd);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	void testGetTestByIdPositive() {
		Long testId = 1L;
		TestManagement test = new TestManagement();

		when(testService.getTestById(testId)).thenReturn(test);

		ResponseEntity<?> responseEntity = testController.getTestById(testId);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	void testGetTestByIdNegativeTestIdNotExistException() {
	    Long testId = 1L;

	    when(testService.getTestById(testId)).thenThrow(new TestIdNotExistException("Test ID not found"));

	    ResponseEntity<?> responseEntity = testController.getTestById(testId);

	    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	    assertNotNull(responseEntity.getBody());
	    }
	
	@Test
	void testGetTestByIdNegativeNoSuchElementException() {
		Long testId = null;

		ResponseEntity<?> responseEntity = testController.getTestById(testId);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	void testGetAllTestPositive() {
		List<TestManagement> tests = new ArrayList<>();

		when(testService.getTest()).thenReturn(tests);

		ResponseEntity<?> responseEntity = testController.getAllTest();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	void testGetAllTestNegative() {
		when(testService.getTest()).thenThrow(new RuntimeException("Error getting all tests"));

		ResponseEntity<?> responseEntity = testController.getAllTest();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	void testUpdateTestPositive() {
		Long testId = 1L;
		TestManagement updatedTest = new TestManagement();

		TestManagement existingTest = new TestManagement();
		when(testService.getTestById(testId)).thenReturn(existingTest);
		when(testService.updateTest(existingTest)).thenReturn(updatedTest);

		ResponseEntity<?> responseEntity = testController.updateTest(testId, updatedTest);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	void testUpdateTestNegativeTestIdNotExistException() {
		Long testId = 1L;
		TestManagement updatedTest = new TestManagement();

		when(testService.getTestById(testId)).thenThrow(new TestIdNotExistException("Test ID not found"));

		ResponseEntity<?> responseEntity = testController.updateTest(testId, updatedTest);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	void testDeleteTestPositive() {
		Long testId = 1L;

		ResponseEntity<String> responseEntity = testController.deleteTest(testId);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	void testDeleteTestNegativeTestIdNotExistException() {
		Long testId = 1L;

		doThrow(new TestIdNotExistException("Test ID not found")).when(testService).deleteTestById(testId);

		ResponseEntity<String> responseEntity = testController.deleteTest(testId);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	void testDeleteTestNegative() {
		Long testId = null;

		ResponseEntity<String> responseEntity = testController.deleteTest(testId);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

}
