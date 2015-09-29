package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.JoinGroupResult;
import pie.services.StudentService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StudentJoinGroupServlet extends HttpServlet {

	private static final long serialVersionUID = 1354541147333762368L;
	
	StudentService studentService;

	@Inject
	public StudentJoinGroupServlet(StudentService studentService) {
		this.studentService = studentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		int groupID = Integer.parseInt(request.getParameter("groupID"));
		int studentID = Integer.parseInt(request.getParameter("studentID"));
		String groupCode = request.getParameter("groupCode");

		JoinGroupResult joinGroupResult = studentService.joinGroup(groupID, studentID, groupCode);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", joinGroupResult.toString());
		responseObject.put("message", joinGroupResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
