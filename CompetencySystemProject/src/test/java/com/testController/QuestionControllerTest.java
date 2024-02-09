package com.testController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import com.controller.QuestionController;
import com.entity.Question;
import com.exception.CategoryNotFoundException;
import com.exception.QuestionNotFoundException;
import com.service.QuestionService;

@ExtendWith(SpringExtension.class)
class QuestionControllerTest {

	@Mock
	private QuestionService questionService;

	@InjectMocks
	private QuestionController questionController;

	@Test
	void testGetAllQuestions_Positive() {
		List<Question> questions = new ArrayList<>();
		questions.add(
				createQuestion(1L, "Question 1", "Option 1", "Option 2", "Option 3", "Option 4", "Answer 1", "10"));
		questions.add(
				createQuestion(2L, "Question 2", "Option 1", "Option 2", "Option 3", "Option 4", "Answer 2", "20"));
		when(questionService.getAllQuestions()).thenReturn(questions);
		List<Question> result = questionController.getAllQuestions();
		assertEquals(2, result.size());
	}

	@Test
	void testGetQuestionById_Positive() {
		Long questionId = 1L;
		Question question = createQuestion(questionId, "Question 1", "Option 1", "Option 2", "Option 3", "Option 4",
				"Answer 1", "10");
		when(questionService.getQuestionById(questionId)).thenReturn(question);
		ResponseEntity<?> response = questionController.getQuestionById(questionId);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(question, response.getBody());
	}

	@Test
	void testGetQuestionById_Negative() {
		Long questionId = 1L;
		when(questionService.getQuestionById(questionId)).thenThrow(QuestionNotFoundException.class);
		ResponseEntity<?> response = questionController.getQuestionById(questionId);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Id is not exist", response.getBody());
	}

	@Test
	void testCreateQuestion_Positive() {
		Question question = createQuestion(1L, "Question 1", "Option 1", "Option 2", "Option 3", "Option 4", "Answer 1",
				"10");
		when(questionService.saveQuestion(any(Question.class), any(String.class))).thenReturn(question);
		ResponseEntity<?> response = questionController.createQuestion(question, "categoryName");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(question, response.getBody());
	}

	@Test
	void testCreateQuestion_Negative() {
		Question question = createQuestion(1L, "Question 1", "Option 1", "Option 2", "Option 3", "Option 4", "Answer 1",
				"10");
		when(questionService.saveQuestion(any(Question.class), any(String.class)))
				.thenThrow(IllegalArgumentException.class);
		ResponseEntity<?> response = questionController.createQuestion(question, "categoryName");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testUpdateQuestion_Positive() {
		Long questionId = 1L;
		Question updatedQuestion = createQuestion(questionId, "Updated Question", "Updated Option 1",
				"Updated Option 2", "Updated Option 3", "Updated Option 4", "Updated Answer", "15");
		when(questionService.updateQuestion(questionId, updatedQuestion)).thenReturn(updatedQuestion);
		ResponseEntity<?> response = questionController.updateQuestion(questionId, updatedQuestion);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedQuestion, response.getBody());
	}

	@Test
	void testUpdateQuestion_Negative() {
		Long questionId = 1L;
		Question updatedQuestion = createQuestion(questionId, "Updated Question", "Updated Option 1",
				"Updated Option 2", "Updated Option 3", "Updated Option 4", "Updated Answer", "15");
		when(questionService.updateQuestion(questionId, updatedQuestion)).thenThrow(QuestionNotFoundException.class);
		ResponseEntity<?> response = questionController.updateQuestion(questionId, updatedQuestion);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Id is not found", response.getBody());
	}

	@Test
	void testDeleteQuestion_Positive() {
		Long questionId = 1L;
		ResponseEntity<String> response = questionController.deleteQuestion(questionId);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Question with ID " + questionId + " deleted successfully", response.getBody());
	}

	@Test
	void testDeleteQuestion_Negative() {
		Long questionId = 1L;
		doThrow(QuestionNotFoundException.class).when(questionService).deleteQuestion(questionId);
		ResponseEntity<String> response = questionController.deleteQuestion(questionId);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Question not found with ID: " + questionId, response.getBody());
	}

	@Test
	public void testUploadQuestionsWithValidFile() throws IOException, CategoryNotFoundException {
		MultipartFile file = mock(MultipartFile.class);
		List<Question> questions = new ArrayList<>();
		questions.add(new Question());

		when(questionService.saveQuestionsFromExcel(file)).thenReturn(questions);

		ResponseEntity<?> response = questionController.uploadQuestions(file);

		assert response.getStatusCode().equals(HttpStatus.OK);
		assert response.getBody().equals(questions);
	}

	@Test
	public void testUploadQuestionsWithEmptyFile() {
		MultipartFile file = mock(MultipartFile.class);
		when(file.isEmpty()).thenReturn(true);

		ResponseEntity<?> response = questionController.uploadQuestions(file);

		assert response.getStatusCode().equals(HttpStatus.BAD_REQUEST);
		assert response.getBody().equals("File is empty");
	}

	@Test
	public void testUploadQuestionsWithCategoryNotFoundException() throws IOException, CategoryNotFoundException {
		MultipartFile file = mock(MultipartFile.class);
		when(questionService.saveQuestionsFromExcel(file))
				.thenThrow(new CategoryNotFoundException("Category not found"));

		ResponseEntity<?> response = questionController.uploadQuestions(file);

		assert response.getStatusCode().equals(HttpStatus.BAD_REQUEST);
		assert response.getBody().equals("Category not found: Category not found");
	}

	@Test
	public void testUploadQuestionsWithIOException() throws IOException, CategoryNotFoundException {
		MultipartFile file = mock(MultipartFile.class);
		when(questionService.saveQuestionsFromExcel(file)).thenThrow(new IOException());

		ResponseEntity<?> response = questionController.uploadQuestions(file);

		assert response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR);
		assert response.getBody().equals("Failed to upload questions. Please try again later.");
	}

	private Question createQuestion(Long id, String content, String option1, String option2, String option3,
			String option4, String answer, String marks) {
		Question question = new Question();
		question.setQuestionId(id);
		question.setContent(content);
		question.setOption1(option1);
		question.setOption2(option2);
		question.setOption3(option3);
		question.setOption4(option4);
		question.setAnswer(answer);
		question.setMarks(marks);
		return question;
	}
}