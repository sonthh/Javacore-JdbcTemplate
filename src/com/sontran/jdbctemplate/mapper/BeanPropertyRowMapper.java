package com.sontran.jdbctemplate.mapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanPropertyRowMapper<T> implements RowMapper<T> {
	private Class<T> clazz;

	public BeanPropertyRowMapper(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T mapRow(ResultSet rs) {
		T result = null;
		try {
			Field[] fields = clazz.getDeclaredFields();
			//CONSTUCTOR 0 PARAM
			result = clazz.getConstructor().newInstance();
			for (Field field : fields) {
				field.setAccessible(true);//ACESSS PRIVATE PROPERTIES
				try {
					//<=>: objCategory.setName(rs.getString("name"));
					field.set(result, rs.getObject(field.getName())); 
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
