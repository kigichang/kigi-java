package tw.kigi.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import tw.kigi.data.annotation.BelongsTo;
import tw.kigi.data.annotation.HasMany;
import tw.kigi.data.annotation.HasOne;
import tw.kigi.data.annotation.ManyToMany;
import tw.kigi.data.annotation.OrderBy;
import tw.kigi.util.Convention;

public class Relation {
	String name = null;
	RelationType type = null;
	Schema schema = null;
	Schema relation = null;
	Column foreignKey = null;
	String conditions = null;
	Column[] properties = null;
	Sort[] sorts = null;
	
	Method setter = null;
	
	private void init(Field field, RelationType type, String schema, String foreign_key, String relation, String conditions, String[] properties, OrderBy[] sorts) throws SQLException {
		this.name = Convention.toPropertyName(field.getName());
		try {
			Class<?> clazz = Class.forName(schema);
			setter = clazz.getMethod("set" + this.name, field.getType());
			this.schema = Schema.getSchema(clazz);
			if (RelationType.ManyToMany != type) {
				this.foreignKey = this.schema.getColumnByProperty(this.schema.append(foreign_key));
			}
			else {
				this.relation = Schema.getSchema(Class.forName(relation));
			}
			if (properties == null || properties.length <= 0) {
				this.properties = this.schema.convertStringToColumn(properties);
			}
			else {
				this.properties = this.schema.getProperties();
			}
			
			this.sorts = this.schema.convertSort(sorts);
			
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			new SQLException(e);
		}
		
		
		this.type = type;
		this.conditions = conditions;
		
	}
	
	public Relation(Field field, HasOne relation) throws SQLException {
		init(field, 
				RelationType.HasOne, 
				relation.schema(),
				relation.foreignKey(),
				null, 
				relation.conditions(), 
				relation.properties(), 
				relation.sort());
	}
	
	public Relation(Field field, HasMany relation) throws SQLException {
		init(field, 
				RelationType.HasMany, 
				relation.schema(), 
				relation.foreignKey(), 
				null,
				relation.conditions(), 
				relation.properties(), 
				relation.sort());
	}

	public Relation(Field field, BelongsTo relation) throws SQLException {
		init(field, 
				RelationType.BelongsTo, 
				relation.schema(), 
				relation.foreignKey(), 
				null,
				relation.conditions(), 
				relation.properties(), 
				relation.sort());
	}

	public Relation(Field field, ManyToMany relation) throws SQLException {
		init(field,
				RelationType.ManyToMany,
				relation.schema(),
				null,
				relation.relation(),
				relation.conditions(),
				relation.properties(),
				relation.sort());
	}
	
	public String getName() {
		return name;
	}
	public RelationType getType() {
		return type;
	}
	public Schema getSchema() {
		return schema;
	}
	public Schema getRelation() {
		return relation;
	}
	public Column getForeignKey() {
		return foreignKey;
	}
	public String getConditions() {
		return conditions;
	}
	public Column[] getProperties() {
		return properties;
	}
	public Sort[] getSorts() {
		return sorts;
	}
	
	public void set(Object data, Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		setter.invoke(data, val);
	}

}
