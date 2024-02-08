package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;

import com.entity.Question;

public interface QuestionService {

	List<Question> getAllQuestions();

	Question getQuestionById(Long id);
	
	Question saveQuestion(Question question, String categoryName);

	void deleteQuestion(Long id);

	Question updateQuestion(Long id, Question updatedQuestion);

	List<Question> importQuestionsFromExcel(InputStream excelInputStream, Long categoryId)
			throws EncryptedDocumentException, IOException;

}
