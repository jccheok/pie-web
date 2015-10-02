package pie.services;

import java.io.ObjectInputStream.GetField;
import java.util.Properties;
import java.util.Date;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.MessagingException;

public class EmailService {

	public final static String emailServer = "mailgun@sandboxbb961a7a116140dd8e38a5e3a0b3a1b4.mailgun.org";
	public final static String emailID = "postmaster@sandboxbb961a7a116140dd8e38a5e3a0b3a1b4.mailgun.org";
	public final static String emailPass = "dff0e4770af27c2447dd1f4b4c3c2d23";
	public final static String emailName = "PIE - Partners In Education <mailgun@sandboxbb961a7a116140dd8e38a5e3a0b3a1b4.mailgun.org>";
	public final static String railServer = "smtp.mailgun.org";
	public static String verificationLink = "http://piedev-rpmaps.rhcloud.com/verifyUserServlet?uid=";

	public String getVerificationEmailAddress(int userID) {

		UserService userService = new UserService();
		String userEmail = userService.getUser(userID).getUserEmail();

		return userEmail;
	}

	public String[] getVerificationEmailContents(int userID) {
		String[] emailContents = new String[2];

		verificationLink += userID;
		String emailSubject = "Activate your SG-PIE Account";
		String emailContent = "<table class=\"body\" style=\"box-sizing:border-box;border-collapse:separate !important;border-spacing:0;width:100%;background:#f6f6f6;\" width=\"100%\"><tbody><tr><td style=\"box-sizing:border-box;vertical-align:top;\" valign=\"top\"></td><td class=\"ecxcontainer\" style=\"box-sizing:border-box;vertical-align:top;display:block;max-width:580px;width:580px;padding:10px;\" valign=\"top\"><div class=\"ecxcontent\" style=\"box-sizing:border-box;display:block;max-width:580px;padding:10px;\"><table class=\"ecxmain\" style=\"box-sizing:border-box;border-collapse:separate !important;border-radius:3px;border-spacing:0;width:100%;background:#fff;border:1px solid #e9e9e9;\" width=\"100%\"><tbody><tr><td class=\"ecxwrapper\" style=\"box-sizing:border-box;vertical-align:top;padding:30px;\" valign=\"top\"><table style=\"box-sizing:border-box;border-collapse:separate !important;\" width=\"100%\"><tbody><tr><td style=\"box-sizing:border-box;vertical-align:top;\" valign=\"top\"><p style=\"font-family:\'Helvetica Neue\', Helvetica, Arial, \'Lucida Grande\', sans-serif;font-size:14px;font-weight:normal;padding:0;\">Thank you for signing up to use PIE (Partner In Education).<br>Please click the button below to activate your account.</p><table class=\"ecxbtn ecxbtn-primary\" style=\"box-sizing:border-box;border-collapse:separate !important;width:auto;\" width=\"auto\"><tbody><tr><center><td style=\"box-sizing:border-box;vertical-align:top;border-radius:5px;text-align:center;background:#4AC6A7;\" align=\"center\" valign=\"top\"><a href=\""
				+ verificationLink
				+ "\" style=\"box-sizing:border-box;border-radius:5px;color:#fff;text-decoration:none;cursor:pointer;display:inline-block;font-size:14px;font-weight:bold;text-transform:capitalize;background:#4AC6A7;padding:12px 25px;border:1px solid #4AC6A7;\" target=\"_blank\">Activate PIE account</a></td></tr></tbody></table></center><br><p style=\"font-family:\'Helvetica Neue\', Helvetica, Arial, \'Lucida Grande\', sans-serif;font-size:14px;font-weight:normal;padding:0;\">Please do not reply to this mail as it is an <b>auto-generated</b> email and cannot be responded to.</p></td></tr></tbody></table></td></tr></tbody></table></div></td><td style=\"box-sizing:border-box;vertical-align:top;\" valign=\"top\"></td></tr></tbody></table>";

		emailContents[0] = emailSubject;
		emailContents[1] = emailContent;

		return emailContents;
	}
	
	public Message prepareEmailMessage(String[] emailContents, String userEmail, Session session) {

		Message msg = null;
		try{
			
			msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(emailName));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
			msg.setSubject(emailContents[0]);
			msg.setContent(emailContents[1], "text/html; charset=utf-8");
			msg.setSentDate(new Date());
			
		}catch(MessagingException mex){
			
			mex.printStackTrace();

		}
		return msg;

	}
	
	public Session startSession(Properties props){
		Session session = null;
		try{
			
			session = Session.getDefaultInstance(props);
			
		}catch(Exception e){
			System.out.println(e);
		}
		
		return session;
	}
	
	public Properties getProperties(){
		Properties properties = null;
		try{
			
			properties = System.getProperties();
			properties.put("mail.smtps.host", railServer);
			properties.put("mail.smtps.auth", "true");
			properties.setProperty("mail.user", emailServer);
			properties.setProperty("mail.password", emailPass);
			
		}catch(Exception e){
			System.out.println(e);
		}
		return properties;
	}

	public boolean sendVerificationEmail(int userID) {

		boolean isSent = false;
		
		String userEmail = null;
		String[] emailContents = {};
		Message emailMessage = null;
		Session session = null;
		
		try {
			
			userEmail = getVerificationEmailAddress(userID);
			emailContents = getVerificationEmailContents(userID);
			session = startSession(getProperties());
			
			emailMessage = prepareEmailMessage(emailContents, userEmail, session);
			
			SMTPTransport smtp = (SMTPTransport) session.getTransport("smtps");
			smtp.connect(railServer, emailID, emailPass);
			smtp.sendMessage(emailMessage, emailMessage.getAllRecipients());
			smtp.close();

			isSent = true;

		} catch (Exception e) {
			System.out.println(e);
		}
		
		return isSent;
	}

}
