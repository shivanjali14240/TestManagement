package com.service;

import java.util.List;

import com.entity.TestManagement;

public interface TestService {

	public TestManagement addTest(TestManagement test);

	public TestManagement updateTest(TestManagement test);

	public List<TestManagement> getTest();

	public TestManagement getTestById(Long testId);

	public void deleteTestById(Long testId);
}
