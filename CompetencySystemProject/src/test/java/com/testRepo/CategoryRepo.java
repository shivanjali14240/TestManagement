package com.testRepo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.entity.Category;
import com.repository.CategoryRepository;


@SpringBootTest
public class CategoryRepo {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@BeforeEach
	public void setUp() {
		Category category1 = new Category();
		category1.setName("Category1");
		categoryRepository.save(category1);

		Category category2 = new Category();
		category2.setName("Category2");
		categoryRepository.save(category2);
	}

	@AfterEach
	public void tearDown() {
		categoryRepository.deleteAll();
	}

	@Test
	public void testFindByName_Exists() {
		Optional<Category> foundCategory = categoryRepository.findByName("Category1");
		assertTrue(foundCategory.isPresent());
		assertEquals("Category1", foundCategory.get().getName());
	}

	@Test
	public void testFindByName_NotExists() {
		Optional<Category> foundCategory = categoryRepository.findByName("NonExistentCategory");
		assertFalse(foundCategory.isPresent());
	}

	@Test
	public void testSaveCategory() {
		Category category = new Category();
		category.setName("NewCategory");
		Category savedCategory = categoryRepository.save(category);
		assertNotNull(savedCategory);
		assertEquals("NewCategory", savedCategory.getName());
	}

	@Test
	public void testDeleteCategory() {
		Category category = categoryRepository.findByName("Category2").orElse(null);
		assertNotNull(category);
		categoryRepository.delete(category);
		Optional<Category> deletedCategory = categoryRepository.findById(category.getCategory_id());
		assertFalse(deletedCategory.isPresent());
	}

}
