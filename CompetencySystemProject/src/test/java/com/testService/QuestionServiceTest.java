package com.testService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

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
	
	@Test
    void testSaveQuestionsFromExcel() throws IOException {
        byte[] excelData = generateExcelBytes();
        Category mockCategory = new Category();
        mockCategory.setName("Test Category");
        when(categoryRepository.findByName("Test Category")).thenReturn(Optional.of(mockCategory));
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> {
            Question savedQuestion = invocation.getArgument(0);
            savedQuestion.setQuestionId(1l); 
            return savedQuestion;
        });
        MultipartFile multipartFile = new MockMultipartFile("file", excelData);
        List<Question> savedQuestions = questionService.saveQuestionsFromExcel(multipartFile);
        assertNotNull(savedQuestions);
        assertFalse(savedQuestions.isEmpty());
        assertEquals(2, savedQuestions.size());
    }

    @Test
    void testSaveQuestionsFromExcelCategoryNotFound() throws IOException {
        byte[] excelData = generateExcelBytes();
        when(categoryRepository.findByName("Test Category")).thenReturn(Optional.empty());
        MultipartFile multipartFile = new MockMultipartFile("file", excelData);
        assertThrows(IllegalArgumentException.class, () -> questionService.saveQuestionsFromExcel(multipartFile));
    }

    @Test
    void testSaveQuestionsFromExcelWorkbookCreationFailed() throws IOException {
        byte[] excelData = "Invalid Excel Data".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("file", excelData);
        assertThrows(IOException.class, () -> questionService.saveQuestionsFromExcel(multipartFile));
    }

    private byte[] generateExcelBytes() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Content");
        headerRow.createCell(1).setCellValue("Option1");
        headerRow.createCell(2).setCellValue("Option2");
        headerRow.createCell(3).setCellValue("Option3");
        headerRow.createCell(4).setCellValue("Option4");
        headerRow.createCell(5).setCellValue("Answer");
        headerRow.createCell(6).setCellValue("Marks");
        headerRow.createCell(7).setCellValue("Category");

        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("Question 1");
        row1.createCell(1).setCellValue("Option 1");
        row1.createCell(2).setCellValue("Option 2");
        row1.createCell(3).setCellValue("Option 3");
        row1.createCell(4).setCellValue("Option 4");
        row1.createCell(5).setCellValue("Answer 1");
        row1.createCell(6).setCellValue(5);
        row1.createCell(7).setCellValue("Test Category");

        Row row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("Question 2");
        row2.createCell(1).setCellValue("Option 1");
        row2.createCell(2).setCellValue("Option 2");
        row2.createCell(3).setCellValue("Option 3");
        row2.createCell(4).setCellValue("Option 4");
        row2.createCell(5).setCellValue("Answer 2");
        row2.createCell(6).setCellValue(10);
        row2.createCell(7).setCellValue("Test Category");

        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            workbook.write(bos);
            bytes = bos.toByteArray();
        }
        return bytes;
    }

}
