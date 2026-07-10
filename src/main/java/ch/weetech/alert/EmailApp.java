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
package ch.weetech.alert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
 * 
 * Utility application class responsible for handling email transmission operations.
 * Provides capabilities to configure mail sessions, authenticate with SMTP servers, 
 * and assemble multipart email messages.
 * 
 * https://developers.google.com/apps-script/reference/mail
 *
 * https://www.tutorialspoint.com/java/java_sending_email.htm
 * https://mkyong.com/java/java-how-to-send-email/
 * https://www.javatpoint.com/example-of-sending-email-using-java-mail-api
 *
 * @author jason
 *
 */
@SuppressWarnings("doclint:missing")
public class EmailApp {

	/**
     * Sends a text-based email message with optional CC, BCC, and file attachments 
     * using the provided SMTP server configuration.
     * 
     * <p>This method initializes server connections, maps custom email abstractions 
     * into standard internet addresses, handles optional TLS authentication, 
     * builds the appropriate mime-type message tree, and executes transmission over 
     * the network transport layers.</p>
     * 
     * @param from the sender's email configuration containing address and display name
     * @param recipients the primary (TO) recipients list; must not be null or empty
     * @param cc the optional carbon copy (CC) recipients list; pass null if none
     * @param bcc the optional blind carbon copy (BCC) recipients list; pass null if none
     * @param subject the main subject header text of the email
     * @param body the structured message body wrapper containing text and content-type information
     * @param attachments the optional file attachments collection; pass null if none
     * @param smtp the active SMTP configurations containing host, port, credentials, and TLS settings
     * @param debug boolean flag to toggle system level verbose logging out to standard display streams
     * @return true if compilation succeeds and message is routed safely to transport pipeline layers
     * @throws AddressException if email address compilation, formatting parsing, or layout failure occurs
     * @throws MessagingException if server authentication blocks, connection drops, or transmission breaks
     * @throws UnsupportedEncodingException if character maps fail during personalization assembly steps
     */
    public static boolean sendText(EmailAddress from, List<EmailAddress> recipients, List<EmailAddress> cc, List<EmailAddress> bcc, String subject, EmailBody body,
            List<EmailAttachment> attachments, SMTP smtp, boolean debug) throws AddressException, MessagingException, UnsupportedEncodingException {
        assert smtp != null;
        assert recipients != null;
        assert recipients.size() > 0;

        Properties prop = System.getProperties();
        prop.put(SMTP.MAIL_SMTP_HOST, smtp.getSmtpHost());
        prop.put(SMTP.MAIL_SMTP_PORT, smtp.getSmtpPort());

        if (debug)
            prop.put("mail.debug", "true");

        Session session = null;
        if (smtp.isSmtpAuth()) {
            prop.put(SMTP.MAIL_SMTP_AUTH, smtp.isSmtpAuth());
            prop.put(SMTP.MAIL_SMTP_STARTTLS_ENABLE, smtp.isSmtpStartTlsEnable());

            //session = Session.getDefaultInstance(prop, auth);
            session = Session.getDefaultInstance(prop, new Authenticator() {
                 public PasswordAuthentication getPasswordAuthentication() {
                     String username = smtp.getSmtpUsername();
                     String password = smtp.getSmtpPassword();
                     return new PasswordAuthentication(username, password);
                  }
            });
        } else {
            session = Session.getInstance(prop, null);
        }

        Message msg = new MimeMessage(session);

        if (from.getName() != null) {
            msg.setFrom(new InternetAddress(from.getAddress(), from.getName(), StandardCharsets.UTF_8.name()));
        } else {
            msg.setFrom(new InternetAddress(from.getAddress()));
        }

        msg.addRecipients(Message.RecipientType.TO,
                recipients.stream().map(r -> {
                    try {
                        if (r.getName() != null) {
                            return new InternetAddress(r.getAddress(), r.getName(), StandardCharsets.UTF_8.name());
                        } else {
                            return new InternetAddress(r.getAddress());
                        }

                    } catch (AddressException | UnsupportedEncodingException e) { }
                    return null;
                }).toArray(InternetAddress[]::new)
        );

        if (cc != null) {
            msg.addRecipients(Message.RecipientType.CC,
                    cc.stream().map(ccc -> {
                        try {
                            if (ccc.getName() != null) {
                                return new InternetAddress(ccc.getAddress(), ccc.getName(), StandardCharsets.UTF_8.name());
                            } else {
                                return new InternetAddress(ccc.getAddress());
                            }

                        } catch (AddressException | UnsupportedEncodingException e) { }
                        return null;
                    })
                    .toArray(InternetAddress[]::new)
            );
        }

        if (bcc != null) {
            msg.addRecipients(Message.RecipientType.BCC,
                    bcc.stream().map(bccc -> {
                        try {
                            if (bccc.getName() != null) {
                                return new InternetAddress(bccc.getAddress(), bccc.getName(), StandardCharsets.UTF_8.name());
                            } else {
                                return new InternetAddress(bccc.getAddress());
                            }

                        } catch (AddressException | UnsupportedEncodingException e) { }
                        return null;
                    })
                    .toArray(InternetAddress[]::new)
            );
        }


        if (attachments != null) {
            MimeMultipart altAndAtt = new MimeMultipart(Email.Type.mixed.toString());

            MimeBodyPart plain = new MimeBodyPart();
            plain.setContent(body.getContent(), body.getContent());
            altAndAtt.addBodyPart(plain);

            for (EmailAttachment ea : attachments) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                try {
                    if ("text/plain".equals(ea.getContentType())) {
                        messageBodyPart.setText(ea.getContent());
                        messageBodyPart.setFileName(ea.getName());
                    } else {
                        messageBodyPart.attachFile(ea.getFile());
                        messageBodyPart.setFileName(ea.getName());
                    }
                } catch (IOException e) {

                }
                altAndAtt.addBodyPart(messageBodyPart);
            }
            msg.setContent(altAndAtt);
        } else {
            msg.setSubject(subject);
            msg.setText(body.getContent());
        }

