package com.testService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.entity.TestManagement;
import com.exception.TestIdNotExistException;
import com.repository.TestRepository;
import com.service.implementation.TestServiceImpl;

@SpringBootTest
class TestServiceImplTest {

	@Mock
	private TestRepository repository;

	@InjectMocks
	private TestServiceImpl testService;

	@Test
	void testAddTest_Positive() {
		TestManagement test = new TestManagement();
		test.setTitle("Mock Test");
		test.setDescription("Sample test description");
		test.setMaxMarks(100);
		test.setNumberofQuestions(20);
		test.setActive(true);
		when(repository.save(test)).thenReturn(test);
		TestManagement savedTest = testService.addTest(test);
		assertNotNull(savedTest);
		assertEquals("Mock Test", savedTest.getTitle());
		assertEquals("Sample test description", savedTest.getDescription());
		assertEquals(100, savedTest.getMaxMarks());
		assertEquals(20, savedTest.getNumberofQuestions());
		assertTrue(savedTest.isActive());
	}

	@Test
	void testAddTest_Negative() {
		TestManagement test = new TestManagement();
		when(repository.save(any(TestManagement.class))).thenThrow(IllegalArgumentException.class);
		assertThrows(IllegalArgumentException.class, () -> testService.addTest(test));
	}

	@Test
	void testGetTest_Positive() {
		List<TestManagement> tests = new ArrayList<>();
		tests.add(new TestManagement());
		tests.add(new TestManagement());
		tests.add(new TestManagement());
		when(repository.findAll()).thenReturn(tests);
		List<TestManagement> retrievedTests = testService.getTest();
		assertNotNull(retrievedTests);
		assertEquals(3, retrievedTests.size());
	}

	@Test
	void testGetTestById_Positive() {
		Long testId = 123L;
		TestManagement test = new TestManagement();
		when(repository.findById(testId)).thenReturn(Optional.of(test));
		TestManagement retrievedTest = testService.getTestById(testId);
		assertNotNull(retrievedTest);
	}

	@Test
	void testGetTestById_Negative() {
		Long testId = 999L;
		when(repository.findById(testId)).thenReturn(Optional.empty());
		assertThrows(TestIdNotExistException.class, () -> testService.getTestById(testId));
	}

	@Test
	void testUpdateTest_Positive() {
		Long testId = 123L;
		TestManagement test = new TestManagement();
		test.setTitle("Mock Test");
		test.setDescription("Sample test description");
		test.setMaxMarks(100);
		test.setNumberofQuestions(20);
		test.setActive(true);
		when(repository.findById(testId)).thenReturn(Optional.of(test));
		when(repository.save(test)).thenReturn(test);
		TestManagement updatedTest = testService.updateTest(testId, test);
		assertNotNull(updatedTest);
		assertEquals("Mock Test", updatedTest.getTitle());
		assertEquals("Sample test description", updatedTest.getDescription());
		assertEquals(100, updatedTest.getMaxMarks());
		assertEquals(20, updatedTest.getNumberofQuestions());
		assertTrue(updatedTest.isActive());
	}

	@Test
	void testUpdateTest_Negative() {
		Long testId = 999L;
		TestManagement test = new TestManagement();
		when(repository.findById(testId)).thenReturn(Optional.empty());
		assertThrows(TestIdNotExistException.class, () -> testService.updateTest(testId, test));
	}

	@Test
	void testDeleteTestById_Positive() {
		TestManagement test = new TestManagement();
		test.setTestId(123L); 
		when(repository.existsById(123L)).thenReturn(true);
		assertDoesNotThrow(() -> testService.deleteTestById(123L));
		verify(repository).deleteById(123L);
	}

	@Test
	void testDeleteTestById_Negative() {
		Long testId = 999L;
		when(repository.existsById(testId)).thenReturn(false);
		assertThrows(TestIdNotExistException.class, () -> testService.deleteTestById(testId));
	}
}