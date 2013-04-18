package tw.kigi.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class EnumType<E extends EnumValue<T>, T> implements Operator {

	//private Class<E> clazz = null;
	private Primitive type = null;
	private EnumValue<T> enum_value = null;
	
	public EnumType(Class<E> clazz) throws SQLException {
		if (clazz.getEnumConstants() == null) {
			throw new SQLException(clazz.getSimpleName() + " Not a Enum Type");
		}
		
		//this.clazz = clazz;
		enum_value = (EnumValue<T>)clazz.getEnumConstants()[0];
		type = enum_value.toPrimitive();
	}
	
	@Override
	public Object parseValue(String value) throws ParseException {
		return enum_value.toEnum(type.parseValue(value));
	}

	@Override
	public Object defaultValue(String value) throws ParseException {
		return enum_value.toEnum(type.defaultValue(value));
	}

	@Override
	public void setParam(PreparedStatement statement, int index, Object x)
			throws SQLException {
		@SuppressWarnings("unchecked")
		EnumValue<T> v = (EnumValue<T>)x;
		
		type.setParam(statement, index, v.toValue());
		
	}

	@Override
	public Object getResult(ResultSet rs, String columnLabel)
			throws SQLException {
		
		try {
			return enum_value.toEnum(type.getResult(rs, columnLabel));
		} catch (ParseException e) {
			throw new SQLException(e);
		}
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		try {
			return enum_value.toEnum(type.getResult(rs, columnIndex));
		} catch (ParseException e) {
			throw new SQLException(e);
		}
	}

}
