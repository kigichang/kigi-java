package tw.kigi.data;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public abstract class Query<T> {

	protected Schema schema = null;
	protected Connection conn = null;
	
	protected String[] properties = null;
	protected String condition = null;
	protected String having = null;
	protected String[] group = null;
	protected List<Sort> order = new ArrayList<Sort>();
	protected Object[] values = null;
	protected Object lastInsertId = null;
	protected boolean needGenerateKeys = false;
	protected Class<T> clazz = null;
	protected StringBuilder sql = new StringBuilder();;
	
	public Query(Connection conn, Class<T> clazz) throws SQLException {
		this.clazz = clazz;
		this.conn = conn;
		this.schema = Schema.getSchema(clazz);
	}
	
	public Query<T> clear() {
		properties = null;
		condition = null;
		having = null;
		group = null;
		order.clear();
		values = null;
		needGenerateKeys = false;
		return this;
	}
	
	public Query<T> properties(String... properties) {
		this.properties = schema.append(properties);
		return this;
	}
	
	public Query<T> condition(String condition) {
		this.condition = condition;
		return this;
	}
	
	public Query<T> having(String condition) {
		this.having = condition;
		return this;
	}
	
	public Query<T> group(String... group) {
		this.group = schema.append(group);
		return this;
	}
	
	public Query<T> order(String property, Direction direction) throws SQLException {
		property = schema.append(property);
		Column column = schema.getColumnByProperty(property);
		order.add(new Sort(column, direction));
		return this;
	}
	
	public Query<T> values(Object... objs) {
		this.values = objs;
		return this;
	}
	
	public abstract T[] paginate(int start, int length) throws SQLException, ParseException;
	
	
	protected String generateSelect(String[] properties) throws SQLException {
		StringBuilder sb = new StringBuilder("SELECT ");
		for (String property: properties) {
			Column column = schema.getColumnByProperty(property);
			if (column.isExpression()) {
				sb.append('(').append(column.getExpression()).append(") AS ").append(column.getLabel()).append(',');
			}
			else {
				sb.append(column.getFullColumnName()).append(" AS ").append(column.getLabel()).append(',');
			}
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	
	protected String generateGroup(String[] groups) throws SQLException {
		if (groups == null || groups.length <= 0) {
			return "";
		}
		
		StringBuilder ret = new StringBuilder(" GROUP BY ");
		
		for (String p : groups) {
			Column column = schema.getColumnByProperty(p);
			ret.append(column.getLabel()).append(',');
		}
		
		ret.setLength(ret.length() - 1);
		return ret.toString();
	}
	
	protected String generateOrder(Sort[] sorts) throws SQLException {
		if (sorts == null || sorts.length <= 0) {
			return "";
		}
		
		StringBuilder ret = new StringBuilder(" ORDER BY ");
		for (Sort s : sorts) {
			Column column = s.getColumn();
			//ret.append(column.getLabel()).append(' ').append(s.getDirection().name()).append(',');
			if (column.isExpression()) {
				ret.append('(').append(column.getExpression()).append(')');
			}
			else {
				ret.append(column.getFullColumnName());
			}
			
			ret.append(' ').append(s.getDirection().name()).append(',');
		}
		
		ret.setLength(ret.length() - 1);
		return ret.toString();
	}
	
	protected String generateCondition(String condition) throws SQLException {
		if (condition == null || condition.length() <= 0) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder(condition);
		
		for (String s : schema.getProperties()) {
			int pos = 0, start = -1, len = s.length();
			
			while((start = sb.indexOf(s, pos)) >= 0) {
				sb.replace(start, start + len, schema.getColumnByProperty(s).getFullColumnName());
			}
		}
		
		return sb.toString();
	}
	
	protected String selectString() throws SQLException {
		StringBuilder select = new StringBuilder();
		if (properties == null) {
			select.append(generateSelect(schema.getProperties()));
		}
		else {
			select.append(generateSelect(properties));
		}
		
		
		select.append(" FROM ").append(schema.getTableName()).append(" AS ").append(schema.getName());
		
		if (condition != null) {
			select.append(" WHERE ").append(generateCondition(condition));
		}
		
		select.append(generateGroup(group));
		select.append(generateOrder(order.toArray(new Sort[order.size()])));
		
		if (having != null) {
			select.append(" HAVING ").append(generateCondition(having));
		}
		
		return select.toString();
	}
	
	protected T toObject(ResultSet rs, ResultSetMetaData meta) throws SQLException {
		try {
			T obj = clazz.newInstance();
			for (int i = 1, len = meta.getColumnCount(); i <= len; i++) {
				String label = meta.getColumnLabel(i);
				Column column = schema.getColumnByLabel(label);
				column.getSetter().invoke(obj, column.getType().getResult(rs, label));
			}
			return obj;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new SQLException(e);
		}
	}
	
	protected void setValue(PreparedStatement stmt, Object[] values) throws SQLException {
		if (values != null) {
			int idx = 1;
			for (Object obj : values) {
				Operator p = Types.toOperator(obj.getClass());
				p.setParam(stmt, idx++, obj);
			}
		}
	}
	
	protected T[] findBySQL(String sql) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		List<T> ret = new ArrayList<T>();
		
		
		try {
			stmt = conn.prepareStatement(sql);
			
			setValue(stmt, values);
			
			rs = stmt.executeQuery();
			meta = rs.getMetaData();
			
			while(rs.next()) {
				ret.add(toObject(rs, meta));
			}
		}
		finally {
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { }
			}
			
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) { }
			}
			clear();
		}
		
		@SuppressWarnings("unchecked")
		T[] out = (T[])Array.newInstance(clazz, ret.size());
		return ret.toArray(out);
	}
	
	public T[] find() throws SQLException {
		sql = new StringBuilder(selectString());
		return findBySQL(sql.toString());
	}
	
	public T findOne() throws SQLException {
		T[] ret = find();
		return ret.length > 0 ? ret[0] : null;
	}
	
	protected String generateUpdate(String[] properties) throws SQLException {
		StringBuilder p = new StringBuilder("UPDATE ").append(schema.getTableName()).append(" SET ");
		
		if (properties != null) {
			for(String property: properties) {
				Column column = schema.getColumnByProperty(property);
				
				if (!column.isExpression()) {
					p.append(column.getColumnName()).append(" = ?,");
				}
			}
			p.setLength(p.length() - 1);
			return p.toString();
		}
		
		throw new SQLException("No Property Found");
	}
	
	protected String updateString() throws SQLException {
		StringBuilder sb = new StringBuilder().append(generateUpdate(properties));
		
		if (condition != null) {
			sb.append(" WHERE ").append(generateCondition(condition));
		}
		
		return sb.toString();
	}
	
	
	public int update() throws SQLException {
		sql = new StringBuilder(updateString());
		
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			setValue(stmt, values);
			return stmt.executeUpdate();
		}
		finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) { }
			}
			clear();
		}
	}
	
	public int insert(T data) throws SQLException {
		lastInsertId = null;
		StringBuilder sb1 = new StringBuilder("INSERT INTO ").append(schema.getTableName()).append(" (");
		StringBuilder sb2 = new StringBuilder(" VALUES (");
		
		Column[] columns = schema.getAllColumns();
		Column[] ins_columns = new Column[columns.length];
		Object[] ins_values = new Object[columns.length];
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			int ins_count = 0;
			
			for (Column column : columns) {
				if (column.isExpression()) {
					continue;
				}
				
				Object val = column.getGetter().invoke(data);
				if (val != null) {
					sb1.append(column.getColumnName()).append(',');
					sb2.append("?,");
					ins_columns[ins_count] = column;
					ins_values[ins_count] = val;
					ins_count++;
				}
				else if (column.isAutoIncrement()) {
					if (!"".equals(column.getSequence())) {
						sb1.append(column.getColumnName()).append(',');
						sb2.append(column.getSequence()).append(',');
					}
					needGenerateKeys = true;
				}
				else if (!column.nullAble()){
					sb1.append(column.getColumnName()).append(',');
					sb2.append("?,");
					ins_columns[ins_count] = column;
					ins_values[ins_count] = column.getDefaultValue();
					ins_count++;
				}
			}
			
			sb1.replace(sb1.length() - 1, sb1.length(), ")");
			sb2.replace(sb2.length() - 1, sb2.length(), ")");
			sql = new StringBuilder(sb1).append(sb2);
			
			stmt = needGenerateKeys ? conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS) 
					: conn.prepareStatement(sql.toString());
			
			for (int i = 0; i < ins_count; i++) {
				ins_columns[i].getType().setParam(stmt, i + 1, ins_values[i]);
			}
			
			int ret = stmt.executeUpdate();
			if (needGenerateKeys) {
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					lastInsertId = schema.getAutoIncrementColumn().getType().getResult(rs, 1);
					schema.getAutoIncrementColumn().getSetter().invoke(data, lastInsertId);
				}
			}
			return ret;
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new SQLException(e);
		}
		finally {
			if (rs != null) {
				try { rs.close(); } catch(SQLException e) { }
			}
			if (stmt != null) {
				try { stmt.close(); } catch(SQLException e) { }
			}
			clear();
		}
		
	}
	
	public int update(T data) throws SQLException {
		StringBuilder sb1 = new StringBuilder("UPDATE ").append(schema.getTableName()).append(" SET ");
		StringBuilder sb2 = new StringBuilder(" WHERE ");
		
		Column[] columns = schema.getAllColumns();
		
		Column[] primaries = new Column[schema.getPrimaries().length];
		Object[] primary_values = new Object[primaries.length];
		
		Column[] set_columns = new Column[columns.length - primaries.length];
		Object[] set_values = new Object[set_columns.length];
		
		int set_count = 0, primary_count = 0;
		
		PreparedStatement stmt = null;
		try {
			for(Column column : columns) {
				if (column.isExpression()) {
					continue;
				}
			
				Object val = column.getGetter().invoke(data);
				if (val != null) {
					if (column.isPrimary()) {
						primaries[primary_count] = column;
						primary_values[primary_count] = val;
						primary_count++;
						sb2.append(column.getColumnName()).append(" = ? AND ");
					}
					else {
						set_columns[set_count] = column;
						set_values[set_count] = val;
						set_count++;
						sb1.append(column.getColumnName()).append(" = ?,");
					}
				}
			}
			
			if (primary_count <= 0) {
				throw new SQLException("Value of Primary Key Not Set");
			}
			
			sb1.setLength(sb1.length() - 1);
			sb2.setLength(sb2.length() - 4);
			
			sql = new StringBuilder(sb1).append(sb2);
			
			stmt = conn.prepareStatement(sql.toString());
			
			for(int i = 0; i < set_count; i++) {
				set_columns[i].getType().setParam(stmt, i + 1, set_values[i]);
			}
			
			for (int i = 0; i < primary_count; i++) {
				primaries[i].getType().setParam(stmt, set_count + i + 1, primary_values[i]);
			}
			
			return stmt.executeUpdate();
			
			
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new SQLException(e);
		}
		finally {
			if (stmt != null) {
				try { stmt.close(); } catch(SQLException e) { }
			}
			clear();
		}
	}
	
	protected String deleteString() throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(schema.getTableName());
		if (condition != null) {
			sb.append(" WHERE ").append(generateCondition(condition));
		}
		return sb.toString();
	}
	
	public int delete() throws SQLException {
		sql = new StringBuilder(deleteString());
		
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement(sql.toString());
			setValue(stmt, values);
			return stmt.executeUpdate();
			
		}
		finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) { }
			}
			clear();
		}
	}
	
	protected String insertString() throws SQLException {
		if (properties == null) {
			throw new SQLException("No Property Found");
		}
		
		StringBuilder sb1 = new StringBuilder();
		sb1.append("INSERT INTO ").append(schema.getTableName()).append("(");
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append(" VALUES (");
		
		boolean found_auto_increment = false;
		for (String p : properties) {
			Column column = schema.getColumnByProperty(p);
			if (column.isExpression()) {
				throw new SQLException("Expression Column Found " + column.getPropertyName());
			}
			
			if (column.isAutoIncrement()) {
				found_auto_increment = true;
			}
			
			sb1.append(column.getColumnName()).append(',');
			sb2.append("?,");
		}
		
		needGenerateKeys = (schema.hasAutoIncrement() && !found_auto_increment);
		
		if (needGenerateKeys) {
			Column auto_column = schema.getAutoIncrementColumn();
			if (!"".equals(auto_column.getSequence())) {
				sb1.append(auto_column.getColumnName()).append(',');
				sb2.append(auto_column.getSequence()).append(',');
			}
		}
		
		sb1.replace(sb1.length() - 1, sb1.length(), ")");
		sb2.replace(sb2.length() - 1, sb2.length(), ")");
		
		return sb1.append(sb2).toString();
	}
	
	public int insert() throws SQLException {
		lastInsertId = null;
		sql = new StringBuilder(insertString());
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = needGenerateKeys ? conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS) 
									: conn.prepareStatement(sql.toString());
			
			setValue(stmt, values);
			int ret = stmt.executeUpdate();
			if (needGenerateKeys) {
				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					lastInsertId = schema.getAutoIncrementColumn().getType().getResult(rs, 1);
				}
			}
			return ret;
		}
		finally {
			if (rs != null) {
				try { rs.close(); } catch(SQLException e) { }
			}
			if (stmt != null) {
				try { stmt.close(); } catch(SQLException e) { }
			}
			
			clear();
		}
	}
	
	public Object getLastInsertId() {
		return lastInsertId;
	}
	
	public String getSQL() {
		return sql.toString();
	}
	
}
