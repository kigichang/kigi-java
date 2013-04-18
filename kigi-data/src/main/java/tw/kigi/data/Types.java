package tw.kigi.data;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;

public final class Types {
	private static HashMap<Class<?>, Operator> primitives = null;
	
	private static void init() {
		primitives = new HashMap<Class<?>, Operator>();
		
		primitives.put(String.class, Primitive.STRING);
		primitives.put(BigDecimal.class, Primitive.BIGDECIMAL);
		primitives.put(Short.class, Primitive.SHORT);
		primitives.put(Integer.class, Primitive.INTEGER);
		primitives.put(Long.class, Primitive.LONG);
		primitives.put(Float.class, Primitive.FLOAT);
		primitives.put(Double.class, Primitive.DOUBLE);
		primitives.put(java.util.Date.class, Primitive.DATETIME);
		primitives.put(java.sql.Date.class, Primitive.DATE);
		primitives.put(java.sql.Time.class, Primitive.TIME);
		primitives.put(java.sql.Timestamp.class, Primitive.TIMESTAMP);
		primitives.put(Character.class, Primitive.CHAR);
		primitives.put(Boolean.class, Primitive.BOOLEAN);
	}
	public static Operator toOperator(Class<?> clazz) throws SQLException {
		if (primitives == null) {
			init();
		}
		
		Operator p = primitives.get(clazz);
		if (p != null) {
			return p;
		}
		else {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			EnumType e_type = new EnumType(clazz);
			primitives.put(clazz, e_type);
			return e_type;
		}
		//throw new SQLException("Not Supported Type " + clazz.getName());
	}
	
}
