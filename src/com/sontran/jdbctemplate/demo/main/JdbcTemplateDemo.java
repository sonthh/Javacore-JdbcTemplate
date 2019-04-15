package com.sontran.jdbctemplate.demo.main;

import java.util.ArrayList;
import java.util.List;

import com.sontran.jdbctemplate.demo.entity.Category;
import com.sontran.jdbctemplate.demo.repository.CategoryRepository;

public class JdbcTemplateDemo {
	
	public static void main(String[] args) {
		CategoryRepository dao = new CategoryRepository();
		List<Category> categories;
		Category category;
		List<String> listCategoryName;
		String name;
		
		System.out.println("ONE ITEM BY ID");
		category = dao.findOneById(4);
		System.out.println(category);
		
		System.out.println("ALL ITEMS");
		categories = dao.finnAll();
		categories.forEach(item -> {
			System.out.println(item);
		});
		
		System.out.println("ALL ITEMS RESULTSET EXTRACTOR");
		categories = dao.finnAll2();
		categories.forEach(item -> {
			System.out.println(item);
		});
		
		System.out.println("ALL ITEMS id < 5 RESULTSET EXTRACTOR");
		categories = dao.findByIdLessThan(5);
		categories.forEach(item -> {
			System.out.println(item);
		});
		
		System.out.println("USING NOT IN OPERATOR (SET ARRAY IN JDBC)");
		
		
		
		System.out.println("\nONE ITEM BY ID: 3");
		category = dao.findOneById(3);
		System.out.println(category);
		
		System.out.println("UPDATE ITEM ID: 3");
		category.setName("new Name");
		if (dao.update(category)) {
			System.out.println("update success!");
		} else {
			System.out.println("update fail!");
		}
		
		System.out.println("\nONE ITEM BY ID: 3");
		category = dao.findOneById(3);
		System.out.println(category);
		
		
		System.out.println("ALL CATEGORY NAME");
		listCategoryName = dao.findAllName();
		listCategoryName.forEach(item -> {
			System.out.println(item);
		});
		
		System.out.println("\nALL CATEGORY NAME 2");
		listCategoryName = dao.findAllName2();
		listCategoryName.forEach(item -> {
			System.out.println(item);
		});
		
		System.out.println("\nCATEGORY NAME BY ID");
		name = dao.findCategoryNameById(2);
		System.out.println(name);
		
		System.out.println("\nCATEGORY NAME BY ID 2");
		name = dao.findCategoryNameById2(2);
		System.out.println(name);
		
		
		//DÙNG NOT IN
		System.out.println("NOT IN OPERATOR");
		categories = dao.findByIdNotIn(new int[] { 1, 2, 3 });
		System.out.println(categories.size());
		categories.forEach(item -> {
			System.out.println(item);
		});
		
		System.out.println("UPDATE ITEM");
		Long id = dao.save(new Category(0, "new category"));
		System.out.println("id mới thêm; " + id);
		
		
		List<Category> list = new ArrayList<>();
		list.add(new Category(null, "new category 1"));
		list.add(new Category(null, "new category 2"));
		dao.saveListItem(list);
		System.out.println("ALL ITEMS");
		categories = dao.finnAll();
		categories.forEach(item -> {
			System.out.println(item);
		});
	}
	
	
	
}
