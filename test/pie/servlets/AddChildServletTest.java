package pie.servlets;

import static org.junit.Assert.*;
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

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import pie.User;
import pie.constants.AddChildResult;
import pie.constants.SupportedPlatform;
import pie.services.ParentService;
import pie.utilities.Utilities;

public class AddChildServletTest {

	AddChildServlet addChildServlet;

	String PARENT_ID = "1";
	String RELATIONSHIP_ID = "1";
	String STUDENT_CODE = "1234";

	ParentService mockParentService = mock(ParentService.class);
	HttpServletRequest mockRequest = mock(HttpServletRequest.class);
	HttpServletResponse mockResponse = mock(HttpServletResponse.class);
	Utilities mockUti = mock(Utilities.class);
	PrintWriter mockWriter = mock(PrintWriter.class);

	private Injector mockInjector;

	@Before
	public void setupMocks() throws Exception {
		when(mockRequest.getParameter("parentID")).thenReturn(PARENT_ID);
		when(mockRequest.getParameter("relationshipID")).thenReturn(RELATIONSHIP_ID);
		when(mockRequest.getParameter("studentCode")).thenReturn(STUDENT_CODE);

		when(mockResponse.getWriter()).thenReturn(mockWriter);

		mockInjector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(ParentService.class).toInstance(mockParentService);
			}
		});

		addChildServlet = mockInjector.getInstance(AddChildServlet.class);
	}

	@After
	public void tearDown() throws Exception {
		mockInjector = null;
	}

	@Test
	public void addChildToParentSuccess() throws ServletException, IOException {
		when(mockParentService.addChild(Integer.parseInt(PARENT_ID), Integer.parseInt(RELATIONSHIP_ID), STUDENT_CODE))
				.thenReturn(AddChildResult.SUCCESS);

		addChildServlet.doPost(mockRequest, mockResponse);

		ArgumentCaptor<String> stringCapture = ArgumentCaptor.forClass(String.class);
		verify(mockWriter).write(stringCapture.capture());
		JSONObject servletResponse = new JSONObject(stringCapture.getValue());

		assertEquals(servletResponse.get("result"), AddChildResult.SUCCESS.toString());
	}

	@Test
	public void addChildToParentInvalidStudentCode() {
		when(mockParentService.addChild(Integer.parseInt(PARENT_ID), Integer.parseInt(RELATIONSHIP_ID), STUDENT_CODE))
				.thenReturn(AddChildResult.INVALID_STUDENT_CODE);

		try {
			addChildServlet.doPost(mockRequest, mockResponse);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArgumentCaptor<String> stringCapture = ArgumentCaptor.forClass(String.class);
		verify(mockWriter).write(stringCapture.capture());
		JSONObject servletResponse = new JSONObject(stringCapture.getValue());

		assertEquals(servletResponse.get("result"), AddChildResult.INVALID_STUDENT_CODE.toString());
	}

	@Test
	public void addChildToParentChildAlreadyAdded() {
		when(mockParentService.addChild(Integer.parseInt(PARENT_ID), Integer.parseInt(RELATIONSHIP_ID), STUDENT_CODE))
				.thenReturn(AddChildResult.CHILD_ALREADY_ADDED);

		try {
			addChildServlet.doPost(mockRequest, mockResponse);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArgumentCaptor<String> stringCapture = ArgumentCaptor.forClass(String.class);
		verify(mockWriter).write(stringCapture.capture());
		JSONObject servletResponse = new JSONObject(stringCapture.getValue());

		assertEquals(servletResponse.get("result"), AddChildResult.CHILD_ALREADY_ADDED.toString());
	}
}
