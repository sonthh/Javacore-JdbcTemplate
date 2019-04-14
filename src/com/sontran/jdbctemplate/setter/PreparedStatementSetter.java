package com.sontran.jdbctemplate.setter;

import java.sql.PreparedStatement;

public interface PreparedStatementSetter<T> {
	void setValues(PreparedStatement ps);
}
