package com.sontran.jdbctemplate.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface BatchPreparedStatementSetter<T> {
	public void setValues(PreparedStatement pst, int i) throws SQLException;
	public int getBatchSize();
}
