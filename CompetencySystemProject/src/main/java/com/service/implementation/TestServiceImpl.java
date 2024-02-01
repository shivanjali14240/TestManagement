package com.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.TestManagement;
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
	public TestManagement updateTest(TestManagement exam) {
		return repository.save(exam);
	}

	@Override
	public List<TestManagement> getTest() {
		return new ArrayList<>(repository.findAll());
	}

	@Override
	public TestManagement getTestById(Long testId) {
		return repository.findById(testId).get();
	}

	@Override
	public void deleteTestById(Long testId) {
		repository.deleteById(testId);
	}

}
