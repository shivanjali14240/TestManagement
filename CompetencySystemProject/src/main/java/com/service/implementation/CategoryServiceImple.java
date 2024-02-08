package com.service.implementation;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.Category;
import com.exception.CategoryNotFoundException;
import com.repository.CategoryRepository;
import com.service.CategoryService;

@Service
public class CategoryServiceImple implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public Category addCategory(Category category) {
		return this.categoryRepository.save(category);
	}

	@Override
	public ArrayList<Category> getAllCatogory() {
		return (ArrayList<Category>) this.categoryRepository.findAll();
	}

	@Override
	public Category getCategoryById(Long categoryId) {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		return optionalCategory
				.orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
	}

	@Override
	public Category updateCategory(Long categoryId, Category updatedCategory) {
		if (exists(categoryId)) {
			updatedCategory.setCategory_id(categoryId);
			return categoryRepository.save(updatedCategory);
		} else {
			throw new CategoryNotFoundException("Category not found with ID: " + categoryId);
		}
	}

	@Override
	public void deleteCategory(Long categoryId) {
		if (exists(categoryId)) {
			categoryRepository.deleteById(categoryId);
		} else {
			throw new CategoryNotFoundException("Category not found with ID: " + categoryId);
		}
	}

	public boolean exists(Long categoryId) {
		return categoryRepository.existsById(categoryId);
	}

}
