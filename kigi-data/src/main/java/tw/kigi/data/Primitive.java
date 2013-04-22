package tw.kigi.data;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public enum Primitive implements Operator {

	STRING(String.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			statement.setString(index, (String)x);
			
		}

		@Override
		public String getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			return rs.getString(columnLabel);
		}

		@Override
		public String getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getString(columnIndex);
		}

		@Override
		public String parseValue(String value) throws ParseException {
			return value;
		}

		@Override
		public Object defaultValue(String value) throws ParseException {
			return value;
		}
	},
	
	BIGDECIMAL(BigDecimal.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			statement.setBigDecimal(index, (BigDecimal)x);
			
		}

		@Override
		public BigDecimal getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			return rs.getBigDecimal(columnLabel);
		}

		@Override
		public BigDecimal getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getBigDecimal(columnIndex);
		}

		@Override
		public BigDecimal parseValue(String value) throws ParseException {
			try {
				return value == null ? null : new BigDecimal(value);
			}
			catch(NumberFormatException e) {
				throw new ParseException(e.toString(), 1);
			}
		}

		@Override
		public BigDecimal defaultValue(String value) throws ParseException {
			return parseValue(value);
		}
		
	},
	
	LONG(Long.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			if (x != null) {
				statement.setLong(index, (Long)x);
			}
			else {
				statement.setNull(index, java.sql.Types.BIGINT);
			}
			
		}

		@Override
		public Long getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			return rs.getLong(columnLabel);
		}

		@Override
		public Long getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getLong(columnIndex);
		}

		@Override
		public Long parseValue(String value) throws ParseException {
			try {
				return value == null ? null : new Long(value);
			}
			catch(NumberFormatException e) {
				throw new ParseException(e.toString(), 1);
			}
		}

		@Override
		public Long defaultValue(String value) throws ParseException {
			switch(value) {
			case "min":
			case "Min":
			case "MIN":
				return Long.MIN_VALUE;
			case "max":
			case "Max":
			case "MAX":
				return Long.MAX_VALUE;
			default:
				return parseValue(value);
			}
		}
		
	},
	
	INTEGER(Integer.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			if (x != null) {
				statement.setInt(index, (Integer)x);
			}
			else {
				statement.setNull(index, java.sql.Types.INTEGER);
			}
			
		}

		@Override
		public Integer getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			return rs.getInt(columnLabel);
		}

		@Override
		public Integer getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getInt(columnIndex);
		}

		@Override
		public Integer parseValue(String value) throws ParseException {
			try {
				return value == null ? null : new Integer(value);
			}
			catch(NumberFormatException e) {
				throw new ParseException(e.toString(), 1);
			}
		}

		@Override
		public Integer defaultValue(String value) throws ParseException {
			switch(value) {
			case "min":
			case "Min":
			case "MIN":
				return Integer.MIN_VALUE;
			case "max":
			case "Max":
			case "MAX":
				return Integer.MAX_VALUE;
			default:
				return parseValue(value);
			}
		}
		
	},
	
	SHORT(Short.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			if (x != null) {
				statement.setShort(index, (Short)x);
			}
			else {
				statement.setNull(index, java.sql.Types.SMALLINT);
			}
			
		}

		@Override
		public Short getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			return rs.getShort(columnLabel);
		}

		@Override
		public Short getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getShort(columnIndex);
		}

		@Override
		public Short parseValue(String value) throws ParseException {
			try {
				return value == null ? null : new Short(value);
			}
			catch(NumberFormatException e) {
				throw new ParseException(e.toString(), 1);
			}
		}

		@Override
		public Short defaultValue(String value) throws ParseException {
			switch(value) {
			case "min":
			case "Min":
			case "MIN":
				return Short.MIN_VALUE;
			case "max":
			case "Max":
			case "MAX":
				return Short.MAX_VALUE;
			default:
				return parseValue(value);
			}
		}
		
	},
	
	FLOAT(Float.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			if (x != null) {
				statement.setFloat(index, (Float)x);
			}
			else {
				statement.setNull(index, java.sql.Types.FLOAT);
			}
		}

		@Override
		public Float getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			return rs.getFloat(columnLabel);
		}

		@Override
		public Float getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getFloat(columnIndex);
		}

		@Override
		public Float parseValue(String value) throws ParseException {
			try {
				return value == null ? null : new Float(value);
			}
			catch(NumberFormatException e) {
				throw new ParseException(e.toString(), 1);
			}
		}

		@Override
		public Float defaultValue(String value) throws ParseException {
			switch(value) {
			case "min":
			case "Min":
			case "MIN":
				return Float.MIN_VALUE;
			case "max":
			case "Max":
			case "MAX":
				return Float.MAX_VALUE;
			default:
				return parseValue(value);
			}
		}
		
	},
	
	DOUBLE(Double.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			if (x != null) {
				statement.setDouble(index, (Double)x);
			}
			else {
				statement.setNull(index, java.sql.Types.DOUBLE);
			}
			
		}

		@Override
		public Double getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			return rs.getDouble(columnLabel);
		}

		@Override
		public Double getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getDouble(columnIndex);
		}

		@Override
		public Double parseValue(String value) throws ParseException {
			try {
				return value == null ? null : new Double(value);
			}
			catch(NumberFormatException e) {
				throw new ParseException(e.toString(), 1);
			}
		}

		@Override
		public Double defaultValue(String value) throws ParseException {
			switch(value) {
			case "min":
			case "Min":
			case "MIN":
				return Double.MIN_VALUE;
			case "max":
			case "Max":
			case "MAX":
				return Double.MAX_VALUE;
			default:
				return parseValue(value);
			}
		}
		
	},
	
	DATETIME(java.util.Date.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			if (x != null) {
				statement.setTimestamp(index, new java.sql.Timestamp(((java.util.Date)x).getTime()));
			}
			else {
				statement.setNull(index, java.sql.Types.TIMESTAMP);
			}
			
		}

		@Override
		public java.util.Date getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			java.sql.Timestamp t = rs.getTimestamp(columnLabel);
			return (t != null ? new java.util.Date(t.getTime()) : null);
		}

		@Override
		public java.util.Date getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			java.sql.Timestamp t = rs.getTimestamp(columnIndex);
			return (t != null ? new java.util.Date(t.getTime()) : null);
		}

		@Override
		public java.util.Date parseValue(String value) throws ParseException {
			try {
				return value == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
			}
			catch(NullPointerException | IllegalArgumentException e) {
				throw new ParseException(e.toString(), 1);
			}
		}

		@Override
		public java.util.Date defaultValue(String value) throws ParseException {
			switch(value) {
			case "min":
			case "Min":
			case "MIN":
				return parseValue("0001-01-01 00:00:00");
			case "max":
			case "Max":
			case "MAX":
				return parseValue("9999-12-31 23:59:59");
			case "now":
			case "Now":
			case "NOW":
				return new java.util.Date();
			default:
				return parseValue(value);
			}
		}
		
		public String toString(java.util.Date date) throws ParseException {
			if (date != null) {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			}
			throw new ParseException("Value is Null", 1);
		}
	},
	
	DATE(java.sql.Date.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			if (x != null) {
				statement.setDate(index, (java.sql.Date)x);
			}
			else {
				statement.setNull(index, java.sql.Types.DATE);
			}
			
		}

		@Override
		public java.sql.Date getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			return rs.getDate(columnLabel);
		}

		@Override
		public java.sql.Date getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getDate(columnIndex);
		}

		@Override
		public java.sql.Date parseValue(String value) throws ParseException {
			try {
				return value == null ? null 
						: new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(value).getTime());
			}
			catch(NullPointerException | IllegalArgumentException e) {
				throw new ParseException(e.toString(), 1);
			}
		}

		@Override
		public java.sql.Date defaultValue(String value) throws ParseException {
			switch(value) {
			case "min":
			case "Min":
			case "MIN":
				return parseValue("0001-01-01");
			case "max":
			case "Max":
			case "MAX":
				return parseValue("9999-12-31");
			case "now":
			case "Now":
			case "NOW":
				return new java.sql.Date(System.currentTimeMillis());
			default:
				return parseValue(value);
			}
		}
		
		public String toString(java.sql.Date date) throws ParseException {
			if (date != null) {
				return new SimpleDateFormat("yyyy-MM-dd").format(date);
			}
			throw new ParseException("Value is Null", 1);
		}
	},
	
	TIME(java.sql.Time.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			if (x != null) {
				statement.setTime(index, (java.sql.Time)x);
			}
			else {
				statement.setNull(index, java.sql.Types.TIME);
			}
		}

		@Override
		public java.sql.Time getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			return rs.getTime(columnLabel);
		}

		@Override
		public java.sql.Time getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getTime(columnIndex);
		}

		@Override
		public java.sql.Time parseValue(String value) throws ParseException {
			try {
				return value == null ? null : new java.sql.Time(new SimpleDateFormat("HH:mm:ss").parse(value).getTime());
			}
			catch(NullPointerException | IllegalArgumentException e) {
				throw new ParseException(e.toString(), 1);
			}
		}

		@Override
		public java.sql.Time defaultValue(String value) throws ParseException {
			switch(value) {
			case "min":
			case "Min":
			case "MIN":
				return parseValue("00:00:00");
			case "max":
			case "Max":
			case "MAX":
				return parseValue("23:59:59");
			case "now":
			case "Now":
			case "NOW":
				return new java.sql.Time(System.currentTimeMillis());
			default:
				return parseValue(value);
			}
		}
		
		public String toString(java.sql.Time time) throws ParseException {
			if (time != null) {
				return new SimpleDateFormat("HH:mm:ss").format(time);
			}
			throw new ParseException("Value is Null", 1);
		}
		
	},
	
	TIMESTAMP(java.sql.Timestamp.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			if (x != null) {
				statement.setTimestamp(index, (java.sql.Timestamp)x);
			}
			else {
				statement.setNull(index, java.sql.Types.TIMESTAMP);
			}
			
		}

		@Override
		public java.sql.Timestamp getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			return rs.getTimestamp(columnLabel);
		}

		@Override
		public java.sql.Timestamp getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getTimestamp(columnIndex);
		}

		@Override
		public java.sql.Timestamp parseValue(String value) throws ParseException {
			try {
				return value == null ? null : new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value).getTime());
			}
			catch(NullPointerException | IllegalArgumentException e) {
				throw new ParseException(e.toString(), 1);
			}
		}

		@Override
		public Object defaultValue(String value) throws ParseException {
			switch(value) {
			case "min":
			case "Min":
			case "MIN":
				return parseValue("0001-01-01 00:00:00");
			case "max":
			case "Max":
			case "MAX":
				return parseValue("9999-12-31 23:59:59");
			case "now":
			case "Now":
			case "NOW":
				return new java.sql.Timestamp(System.currentTimeMillis());
			default:
				return parseValue(value);
			}
		}
		
		public String toString(java.sql.Timestamp timestamp) throws ParseException {
			if (timestamp != null) {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
			}
			throw new ParseException("Value is Null", 1);
		}
		
	},
	
	CHAR(Character.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			if (x != null) {
				statement.setString(index, String.valueOf((char)x));
			}
			else {
				statement.setNull(index, java.sql.Types.CHAR);
			}
			
		}

		@Override
		public Character getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			String s = rs.getString(columnLabel);
			return s == null || s.length() == 0 ? null : s.charAt(0);
		}

		@Override
		public Character getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			String s = rs.getString(columnIndex);
			return s == null || s.length() == 0 ? null : s.charAt(0);
		}

		@Override
		public Character parseValue(String value) throws ParseException {
			return value == null || value.length() == 0 ? null : value.charAt(0);
		}

		@Override
		public Object defaultValue(String value) throws ParseException {
			return parseValue(value);
		}
		
	},
	
	BOOLEAN(Boolean.class) {

		@Override
		public void setParam(PreparedStatement statement, int index, Object x) throws SQLException {
			if (x != null) {
				statement.setBoolean(index, (Boolean)x);
			}
			else {
				statement.setNull(index, java.sql.Types.BOOLEAN);
			}
			
		}

		@Override
		public Boolean getResult(ResultSet rs, String columnLabel)
				throws SQLException {
			return rs.getBoolean(columnLabel);
		}

		@Override
		public Boolean getResult(ResultSet rs, int columnIndex)
				throws SQLException {
			return rs.getBoolean(columnIndex);
		}

		@Override
		public Boolean parseValue(String value) throws ParseException {
			if (value == null || value.length() == 0) {
				return null;
			}
			
			switch(value.toLowerCase()) {
			case "yes":
			case "true":
			case "on":
			case "1":
			case "y":
			case "ok":
				return Boolean.TRUE;
			case "no":
			case "false":
			case "off":
			case "0":
			case "n":
			case "cancel":
				return Boolean.FALSE;
			default:
				throw new ParseException("Can Not Verify Value to Boolean", 1);
			}
			
		}

		@Override
		public Object defaultValue(String value) throws ParseException {
			return parseValue(value);
		}
		
	};
	
	private Class<?> clazz = null;
	
	private Primitive(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}

}
