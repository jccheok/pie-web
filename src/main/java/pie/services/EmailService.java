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

	public final static String EMAIL_SERVER = "mailgun@sandboxbb961a7a116140dd8e38a5e3a0b3a1b4.mailgun.org";
	public final static String EMAIL_ID = "postmaster@sandboxbb961a7a116140dd8e38a5e3a0b3a1b4.mailgun.org";
	public final static String EMAIL_PASS = "dff0e4770af27c2447dd1f4b4c3c2d23";
	public final static String EMAIL_NAME = "PIE - Partners In Education <mailgun@sandboxbb961a7a116140dd8e38a5e3a0b3a1b4.mailgun.org>";
	public final static String RAIL_SERVER = "smtp.mailgun.org";

	public boolean sendEmail(String emailSubject, String emailContent, String[] userEmails) {
		
		boolean sendResult = false;
		
		Session session = getEmailServerSession();
		
		try {
			
			Message emailMessage = prepareEmailMessage(emailSubject, emailContent, userEmails, session);
			SMTPTransport smtp = (SMTPTransport) session.getTransport("smtps");
			smtp.connect(RAIL_SERVER, EMAIL_ID, EMAIL_PASS);
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
		properties.put("mail.smtps.host", RAIL_SERVER);
		properties.put("mail.smtps.auth", "true");
		properties.setProperty("mail.user", EMAIL_SERVER);
		properties.setProperty("mail.password", EMAIL_PASS);
		
		return Session.getDefaultInstance(properties);
	}
	
	private Message prepareEmailMessage(String emailSubject, String emailContent, String[] emailRecipients, Session session) throws AddressException, MessagingException {
		
		Message emailMessage = new MimeMessage(session);
		emailMessage.setFrom(new InternetAddress(EMAIL_NAME));
		for (String email : emailRecipients) {
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		}
		emailMessage.setSubject(emailSubject);
		emailMessage.setContent(emailContent, "text/html; charset=utf-8");
		emailMessage.setSentDate(new Date());
		
		return emailMessage;
	}
}
