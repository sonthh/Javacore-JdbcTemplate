package com.sontran.jdbctemplate.query;

import java.util.List;

import com.sontran.jdbctemplate.mapper.BeanPropertyRowMapper;
import com.sontran.jdbctemplate.mapper.RowMapper;


public interface IJdbcTemplate {
	// RETURN LIST OBJECT
	<T> List<T> query(String sql, Object[] parameters, RowMapper<T> rowMapper);
	<T> List<T> query(String sql, RowMapper<T> rowMapper);
	<T> List<T> query(String sql, Object[] parameters, BeanPropertyRowMapper<T> rowMapper);
	<T> List<T> query(String sql, BeanPropertyRowMapper<T> rowMapper);
	
	// FOR OBJECT DATATYPE
	<T> T queryForObject(String sql, Object[] parameters, RowMapper<T> rowMapper);
	<T> T queryForObject(String sql, RowMapper<T> rowMapper);
	<T> T queryForObject(String sql, Object[] parameters, BeanPropertyRowMapper<T> rowMapper);
	<T> T queryForObject(String sql, BeanPropertyRowMapper<T> rowMapper);
	
	// FOR PRIMITY DATATYPE 
	<T> T queryForObject(String sql, Class<T> clazz);
	<T> T queryForObject(String sql, Object[] parameters, Class<T> clazz);
	
	<T> List<T> queryForList(String sql, Class<T> clazz);
	<T> List<T> queryForList(String sql, Object[] parameters, Class<T> clazz);
	
	// UPDATE
	boolean update(String sql, Object[] parameters);
	boolean update(String sql);
	
	// INSERT
	//Long insert(String sql, Object... parameters);
	Long insert(String sql, Object[] parameters);
	Long insert(String sql);
}
