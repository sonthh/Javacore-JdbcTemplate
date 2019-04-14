package com.sontran.jdbctemplate.demo.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.sontran.jdbctemplate.demo.entity.Category;
import com.sontran.jdbctemplate.mapper.BeanPropertyRowMapper;
import com.sontran.jdbctemplate.mapper.RowMapper;
import com.sontran.jdbctemplate.query.JdbcTemplate;
import com.sontran.jdbctemplate.setter.BatchPreparedStatementSetter;

public class CategoryRepository {
	private JdbcTemplate jdbcTemplate;

	public CategoryRepository() {
		jdbcTemplate = new JdbcTemplate(MyDataSource.getInstance());
	}
	
	//TẠO MAPPER DÙNG NHIỀU LẦN Ở CÁC PHƯƠNG THỨC
	public BeanPropertyRowMapper<Category> getBeanPropertyRowMapper() {
		return new BeanPropertyRowMapper<>(Category.class);
	}

	public List<Category> finnAll() {
		String sql = "SELECT * FROM categories";
		return jdbcTemplate.query(sql, getBeanPropertyRowMapper());
	}

	public Category findOneById(int id) {
		String sql = "SELECT * FROM categories WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, getBeanPropertyRowMapper());
	}


	public List<Category> findByIdNotIn(int[] ids) {
		String sql = "SELECT * FROM categories WHERE id NOT IN(?)";
		return jdbcTemplate.query(sql, new Object[] { ids }, getBeanPropertyRowMapper());
	}

	public List<String> findAllName() {
		String sql = "SELECT name FROM categories";
		return jdbcTemplate.query(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs) throws SQLException {
				return rs.getString("name");
			}
		});
	}

	public List<String> findAllName2() {
		String sql = "SELECT name FROM categories";
		return jdbcTemplate.queryForList(sql, String.class);
	}

	public String findCategoryNameById(int id) {
		String sql = "SELECT name FROM categories where id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs) throws SQLException {
				return rs.getString("name"); //RS.GETSTRING CẦN TRY CATCH T THROWS LUÔN
			}
		});
	}

	public String findCategoryNameById2(int id) {
		String sql = "SELECT name FROM categories where id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, String.class);
	}

	public boolean update(Category category) {
		return jdbcTemplate.update("UPDATE categories SET name = ? WHERE id = ?",
				new Object[] { category.getName(), category.getId() });
	}
	
	public Long save(Category category) {
		return jdbcTemplate.insert("INSERT INTO categories(name) VALUES(?)", new Object[] { category.getName() });
	}
	
	public void saveListItem(List<Category> categories) {
		jdbcTemplate.batchUpdate("INSERT INTO categories(name) VALUES(?)", new BatchPreparedStatementSetter<Category>() {

			@Override
			public void setValues(PreparedStatement pst, int i) throws SQLException {
				pst.setString(1, categories.get(i).getName());
			}

			@Override
			public int getBatchSize() {
				return categories.size();
			}
		});
	}
}
