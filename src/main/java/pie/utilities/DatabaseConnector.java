package pie.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
	
	
	// OpenShift
	private static String MYSQL_USERNAME = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
	private static String MYSQL_PASSWORD = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
	private static String MYSQL_DATABASE_HOST = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
	private static String MYSQL_DATABASE_PORT = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
	private static String MYSQL_DATABASE_NAME = "pie";
	
	// Local
	/*
	private static String MYSQL_USERNAME = "root";
	private static String MYSQL_PASSWORD = "";
	private static String MYSQL_DATABASE_HOST = "127.0.0.1";
	private static String MYSQL_DATABASE_PORT = "3307";
	private static String MYSQL_DATABASE_NAME = "pie";
	*/
	
	private static String MYSQL_PARAMETERS = "zeroDateTimeBehavior=convertToNull";
	
	private static String url = String.format("jdbc:mysql://%s:%s/%s?%s", MYSQL_DATABASE_HOST, MYSQL_DATABASE_PORT, MYSQL_DATABASE_NAME, MYSQL_PARAMETERS);

	public static Connection getConnection() {

		Connection connection = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception exception) {
			System.out.println(exception); 
		}

		try {

			connection = DriverManager.getConnection(url, MYSQL_USERNAME, MYSQL_PASSWORD);
		} catch (SQLException exception) {

			System.out.println(exception); 
		}
		return connection;
	}

}