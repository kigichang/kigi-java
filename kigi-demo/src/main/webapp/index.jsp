<%@page import="tw.kigi.test.ItemDeleted" %>
<%@page import="tw.kigi.data.EnumValue"%>
<%@page import="tw.kigi.data.EnumType"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="tw.kigi.data.Query"%>
<%@page import="tw.kigi.data.Direction"%>
<%@page import="tw.kigi.data.driver.MySQL" %>
<%@page import="tw.kigi.test.Item"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
中文測試<br />
<%
Class.forName("com.mysql.jdbc.Driver");
String url = "jdbc:mysql://localhost:3306/account?user=yunho&password=056962633&useUnicode=true&characterEncoding=UTF8";
Connection conn = DriverManager.getConnection(url);
Query<Item> q = new MySQL<Item>(conn, Item.class);

q.properties("CategoryId", "Entry", "DocNo", "Memo", "Money", "Deleted", "Created", "Updated")
.values(new Long(1), 
		new java.sql.Date(System.currentTimeMillis()), 
		"100", 
		"中文測試", 
		new Integer(1000), 
		'N', 
		new java.util.Date(), 
		new java.sql.Timestamp(System.currentTimeMillis()))
.insert();

java.math.BigDecimal id = (java.math.BigDecimal)q.getLastInsertId();

q.properties("CategoryId", "Money")
    .condition("Item.Id = ?")
    .values(new Long(1), new Integer(5), new java.math.BigDecimal(20))
    .update();
//q.clear();

q.condition("Item.Id = ?").values(new java.math.BigDecimal(20)).delete();
//q.clear();

Item[] items = q.condition("Item.CategoryId = ?")
.values(new Long(1))
//.order("Money", Direction.ASC)
.order("Id", Direction.DESC)
.find();
//.paginate(0, 5);

Item item_tmp = new Item();
item_tmp.setId(id);
item_tmp.setMemo("中文哈哈哈");
q.update(item_tmp);

//item_tmp.setId(new java.math.BigDecimal(999999));
item_tmp.setId(null);
item_tmp.setCategoryId(998L);
item_tmp.setEntry(new java.sql.Date(System.currentTimeMillis()));
item_tmp.setDocNo("998");
item_tmp.setMemo("中文大亂鬥2");
item_tmp.setMoney(998);
item_tmp.setDeleted(ItemDeleted.ALIVE);
//item_tmp.setCreated(new java.util.Date());
//q.insert(item_tmp);
//id = item_tmp.getId();
conn.close();

EnumType<ItemDeleted, Character> e_type = new EnumType<ItemDeleted, Character>(ItemDeleted.class);

for (Item item : items) {
%>
[<%=item.getId() %>]
[<%=item.getCategoryId() %>]
[<%=item.getEntry() %>]
[<%=item.getDocNo() %>]
[<%=item.getMemo() %>]
[<%=item.getMoney() %>]
[<%=item.getDeleted() %>]
[<%=item.getCreated() %>]
[<%=item.getUpdated() %>]<br />
<%
}
%>
<br />
<%=q.getSQL() %>
<br />
LastId <%=id %>
<br />
Item.CategoryId=<%=request.getParameter("Item.CategoryId") %>
test reset
</body>
</html>