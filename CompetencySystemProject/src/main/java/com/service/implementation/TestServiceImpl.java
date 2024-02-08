package com.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.TestManagement;
import com.exception.TestIdNotExistException;
import com.repository.TestRepository;
import com.service.TestService;

@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private TestRepository repository;

	@Override
	public TestManagement addTest(TestManagement exam) {
		return repository.save(exam);
	}

	@Override
	public List<TestManagement> getTest() {
		return new ArrayList<>(repository.findAll());
	}

	@Override
	public TestManagement getTestById(Long id) {
		Optional<TestManagement> optionalTestManagement = repository.findById(id);
		return optionalTestManagement.orElseThrow(() -> new TestIdNotExistException("Test not found with id: " + id));
	}

	@Override
	public TestManagement updateTest(Long id, TestManagement test) {
		TestManagement existingTest = getTestById(id);
		if (existingTest != null) {
			test.setTestId(id);
			return repository.save(test);
		} else {
			throw new TestIdNotExistException("Test not found with id: " + id);
		}
	}

	@Override
	public void deleteTestById(Long testId) {
		if(!repository.existsById(testId)) {
			throw new TestIdNotExistException("Test not found with id: " + testId);
		}
		repository.deleteById(testId);
	}

}
