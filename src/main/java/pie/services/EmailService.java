package pie.services;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

public class EmailService {

	public final static String MAILGUN_USERNAME = "postmaster@sandbox9a83076ecf2f445192f5d74f7f3311e3.mailgun.org";
	public final static String MAILGUN_PASS = "acd6f3762e3032b01de541a963f2ffc9";
	public final static String MAILGUN_SERVER = "smtp.mailgun.org";

	public boolean sendEmail(String emailSubject, String emailContent, String[] userEmails) {
		
		boolean sendResult = false;
		
		Session session = getEmailServerSession();
		
		try {
			
			Message emailMessage = prepareEmailMessage(emailSubject, emailContent, userEmails, session);
			SMTPTransport smtp = (SMTPTransport) session.getTransport("smtps");
			smtp.connect(MAILGUN_SERVER, MAILGUN_USERNAME, MAILGUN_PASS);
			smtp.sendMessage(emailMessage, emailMessage.getAllRecipients());
			smtp.close();

			sendResult = true;
			
		} catch (Exception e) {
			
			System.out.println(e);
		}
		
		return sendResult;
	}
	
	private Session getEmailServerSession() {
		
		Properties properties = null;
		properties = System.getProperties();
		properties.put("mail.smtps.host", MAILGUN_SERVER);
		properties.put("mail.smtps.auth", "true");
		
		return Session.getInstance(properties, null);
	}
	
	private Message prepareEmailMessage(String emailSubject, String emailContent, String[] emailRecipients, Session session) throws AddressException, MessagingException {
		
		Message emailMessage = new MimeMessage(session);
		emailMessage.setFrom(new InternetAddress("PIE - Partners In Education"));
		for (String email : emailRecipients) {
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		}
		emailMessage.setSubject(emailSubject);
		emailMessage.setContent(emailContent, "text/html; charset=utf-8");
		emailMessage.setSentDate(new Date());
		
		return emailMessage;
	}
}
