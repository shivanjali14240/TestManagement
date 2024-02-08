package com.service;

import java.util.List;

import com.entity.Question;

public interface QuestionService {

	List<Question> getAllQuestions();

	Question getQuestionById(Long id);
	
	void deleteQuestion(Long id);

	Question updateQuestion(Long id, Question updatedQuestion);

	/*
	 * List<Question> importQuestionsFromExcel(InputStream excelInputStream, Long
	 * categoryId) throws EncryptedDocumentException, IOException;
	 */
	Question saveQuestion(Question question,String name);

}
