package pie.utilities;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DatabaseConnector {
	
	public static Connection getConnection() {
		
		Context initContext = null;
		Connection connection = null;
		
		try {
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource dataSource = (DataSource) envContext.lookup("jdbc/MySQLDS");
			
			try {
				connection = dataSource.getConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return connection;
	}

}