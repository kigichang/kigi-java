package tw.kigi.data.driver;

import java.sql.Connection;
import java.sql.SQLException;

import tw.kigi.data.Query;

public class Oracle<T> extends Query<T> {

	public Oracle(Connection conn, Class<T> clazz) throws SQLException {
		super(conn, clazz);
		
	}

	@Override
	public T[] paginate(int start, int length) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
