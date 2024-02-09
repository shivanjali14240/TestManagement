package com.testController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
class TestControllerTest {

	@Mock
	private TestService testService;

	@InjectMocks
	private TestController testController;

	@Test
	void testAddTest_Positive() {
		TestManagement test = new TestManagement();
		test.setTitle("Sample Test");
		test.setDescription("This is a sample test");
		test.setMaxMarks(100);
		test.setNumberofQuestions(10);
		when(testService.addTest(any(TestManagement.class))).thenReturn(test);
		ResponseEntity<?> response = testController.addTest(test);
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(test, response.getBody());
	}

	@Test
    void testAddTest_Negative() {
        when(testService.addTest(any(TestManagement.class))).thenThrow(new RuntimeException("Test addition failed"));
        ResponseEntity<?> response = testController.addTest(new TestManagement());
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while adding test", response.getBody());
    }

	@Test
	void testGetTestById_Positive() {
		TestManagement test = new TestManagement();
		test.setTestId(1L);
		test.setTitle("Sample Test");
		when(testService.getTestById(1L)).thenReturn(test);
		ResponseEntity<?> response = testController.getTestById(1L);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(test, response.getBody());
	}

	@Test
    void testGetTestById_Negative() {
        when(testService.getTestById(1L)).thenThrow(new TestIdNotExistException("Test ID not found"));
        ResponseEntity<?> response = testController.getTestById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Test with ID 1 not found", response.getBody());
    }

	@Test
	void testGetAllTest_Positive() {
		List<TestManagement> tests = new ArrayList<>();
		TestManagement test1 = new TestManagement();
		test1.setTestId(1L);
		test1.setTitle("Test 1");
		TestManagement test2 = new TestManagement();
		test2.setTestId(2L);
		test2.setTitle("Test 2");
		tests.add(test1);
		tests.add(test2);
		when(testService.getTest()).thenReturn(tests);
		ResponseEntity<?> response = testController.getAllTest();
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(tests, response.getBody());
	}

	@Test
    void testGetAllTest_Negative() {
        when(testService.getTest()).thenThrow(new RuntimeException("Failed to retrieve tests"));
        ResponseEntity<?> response = testController.getAllTest();
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while getting all tests", response.getBody());
    }

	@Test
	void testUpdateTest_Positive() {
		TestManagement test = new TestManagement();
		test.setTestId(1L);
		test.setTitle("Updated Test");
		when(testService.updateTest(1L, test)).thenReturn(test);
		ResponseEntity<?> response = testController.updateTest(1L, test);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(test, response.getBody());
	}

	@Test
    void testUpdateTest_Negative() {
        when(testService.updateTest(1L, new TestManagement())).thenThrow(new TestIdNotExistException("Test ID not found"));
        ResponseEntity<?> response = testController.updateTest(1L, new TestManagement());
        assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Test with ID 1 not found", response.getBody());
    }

	@Test
	void testDeleteTest_Positive() {
		doNothing().when(testService).deleteTestById(1L);
		ResponseEntity<?> response = testController.deleteTest(1L);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Test with ID 1 deleted successfully", response.getBody());
	}

	@Test
	void testDeleteTest_Negative() {
		doThrow(new TestIdNotExistException("Test ID not found")).when(testService).deleteTestById(1L);
		ResponseEntity<?> response = testController.deleteTest(1L);
		assertNotNull(response);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Test with ID 1 not found", response.getBody());
	}
}
