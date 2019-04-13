package com.sontran.jdbctemplate.demo.repository;

import com.sontran.jdbctemplate.datasource.DataSource;

public class MyDataSource extends DataSource {
	
	private static DataSource instance = null;
	
	public static DataSource getInstance() {
		if (instance == null) {
			instance = new DataSource("jdbc:mysql://localhost:3306/jdbcTemplate?useUnicode=true&characterEncoding=UTF-8",
					"root", "", "com.mysql.jdbc.Driver");
		}
		return 	instance;
	}
}
