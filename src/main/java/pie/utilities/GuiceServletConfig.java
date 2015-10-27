package pie.utilities;

import pie.filters.AuthFilter;
import pie.filters.ResponseFilter;
import pie.servlets.GenerateCodeServlet;
import pie.servlets.GetCitiesServlet;
import pie.servlets.GetRelationshipsServlet;
import pie.servlets.GetSecurityQuestionsServlet;
import pie.servlets.GetSubjectsServlet;
import pie.servlets.groups.DeactivateGroupServlet;
import pie.servlets.groups.EnlistStudentServlet;
import pie.servlets.groups.EnlistStudentsByBatchServlet;
import pie.servlets.groups.GetGroupDetailsServlet;
import pie.servlets.groups.RegisterGroupServlet;
import pie.servlets.groups.StaffJoinGroupServlet;
import pie.servlets.groups.StudentJoinGroupServlet;
import pie.servlets.groups.StudentLeaveGroupServlet;
import pie.servlets.groups.TransferGroupOwnershipServlet;
import pie.servlets.groups.UpdateGroupServlet;
import pie.servlets.groups.ViewGroupMembersServlet;
import pie.servlets.groups.ViewOpenGroupsServlet;
import pie.servlets.groups.ViewStudentJoinedGroupsServlet;
import pie.servlets.homework.DeleteGroupHomeworkServlet;
import pie.servlets.homework.DeleteHomeworkAttachmentServlet;
import pie.servlets.homework.DeleteHomeworkServlet;
import pie.servlets.homework.DeleteUserHomeworkServlet;
import pie.servlets.homework.DownloadHomeworkAttachmentServlet;
import pie.servlets.homework.GetAllDraftHomeworkServlet;
import pie.servlets.homework.GetAllDraftPublishedHomeworkServlet;
import pie.servlets.homework.GetAllPublishedHomeworkServlet;
import pie.servlets.homework.GetAllSentHomeworkServlet;
import pie.servlets.homework.GetAllUserHomeworkServlet;
import pie.servlets.homework.GetHomeworkDetailsServlet;
import pie.servlets.homework.GetHomeworkRecipientsServlet;
import pie.servlets.homework.GetIndividualStudentReport;
import pie.servlets.homework.GetGroupHomeworkDetailsServlet;
import pie.servlets.homework.GetUserHomeworkDetailsServlet;
import pie.servlets.homework.GradeHomeworkServlet;
import pie.servlets.homework.PublishDraftHomeworkServlet;
import pie.servlets.homework.PublishHomeworkServlet;
import pie.servlets.homework.SaveHomeworkAsDraftServlet;
import pie.servlets.homework.SavePublishedHomeworkAsDraftServlet;
import pie.servlets.homework.SendDraftPublishedHomeworkServlet;
import pie.servlets.homework.SendPublishedHomeworkServlet;
import pie.servlets.homework.SetArchiveHomeworkServlet;
import pie.servlets.homework.SetMarkHomeworkServlet;
import pie.servlets.homework.SetReadHomeworkServlet;
import pie.servlets.homework.SetSubmitHomeworkServlet;
import pie.servlets.homework.UpdateDraftHomeworkServlet;
import pie.servlets.homework.UpdateDraftPublishedHomeworkServlet;
import pie.servlets.notes.DeleteNoteAttachmentServlet;
import pie.servlets.notes.DeleteNoteServlet;
import pie.servlets.notes.DownloadNoteAttachmentServlet;
import pie.servlets.notes.GetAllDraftNoteServlet;
import pie.servlets.notes.GetAllSentNotesServlet;
import pie.servlets.notes.GetAllUserNoteServlet;
import pie.servlets.notes.GetNoteDetailsServlet;
import pie.servlets.notes.SaveNoteAsDraftServlet;
import pie.servlets.notes.SendDraftNoteServlet;
import pie.servlets.notes.SendNoteServlet;
import pie.servlets.notes.SetNoteIsArchivedServlet;
import pie.servlets.notes.SetNoteIsReadServlet;
import pie.servlets.notes.SetNoteResponseServlet;
import pie.servlets.notes.UpdateNoteDraftServlet;
import pie.servlets.parent.AddChildServlet;
import pie.servlets.parent.SetAsMainParentServlet;
import pie.servlets.parent.ViewParentChildrenServlet;
import pie.servlets.staff.GetAvailableRecipientsServlet;
import pie.servlets.staff.GetStaffReportServlet;
import pie.servlets.student.GetStudentReportServlet;
import pie.servlets.user.GetUserSecurityQuestionServlet;
import pie.servlets.user.GetUserTokenServlet;
import pie.servlets.user.LoginServlet;
import pie.servlets.user.RegisterParentServlet;
import pie.servlets.user.RegisterStaffServlet;
import pie.servlets.user.RegisterStudentServlet;
import pie.servlets.user.ResetPasswordServlet;
import pie.servlets.user.UpdatePasswordServlet;
import pie.servlets.user.UpdateUserAccountDetailsServlet;
import pie.servlets.user.VerifyUserServlet;

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
				serve("*/servlets/cities").with(GetCitiesServlet.class);
				serve("*/servlets/forgetpassword").with(GetUserSecurityQuestionServlet.class);
				serve("*/servlets/resetpassword").with(ResetPasswordServlet.class);
				serve("*/servlets/getsecurityquestions").with(GetSecurityQuestionsServlet.class);
				serve("*/servlets/getsubjects").with(GetSubjectsServlet.class);

				serve("*/servlets/secured/opengroups").with(ViewOpenGroupsServlet.class);
				serve("*/servlets/secured/updatepassword").with(UpdatePasswordServlet.class);
				serve("*/servlets/secured/updateaccountdetails").with(UpdateUserAccountDetailsServlet.class);
				serve("*/servlets/secured/authenticate").with(GetUserTokenServlet.class);

				serve("*/servlets/secured/student/leavegroup").with(StudentLeaveGroupServlet.class);
				serve("*/servlets/secured/student/joingroup").with(StudentJoinGroupServlet.class);
				serve("*/servlets/secured/student/joinedgroups").with(ViewStudentJoinedGroupsServlet.class);

				serve("*/servlets/secured/parent/children").with(ViewParentChildrenServlet.class);// tested
				serve("*/servlets/secured/parent/addchild").with(AddChildServlet.class);
				serve("*/servlets/secured/parent/relationships").with(GetRelationshipsServlet.class);// tested
				serve("*/servlets/secured/parent/setmainparent").with(SetAsMainParentServlet.class);

				serve("*/servlets/secured/staff/gencode").with(GenerateCodeServlet.class);// tested
				serve("*/servlets/secured/staff/registergroup").with(RegisterGroupServlet.class);
				serve("*/servlets/secured/staff/joingroup").with(StaffJoinGroupServlet.class);
				serve("*/servlets/secured/staff/groupdetails").with(GetGroupDetailsServlet.class);
				serve("*/servlets/secured/staff/staffreport").with(GetStaffReportServlet.class);
				serve("*/servlets/secured/staff/studentreport").with(GetStudentReportServlet.class);

				serve("*/servlets/secured/staff/group/member/groupmembers").with(ViewGroupMembersServlet.class);
				serve("*/servlets/secured/staff/group/admin/updategroup").with(UpdateGroupServlet.class);
				serve("*/servlets/secured/staff/group/owner/enliststudentsbatch")
						.with(EnlistStudentsByBatchServlet.class);
				serve("*/servlets/secured/staff/group/owner/enliststudent").with(EnlistStudentServlet.class);
				serve("*/servlets/secured/staff/group/owner/transfergroupownership")
						.with(TransferGroupOwnershipServlet.class);
				serve("*/servlets/secured/staff/group/owner/deactivategroup").with(DeactivateGroupServlet.class);
				serve("*/servlets/secured/staff/group/member/allrecipients").with(GetAvailableRecipientsServlet.class);// tested

				serve("*/servlets/secured/staff/group/sendnote").with(SendNoteServlet.class); // tested
				serve("*/servlets/secured/staff/group/savenoteasdraft").with(SaveNoteAsDraftServlet.class); // tested
				serve("*/servlets/secured/staff/group/alldraftnote").with(GetAllDraftNoteServlet.class); // tested
				serve("*/servlets/secured/staff/group/senddraftnote").with(SendDraftNoteServlet.class); // tested
				serve("*/servlets/secured/staff/group/deletenote").with(DeleteNoteServlet.class); // tested
				serve("*/servlets/secured/staff/group/notedetails").with(GetNoteDetailsServlet.class); // tested
				serve("*/servlets/secured/staff/group/allsentnotes").with(GetAllSentNotesServlet.class); // tested
				serve("*/servlets/secured/staff/group/updatedraftnote").with(UpdateNoteDraftServlet.class); // tested

				serve("*/servlets/secured/staff/group/downloadnoteattachment")
						.with(DownloadNoteAttachmentServlet.class); // tested
				serve("*/servlets/secured/staff/group/deletenoteattachment").with(DeleteNoteAttachmentServlet.class); // tested
				serve("*/servlets/secured/staff/group/noteisread").with(SetNoteIsReadServlet.class); // tested
				serve("*/servlets/secured/staff/group/noteisarchive").with(SetNoteIsArchivedServlet.class); // tested
				serve("*/servlets/secured/staff/group/setnotearchive").with(SetNoteIsArchivedServlet.class); // tested
				serve("*/servlets/secured/staff/group/sendnoteresponse").with(SetNoteResponseServlet.class); // tested
				serve("*/servlets/secured/staff/group/allreceivednote").with(GetAllUserNoteServlet.class);// tested

				serve("*/servlets/secured/staff/group/createhomework").with(PublishHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/createdrafthomework").with(PublishDraftHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/savehomeworkasdraft").with(SaveHomeworkAsDraftServlet.class);
				serve("*/servlets/secured/staff/group/updatehomeworkdraft").with(UpdateDraftHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/allhomeworkdraft").with(GetAllDraftHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/allcreatedhomework").with(GetAllPublishedHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/deletehomework").with(DeleteHomeworkServlet.class);
				serve("*/servlets/secured/staff/group/homeworkdetails").with(GetHomeworkDetailsServlet.class);


				serve("*/servlets/secured/staff/group/sendpublishedhomework").with(SendPublishedHomeworkServlet.class);// tested
				serve("*/servlets/secured/staff/group/senddraftpublishedhomework").with(SendDraftPublishedHomeworkServlet.class);// tested
				serve("*/servlets/secured/staff/group/savepublishedhomeworkasdraft").with(SavePublishedHomeworkAsDraftServlet.class);// tested
				serve("*/servlets/secured/staff/group/updatedraftpublishedhomework").with(UpdateDraftPublishedHomeworkServlet.class);// tested
				serve("*/servlets/secured/staff/group/allsenthomework").with(GetAllSentHomeworkServlet.class);// tested
				serve("*/servlets/secured/staff/group/alldraftpublishedhomework").with(GetAllDraftPublishedHomeworkServlet.class);// tested
				serve("*/servlets/secured/staff/group/publishedhomeworkdetails").with(GetGroupHomeworkDetailsServlet.class);// tested
				serve("*/servlets/secured/staff/group/deletegrouphomework").with(DeleteGroupHomeworkServlet.class);// tested

				serve("*/servlets/secured/staff/group/homeworkrecipients").with(GetHomeworkRecipientsServlet.class);// tested
				serve("*/servlets/secured/staff/group/markhomework").with(SetMarkHomeworkServlet.class); // tested
				serve("*/servlets/secured/staff/group/gradehomework").with(GradeHomeworkServlet.class);// tested
				serve("*/servlets/secured/staff/group/submithomework").with(SetSubmitHomeworkServlet.class);// tested
				serve("*/servlets/secured/deletehomework").with(DeleteUserHomeworkServlet.class); // tested
				serve("*/servlets/secured/archivehomework").with(SetArchiveHomeworkServlet.class);// tested
				serve("*/servlets/secured/readhomework").with(SetReadHomeworkServlet.class);// tested
				serve("*/servlets/secured/userhomeworkdetails").with(GetUserHomeworkDetailsServlet.class);

				serve("*/servlets/secured/allreceivedhomework").with(GetAllUserHomeworkServlet.class);//tested

				serve("*/servlets/secured/staff/group/downloadhomeworkattachment")
						.with(DownloadHomeworkAttachmentServlet.class);
				serve("*/servlets/secured/staff/group/deletehomeworkattachment")
						.with(DeleteHomeworkAttachmentServlet.class);
				serve("*/servlets/secured/homeworkreport").with(GetIndividualStudentReport.class);

			}
		});
	}

}
