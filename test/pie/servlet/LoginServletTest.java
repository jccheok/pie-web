package pie.servlet;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pie.service.UserService;
import pie.service.UserService.LoginResult;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class LoginServletTest {
	
	String EMAIL = "admin@rp.edu.sg";
	String PASSWORD = "root";
	
	LoginServlet loginServlet;
	
	UserService mockUserService = mock(UserService.class);
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	PrintWriter writer = mock(PrintWriter.class);
	
	private Injector mockInjector;
	
	@Before
	public void setupMocks() throws Exception {
		
		when(request.getParameter("userEmail")).thenReturn(EMAIL);
		when(request.getParameter("userPassword")).thenReturn(PASSWORD);
		when(response.getWriter()).thenReturn(writer);
		
		mockInjector = Guice.createInjector(new AbstractModule() {
			
			@Override
			protected void configure() {
				bind(UserService.class).toInstance(mockUserService);
			}
		});
		
		loginServlet = mockInjector.getInstance(LoginServlet.class);
		
	}
	
	@After
	public void tearDown() throws Exception {
		mockInjector = null;
	}
 	
	@Test
	public void testSuccess() throws ServletException, IOException {
		
		when(mockUserService.login(EMAIL, PASSWORD)).thenReturn(LoginResult.SUCCESS);
		loginServlet.doPost(request, response);
		verify(writer).write("{\"result\":\"SUCCESS\"}");
	}
	
	@Test
	public void testNotMatching() throws ServletException, IOException {
		
		when(mockUserService.login(EMAIL, PASSWORD)).thenReturn(LoginResult.NOT_MATCHING);
		loginServlet.doPost(request, response);
		verify(writer).write("{\"result\":\"NOT_MATCHING\"}");
	}
	
	@Test
	public void testNotRegistered() throws ServletException, IOException {
		
		when(mockUserService.login(EMAIL, PASSWORD)).thenReturn(LoginResult.NOT_REGISTERED);
		loginServlet.doPost(request, response);
		verify(writer).write("{\"result\":\"NOT_REGISTERED\"}");
	}
	
	@Test
	public void testNotValid() throws ServletException, IOException {
		
		when(mockUserService.login(EMAIL, PASSWORD)).thenReturn(LoginResult.NOT_VALID);
		loginServlet.doPost(request, response);
		verify(writer).write("{\"result\":\"NOT_VALID\"}");
	}
	
	@Test
	public void notVerified() throws ServletException, IOException {
		
		when(mockUserService.login(EMAIL, PASSWORD)).thenReturn(LoginResult.NOT_VERIFIED);
		loginServlet.doPost(request, response);
		verify(writer).write("{\"result\":\"NOT_VERIFIED\"}");
	}

}
