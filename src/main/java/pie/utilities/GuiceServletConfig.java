package pie.utilities;

import pie.filters.AuthFilter;
import pie.filters.ResponseFilter;
import pie.servlets.AddChildServlet;
import pie.servlets.AddChildToGroupServlet;
import pie.servlets.EnlistStudentsToGroupServlet;
import pie.servlets.GenerateCodeServlet;
import pie.servlets.LoginServlet;
import pie.servlets.RegisterGroupServlet;
import pie.servlets.RegisterParentServlet;
import pie.servlets.RegisterSchoolServlet;
import pie.servlets.RegisterStaffServlet;
import pie.servlets.RegisterStudentServlet;
import pie.servlets.StaffJoinGroupServlet;
import pie.servlets.StudentJoinGroupServlet;
import pie.servlets.UpdateGroupServlet;
import pie.servlets.VerifyUserServlet;
import pie.servlets.ViewCitiesServlet;
import pie.servlets.ViewGroupDetailsServlet;
import pie.servlets.ViewParentChildrenServlet;
import pie.servlets.ViewRelationshipsServlet;
import pie.servlets.ViewStaffJoinedGroupsServlet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
			
			@Override
			protected void configureServlets() {
				
				// Filters
				filter("/servlets/*").through(ResponseFilter.class);
				filter("/servlets/secured/*").through(AuthFilter.class);
				
				// Servlets
				serve("*/servlets/login").with(LoginServlet.class);
				serve("*/servlets/registerparent").with(RegisterParentServlet.class);
				serve("*/servlets/registerstudent").with(RegisterStudentServlet.class);
				serve("*/servlets/registerstaff").with(RegisterStaffServlet.class);
				serve("*/servlets/verify").with(VerifyUserServlet.class);
				serve("*/servlets/cities").with(ViewCitiesServlet.class);
				
				serve("*/servlets/secured/student/joingroup").with(StudentJoinGroupServlet.class);
				
				serve("*/servlets/secured/parent/children").with(ViewParentChildrenServlet.class);
				serve("*/servlets/secured/parent/addchild").with(AddChildServlet.class);
				serve("*/servlets/secured/parent/addchildtogroup").with(AddChildToGroupServlet.class);
				serve("*/servlets/secured/parent/relationships").with(ViewRelationshipsServlet.class);
				
				serve("*/servlets/secured/staff/gencode").with(GenerateCodeServlet.class);
				serve("*/servlets/secured/staff/registergroup").with(RegisterGroupServlet.class);
				serve("*/servlets/secured/staff/joingroup").with(StaffJoinGroupServlet.class);
				serve("*/servlets/secured/staff/groupdetails").with(ViewGroupDetailsServlet.class);
				serve("*/servlets/secured/staff/joinedgroups").with(ViewStaffJoinedGroupsServlet.class);
				
				serve("*/servlets/secured/staff/group/member/groupmembers").with(ViewGroupDetailsServlet.class);
				serve("*/servlets/secured/staff/group/admin/updategroup").with(UpdateGroupServlet.class);
				serve("*/servlets/secured/staff/group/owner/enliststudents").with(EnlistStudentsToGroupServlet.class);
				
				serve("*/servlets/secured/admin/registerschool").with(RegisterSchoolServlet.class);
				
			}
		});
	}

}
