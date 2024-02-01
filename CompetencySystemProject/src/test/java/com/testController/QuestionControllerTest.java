package com.testController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.controller.QuestionController;
import com.entity.Question;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.QuestionService;

@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

	@MockBean
	private QuestionService questionService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetAllQuestions() throws Exception {
		List<Question> questions = List.of(
				new Question(1L, "Question 1", "Option 1", "Option 2", "Option 3", "Option 4", "Answer 1", 10, null, null),
				new Question(2L, "Question 2", "Option 1", "Option 2", "Option 3", "Option 4", "Answer 2", 15, null, null));

		when(questionService.getAllQuestions()).thenReturn(questions);

		mockMvc.perform(MockMvcRequestBuilders.get("/testmanagement/api/v1/questions")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2));
	}

	@Test
	public void testGetQuestionById() throws Exception {
		long questionId = 1L;
		Question question = new Question(questionId, "Question 1", "Option 1", "Option 2", "Option 3", "Option 4",
				"Answer 1", 10, null, null);

		when(questionService.getQuestionById(questionId)).thenReturn(question);

		mockMvc.perform(MockMvcRequestBuilders.get("/testmanagement/api/v1/questions/{id}", questionId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.questionId").value(questionId));
	}

	@Test
	public void testCreateQuestion() throws Exception {
		Question questionToCreate = new Question(null, "New Question", "Option 1", "Option 2", "Option 3", "Option 4",
				"Answer", 5, null, null);
		Question createdQuestion = new Question(1L, "New Question", "Option 1", "Option 2", "Option 3", "Option 4",
				"Answer", 5, null, null);

		when(questionService.saveQuestion(any(Question.class))).thenReturn(createdQuestion);

		mockMvc.perform(MockMvcRequestBuilders.post("/testmanagement/api/v1/questions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(questionToCreate))).andExpect(status().isOk())
				.andExpect(jsonPath("$.questionId").value(1L));
	}

	@Test
	public void testUpdateQuestion() throws Exception {
		long questionId = 1L;
		Question existingQuestion = new Question(questionId, "Existing Question", "Option 1", "Option 2", "Option 3",
				"Option 4", "Answer", 10, null, null);
		Question updatedQuestion = new Question(questionId, "Updated Question", "Option 1", "Option 2", "Option 3",
				"Option 4", "Answer", 15, null, null);

		when(questionService.getQuestionById(questionId)).thenReturn(existingQuestion);
		when(questionService.saveQuestion(any(Question.class))).thenReturn(updatedQuestion);

		mockMvc.perform(MockMvcRequestBuilders.put("/testmanagement/api/v1/questions/{id}", questionId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedQuestion)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.questionId").value(questionId))
				.andExpect(jsonPath("$.content").value(updatedQuestion.getContent())); 
	}

	@Test
	public void testDeleteQuestion() throws Exception {
		long questionId = 1L;

		when(questionService.getQuestionById(questionId)).thenReturn(
				new Question(questionId, "Question", "Option 1", "Option 2", "Option 3", "Option 4", "Answer", 10, null, null));

		mockMvc.perform(MockMvcRequestBuilders.delete("/testmanagement/api/v1/questions/{id}", questionId)).andExpect(status().isOk());

		verify(questionService).deleteQuestion(questionId);
	}
}
