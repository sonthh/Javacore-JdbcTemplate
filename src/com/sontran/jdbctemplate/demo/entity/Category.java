package com.sontran.jdbctemplate.demo.entity;

public class Category {
	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		//System.out.println("Category.setId()");
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		//System.out.println("Category.setName()");
		this.name = name;
	}

	public Category(int id, String name) {
		super();
		//System.out.println("Category.Category()");
		this.id = id;
		this.name = name;
	}

	public Category() {
		super();
		//System.out.println("Category.Category()");
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + "]";
	}

}
