package tw.kigi.data.driver;

import java.sql.Connection;
import java.sql.SQLException;

import tw.kigi.data.Query;

public class MySQL<T> extends Query<T> {

	public MySQL(Connection conn, Class<T> clazz) throws SQLException {
		super(conn, clazz);
	}

	@Override
	public T[] paginate(int start, int length) throws SQLException {
		sql = new StringBuilder(selectString()).append(" LIMIT ").append(start).append(',').append(length);
		return findBySQL(sql.toString());
	}

}
