package com.testController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.controller.CategoryController;
import com.entity.Category;
import com.exception.CategoryNotFoundException;
import com.service.CategoryService;

@SpringBootTest
class CategoryControllerTest {

	@Mock
	private CategoryService categoryService;

	@InjectMocks
	private CategoryController categoryController;

	@Test
	public void testAddNewCategory_Success() {
		Category category = new Category();
		category.setCategory_id(1L);
		category.setName("Test Category");
		category.setDescription("This is a test category");
		when(categoryService.addCategory(category)).thenReturn(category);
		ResponseEntity<?> result = categoryController.addNewCategory(category);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(category, result.getBody());
	}

	@Test
	public void testGetAllCategory_Success() {
		ArrayList<Category> mockCategories = new ArrayList<>();
		Category category = new Category();
		category.setCategory_id(1L);
		category.setName("Test Category");
		category.setDescription("This is a test category");
		mockCategories.add(category);
		when(categoryService.getAllCatogory()).thenReturn(mockCategories);
		ArrayList<Category> result = categoryController.getAllCategory();
		assertNotNull(result);
		assertEquals(mockCategories.size(), result.size());
		assertEquals(mockCategories.get(0), result.get(0));
	}

	@Test
	public void testGetCategoryById_Success() {
		Long categoryId = 1L;
		Category category = new Category();
		category.setCategory_id(categoryId);
		category.setName("Test Category");
		category.setDescription("This is a test category");
		when(categoryService.getCategoryById(categoryId)).thenReturn(category);
		ResponseEntity<?> result = categoryController.getCategoryById(categoryId);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(category, result.getBody());
	}

	@Test
	public void testGetCategoryById_CategoryNotFound() {
		Long categoryId = 1L;
		when(categoryService.getCategoryById(categoryId))
				.thenThrow(new CategoryNotFoundException("Category not found"));
		ResponseEntity<?> result = categoryController.getCategoryById(categoryId);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertEquals("Category not found with ID: " + categoryId, result.getBody());
	}

	@Test
	public void testUpdateCategory_Success() {
		Long categoryId = 1L;
		Category updatedCategory = new Category();
		updatedCategory.setCategory_id(categoryId);
		updatedCategory.setName("Updated Category");
		when(categoryService.updateCategory(categoryId, updatedCategory)).thenReturn(updatedCategory);
		ResponseEntity<?> result = categoryController.updateCategory(categoryId, updatedCategory);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(updatedCategory, result.getBody());
	}

	@Test
	public void testUpdateCategory_CategoryNotFound() {
		Long categoryId = 1L;
		Category updatedCategory = new Category();
		updatedCategory.setCategory_id(categoryId);
		updatedCategory.setName("Updated Category");
		when(categoryService.updateCategory(categoryId, updatedCategory))
				.thenThrow(new CategoryNotFoundException("Category not found"));
		ResponseEntity<?> result = categoryController.updateCategory(categoryId, updatedCategory);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertEquals("Category not found with ID: " + categoryId, result.getBody());
	}

	@Test
	public void testDeleteCategory_Success() {
		Long categoryId = 1L;
		ResponseEntity<?> result = categoryController.deleteCategory(categoryId);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("Category with ID " + categoryId + " deleted successfully", result.getBody());
	}

	@Test
	public void testDeleteCategory_CategoryNotFound() {
		Long categoryId = 1L;
		doThrow(new CategoryNotFoundException("Category not found")).when(categoryService).deleteCategory(categoryId);
		ResponseEntity<?> result = categoryController.deleteCategory(categoryId);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertEquals("Category not found with ID: " + categoryId, result.getBody());
	}
}
