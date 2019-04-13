package com.sontran.jdbctemplate.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sontran.jdbctemplate.datasource.DataSource;
import com.sontran.jdbctemplate.mapper.BeanPropertyRowMapper;
import com.sontran.jdbctemplate.mapper.RowMapper;

public class JdbcTemplate implements IJdbcTemplate {

	private Connection conn = null;
	private PreparedStatement pst = null;
	private Statement st = null;
	private ResultSet rs = null;
	private DataSource dataSource;

	public JdbcTemplate(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private void setParameter(PreparedStatement pst, Connection conn, Object... parameters) throws SQLException {
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				Object parameter = parameters[i];
				int index = i + 1;
				//cách 1
				pst.setObject(index, parameter);
				
				//cách 2
				/*if (parameter instanceof Integer) {
					pst.setInt(index, (Integer) parameter);
				} else if (parameter instanceof String) {
					pst.setString(index, (String) parameter);
				} else if (parameter instanceof Timestamp) {
					pst.setTimestamp(index, (Timestamp) parameter);
				} else if (parameter instanceof Long) {
					pst.setLong(index, (Long) parameter);
				} else if (parameter instanceof Float) {
					pst.setFloat(index, (Float) parameter);
				} else if (parameter instanceof Double) {
					pst.setDouble(index, (Double) parameter);
				} else if (parameter instanceof Boolean) {
					pst.setBoolean(index, (Boolean) parameter);
				} else if (parameter.getClass().isArray()) {
					//pst.setArray(index, (Array) parameter);
					if (parameter instanceof int[]) {
						int[] arr = (int[]) parameter;
						final Object[] ar = new Object[arr.length];
						for (int j = 0; j < ar.length; j++) {
							ar[j] = arr[j];
						}
						for (Object object : ar) {
							System.out.println(object);
						}
						System.out.println(index);
						//đang bị lỗi
						//Array array = conn.createArrayOf("VARCHAR", new Object[] {"1","2"});
						//pst.setArray(index, array);
					}
					
				}*/
			}
		}
	}

	@Override
	public <T> List<T> query(String sql, Object[] parameters, RowMapper<T> rowMapper) {
		List<T> results = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			pst = conn.prepareStatement(sql);
			this.setParameter(pst, conn, parameters);
			rs = pst.executeQuery();
			while (rs.next()) {
				results.add(rowMapper.mapRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataSource.close(rs, pst, conn);
		}
		return results;
	}

	@Override
	public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
		List<T> results = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				results.add(rowMapper.mapRow(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataSource.close(rs, st, conn);
		}
		return results;
	}

	@Override
	public <T> List<T> query(String sql, Object[] parameters, BeanPropertyRowMapper<T> rowMapper) {
		List<T> results = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			pst = conn.prepareStatement(sql);
			this.setParameter(pst, conn, parameters);
			System.out.println(pst.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				results.add(rowMapper.mapRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataSource.close(rs, pst, conn);
		}
		return results;
	}

	@Override
	public <T> List<T> query(String sql, BeanPropertyRowMapper<T> rowMapper) {
		List<T> results = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				results.add(rowMapper.mapRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataSource.close(rs, st, conn);
		}
		return results;
	}

	@Override
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
		T result = null;
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				result = rowMapper.mapRow(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataSource.close(rs, st, conn);
		}
		return result;
	}

	@Override
	public <T> T queryForObject(String sql, BeanPropertyRowMapper<T> rowMapper) {
		T result = null;
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				result = rowMapper.mapRow(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataSource.close(rs, st, conn);
		}
		return result;
	}

	@Override
	public boolean update(String sql, Object[] parameters) {
		boolean success = true;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			this.setParameter(pst, conn, parameters);
			success = pst.executeUpdate() > 0 ? true : false;
			conn.commit();
		} catch (SQLException e) {
			dataSource.rollback(conn);
		} finally {
			dataSource.close(pst, conn);
		}
		return success;
	}
	
	@Override
	public boolean update(String sql) {
		boolean success = true;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			success = st.executeUpdate(sql) > 0 ? true : false;
			conn.commit();
		} catch (SQLException e) {
			dataSource.rollback(conn);
		} finally {
			dataSource.close(st, conn);
		}
		return success;
	}
	
	@Override
	public Long insert(String sql) {
		Long id = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.executeUpdate(sql);
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(1);
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			dataSource.rollback(conn);
		} finally {
			dataSource.close(rs, st, conn);
		}
		return id;
	}

	@Override
	public Long insert(String sql, Object[] parameters) {
		Long id = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setParameter(pst, conn, parameters);
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(1);
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			dataSource.rollback(conn);
		} finally {
			dataSource.close(rs, pst, conn);
		}
		return id;
	}

	@Override
	public <T> T queryForObject(String sql, Object[] parameters, RowMapper<T> rowMapper) {
		T result = null;
		try {
			conn = dataSource.getConnection();
			pst = conn.prepareStatement(sql);
			this.setParameter(pst, conn, parameters);
			rs = pst.executeQuery();
			if (rs.next()) {
				result = rowMapper.mapRow(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataSource.close(rs, pst, conn);
		}
		return result;
	}

	@Override
	public <T> T queryForObject(String sql, Object[] parameters, BeanPropertyRowMapper<T> rowMapper) {
		T result = null;
		try {
			conn = dataSource.getConnection();
			pst = conn.prepareStatement(sql);
			this.setParameter(pst, conn, parameters);
			rs = pst.executeQuery();
			if (rs.next()) {
				result = rowMapper.mapRow(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataSource.close(rs, pst, conn);
		}
		return result;
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> clazz) {
		/*if (!clazz.isPrimitive()) {
			try {
				throw new JdbcTemplateException("Không phải wrapper class");
			} catch (JdbcTemplateException e) {
				e.printStackTrace();
			}
		}*/
		
		T result = null;
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				result = rs.getObject(0, clazz);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataSource.close(rs, st, conn);
		}
		return result;
	}

	@Override
	public <T> T queryForObject(String sql, Object[] parameters, Class<T> clazz) {
		T result = null;
		try {
			conn = dataSource.getConnection();
			pst = conn.prepareStatement(sql);
			this.setParameter(pst, conn, parameters);
			rs = pst.executeQuery();
			if (rs.next()) {
				result = rs.getObject(1, clazz);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataSource.close(rs, pst, conn);
		}
		return result;
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> clazz) {
		List<T> results = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				results.add(rs.getObject(1, clazz));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataSource.close(rs, st, conn);
		}
		return results;
	}

	@Override
	public <T> List<T> queryForList(String sql, Object[] parameters, Class<T> clazz) {
		List<T> results = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			pst = conn.prepareStatement(sql);
			this.setParameter(pst, conn, parameters);
			rs = pst.executeQuery();
			while (rs.next()) {
				results.add(rs.getObject(1, clazz));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataSource.close(rs, pst, conn);
		}
		return results;
	}

}
