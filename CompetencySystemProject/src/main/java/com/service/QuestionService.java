package com.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.entity.Question;

public interface QuestionService {

	List<Question> getAllQuestions();

	Question getQuestionById(Long id);

	void deleteQuestion(Long id);

	Question updateQuestion(Long id, Question updatedQuestion);

	public List<Question> saveQuestionsFromExcel(MultipartFile file) throws IOException;

	Question saveQuestion(Question question, String name);

}
