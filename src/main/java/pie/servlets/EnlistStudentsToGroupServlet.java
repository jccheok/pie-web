package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.services.StudentService;
import pie.utilities.Utilities;

public class EnlistStudentsToGroupServlet {
	
	StudentService studentService;
	
	public EnlistStudentsToGroupServlet(StudentService studentService){
		this.studentService = studentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject studentObject = null;
		int schoolID = 0;
		int groupID = 0;
		
		try {
		
			Map<String, String> requestParameters = Utilities.getParameters(request, "studentList", "schoolID", "groupID");
			studentObject = new JSONObject().getJSONObject(requestParameters.get("studentList"));
			schoolID = Integer.parseInt(requestParameters.get("schoolID"));
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			
		}
		
		boolean enlistStudentResult = studentService.enlistStudentToGroups(studentObject, schoolID, groupID);
		
		JSONObject responseObject = new JSONObject();

		if(enlistStudentResult){
			responseObject.put("result", "Success");
			responseObject.put("message", "Enlist students to group");
		}else{
			responseObject.put("result", "Failed");
			responseObject.put("message", "Failed to enlist students to group");
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
