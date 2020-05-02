package co.weetech.alert;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class EmailAppTest {


	@Disabled("write when powermockito support junit5")
	@Test
    public void sendTextViaSMTP() {
		
		String from = "noreply@foo.com";
		List<String> recipients = List.of("jason@bar.com");
		List<String> cc = null;
		String subject = "email subject";
		String body = "Hello, this is a test.";
		List<Object> attachments = null;
		Map<String, String> smtp = new HashMap<String, String>();
		smtp.put("mail.smtp.auth", "true");
		smtp.put("mail.smtp.port", "587");
		smtp.put("mail.smtp.host", "mymail.smtp.com");
		smtp.put("mail.smtp.username", "jason@foo.com");
		smtp.put("mail.smtp.password", "");
		
		try {			
			//when(EmailApp.sendText(from, recipients, cc, subject, body, attachments, smtp, true)).thenReturn(true);
			
			assertTrue(EmailApp.sendText(from, recipients, cc, subject, body, attachments, smtp, true));
		} catch (MessagingException e) {
			e.printStackTrace();
			fail("must not fail");
		}
    }
	
	@Disabled("write when powermockito support junit5")
	@Test
    public void sendTextViaLocalhost() {
		String from = "noreply@foo.com";
		List<String> recipients = List.of("jason@bar.com");
		List<String> cc = null;
		String subject = "email subject";
		String body = "Hello, this is a test.";
		Map<String, String> smtp = new HashMap<String, String>();		
		
		try {
			assertTrue(EmailApp.sendText(from, recipients, cc, subject, body, null, smtp, true));
		} catch (MessagingException e) {
			e.printStackTrace();
			fail("must not fail");
		}
    }
	
	@Disabled("write when powermockito support junit5")
	@Test
    public void sendHTML() {
		String from = "noreply@foo.com";
		List<String> recipients = List.of("jason@bar.com");
		List<String> cc = null;
		String subject = "email  好";
		String body = "<p>Hello, this is a test.</p>";
		Map<String, String> smtp = new HashMap<String, String>();
		smtp.put("mail.smtp.auth", "true");
		smtp.put("mail.smtp.port", "587");
		smtp.put("mail.smtp.host", "mymail.smtp.com");
		smtp.put("mail.smtp.username", "jason@foo.com");
		smtp.put("mail.smtp.password", "");
		
		try {
			assertTrue(EmailApp.sendHtml(from, recipients, cc, subject, body, null, smtp, true));
		} catch (MessagingException e) {
			e.printStackTrace();
			fail("must not fail");
		}

    }

}
