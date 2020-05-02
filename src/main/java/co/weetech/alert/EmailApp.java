/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.weetech.alert;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



/**
 * https://developers.google.com/apps-script/reference/mail
 * 
 * https://www.tutorialspoint.com/java/java_sending_email.htm
 * https://mkyong.com/java/java-how-to-send-email/
 * https://www.javatpoint.com/example-of-sending-email-using-java-mail-api
 * 
 * @author jason
 *
 */
public class EmailApp {

	// support bcc?
	// support sender name?
	public static boolean sendText(String from, List<String> recipients, List<String> cc, String subject, String body, 
			List<Object> attachments, Map<String, String> smtp, boolean debug) throws AddressException, MessagingException {
        Properties prop = System.getProperties();

        prop.put("mail.smtp.host", "localhost");
        prop.put("mail.smtp.port", "25");
        
        if (smtp != null) {
        	prop.put("mail.smtp.host", smtp.getOrDefault("mail.smtp.host", "localhost"));
            prop.put("mail.smtp.port", smtp.getOrDefault("mail.smtp.port", "25"));
        }
        
        if (debug)
        	prop.put("mail.debug", "true");
        
        Session session = null;
        if (smtp != null && smtp.get("mail.smtp.auth") != null) {
        	prop.put("mail.smtp.auth", smtp.get("mail.smtp.auth"));
        	prop.put("mail.smtp.starttls.enable", smtp.getOrDefault("mail.smtp.starttls.enable", "false"));
        	
            //session = Session.getDefaultInstance(prop, auth);
            session = Session.getDefaultInstance(prop, new Authenticator() {
            	 public PasswordAuthentication getPasswordAuthentication() {
                     String username = smtp.getOrDefault("mail.smtp.username", "");
                     String password = smtp.getOrDefault("mail.smtp.password", "");
                     return new PasswordAuthentication(username, password);
                  }
			});
        } else {
        	session = Session.getInstance(prop, null);
        }

        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(from));
        msg.addRecipients(Message.RecipientType.TO, recipients.stream()
        		.map(s -> { try { return new InternetAddress(s); } catch (AddressException e) { } return null; })
        		.toArray(InternetAddress[]::new)  
        );
        if (cc != null)
        	msg.addRecipients(Message.RecipientType.CC, cc.stream()
        			.map(s -> { try { return new InternetAddress(s); } catch (AddressException e) { } return null; })
        			.toArray(InternetAddress[]::new)
        	);

        msg.setSubject(subject);
        msg.setText(body);
		
		// send
        try (Transport t = session.getTransport()) {
        	t.connect();
            t.sendMessage(msg, msg.getAllRecipients());
        }

        return true;
	}

	public static boolean sendHtml(String from, List<String> recipients, List<String> cc, String subject, String msgHtml, 
			List<Object> attachments, Map<String, String> smtp, boolean debug) throws AddressException, MessagingException {
        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", "localhost");
        prop.put("mail.smtp.port", "25");
        
        if (smtp != null) {
        	prop.put("mail.smtp.host", smtp.getOrDefault("mail.smtp.host", "localhost"));
            prop.put("mail.smtp.port", smtp.getOrDefault("mail.smtp.port", "25"));
        }
        
        if (debug)
        	prop.put("mail.debug", "true");
        
        Session session = null;
        if (smtp != null && smtp.get("mail.smtp.auth") != null) {
        	prop.put("mail.smtp.auth", smtp.get("mail.smtp.auth"));
        	prop.put("mail.smtp.starttls.enable", smtp.getOrDefault("mail.smtp.starttls.enable", "false"));
        	
            //session = Session.getDefaultInstance(prop, auth);
            session = Session.getDefaultInstance(prop, new Authenticator() {
            	 public PasswordAuthentication getPasswordAuthentication() {
                     String username = smtp.getOrDefault("mail.smtp.username", "");
                     String password = smtp.getOrDefault("mail.smtp.password", "");
                     return new PasswordAuthentication(username, password);
                  }
			});
        } else {
        	session = Session.getInstance(prop, null);
        }

        MimeMessage msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(from));
        
        msg.setFrom(new InternetAddress(from));
        msg.addRecipients(Message.RecipientType.TO, recipients.stream()
        		.map(s -> { try { return new InternetAddress(s); } catch (AddressException e) { } return null; })
        		.toArray(InternetAddress[]::new)  
        );
        if (cc != null)
        	msg.addRecipients(Message.RecipientType.CC, cc.stream()
        			.map(s -> { try { return new InternetAddress(s); } catch (AddressException e) { } return null; })
        			.toArray(InternetAddress[]::new)
        	);
        
        msg.setSubject(subject, "UTF-8");

        MimeMultipart content = new MimeMultipart("alternative");
        MimeBodyPart html = new MimeBodyPart();

        html.setContent(msgHtml, "text/html; charset=\"UTF-8\"");
        html.setHeader("Content-Transfer-Encoding", "8bit");
        content.addBodyPart(html);

        msg.setContent(content, "UTF-8");

		// send
        try (Transport t = session.getTransport()) {
        	t.connect();
            t.sendMessage(msg, msg.getAllRecipients());
        }
        return true;
	}

}
