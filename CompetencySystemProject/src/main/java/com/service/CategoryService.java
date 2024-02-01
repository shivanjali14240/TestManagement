package com.service;

import java.util.ArrayList;

import com.entity.Category;

public interface CategoryService {

	Category addCategory(Category category);

	ArrayList<Category> getAllCatogory();

	Category getCategoryById(Long categoryId);

	Category updateCategory(Category category);

	void deleteCategory(Long categoryId);
	public boolean exists(Long categoryId);
}
