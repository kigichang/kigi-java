package tw.kigi.test;

import java.math.BigDecimal;

import tw.kigi.data.annotation.Property;

public class Item {
	
	@Property(defaultValue = "0", primary=true, autoIncrement=true)
	private BigDecimal id;
	
	@Property(defaultValue = "0")
	private Long categoryId;
	
	@Property(defaultValue = "0001-01-01")
	private java.sql.Date entry;
	
	@Property(defaultValue = "")
	private String docNo;
	
	@Property(defaultValue = "")
	private String memo;
	
	@Property(defaultValue = "0")
	private Integer money;
	
	@Property(defaultValue = "N")
	//private Character deleted;
	private ItemDeleted deleted;
	
	@Property(defaultValue = "now")
	private java.util.Date created;
	
	@Property(defaultValue = "now")
	private java.sql.Timestamp updated;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public java.sql.Date getEntry() {
		return entry;
	}

	public void setEntry(java.sql.Date entry) {
		this.entry = entry;
	}

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public ItemDeleted getDeleted() {
		return deleted;
	}

	public void setDeleted(ItemDeleted deleted) {
		this.deleted = deleted;
	}

	public java.util.Date getCreated() {
		return created;
	}

	public void setCreated(java.util.Date created) {
		this.created = created;
	}

	public java.sql.Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(java.sql.Timestamp updated) {
		this.updated = updated;
	}
}
