package tw.kigi.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public interface Operator {

	Object parseValue(String value) throws ParseException;
	Object defaultValue(String value) throws ParseException;
	
	void setParam(PreparedStatement statement, int index, Object x) throws SQLException;
	Object getResult(ResultSet rs, String columnLabel) throws SQLException;
	Object getResult(ResultSet rs, int columnIndex) throws SQLException;
}
