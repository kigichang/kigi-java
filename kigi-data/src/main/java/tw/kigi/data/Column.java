package tw.kigi.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.ParseException;

import tw.kigi.data.annotation.Property;
import tw.kigi.util.Convention;

public final class Column {
	
	private String name = null;
	private String tableName = null;
	
	private String propertyName = null;
	private String columnName = null;
	private String fullPropertyName = null;
	private String fullColumnName = null;
	
	private String label = null;
	
	private Operator type = null;
	
	private Object defaultValue = null;
	private String expression = null;
	
	private Expr expr = Expr.NO;
	
	private boolean primary = false;
	
	private boolean autoIncrement = false;
	
	private String sequence = null;
	
	private boolean nullAble = false;
	
	private Method setter = null;
	private Method getter = null;
	
	public Column(String table_name, Class<?> clazz, Field field, Property property) throws SQLException {
		name = field.getName();
		type = Types.toOperator(field.getType());
		propertyName = Convention.toPropertyName(name);
		tableName = table_name;
		
		try {
			defaultValue = type.defaultValue(property.defaultValue());
			setter = clazz.getMethod("set" + propertyName, field.getType());
			getter = clazz.getMethod((Boolean.class.equals(field.getType()) ? "is" : "get") + propertyName);
		} catch (NoSuchMethodException | SecurityException | ParseException e) {
			throw new SQLException(e);
		}
		
		propertyName = !"".equals(property.name()) ? property.name() : propertyName;
		fullPropertyName = new StringBuilder(clazz.getSimpleName()).append('.').append(propertyName).toString();
		
		columnName = !"".equals(property.columnName()) ? property.columnName() : Convention.toColumnName(propertyName);
		fullColumnName = new StringBuilder(clazz.getSimpleName()).append('.').append(columnName).toString();
		
		label = new StringBuilder(clazz.getSimpleName()).append('_').append(propertyName).toString();
				
		//expression = property.expression();
		//_expression = !"".equals(expression);
		if ("".equals(property.expression()) && "".equals(property.expression())) {
			expr = Expr.NO;
			expression = "";
		}
		else if (!"".equals(property.expression())) {
			expr = Expr.OTHER;
			expression = property.expression();
		}
		else if (!"".equals(property.group())) {
			expr = Expr.GROUP;
			expression = property.group();
		}
		
		primary = property.primary();
		autoIncrement = property.autoIncrement();
		sequence = property.sequence();
		nullAble = property.nullAble();
	}
	
	public String getName() {
		return name;
	}
	public String getTableName() {
		return tableName;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public String getColumnName() {
		return columnName;
	}
	public String getFullPropertyName() {
		return fullPropertyName;
	}
	public String getFullColumnName() {
		return fullColumnName;
	}
	public String getLabel() {
		return label;
	}
	public Operator getType() {
		return type;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public String getExpression() {
		return expression;
	}
	public Expr getExprType() {
		return expr;
	}
	public boolean isExpression() {
		return expr != Expr.NO;
	}
	public boolean isPrimary() {
		return primary;
	}
	public boolean isAutoIncrement() {
		return autoIncrement;
	}
	public String getSequence() {
		return sequence;
	}
	
	/*public Method getSetter() {
		return setter;
	}
	
	public Method getGetter() {
		return getter;
	}*/
	
	public boolean nullAble() {
		return nullAble;
	}
	
	public Object get(Object data) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return getter.invoke(data);
	}
	
	public void set(Object data, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setter.invoke(data, value);
	}
}
