package com.testController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.controller.QuestionController;
import com.entity.Question;
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

	/*
	 * @Test void testImportQuestions_Positive() throws IOException { MultipartFile
	 * file = new MockMultipartFile("file", "filename.xlsx",
	 * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
	 * "content".getBytes()); Long categoryId = 1L; List<Question> importedQuestions
	 * = new ArrayList<>();
	 * when(questionService.importQuestionsFromExcel(any(InputStream.class),
	 * any(Long.class))) .thenReturn(importedQuestions);
	 * ResponseEntity<List<Question>> response =
	 * questionController.importQuestions(file, categoryId);
	 * assertEquals(HttpStatus.OK, response.getStatusCode());
	 * assertEquals(importedQuestions, response.getBody()); }
	 * 
	 * @Test void testImportQuestions_Negative() throws IOException { MultipartFile
	 * file = new MockMultipartFile("file", "uploadFile.xlsx",
	 * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
	 * "content".getBytes()); Long categoryId = 1L;
	 * when(questionService.importQuestionsFromExcel(any(InputStream.class),
	 * any(Long.class))) .thenThrow(IOException.class);
	 * ResponseEntity<List<Question>> response =
	 * questionController.importQuestions(file, categoryId);
	 * assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()); }
	 */

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