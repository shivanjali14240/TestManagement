package com.testRepo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.boot.test.context.SpringBootTest;

import com.entity.Question;
import com.repository.CategoryRepository;
import com.repository.QuestionRepository;
import com.service.implementation.QuestionServiceImpl;

@SpringBootTest
public class QuestionRepo {

	@Mock
	private QuestionRepository questionRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private QuestionServiceImpl questionService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testSaveQuestion() {
		Question question = new Question(null, "Question 1", "Option 1", "Option 2", "Option 3", "Option 4", "Option 1",
				"1", null, null);
		Question savedQuestion = new Question(1L, "Question 1", "Option 1", "Option 2", "Option 3", "Option 4",
				"Option 1", "1", null, null);
		when(questionRepository.save(question)).thenReturn(savedQuestion);
		Question result = questionService.saveQuestion(question, null);
		assertEquals(savedQuestion.getQuestionId(), result.getQuestionId());
		assertEquals(savedQuestion.getContent(), result.getContent());
	}

	@Test
	void testGetAllQuestions() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(new Question(1L, "Question 1", "Option 1", "Option 2", "Option 3", "Option 4", "Option 1", "1",
				null, null));
		questionList.add(new Question(2L, "Question 2", "Option A", "Option B", "Option C", "Option D", "Option A", "2",
				null, null));

		when(questionRepository.findAll()).thenReturn(questionList);
		List<Question> result = questionService.getAllQuestions();
		assertEquals(questionList.size(), result.size());
		assertEquals(questionList.get(0).getContent(), result.get(0).getContent());
		assertEquals(questionList.get(1).getContent(), result.get(1).getContent());
	}

	@Test
	void testGetQuestionById() {
		Question question = new Question(1L, "Question 1", "Option 1", "Option 2", "Option 3", "Option 4", "Option 1",
				"1", null, null);
		Optional<Question> optionalQuestion = Optional.of(question);
		when(questionRepository.findById(1L)).thenReturn(optionalQuestion);
		Optional<Question> result = Optional.ofNullable(questionService.getQuestionById(1L));
		assertTrue(result.isPresent());
		assertEquals(question.getQuestionId(), result.get().getQuestionId());
		assertEquals(question.getContent(), result.get().getContent());
	}

	@Test
	void testUpdateQuestion() {
		Question existingQuestion = new Question(1L, "Question 1", "Option 1", "Option 2", "Option 3", "Option 4",
				"Option 1", "1", null, null);
		Question updatedQuestion = new Question(1L, "Updated Question", "Option A", "Option B", "Option C", "Option D",
				"Option A", "2", null, null);

		when(questionRepository.findById(1L)).thenReturn(Optional.of(existingQuestion));
		when(questionRepository.save(existingQuestion)).thenReturn(updatedQuestion);
		Question result = questionService.updateQuestion(1L, updatedQuestion);
		assertEquals(updatedQuestion.getQuestionId(), result.getQuestionId());
		assertEquals(updatedQuestion.getContent(), result.getContent());
	}

	@Test
	void testDeleteQuestion() {
		Question question = new Question(1L, "Question 1", "Option 1", "Option 2", "Option 3", "Option 4", "Option 1",
				"1", null, null);
		when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
		questionService.deleteQuestion(1L);
		verify(questionRepository, times(1)).deleteById(1L);
	}
}
