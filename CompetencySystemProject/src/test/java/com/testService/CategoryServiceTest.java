package com.testService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.entity.Category;
import com.exception.CategoryNotFoundException;
import com.repository.CategoryRepository;
import com.service.CategoryService;
import com.service.implementation.CategoryServiceImple;

public class CategoryServiceTest {
	   @Mock
	   private CategoryRepository categoryRepository;

	    @InjectMocks
	    private CategoryService categoryService = new CategoryServiceImple();

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void testAddCategory() {
	        Category categoryToAdd = new Category();

	        when(categoryRepository.save(categoryToAdd)).thenReturn(categoryToAdd);

	        Category addedCategory = categoryService.addCategory(categoryToAdd);

	        assertNotNull(addedCategory);
	    }

	    @Test
	    void testGetAllCategory() {
	        ArrayList<Category> categories = new ArrayList<>();

	        when(categoryRepository.findAll()).thenReturn(categories);

	        ArrayList<Category> resultCategories = categoryService.getAllCatogory();

	        assertNotNull(resultCategories);
	        assertEquals(categories.size(), resultCategories.size());
	    }

	    @Test
	    void testGetCategoryById() {
	        Long categoryId = 1L;
	        Category category = new Category();

	        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

	        Category resultCategory = categoryService.getCategoryById(categoryId);

	        assertNotNull(resultCategory);
	        
	    }

	    @Test
	    void testGetCategoryByIdNotFoundException() {
	        Long categoryId = 1L;

	        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

	        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
	    }

	    @Test
	    void testUpdateCategory() {
	        Category categoryToUpdate = new Category();

	        when(categoryRepository.save(categoryToUpdate)).thenReturn(categoryToUpdate);

	        Category updatedCategory = categoryService.updateCategory(categoryToUpdate);

	        assertNotNull(updatedCategory);
	    }

	    @Test
	    void testDeleteCategory() {
	        Long categoryId = 1L;
	        Category categoryToDelete = new Category();

	        doNothing().when(categoryRepository).delete(categoryToDelete);

	        assertDoesNotThrow(() -> categoryService.deleteCategory(categoryId));
	    }

	    @Test
	    void testDeleteCategoryNotFoundException() {
	        Long categoryId = 1L;

	        doThrow(new CategoryNotFoundException("Category with id " + categoryId + " not found"))
	                .when(categoryRepository).delete(any());

	        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(categoryId));
	    }

	    @Test
	    void testDeleteCategoryRuntimeException() {
	        Long categoryId = 1L;

	        doThrow(new RuntimeException("Failed to delete category")).when(categoryRepository).delete(any());

	        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(categoryId));
	    }

}
