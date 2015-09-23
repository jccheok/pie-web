package pie.servlet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import pie.service.UserService;
import pie.service.UserService.LoginResult;

public class LoginServletTest {
	
	String EMAIL = "admin@rp.edu.sg";
	String PASSWORD = "root";
	
	UserService userService = mock(UserService.class);
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	PrintWriter writer = mock(PrintWriter.class);
	
	LoginServlet servlet = new LoginServlet(userService);
	
	@Test
	public void testSuccess() throws ServletException, IOException {
		
		when(request.getParameter("userEmail")).thenReturn(EMAIL);
		when(request.getParameter("userPassword")).thenReturn(PASSWORD);
		
		when(userService.login(EMAIL, PASSWORD)).thenReturn(LoginResult.SUCCESS);
		when(response.getWriter()).thenReturn(writer);
		
		servlet.doPost(request, response);
		
		verify(writer).write("{\"result\":\"SUCCESS\"}");
	}
	
	@Test
	public void testNotVerified() throws ServletException, IOException {
		
		when(request.getParameter("userEmail")).thenReturn(EMAIL);
		when(request.getParameter("userPassword")).thenReturn(PASSWORD);
		
		when(userService.login(EMAIL, PASSWORD)).thenReturn(LoginResult.NOT_VERIFIED);
		when(response.getWriter()).thenReturn(writer);
		
		servlet.doPost(request, response);
		
		verify(writer).write("{\"result\":\"NOT_VERIFIED\"}");
	}
	
	@Test
	public void testNotMatching() throws ServletException, IOException {
		
		when(request.getParameter("userEmail")).thenReturn(EMAIL);
		when(request.getParameter("userPassword")).thenReturn(PASSWORD);
		
		when(userService.login(EMAIL, PASSWORD)).thenReturn(LoginResult.NOT_MATCHING);
		when(response.getWriter()).thenReturn(writer);
		
		servlet.doPost(request, response);
		
		verify(writer).write("{\"result\":\"NOT_MATCHING\"}");
	}
	
	@Test
	public void testNotRegistered() throws ServletException, IOException {
		
		when(request.getParameter("userEmail")).thenReturn(EMAIL);
		when(request.getParameter("userPassword")).thenReturn(PASSWORD);
		
		when(userService.login(EMAIL, PASSWORD)).thenReturn(LoginResult.NOT_REGISTERED);
		when(response.getWriter()).thenReturn(writer);
		
		servlet.doPost(request, response);
		
		verify(writer).write("{\"result\":\"NOT_REGISTERED\"}");
	}
	
	@Test
	public void testNotValid() throws ServletException, IOException {
		
		when(request.getParameter("userEmail")).thenReturn(EMAIL);
		when(request.getParameter("userPassword")).thenReturn(PASSWORD);
		
		when(userService.login(EMAIL, PASSWORD)).thenReturn(LoginResult.NOT_VALID);
		when(response.getWriter()).thenReturn(writer);
		
		servlet.doPost(request, response);
		
		verify(writer).write("{\"result\":\"NOT_VALID\"}");
	}
}
