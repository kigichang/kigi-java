package tw.kigi.test;

import java.math.BigDecimal;

import tw.kigi.data.annotation.HasMany;
import tw.kigi.data.annotation.Property;

public class Category {
	@Property(defaultValue = "0", primary=true, autoIncrement=true)
	private BigDecimal id;
	
	@Property(defaultValue = "")
	private String name;
	
	@Property(defaultValue="1")
	private CategoryType type;
	
	@Property(defaultValue="now")
	private java.util.Date Created;
	
	@Property(defaultValue="now")
	private java.sql.Timestamp Updated;

	@HasMany(conditions = "Item.Deleted = 'N'", foreignKey = "CategoryId", length = 0, schema = "Item", start = 0)
	private Item[] items;
	
	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CategoryType getType() {
		return type;
	}

	public void setType(CategoryType type) {
		this.type = type;
	}

	public java.util.Date getCreated() {
		return Created;
	}

	public void setCreated(java.util.Date created) {
		Created = created;
	}

	public java.sql.Timestamp getUpdated() {
		return Updated;
	}

	public void setUpdated(java.sql.Timestamp updated) {
		Updated = updated;
	}
	
}
