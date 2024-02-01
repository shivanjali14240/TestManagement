package com.testController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.controller.CategoryController;
import com.entity.Category;
import com.exception.CategoryNotFoundException;
import com.service.CategoryService;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

	@Mock
	private CategoryService categoryService;

	@InjectMocks
	private CategoryController categoryController;

	@Test
	void testAddNewCategory() {
		Category categoryToAdd = new Category();
		when(categoryService.addCategory(any(Category.class))).thenReturn(categoryToAdd);
		Category addedCategory = categoryController.addNewCategory(categoryToAdd);
		assertNotNull(addedCategory);
	}

	@Test
	void testGetAllCategory() {
		ArrayList<Category> categories = new ArrayList<>();
		when(categoryService.getAllCatogory()).thenReturn(categories);
		ArrayList<Category> result = categoryController.getAllCategory();
		assertEquals(categories, result);
	}

	@Test
	void testGetCategoryByIdFound() {
		Long categoryId = 1L;
		Category expectedCategory = new Category();
		when(categoryService.getCategoryById(categoryId)).thenReturn(expectedCategory);
		ResponseEntity<?> response = categoryController.getCategoryById(categoryId);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedCategory, response.getBody());
	}

	@Test
	void testGetCategoryByIdNotFound() {
		Long categoryId = 1L;
		when(categoryService.getCategoryById(categoryId)).thenThrow(new CategoryNotFoundException(null));
		ResponseEntity<?> response = categoryController.getCategoryById(categoryId);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Category not found with ID: " + categoryId, response.getBody());
	}

	@Test
	void testUpdateCategoryFound() {
		Long categoryId = 1L;
		Category updatedCategory = new Category();
		when(categoryService.exists(categoryId)).thenReturn(true);
		when(categoryService.updateCategory(any(Category.class))).thenReturn(updatedCategory);
		ResponseEntity<?> response = categoryController.updateCategory(categoryId, updatedCategory);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedCategory, response.getBody());
	}

	@Test
	void testUpdateCategoryNotFound() {
		Long categoryId = 1L;
		Category updatedCategory = new Category();
		when(categoryService.exists(categoryId)).thenReturn(false);
		ResponseEntity<?> response = categoryController.updateCategory(categoryId, updatedCategory);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Category not found with ID: " + categoryId, response.getBody());
	}

	@Test
	void testDeleteCategoryFound() {
		Long categoryId = 1L;
		when(categoryService.exists(categoryId)).thenReturn(true);
		ResponseEntity<?> response = categoryController.deleteCategory(categoryId);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody() == null || response.getBody().toString().isEmpty());
		verify(categoryService, times(1)).deleteCategory(categoryId);
	}

	@Test
	void testDeleteCategoryNotFound() {
		Long categoryId = 1L;
		when(categoryService.exists(categoryId)).thenReturn(false);
		ResponseEntity<?> response = categoryController.deleteCategory(categoryId);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Category not found with ID: " + categoryId, response.getBody());
		verify(categoryService, never()).deleteCategory(anyLong());
	}
}