        // send
        try (Transport t = session.getTransport()) {
            t.connect();
            t.sendMessage(msg, msg.getAllRecipients());
        }

        return true;
    }
    
    /**
     * Sends an HTML-formatted email message with optional CC, BCC, and file attachments 
     * using the specified SMTP server configuration.
     * 
     * <p>This method initializes server properties, sets up optional authenticated 
     * SMTP sessions, converts custom address domains into internet addresses, sets 
     * UTF-8 subject layouts, and builds either structured multipart-alternative or 
     * multipart-mixed MIME message trees before transmission.</p>
     * 
     * @param from the sender's email configuration containing address and display name
     * @param recipients the primary (TO) recipients list; must not be null or empty
     * @param cc the optional carbon copy (CC) recipients list; pass null if none
     * @param bcc the optional blind carbon copy (BCC) recipients list; pass null if none
     * @param subject the main subject header text of the email
     * @param msgHtml the structured message body wrapper containing HTML markup and content-type tags
     * @param attachments the optional file attachments collection; pass null if none
     * @param smtp the active SMTP configurations containing host, port, credentials, and TLS settings
     * @param debug boolean flag to toggle system level verbose logging out to standard display streams
     * @return true if compilation succeeds and message is routed safely to transport pipeline layers
     * @throws AddressException if email address compilation, formatting parsing, or layout failure occurs
     * @throws MessagingException if server authentication blocks, connection drops, or transmission breaks
     * @throws UnsupportedEncodingException if character maps fail during personalization assembly steps
     */
    public static boolean sendHtml(EmailAddress from, List<EmailAddress> recipients, List<EmailAddress> cc, List<EmailAddress> bcc, String subject, EmailBody msgHtml,
            List<EmailAttachment> attachments, SMTP smtp, boolean debug) throws AddressException, MessagingException, UnsupportedEncodingException {
        assert smtp != null;
        assert recipients != null;
        assert recipients.size() > 0;

        Properties prop = System.getProperties();
        prop.put(SMTP.MAIL_SMTP_HOST, smtp.getSmtpHost());
        prop.put(SMTP.MAIL_SMTP_PORT, smtp.getSmtpPort());

        if (debug)
            prop.put("mail.debug", "true");

        Session session = null;
        if (smtp.isSmtpAuth()) {
            prop.put(SMTP.MAIL_SMTP_AUTH, smtp.isSmtpAuth());
            prop.put(SMTP.MAIL_SMTP_STARTTLS_ENABLE, smtp.isSmtpStartTlsEnable());

            //session = Session.getDefaultInstance(prop, auth);
            session = Session.getDefaultInstance(prop, new Authenticator() {
                 public PasswordAuthentication getPasswordAuthentication() {
                     String username = smtp.getSmtpUsername();
                     String password = smtp.getSmtpPassword();
                     return new PasswordAuthentication(username, password);
                  }
            });
        } else {
            session = Session.getInstance(prop, null);
        }

        MimeMessage msg = new MimeMessage(session);

        if (from.getName() != null) {
            msg.setFrom(new InternetAddress(from.getAddress(), from.getName(), StandardCharsets.UTF_8.name()));
        } else {
            msg.setFrom(new InternetAddress(from.getAddress()));
        }

        msg.addRecipients(Message.RecipientType.TO,
                recipients.stream().map(r -> {
                    try {
                        if (r.getName() != null) {
                            return new InternetAddress(r.getAddress(), r.getName(), StandardCharsets.UTF_8.name());
                        } else {
                            return new InternetAddress(r.getAddress());
                        }

                    } catch (AddressException | UnsupportedEncodingException e) { }
                    return null;
                }).toArray(InternetAddress[]::new)
        );

        if (cc != null) {
            msg.addRecipients(Message.RecipientType.CC,
                    cc.stream().map(ccc -> {
                        try {
                            if (ccc.getName() != null) {
                                return new InternetAddress(ccc.getAddress(), ccc.getName(), StandardCharsets.UTF_8.name());
                            } else {
                                return new InternetAddress(ccc.getAddress());
                            }

                        } catch (AddressException | UnsupportedEncodingException e) { }
                        return null;
                    })
                    .toArray(InternetAddress[]::new)
            );
        }

        if (bcc != null) {
            msg.addRecipients(Message.RecipientType.BCC,
                    bcc.stream().map(bccc -> {
                        try {
                            if (bccc.getName() != null) {
                                return new InternetAddress(bccc.getAddress(), bccc.getName(), StandardCharsets.UTF_8.name());
                            } else {
                                return new InternetAddress(bccc.getAddress());
                            }

                        } catch (AddressException | UnsupportedEncodingException e) { }
                        return null;
                    })
                    .toArray(InternetAddress[]::new)
            );
        }

        msg.setSubject(subject, "UTF-8");

        if (attachments != null) {
            MimeMultipart altAndAtt = new MimeMultipart(Email.Type.mixed.toString());

            MimeBodyPart html = new MimeBodyPart();
            html.setContent(msgHtml.getContent(), msgHtml.getContentType());
            altAndAtt.addBodyPart(html);

            for (EmailAttachment ea : attachments) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                try {
                    if ("text/plain".equals(ea.getContentType())) {
                        messageBodyPart.setText(ea.getContent());
                        messageBodyPart.setFileName(ea.getName());
                    } else {
                        messageBodyPart.attachFile(ea.getFile());
                        messageBodyPart.setFileName(ea.getName());
                    }
                } catch (IOException e) {

                }
                altAndAtt.addBodyPart(messageBodyPart);
            }
            msg.setContent(altAndAtt);
        } else {
            MimeMultipart content = new MimeMultipart("alternative");
            MimeBodyPart html = new MimeBodyPart();

            html.setContent(msgHtml.getContent(), msgHtml.getContentType());
            html.setHeader("Content-Transfer-Encoding", "8bit");
            content.addBodyPart(html);

            msg.setContent(content, "UTF-8");
        }

        // send
        try (Transport t = session.getTransport()) {
            t.connect();
            t.sendMessage(msg, msg.getAllRecipients());
        }
        return true;
    }

    /**
     * aka alternative
     * 
     * Sends a multi-format email containing both plain text and HTML alternatives, 
     * along with optional CC, BCC, and file attachments using the specified SMTP server configuration.
     * 
     * <p>This method initializes server properties, sets up optional authenticated 
     * SMTP sessions, converts custom address domains into internet addresses, sets 
     * UTF-8 subject layouts, and builds either structured multipart-alternative or 
     * nested multipart-mixed (containing alternative text variants) MIME message trees 
     * before transmission.</p>
     * 
     * @param from the sender's email configuration containing address and display name
     * @param recipients the primary (TO) recipients list; must not be null or empty
     * @param cc the optional carbon copy (CC) recipients list; pass null if none
     * @param bcc the optional blind carbon copy (BCC) recipients list; pass null if none
     * @param subject the main subject header text of the email
     * @param msgText the structured message body wrapper containing the plain text version of the message
     * @param msgHtml the structured message body wrapper containing HTML markup and content-type tags
     * @param attachments the optional file attachments collection; pass null if none
     * @param smtp the active SMTP configurations containing host, port, credentials, and TLS settings
     * @param debug boolean flag to toggle system level verbose logging out to standard display streams
     * @return true if compilation succeeds and message is routed safely to transport pipeline layers
     * @throws AddressException if email address compilation, formatting parsing, or layout failure occurs
     * @throws MessagingException if server authentication blocks, connection drops, or transmission breaks
     * @throws UnsupportedEncodingException if character maps fail during personalization assembly steps
     */
    public static boolean sendTextAndHtml(EmailAddress from, List<EmailAddress> recipients, List<EmailAddress> cc, List<EmailAddress> bcc, String subject, EmailBody msgText, EmailBody msgHtml,
            List<EmailAttachment> attachments, SMTP smtp, boolean debug) throws AddressException, MessagingException, UnsupportedEncodingException {
        assert smtp != null;
        assert recipients != null;
        assert recipients.size() > 0;

        Properties prop = System.getProperties();
        prop.put(SMTP.MAIL_SMTP_HOST, smtp.getSmtpHost());
        prop.put(SMTP.MAIL_SMTP_PORT, smtp.getSmtpPort());

        if (debug)
            prop.put("mail.debug", "true");

        Session session = null;
        if (smtp.isSmtpAuth()) {
            prop.put(SMTP.MAIL_SMTP_AUTH, smtp.isSmtpAuth());
            prop.put(SMTP.MAIL_SMTP_STARTTLS_ENABLE, smtp.isSmtpStartTlsEnable());

            //session = Session.getDefaultInstance(prop, auth);
            session = Session.getDefaultInstance(prop, new Authenticator() {
                 public PasswordAuthentication getPasswordAuthentication() {
                     String username = smtp.getSmtpUsername();
                     String password = smtp.getSmtpPassword();
                     return new PasswordAuthentication(username, password);
                  }
            });
        } else {
            session = Session.getInstance(prop, null);
        }

        MimeMessage msg = new MimeMessage(session);

        if (from.getName() != null) {
            msg.setFrom(new InternetAddress(from.getAddress(), from.getName(), StandardCharsets.UTF_8.name()));
        } else {
            msg.setFrom(new InternetAddress(from.getAddress()));
        }

        msg.addRecipients(Message.RecipientType.TO,
                recipients.stream().map(r -> {
                    try {
                        if (r.getName() != null) {
                            return new InternetAddress(r.getAddress(), r.getName(), StandardCharsets.UTF_8.name());
                        } else {
                            return new InternetAddress(r.getAddress());
                        }

                    } catch (AddressException | UnsupportedEncodingException e) { }
                    return null;
                }).toArray(InternetAddress[]::new)
        );

        if (cc != null) {
            msg.addRecipients(Message.RecipientType.CC,
                    cc.stream().map(ccc -> {
                        try {
                            if (ccc.getName() != null) {
                                return new InternetAddress(ccc.getAddress(), ccc.getName(), StandardCharsets.UTF_8.name());
                            } else {
                                return new InternetAddress(ccc.getAddress());
                            }

                        } catch (AddressException | UnsupportedEncodingException e) { }
                        return null;
                    })
                    .toArray(InternetAddress[]::new)
            );
        }

        if (bcc != null) {
            msg.addRecipients(Message.RecipientType.BCC,
                    bcc.stream().map(bccc -> {
                        try {
                            if (bccc.getName() != null) {
                                return new InternetAddress(bccc.getAddress(), bccc.getName(), StandardCharsets.UTF_8.name());
                            } else {
                                return new InternetAddress(bccc.getAddress());
                            }

                        } catch (AddressException | UnsupportedEncodingException e) { }
                        return null;
                    })
                    .toArray(InternetAddress[]::new)
            );
        }

        msg.setSubject(subject, "UTF-8");

        if (attachments != null) {
            MimeMultipart altAndAtt = new MimeMultipart(Email.Type.mixed.toString());

            MimeMultipart plainAndHtml = new MimeMultipart( "alternative" );

            MimeBodyPart plain = new MimeBodyPart();
            plain.setContent(msgText, "text/plain; charset=utf-8" );
            plainAndHtml.addBodyPart(plain);

            MimeBodyPart html = new MimeBodyPart();
            html.setContent(msgHtml, "text/html; charset=utf-8" );
            plainAndHtml.addBodyPart(html);

            MimeBodyPart wrapper = new MimeBodyPart();
            wrapper.setContent(plainAndHtml);
            altAndAtt.addBodyPart(wrapper);

            for (EmailAttachment ea : attachments) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                try {
                    if ("text/plain".equals(ea.getContentType())) {
                        messageBodyPart.setText(ea.getContent());
                        messageBodyPart.setFileName(ea.getName());
                    } else {
                        messageBodyPart.attachFile(ea.getFile());
                        messageBodyPart.setFileName(ea.getName());
                    }
                } catch (IOException e) {

                }
                altAndAtt.addBodyPart(messageBodyPart);
            }
            msg.setContent(altAndAtt);
        } else {
            MimeMultipart content = new MimeMultipart("alternative");
            MimeBodyPart html = new MimeBodyPart();

            html.setContent(msgHtml.getContent(), msgHtml.getContentType());
            html.setHeader("Content-Transfer-Encoding", "8bit");
            content.addBodyPart(html);

            msg.setContent(content, "UTF-8");
        }

        // send
        try (Transport t = session.getTransport()) {
            t.connect();
            t.sendMessage(msg, msg.getAllRecipients());
        }
        return true;
    }

}
