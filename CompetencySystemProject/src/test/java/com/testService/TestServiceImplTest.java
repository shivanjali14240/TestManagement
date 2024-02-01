package com.testService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.entity.TestManagement;
import com.repository.TestRepository;
import com.service.TestService;
import com.service.implementation.TestServiceImpl;

public class TestServiceImplTest {
	 @Mock
	    private TestRepository testRepository;

	    @InjectMocks
	    private TestService testService = new TestServiceImpl();

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void testAddTest() {
	        TestManagement testToAdd = new TestManagement();

	        when(testRepository.save(testToAdd)).thenReturn(testToAdd);

	        TestManagement addedTest = testService.addTest(testToAdd);

	        assertNotNull(addedTest);
	    }

	    @Test
	    void testUpdateTest() {
	        TestManagement testToUpdate = new TestManagement();

	        when(testRepository.save(testToUpdate)).thenReturn(testToUpdate);

	        TestManagement updatedTest = testService.updateTest(testToUpdate);

	        assertNotNull(updatedTest);
	    }

	    @Test
	    void testGetTest() {
	        List<TestManagement> tests = new ArrayList<>();

	        when(testRepository.findAll()).thenReturn(tests);

	        List<TestManagement> resultTests = testService.getTest();

	        assertNotNull(resultTests);
	        assertEquals(tests.size(), resultTests.size());
	    }

	    @Test
	    void testGetTestById() {
	        Long testId = 1L;
	        TestManagement test = new TestManagement();

	        when(testRepository.findById(testId)).thenReturn(Optional.of(test));

	        TestManagement resultTest = testService.getTestById(testId);

	        assertNotNull(resultTest);
	    }

	    @Test
	    void testGetTestByIdNotFound() {
	        Long testId = 1L;

	        when(testRepository.findById(testId)).thenReturn(Optional.empty());

	        assertThrows(NoSuchElementException.class, () -> testService.getTestById(testId));
	    }

	    @Test
	    void testDeleteTestById() {
	        Long testId = 1L;

	        assertDoesNotThrow(() -> testService.deleteTestById(testId));
	        verify(testRepository, times(1)).deleteById(testId);
	    }

}
