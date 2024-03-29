package com.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Category;
import com.exception.CategoryNotFoundException;
import com.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/testmanagement/api/v1/categories")
@Slf4j
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping
	public ResponseEntity<?> addNewCategory(@RequestBody Category category) {
		try {
			log.info("Adding a new category: {}", category);
			return ResponseEntity.ok(categoryService.addCategory(category));
		} catch (IllegalArgumentException e) {
			log.error("Error occurred while saving duplicate category by name", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping
	public ArrayList<Category> getAllCategory() {
		log.info("Getting all categories");
		return categoryService.getAllCatogory();
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<?> getCategoryById(@PathVariable("categoryId") Long categoryId) {
		try {
			Category category = categoryService.getCategoryById(categoryId);
			return ResponseEntity.ok(category);
		} catch (CategoryNotFoundException e) {
			log.error("Category not found with ID: {}", categoryId);
			return ResponseEntity.status(404).body("Category not found with ID: " + categoryId);
		} catch (Exception e) {
			log.error("Error occurred while retrieving category by ID", e);
			return ResponseEntity.status(500).body("Internal server error");
		}
	}

	@PutMapping("/{categoryId}")
	public ResponseEntity<?> updateCategory(@PathVariable("categoryId") Long categoryId,
			@RequestBody Category updatedCategory) {
		try {
			log.info("Updating category with ID {}: {}", categoryId, updatedCategory);
			Category updatedCategoryResponse = categoryService.updateCategory(categoryId, updatedCategory);
			return ResponseEntity.ok(updatedCategoryResponse);
		} catch (CategoryNotFoundException e) {
			log.error("Category not found with ID: {}", categoryId);
			return ResponseEntity.status(404).body("Category not found with ID: " + categoryId);
		} catch (Exception e) {
			log.error("Error occurred while updating category", e);
			return ResponseEntity.status(500).body("Internal server error");
		}
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") Long categoryId) {
		try {
			log.info("Deleting category with ID: {}", categoryId);
			categoryService.deleteCategory(categoryId);
			return ResponseEntity.ok("Category with ID " + categoryId + " deleted successfully");
		} catch (CategoryNotFoundException e) {
			log.error("Category not found with ID: {}", categoryId);
			return ResponseEntity.status(404).body("Category not found with ID: " + categoryId);
		} catch (Exception e) {
			log.error("Error occurred while deleting category", e);
			return ResponseEntity.status(500).body("Internal server error");
		}
	}
}
