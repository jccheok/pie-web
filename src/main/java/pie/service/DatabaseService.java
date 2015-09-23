package pie.service;

import java.sql.Connection;

public abstract class DatabaseService {
	
	protected Connection conn;

	public DatabaseService(Connection conn) {
		this.conn = conn;
	}
	
}
