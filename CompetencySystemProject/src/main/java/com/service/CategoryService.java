package com.service;

import java.util.ArrayList;

import com.entity.Category;

public interface CategoryService {

	Category addCategory(Category category);

	ArrayList<Category> getAllCatogory();

	Category getCategoryById(Long categoryId);

	void deleteCategory(Long categoryId);

	public boolean exists(Long categoryId);

	Category updateCategory(Long categoryId, Category updatedCategory);
}
