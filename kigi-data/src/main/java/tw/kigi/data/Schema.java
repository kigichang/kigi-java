package tw.kigi.data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tw.kigi.data.annotation.Property;
import tw.kigi.util.Convention;

public final class Schema {

	protected static HashMap<Class<?>, Schema> schemas = new HashMap<Class<?>, Schema>();
	
	public static Schema getSchema(Class<?> clazz) throws SQLException {
		Schema schema = schemas.get(clazz);
		if (schema == null) {
			schemas.put(clazz, schema = new Schema(clazz));
		}
		return schema;
	}
	
	private Class<?> clazz = null;
	private String name = null;
	private String tableName = null;
	private boolean hasAutoIncrement = false;
	
	private HashMap<String, Column> labels = new HashMap<String, Column>();
	private HashMap<String, Column> columns = new HashMap<String, Column>();
	
	private String[] properties = null;
	private Column[] primaries = null;
	private Column autoIncrementColumn = null;
	
	
	private Schema(Class<?> clazz) throws SQLException {
		this.clazz = clazz;
		name = clazz.getSimpleName();
		boolean tbl_name_found = false;
		
		Field[] fields = clazz.getDeclaredFields();
		
		try {
			for (Field field : fields) {
				int modifiers = field.getModifiers();
			
				if ("tableName".equals(field.getName()) 
						&& (modifiers & Modifier.PUBLIC) == Modifier.PUBLIC 
						&& (modifiers & Modifier.STATIC) == Modifier.STATIC 
						&& (modifiers & Modifier.FINAL) == Modifier.FINAL) {
				
					tableName = field.get(null).toString();
					tbl_name_found = true;
					break;
				}
			}
			
			if (!tbl_name_found) {
				tableName = Convention.toColumnName(name);
			}
			
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new SQLException(e);
		}
		
		List<String> lst = new ArrayList<String>();
		List<Column> lst_p = new ArrayList<Column>();
		
		for (Field field : fields) {
			Property property = field.getAnnotation(Property.class);
			if (property != null) {
				Column column = new Column(tableName, clazz, field, property);
				if (hasAutoIncrement && column.isAutoIncrement()) {
					throw new SQLException("Multi-AutoIncrement Not Supported");
				}
				else if (column.isAutoIncrement()) {
					hasAutoIncrement = true;
					autoIncrementColumn = column;
				}
				
				lst.add(column.getFullPropertyName());
				columns.put(column.getFullPropertyName(), column);
				labels.put(column.getLabel(), column);
				if (column.isPrimary()) {
					lst_p.add(column);
				}
			}
		}
		
		if (columns.size() <= 0) {
			throw new SQLException("No Property Exists");
		}
		
		if (lst_p.size() <= 0) {
			throw new SQLException("No Primary Key Exists");
		}
		
		primaries = lst_p.toArray(new Column[lst_p.size()]);
		
		for (int i = 0, size = lst.size(); i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				String a = lst.get(i);
				String b = lst.get(j);
				if (b.length() > a.length()) {
					lst.set(i, b);
					lst.set(j, a);
				}
			}
		}
		
		properties = lst.toArray(new String[lst.size()]);
	}

	public String append(String property) {
		if (property.indexOf('.') < 0) {
			return new StringBuilder(name).append('.').append(property).toString();
		}
		
		return property;
	}
	
	
	public String[] append(String[] array) {
		if (array != null) {
			for (int i = 0, len = array.length; i < len; i++) {
				array[i] = append(array[i]);
			}
		}
		
		return array;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getName() {
		return name;
	}

	public String getTableName() {
		return tableName;
	}

	public boolean hasAutoIncrement() {
		return hasAutoIncrement;
	}

	public String[] getProperties() {
		return properties;
	}

	public Column getColumnByLabel(String label) throws SQLException {
		Column ret = labels.get(label);
		if (ret == null) {
			throw new SQLException("Column for Label " + label + " Not Found");
		}
		return ret;
	}
	
	public Column getColumnByProperty(String property) throws SQLException {
		Column ret = columns.get(property);
		if (ret == null) {
			throw new SQLException("Column for Property " + property + " Not Found");
		}
		return ret;
	}
	
	public Column[] getAllColumns() {
		return columns.values().toArray(new Column[columns.size()]);
	}

	public Column[] getPrimaries() {
		return primaries;
	}

	public Column getAutoIncrementColumn() {
		return autoIncrementColumn;
	}
	
}
