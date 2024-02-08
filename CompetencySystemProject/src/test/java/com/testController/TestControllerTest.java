package com.testController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

	private TestManagement test;

	public void setUp() {
		test = new TestManagement();
		test.setTestId(1L);
		test.setTitle("Test Title");
		test.setDescription("Test Description");
		test.setMaxMarks(100);
		test.setNumberofQuestions(10);
		test.setActive(true);
	}

	@Test
	public void testAddTest_Positive() {
		when(testService.addTest(any(TestManagement.class))).thenReturn(test);
	    ResponseEntity<?> responseEntity = testController.addTest(test);
	    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
	}

	@Test
	public void testAddTest_Negative() {
		doThrow(new RuntimeException("Test Add Exception")).when(testService).addTest(any(TestManagement.class));
		ResponseEntity<?> responseEntity = testController.addTest(test);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertEquals("Error occurred while adding test", responseEntity.getBody());
	}

	@Test
	void testGetTestById_Positive() {
		Long testId = 1L;
		TestManagement expectedTest = new TestManagement();
		when(testService.getTestById(testId)).thenReturn(expectedTest);
		ResponseEntity<?> responseEntity = testController.getTestById(testId);
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getBody());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		TestManagement returnedTest = (TestManagement) responseEntity.getBody();
		assertEquals(expectedTest, returnedTest);
	}

	@Test
	public void testGetTestById_Negative() {
		doThrow(new TestIdNotExistException("Test ID Not Found")).when(testService).getTestById(2L);
		ResponseEntity<?> responseEntity = testController.getTestById(2L);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Test with ID 2 not found", responseEntity.getBody());
	}

	@Test
	public void testGetAllTest_Positive() {
		List<TestManagement> tests = new ArrayList<>();
		tests.add(test);
		when(testService.getTest()).thenReturn(tests);
		ResponseEntity<?> responseEntity = testController.getAllTest();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertTrue(responseEntity.getBody() instanceof List);
	}

	@Test
	void testUpdateTest_Success() {
		Long testId = 1L;
		TestManagement test = new TestManagement();
		test.setTestId(testId);
		TestManagement updatedTest = new TestManagement();
		when(testService.updateTest(testId, test)).thenReturn(updatedTest);
		ResponseEntity<?> responseEntity = testController.updateTest(testId, test);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	void testUpdateTest_TestIdNotExist() {
		Long testId = 1L;
		TestManagement test = new TestManagement();
		when(testService.updateTest(testId, test)).thenThrow(new TestIdNotExistException(null));
		ResponseEntity<?> responseEntity = testController.updateTest(testId, test);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	void testUpdateTest_InternalServerError() {
		Long testId = 1L;
		TestManagement test = new TestManagement();
		when(testService.updateTest(testId, test)).thenThrow(new RuntimeException("Internal Server Error"));
		ResponseEntity<?> responseEntity = testController.updateTest(testId, test);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
	}

	@Test
	public void testDeleteTest_Positive() {
		ResponseEntity<?> responseEntity = testController.deleteTest(1L);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Test with ID 1 deleted successfully", responseEntity.getBody());
	}

	@Test
	public void testDeleteTest_Negative() {
		doThrow(new TestIdNotExistException("Test ID Not Found")).when(testService).deleteTestById(2L);
		ResponseEntity<?> responseEntity = testController.deleteTest(2L);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Test with ID 2 not found", responseEntity.getBody());
	}
}
