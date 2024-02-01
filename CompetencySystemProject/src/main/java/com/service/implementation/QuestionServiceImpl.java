package com.service.implementation;

import java.io.InputStream;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.Question;
import com.exception.QuestionNotFoundException;
import com.repository.QuestionRepository;
import com.service.QuestionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {
	
	@Autowired
	private QuestionRepository questionRepository;

    @Override
    public List<Question> getAllQuestions() {
        log.info("Fetching all questions");
        return questionRepository.findAll();
    }

    @Override
    public Question getQuestionById(Long id) {
        log.info("Fetching question by id: {}", id);
        return questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id: " + id, null));
    }

    @Override
    public Question saveQuestion(Question question) {
        log.info("Saving question: {}", question);
        return questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(Long id) {
        log.info("Deleting question with id: {}", id);
        if (!questionRepository.existsById(id)) {
            throw new QuestionNotFoundException("Question not found with id: " + id, null);
        }
        questionRepository.deleteById(id);
    }

    @Override
    public void importQuestionsFromExcel(InputStream excelInputStream) {
     	
		
	}
}
