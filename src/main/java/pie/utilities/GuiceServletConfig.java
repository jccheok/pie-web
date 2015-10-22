package pie.utilities;

import pie.filters.AuthFilter;
import pie.filters.ResponseFilter;
import pie.servlets.AddChildServlet;
import pie.servlets.ArchiveHomeworkServlet;
import pie.servlets.DeactivateGroupServlet;
import pie.servlets.DeleteGroupHomeworkServlet;
import pie.servlets.DeleteHomeworkAttachmentServlet;
import pie.servlets.DeleteHomeworkServlet;
import pie.servlets.DeleteNoteAttachmentServlet;
import pie.servlets.DeleteNoteServlet;
import pie.servlets.DeleteUserHomeworkServlet;
import pie.servlets.DownloadHomeworkAttachmentServlet;
import pie.servlets.DownloadNoteAttachmentServlet;
import pie.servlets.EnlistStudentsToGroupServlet;
import pie.servlets.GenerateCodeServlet;
import pie.servlets.GetAllDraftHomeworkServlet;
import pie.servlets.GetAllDraftNoteServlet;
import pie.servlets.GetAllDraftPublishedHomeworkServlet;
import pie.servlets.GetAllPublishedHomeworkServlet;
import pie.servlets.GetAllRecipientsServlet;
import pie.servlets.GetAllSecurityQuestionsServlet;
import pie.servlets.GetAllSentHomeworkServlet;
import pie.servlets.GetAllSentNotesServlet;
import pie.servlets.GetAllSubjectsServlet;
import pie.servlets.GetAllUserHomeworkServlet;
import pie.servlets.GetHomeworkDetailsServlet;
import pie.servlets.GetHomeworkRecipientsServlet;
import pie.servlets.GetNoteDetailsServlet;
import pie.servlets.GetPublishedHomeworkDetailsServlet;
import pie.servlets.GradeHomeworkServlet;
import pie.servlets.LoginServlet;
import pie.servlets.MarkHomeworkServlet;
import pie.servlets.NoteIsArchiveServlet;
import pie.servlets.NoteIsReadServlet;
import pie.servlets.NoteUnArchiveServlet;
import pie.servlets.PublishDraftHomeworkServlet;
import pie.servlets.PublishHomeworkServlet;
import pie.servlets.ReadHomeworkServlet;
import pie.servlets.RegisterGroupServlet;
import pie.servlets.RegisterParentServlet;
import pie.servlets.RegisterSchoolServlet;
import pie.servlets.RegisterStaffServlet;
import pie.servlets.RegisterStudentServlet;
import pie.servlets.ResetPasswordServlet;
import pie.servlets.RetrieveSecurityQuestionServlet;
import pie.servlets.SaveHomeworkAsDraftServlet;
import pie.servlets.SaveNoteAsDraftServlet;
import pie.servlets.SavePublishedHomeworkAsDraftServlet;
import pie.servlets.SendDraftNoteServlet;
import pie.servlets.SendDraftPublishedHomeworkServlet;
import pie.servlets.SendNoteResponseServlet;
import pie.servlets.SendNoteServlet;
import pie.servlets.SendPublishedHomeworkServlet;
import pie.servlets.SetAsMainParentServlet;
import pie.servlets.StaffJoinGroupServlet;
import pie.servlets.StudentJoinGroupServlet;
import pie.servlets.StudentLeaveGroupServlet;
import pie.servlets.SubmitHomeworkServlet;
import pie.servlets.TransferGroupOwnershipServlet;
import pie.servlets.UpdateDraftHomeworkServlet;
import pie.servlets.UpdateDraftPublishedHomeworkServlet;
import pie.servlets.UpdateGroupServlet;
import pie.servlets.UpdateNoteDraftServlet;
import pie.servlets.UpdatePasswordServlet;
import pie.servlets.UpdateUserAccountDetailsServlet;
import pie.servlets.UploadHomeworkAttachmentServlet;
import pie.servlets.UploadNoteServlet;
import pie.servlets.VerifyUserServlet;
import pie.servlets.ViewAllSchoolsServlet;
import pie.servlets.ViewCitiesServlet;
import pie.servlets.ViewGroupDetailsServlet;
import pie.servlets.ViewGroupMembersServlet;
import pie.servlets.ViewOpenGroupsServlet;
import pie.servlets.ViewParentChildrenServlet;
import pie.servlets.ViewRelationshipsServlet;
import pie.servlets.ViewStudentJoinedGroupsServlet;

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
				serve("*/servlets/forgetpassword").with(RetrieveSecurityQuestionServlet.class);
				serve("*/servlets/resetpassword").with(ResetPasswordServlet.class);
				serve("*/servlets/getsecurityquestions").with(GetAllSecurityQuestionsServlet.class);
				serve("*/servlets/getsubjects").with(GetAllSubjectsServlet.class);

				serve("*/servlets/secured/opengroups").with(ViewOpenGroupsServlet.class);
				serve("*/servlets/secured/updatepassword").with(UpdatePasswordServlet.class);
				serve("*/servlets/secured/updateaccountdetails").with(UpdateUserAccountDetailsServlet.class);
			
				serve("*/servlets/secured/student/leavegroup").with(StudentLeaveGroupServlet.class);
				serve("*/servlets/secured/student/joingroup").with(StudentJoinGroupServlet.class);
				serve("*/servlets/secured/student/joinedgroups").with(ViewStudentJoinedGroupsServlet.class);
				
				serve("*/servlets/secured/parent/children").with(ViewParentChildrenServlet.class);//tested
				serve("*/servlets/secured/parent/addchild").with(AddChildServlet.class);
				serve("*/servlets/secured/parent/relationships").with(ViewRelationshipsServlet.class);
				serve("*/servlets/secured/parent/setmainparent").with(SetAsMainParentServlet.class);
				
				serve("*/servlets/secured/staff/gencode").with(GenerateCodeServlet.class);
				serve("*/servlets/secured/staff/registergroup").with(RegisterGroupServlet.class);
				serve("*/servlets/secured/staff/joingroup").with(StaffJoinGroupServlet.class);
				serve("*/servlets/secured/staff/groupdetails").with(ViewGroupDetailsServlet.class);
				
				serve("*/servlets/secured/staff/group/member/groupmembers").with(ViewGroupMembersServlet.class);
				serve("*/servlets/secured/staff/group/admin/updategroup").with(UpdateGroupServlet.class);
				serve("*/servlets/secured/staff/group/owner/enliststudents").with(EnlistStudentsToGroupServlet.class);
				serve("*/servlets/secured/staff/group/owner/transfergroupownership").with(TransferGroupOwnershipServlet.class);
				serve("*/servlets/secured/staff/group/owner/deactivategroup").with(DeactivateGroupServlet.class);
				serve("*/servlets/secured/staff/group/member/allrecipients").with(GetAllRecipientsServlet.class);				
				
				serve("*/servlets/secured/staff/group/sendnote").with(SendNoteServlet.class); //deprecated - not merged with attachment
				serve("*/servlets/secured/staff/group/savenoteasdraft").with(SaveNoteAsDraftServlet.class); //deprecated - not merged with attachment
				serve("*/servlets/secured/staff/group/alldraftnote").with(GetAllDraftNoteServlet.class); // tested
				serve("*/servlets/secured/staff/group/senddraftnote").with(SendDraftNoteServlet.class); // tested
				serve("*/servlets/secured/staff/group/deletenote").with(DeleteNoteServlet.class); // tested
				serve("*/servlets/secured/staff/group/notedetails").with(GetNoteDetailsServlet.class); // tested
				serve("*/servlets/secured/staff/group/allsentnotes").with(GetAllSentNotesServlet.class); // tested
				serve("*/servlets/secured/staff/group/updatedraftnote").with(UpdateNoteDraftServlet.class); //tested
				
				serve("*/servlets/secured/staff/group/downloadnoteattachment").with(DownloadNoteAttachmentServlet.class); //tested
				serve("*/servlets/secured/staff/group/deletenoteattachment").with(DeleteNoteAttachmentServlet.class); //tested
				serve("*/servlets/secured/staff/group/noteisread").with(NoteIsReadServlet.class); //tested
				serve("*/servlets/secured/staff/group/noteisarchive").with(NoteIsArchiveServlet.class); //tested
				serve("*/servlets/secured/staff/group/noteunarchive").with(NoteUnArchiveServlet.class); //tested
				serve("*/servlets/secured/staff/group/sendnoteresponse").with(SendNoteResponseServlet.class); //tested
				serve("*/servlets/secured/staff/group/uploadatt").with(UploadNoteServlet.class);
							
				serve("*/servlets/secured/staff/group/createhomework").with(PublishHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/createdrafthomework").with(PublishDraftHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/savehomeworkasdraft").with(SaveHomeworkAsDraftServlet.class);
				serve("*/servlets/secured/staff/group/updatehomeworkdraft").with(UpdateDraftHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/allhomeworkdraft").with(GetAllDraftHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/allcreatedhomework").with(GetAllPublishedHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/homeworkdetails").with(GetHomeworkDetailsServlet.class);
				serve("*/servlets/secured/staff/group/deletehomework").with(DeleteHomeworkServlet.class);				

				serve("*/servlets/secured/staff/group/sendpublishedhomework").with(SendPublishedHomeworkServlet.class);//tested
				serve("*/servlets/secured/staff/group/senddraftpublishedhomework").with(SendDraftPublishedHomeworkServlet.class);//tested
				serve("*/servlets/secured/staff/group/savepublishedhomeworkasdraft").with(SavePublishedHomeworkAsDraftServlet.class);//tested
				serve("*/servlets/secured/staff/group/updatedraftpublishedhomework").with(UpdateDraftPublishedHomeworkServlet.class);//tested
				serve("*/servlets/secured/staff/group/allsenthomework").with(GetAllSentHomeworkServlet.class);//tested
				serve("*/servlets/secured/staff/group/alldraftpublishedhomework").with(GetAllDraftPublishedHomeworkServlet.class);//tested
				serve("*/servlets/secured/staff/group/publishedhomeworkdetails").with(GetPublishedHomeworkDetailsServlet.class);//tested
				serve("*/servlets/secured/staff/group/deletegrouphomework").with(DeleteGroupHomeworkServlet.class);//tested

				serve("*/servlets/secured/staff/group/homeworkrecipients").with(GetHomeworkRecipientsServlet.class);//tested
				serve("*/servlets/secured/staff/group/markhomework").with(MarkHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/gradehomework").with(GradeHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/submithomework").with(SubmitHomeworkServlet.class);
				serve("*/servlets/secured/student/deletehomework").with(DeleteUserHomeworkServlet.class);
				serve("*/servlets/secured/student/archivehomework").with(ArchiveHomeworkServlet.class);
				serve("*/servlets/secured/student/readhomework").with(ReadHomeworkServlet.class);
				serve("*/servlets/secured/student/allrecievedhomework").with(GetAllUserHomeworkServlet.class);

				serve("*/servlets/secured/staff/group/uploadhomeworkattachment").with(UploadHomeworkAttachmentServlet.class);
				serve("*/servlets/secured/staff/group/downloadhomeworkattachment").with(DownloadHomeworkAttachmentServlet.class);
				serve("*/servlets/secured/staff/group/deletehomeworkattachment").with(DeleteHomeworkAttachmentServlet.class);			
				
				serve("*/servlets/secured/admin/registerschool").with(RegisterSchoolServlet.class);
				serve("*/servlets/secured/admin/allschools").with(ViewAllSchoolsServlet.class);
				
			}
		});
	}

}
