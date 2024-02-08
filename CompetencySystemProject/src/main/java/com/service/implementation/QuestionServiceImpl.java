package com.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.Category;
import com.entity.Question;
import com.exception.QuestionNotFoundException;
import com.repository.CategoryRepository;
import com.repository.QuestionRepository;
import com.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Question> getAllQuestions() {
		return questionRepository.findAll();
	}

	@Override
	public Question getQuestionById(Long id) {
		return questionRepository.findById(id)
				.orElseThrow(() -> new QuestionNotFoundException("Question not found with id: " + id));
	}

	@Override
	public Question saveQuestion(Question question, String name) {
		Category category = categoryRepository.findByName(name)
				.orElseThrow(() -> new IllegalArgumentException("Category not found with name: " + name));
		question.setCategory(category);
		return questionRepository.save(question);
	}

	@Override
	public void deleteQuestion(Long id) {
		try {
			Optional<Question> qusetion = questionRepository.findById(id);
			if (qusetion.isEmpty()) {
				throw new QuestionNotFoundException("Question with id " + id + " not found");
			} else {
				questionRepository.deleteById(id);
			}

		} catch (QuestionNotFoundException questionNotFoundException) {
			throw new QuestionNotFoundException("Question with id " + id + " not found");
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete Question", e);
		}
	}

	@Override
	public Question updateQuestion(Long id, Question updatedQuestion) {
		Optional<Question> optionalQuestion = questionRepository.findById(id);
		if (optionalQuestion.isPresent()) {
			Question question = optionalQuestion.get();
			question.setContent(updatedQuestion.getContent());
			question.setOption1(updatedQuestion.getOption1());
			question.setOption2(updatedQuestion.getOption2());
			question.setOption3(updatedQuestion.getOption3());
			question.setOption4(updatedQuestion.getOption4());
			question.setAnswer(updatedQuestion.getAnswer());
			question.setMarks(updatedQuestion.getMarks());
			return questionRepository.save(question);
		} else {
			throw new QuestionNotFoundException("Question not found with id: " + id);
		}
	}

	/*
	 * public List<Question> importQuestionsFromExcel(InputStream excelInputStream,
	 * Long categoryId) throws EncryptedDocumentException, IOException {
	 * 
	 * Workbook workbook = WorkbookFactory.create(excelInputStream); Sheet sheet =
	 * workbook.getSheetAt(0); List<Question> importedQuestions = new ArrayList<>();
	 * 
	 * for (Row row : sheet) { if (row.getRowNum() == 0) { continue; }
	 * 
	 * Question question = createQuestionFromRow(row, categoryId); if (question !=
	 * null) { importedQuestions.add(questionRepository.save(question)); } }
	 * 
	 * return importedQuestions; }
	 * 
	 * private Question createQuestionFromRow(Row row, Long categoryId) { Question
	 * question = new Question();
	 * question.setContent(getStringValueFromCell(row.getCell(0)));
	 * question.setOption1(getStringValueFromCell(row.getCell(1)));
	 * question.setOption2(getStringValueFromCell(row.getCell(2)));
	 * question.setOption3(getStringValueFromCell(row.getCell(3)));
	 * question.setOption4(getStringValueFromCell(row.getCell(4)));
	 * question.setAnswer(getStringValueFromCell(row.getCell(5)));
	 * question.setMarks(getStringValueFromCell(row.getCell(6))); Category category
	 * = categoryRepository.findById(categoryId).orElse(null);
	 * question.setCategory(category);
	 * 
	 * return question; }
	 * 
	 * private String getStringValueFromCell(Cell cell) { if (cell == null) { return
	 * null; } switch (cell.getCellType()) { case STRING: return
	 * cell.getStringCellValue(); case NUMERIC: return
	 * String.valueOf(cell.getNumericCellValue()); default: return null; } }
	 */

}
