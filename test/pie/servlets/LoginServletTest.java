package pie.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import pie.User;
import pie.UserType;
import pie.constants.LoginResult;
import pie.constants.SupportedPlatform;
import pie.services.UserService;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class LoginServletTest {
	
	String EMAIL = "admin@rp.edu.sg";
	String PASSWORD = "root";
	SupportedPlatform CLIENT_PLATFORM = SupportedPlatform.WEB;
	
	LoginServlet loginServlet;
	
	UserService mockUserService = mock(UserService.class);
	HttpServletRequest mockRequest = mock(HttpServletRequest.class);
	HttpServletResponse mockResponse = mock(HttpServletResponse.class);
	PrintWriter mockWriter = mock(PrintWriter.class);
	
	private Injector mockInjector;
	
	@Before
	public void setupMocks() throws Exception {
		
		when(mockRequest.getParameter("userEmail")).thenReturn(EMAIL);
		when(mockRequest.getParameter("userPassword")).thenReturn(PASSWORD);
		when(mockRequest.getParameter("platformID")).thenReturn(String.valueOf(CLIENT_PLATFORM.getSupportedPlatformID()));
		when(mockResponse.getWriter()).thenReturn(mockWriter);
		
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
		
		User mockUser = new User();
		mockUser.setUserID(1);
		mockUser.setUserFirstName("Test");
		mockUser.setUserLastName("User");
		mockUser.setUserEmail(EMAIL);
		mockUser.setUserMobile("1111111111");
		mockUser.setUserType(UserType.PARENT);
		
		when(mockUserService.loginUser(EMAIL, PASSWORD, CLIENT_PLATFORM)).thenReturn(LoginResult.SUCCESS);
		when(mockUserService.getUser(mockUserService.getUserID(EMAIL))).thenReturn(mockUser);
		
		loginServlet.doPost(mockRequest, mockResponse);
		
		ArgumentCaptor<String> stringCapture = ArgumentCaptor.forClass(String.class);
		verify(mockWriter).write(stringCapture.capture());
		JSONObject servletResponse = new JSONObject(stringCapture.getValue());
		
		assertTrue(servletResponse.has("result"));
		assertEquals(servletResponse.get("result"), LoginResult.SUCCESS.name());
		
		assertTrue(servletResponse.has("user"));
		assertEquals(servletResponse.getJSONObject("user").get("userID"), mockUser.getUserID());
		assertEquals(servletResponse.getJSONObject("user").get("userFirstName"), mockUser.getUserFirstName());
		assertEquals(servletResponse.getJSONObject("user").get("userLastName"), mockUser.getUserLastName());
		assertEquals(servletResponse.getJSONObject("user").get("userType"), mockUser.getUserType().name());
		assertEquals(servletResponse.getJSONObject("user").get("userEmail"), mockUser.getUserEmail());
		assertEquals(servletResponse.getJSONObject("user").get("userMobile"), mockUser.getUserMobile());
		
	}
	
	@Test
	public void testNotMatching() throws ServletException, IOException {
		
		when(mockUserService.loginUser(EMAIL, PASSWORD, CLIENT_PLATFORM)).thenReturn(LoginResult.NOT_MATCHING);
		
		loginServlet.doPost(mockRequest, mockResponse);
		
		ArgumentCaptor<String> stringCapture = ArgumentCaptor.forClass(String.class);
		verify(mockWriter).write(stringCapture.capture());
		
		JSONObject servletResponse = new JSONObject(stringCapture.getValue());
		
		assertTrue(servletResponse.has("result"));
		assertEquals(servletResponse.get("result"), LoginResult.NOT_MATCHING.toString());
		
		assertFalse(servletResponse.has("user"));
	}
	
	@Test
	public void testNotRegistered() throws ServletException, IOException {
		
		when(mockUserService.loginUser(EMAIL, PASSWORD, CLIENT_PLATFORM)).thenReturn(LoginResult.NOT_REGISTERED);
		
		loginServlet.doPost(mockRequest, mockResponse);
		
		ArgumentCaptor<String> stringCapture = ArgumentCaptor.forClass(String.class);
		verify(mockWriter).write(stringCapture.capture());
		
		JSONObject servletResponse = new JSONObject(stringCapture.getValue());
		
		assertTrue(servletResponse.has("result"));
		assertEquals(servletResponse.get("result"), LoginResult.NOT_REGISTERED.toString());
		
		assertFalse(servletResponse.has("user"));
	}
	
	@Test
	public void testNotValid() throws ServletException, IOException {
		
		when(mockUserService.loginUser(EMAIL, PASSWORD, CLIENT_PLATFORM)).thenReturn(LoginResult.NOT_VALID);
		
		loginServlet.doPost(mockRequest, mockResponse);
		
		ArgumentCaptor<String> stringCapture = ArgumentCaptor.forClass(String.class);
		verify(mockWriter).write(stringCapture.capture());
		
		JSONObject servletResponse = new JSONObject(stringCapture.getValue());

		assertTrue(servletResponse.has("result"));
		assertEquals(servletResponse.get("result"), LoginResult.NOT_VALID.toString());
		
		assertFalse(servletResponse.has("user"));
	}
	
	@Test
	public void testNotVerified() throws ServletException, IOException {
		
		when(mockUserService.loginUser(EMAIL, PASSWORD, CLIENT_PLATFORM)).thenReturn(LoginResult.NOT_VERIFIED);
		
		loginServlet.doPost(mockRequest, mockResponse);
		
		ArgumentCaptor<String> stringCapture = ArgumentCaptor.forClass(String.class);
		verify(mockWriter).write(stringCapture.capture());
		
		JSONObject servletResponse = new JSONObject(stringCapture.getValue());
		
		assertTrue(servletResponse.has("result"));
		assertEquals(servletResponse.get("result"), LoginResult.NOT_VERIFIED.toString());
		
		assertFalse(servletResponse.has("user"));
	}
	
	@Test
	public void testUnsupportedPlatform() throws ServletException, IOException {
		
		when(mockUserService.loginUser(EMAIL, PASSWORD, CLIENT_PLATFORM)).thenReturn(LoginResult.PLATFORM_UNSUPPORTED);
		
		loginServlet.doPost(mockRequest, mockResponse);
		
		ArgumentCaptor<String> stringCapture = ArgumentCaptor.forClass(String.class);
		verify(mockWriter).write(stringCapture.capture());
		
		JSONObject servletResponse = new JSONObject(stringCapture.getValue());
		
		assertTrue(servletResponse.has("result"));
		assertEquals(servletResponse.get("result"), LoginResult.PLATFORM_UNSUPPORTED.toString());
		
		assertFalse(servletResponse.has("user"));
	}

}
