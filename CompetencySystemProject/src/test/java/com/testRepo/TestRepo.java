package com.testRepo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.entity.TestManagement;
import com.repository.TestRepository;

@SpringBootTest
public class TestRepo {

	@Autowired
	private TestRepository testRepository;

	@BeforeEach
	public void setUp() {
		testRepository.deleteAll();
	}

	@Test
	public void testSaveTest() {
		TestManagement test = createTest();
		TestManagement savedTest = testRepository.save(test);
		assertThat(savedTest).isNotNull();
		assertThat(savedTest.getTestId()).isNotNull();
		assertThat(savedTest.getTitle()).isEqualTo("Sample Test");
		assertThat(savedTest.getDescription()).isEqualTo("This is a sample test");
		assertThat(savedTest.getMaxMarks()).isEqualTo(100);
		assertThat(savedTest.getNumberofQuestions()).isEqualTo(10);
		assertThat(savedTest.isActive()).isFalse();
	}

	@Test
	public void testGetAllTests() {
		testRepository.save(createTest());
		testRepository.save(createTest());
		testRepository.save(createTest());
		List<TestManagement> tests = testRepository.findAll();
		assertThat(tests).isNotNull();
		assertThat(tests.size()).isEqualTo(3);
	}

	@Test
	public void testGetTestById() {
		TestManagement test = createTest();
		TestManagement savedTest = testRepository.save(test);
		TestManagement retrievedTest = testRepository.findById(savedTest.getTestId()).orElse(null);
		assertThat(retrievedTest).isNotNull();
		assertThat(retrievedTest.getTestId()).isEqualTo(savedTest.getTestId());
		assertThat(retrievedTest.getTitle()).isEqualTo("Sample Test");
		assertThat(retrievedTest.getDescription()).isEqualTo("This is a sample test");
		assertThat(retrievedTest.getMaxMarks()).isEqualTo(100);
		assertThat(retrievedTest.getNumberofQuestions()).isEqualTo(10);
		assertThat(retrievedTest.isActive()).isFalse();
	}

	@Test
	public void testDeleteTest() {
		TestManagement test = createTest();
		TestManagement savedTest = testRepository.save(test);
		testRepository.deleteById(savedTest.getTestId());
		assertThat(testRepository.existsById(savedTest.getTestId())).isFalse();
	}

	private TestManagement createTest() {
		TestManagement test = new TestManagement();
		test.setTitle("Sample Test");
		test.setDescription("This is a sample test");
		test.setMaxMarks(100);
		test.setNumberofQuestions(10);
		test.setActive(false);
		return test;
	}
}
