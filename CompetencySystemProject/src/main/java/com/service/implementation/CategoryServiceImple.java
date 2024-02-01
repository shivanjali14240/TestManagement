package com.service.implementation;

import java.util.ArrayList;

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
		try {
			return this.categoryRepository.findById(categoryId)
					.orElseThrow(() -> new CategoryNotFoundException("Category with id " + categoryId + " not found"));
		} catch (CategoryNotFoundException categoryNotFoundException) {
			throw categoryNotFoundException;
		}
	}

	@Override
	public Category updateCategory(Category category) {
		try {
			return this.categoryRepository.save(category);
		} catch (Exception e) {
			throw new RuntimeException("Failed to Update category", e);
		}
	}

	@Override
	public void deleteCategory(Long categoryId) {
		try {
			Category category = new Category();
			category.setCategory_id(categoryId);
			this.categoryRepository.delete(category);
		} catch (CategoryNotFoundException categoryNotFoundException) {
			throw new CategoryNotFoundException("Category with id " + categoryId + " not found");
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete category", e);
		}

	}
	
	public boolean exists(Long categoryId) {
		return categoryRepository.existsById(categoryId);
	}

}
