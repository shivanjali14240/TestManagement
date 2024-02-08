package com.testService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.entity.Category;
import com.exception.CategoryNotFoundException;
import com.repository.CategoryRepository;
import com.service.implementation.CategoryServiceImple;

@SpringBootTest
class CategoryServiceTest {

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private CategoryServiceImple categoryService;

	@Test
	public void testAddCategory_Success() {
		Category category = new Category();
		category.setName("Test Category");
		category.setDescription("Test description");
		when(categoryRepository.findByName(category.getName())).thenReturn(Optional.empty());
		when(categoryRepository.save(category)).thenReturn(category);
		Category result = categoryService.addCategory(category);
		assertNotNull(result);
		assertEquals(category, result);
	}

	@Test
	public void testAddCategory_DuplicateName() {
		Category category = new Category();
		category.setName("Test Category");
		category.setDescription("Test description");
		when(categoryRepository.findByName(category.getName())).thenReturn(Optional.of(category));
		assertThrows(IllegalArgumentException.class, () -> categoryService.addCategory(category));
	}

	@Test
	public void testGetAllCategory_Success() {
		List<Category> mockCategories = new ArrayList<>();
		Category category1 = new Category();
		category1.setCategory_id(1L);
		category1.setName("Category 1");
		category1.setDescription("Description 1");
		Category category2 = new Category();
		category2.setCategory_id(2L);
		category2.setName("Category 2");
		category2.setDescription("Description 2");
		mockCategories.add(category1);
		mockCategories.add(category2);
		when(categoryRepository.findAll()).thenReturn(mockCategories);
		List<Category> result = categoryService.getAllCatogory();
		assertNotNull(result);
		assertEquals(mockCategories.size(), result.size());
		assertEquals(mockCategories.get(0), result.get(0));
		assertEquals(mockCategories.get(1), result.get(1));
	}

	@Test
	public void testGetCategoryById_Success() {
		Long categoryId = 1L;
		Category category = new Category();
		category.setCategory_id(categoryId);
		category.setName("Test Category");
		category.setDescription("Test description");
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		Category result = categoryService.getCategoryById(categoryId);
		assertNotNull(result);
		assertEquals(category, result);
	}

	@Test
	public void testGetCategoryById_CategoryNotFound() {
		Long categoryId = 1L;
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
		assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
	}

	@Test
	public void testUpdateCategory_Success() {
		Long categoryId = 1L;
		Category updatedCategory = new Category();
		updatedCategory.setCategory_id(categoryId);
		updatedCategory.setName("Updated Category");
		updatedCategory.setDescription("Updated description");
		when(categoryRepository.existsById(categoryId)).thenReturn(true);
		when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
		Category result = categoryService.updateCategory(categoryId, updatedCategory);
		assertNotNull(result);
		assertEquals(updatedCategory, result);
	}

	@Test
	public void testUpdateCategory_CategoryNotFound() {
		Long categoryId = 1L;
		Category updatedCategory = new Category();
		updatedCategory.setCategory_id(categoryId);
		updatedCategory.setName("Updated Category");
		updatedCategory.setDescription("Updated description");
		when(categoryRepository.existsById(categoryId)).thenReturn(false);
		assertThrows(CategoryNotFoundException.class,
				() -> categoryService.updateCategory(categoryId, updatedCategory));
	}

	@Test
	public void testDeleteCategory_Success() {
		Long categoryId = 1L;
		when(categoryRepository.existsById(categoryId)).thenReturn(true);
		categoryService.deleteCategory(categoryId);
		verify(categoryRepository, times(1)).deleteById(categoryId);
	}

	@Test
	public void testDeleteCategory_CategoryNotFound() {
		Long categoryId = 1L;
		when(categoryRepository.existsById(categoryId)).thenReturn(false);
		assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(categoryId));
	}
}
