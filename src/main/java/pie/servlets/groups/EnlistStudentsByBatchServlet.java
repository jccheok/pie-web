package pie.servlets.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.constants.GenericResult;
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EnlistStudentsByBatchServlet extends HttpServlet {
	
	private static final long serialVersionUID = 3674377035466826694L;
	
	StudentService studentService;
	
	@Inject
	public EnlistStudentsByBatchServlet(StudentService studentService){
		this.studentService = studentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String studentsData = null;
		int groupID = 0;
		
		try {
		
			Map<String, String> requestParameters = Utilities.getParameters(request, "studentsData", "groupID");
			studentsData = requestParameters.get("studentsData");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		JSONObject requestObject = new JSONObject(studentsData);
		JSONArray studentList = requestObject.getJSONArray("studentList");
		
		for (int index = 0; index < studentList.length(); index++) {
			
			JSONObject student = studentList.getJSONObject(index);
			
			String studentFirstName = student.getString("studentFirstName");
			String studentLastName = student.getString("studentLastName");
			String studentCode = studentService.generateStudentCode();
			int studentIndexNumber = student.getInt("studentGroupIndexNumber");
			String SUID = student.getString("SUID");
			
			studentService.enlistStudent(studentFirstName, studentLastName, studentCode, groupID, studentIndexNumber);
		}
		
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", GenericResult.SUCCESS.toString());
		responseObject.put("message", "Students successfuly enlisted.");

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
