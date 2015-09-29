package pie.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pie.utilities.DatabaseConnector;

public class TestService extends HttpServlet {

	private static final long serialVersionUID = 3174417002707844043L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		Connection connection = DatabaseConnector.getConnection();

		try {

			boolean reachable = connection.isValid(10);
			out.println("<h1>Connection Status</h1>");
			out.println("Database Connection Status:" + reachable);
			connection.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

}