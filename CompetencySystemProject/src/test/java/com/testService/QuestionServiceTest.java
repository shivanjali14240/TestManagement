package com.testService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.entity.Category;
import com.entity.Question;
import com.exception.QuestionNotFoundException;
import com.repository.CategoryRepository;
import com.repository.QuestionRepository;
import com.service.implementation.QuestionServiceImpl;

public class QuestionServiceTest {
	@Mock
	private QuestionRepository questionRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private QuestionServiceImpl questionService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetAllQuestions_Positive() {
		List<Question> mockQuestions = new ArrayList<>();
		mockQuestions.add(new Question());
		mockQuestions.add(new Question());
		when(questionRepository.findAll()).thenReturn(mockQuestions);
		List<Question> result = questionService.getAllQuestions();
		assertEquals(2, result.size());
	}

	@Test
	public void testGetQuestionById_Positive() {
		Question mockQuestion = new Question();
		mockQuestion.setQuestionId(1L);
		when(questionRepository.findById(1L)).thenReturn(Optional.of(mockQuestion));
		Question result = questionService.getQuestionById(1L);
		assertEquals(1L, result.getQuestionId());
	}

	@Test
	    public void testGetQuestionById_Negative() {
	        when(questionRepository.findById(1L)).thenReturn(Optional.empty());
	        assertThrows(QuestionNotFoundException.class, () -> {
	            questionService.getQuestionById(1L);
	        });
	    }

	@Test
	public void testSaveQuestion_Positive() {
		Question question = new Question();
		question.setContent("Sample question");
		String categoryName = "Sample Category";
		Category mockCategory = new Category();
		mockCategory.setName(categoryName);
		when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(mockCategory));
		when(questionRepository.save(question)).thenReturn(question);
		Question result = questionService.saveQuestion(question, categoryName);
		assertNotNull(result);
		assertEquals("Sample question", result.getContent());
		assertEquals("Sample Category", result.getCategory().getName());
	}

	@Test
	public void testSaveQuestion_Negative() {
		Question question = new Question();
		question.setContent("Sample question");
		String categoryName = "Non-existent Category";
		when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> {
			questionService.saveQuestion(question, categoryName);
		});
	}

	@Test
	public void testDeleteQuestion_Positive() {
		Long questionId = 1L;
		when(questionRepository.findById(questionId)).thenReturn(Optional.of(new Question()));
		questionService.deleteQuestion(questionId);
		verify(questionRepository, times(1)).deleteById(questionId);
	}

	@Test
	public void testDeleteQuestion_Negative() {
		Long questionId = 1L;
		when(questionRepository.findById(questionId)).thenReturn(Optional.empty());
		assertThrows(QuestionNotFoundException.class, () -> {
			questionService.deleteQuestion(questionId);
		});
	}

	@Test
	void testUpdateQuestion_Positive() {
		Long questionId = 1L;
		Question updatedQuestion = new Question();
		when(questionRepository.findById(questionId)).thenReturn(Optional.of(new Question()));
		when(questionRepository.save(any(Question.class))).thenReturn(updatedQuestion);
		Question result = questionService.updateQuestion(questionId, updatedQuestion);
		assertNotNull(result);
	}

	@Test
	public void testUpdateQuestion_Negative() {
		Long questionId = 1L;
		when(questionRepository.findById(questionId)).thenReturn(Optional.empty());
		assertThrows(QuestionNotFoundException.class, () -> {
			questionService.updateQuestion(questionId, new Question());
		});
	}

}
