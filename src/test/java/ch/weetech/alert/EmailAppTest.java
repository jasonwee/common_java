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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.weetech.string.Hex;


@ExtendWith(MockitoExtension.class)
public class EmailAppTest {

    @Mock
    Session sessionMock;

    @Mock
    Transport transportMock;

    @Mock
    Authenticator authenticatorMock;

    @Test
    public void sendTextViaSMTP() {
        SMTP smtp = new SMTP.Builder("mymail.smtp.com", 587)
                .smtpAuth(true)
                .smtpUsername("jason@foo.com")
                .smtpPassword("")
                .build();
        EmailAddress from = new EmailAddress.Builder("noreply@foo.com").name("No Reply").build();
        List<EmailAddress> recipients = List.of(new EmailAddress.Builder("jason@bar.com").build());
        List<EmailAddress> cc = null;
        List<EmailAddress> bcc = null;
        String subject = "email subject";
        EmailBody body = new EmailBody.Builder("Hello, this is a test.", "text/plain; charset=UTF-8").build();
        List<EmailAttachment> attachments = null;

        try (MockedStatic<Session> session = mockStatic(Session.class);
               MockedStatic<Transport> transport = mockStatic(Transport.class);
                MockedConstruction<MimeMessage> mockMimeMessage = Mockito.mockConstruction(MimeMessage.class);
                 ) {
            session.when(() -> Session.getDefaultInstance(Mockito.any(), Mockito.any())).thenReturn(sessionMock);
            when(sessionMock.getTransport()).thenReturn(transportMock);

            assertTrue(EmailApp.sendText(from, recipients, cc, bcc, subject, body, attachments, smtp, true));
        } catch (Exception e) {
            fail("must not fail test", e);
        }
    }

    @Test
    public void sendTextViaLocalhost() {
        SMTP smtp = new SMTP.Builder("localhost", 25).build();
        EmailAddress from = new EmailAddress.Builder("noreply@foo.com").name("No Reply").build();
        List<EmailAddress> recipients = List.of(new EmailAddress.Builder("jason@bar.com").build());
        List<EmailAddress> cc = null;
        List<EmailAddress> bcc = null;
        String subject = "email subject";
        EmailBody body = new EmailBody.Builder("Hello, this is a test.", "text/plain; charset=us-ascii").build();
        List<EmailAttachment> attachments = null;

        try (MockedStatic<Session> session = mockStatic(Session.class);
                MockedStatic<Transport> transport = mockStatic(Transport.class);
                 MockedConstruction<MimeMessage> mockMimeMessage = Mockito.mockConstruction(MimeMessage.class);
                  ) {
            session.when(() -> Session.getInstance(any(), isNull())).thenReturn(sessionMock);
            when(sessionMock.getTransport()).thenReturn(transportMock);

            assertTrue(EmailApp.sendText(from, recipients, cc, bcc, subject, body, attachments, smtp, true));
        } catch (Exception e) {
            fail("must not fail test", e);
        }
    }

    @Test
    public void sendHTML() {
        EmailAddress from = new EmailAddress.Builder("noreply@foo.com").build();
        List<EmailAddress> recipients = List.of(new EmailAddress.Builder("jason@bar.com").build());
        List<EmailAddress> cc = null;
        List<EmailAddress> bcc = null;
        String subject = "email  好";
        EmailBody body = new EmailBody.Builder("<p>Hello, this is a test.</p>", "text/html; charset=\"UTF-8\"").build();
        SMTP smtp = new SMTP.Builder("mymail.smtp.com", 587).smtpAuth(true).smtpUsername("jason@foo.com").smtpPassword("").build();


        try (MockedStatic<Session> session = mockStatic(Session.class);
                MockedStatic<Transport> transport = mockStatic(Transport.class);
                 MockedConstruction<MimeMessage> mockMimeMessage = Mockito.mockConstruction(MimeMessage.class);
                  ) {
             session.when(() -> Session.getDefaultInstance(Mockito.any(), Mockito.any())).thenReturn(sessionMock);
             when(sessionMock.getTransport()).thenReturn(transportMock);

            assertTrue(EmailApp.sendHtml(from, recipients, cc, bcc, subject, body, null, smtp, true));
         } catch (Exception e) {
             fail("must not fail test", e);
         }
    }

    @Test
    public void sendTextEmailWithAttachment() {
        SMTP smtp = new SMTP.Builder("localhost", 25).build();
        EmailAddress from = new EmailAddress.Builder("noreply@foo.com").name("No Reply").build();
        List<EmailAddress> recipients = List.of(new EmailAddress.Builder("jason@bar.com").build());
        List<EmailAddress> cc = null;
        List<EmailAddress> bcc = null;
        String subject = "email subject";
        EmailBody body = new EmailBody.Builder("Hello, this is a test.", "text/plain; charset=us-ascii").build();
        List<EmailAttachment> attachments = List.of(new EmailAttachment.Builder("this is attachment").name("attachment_filename.txt").contentType("text/plain").build());

        try (MockedStatic<Session> session = mockStatic(Session.class);
                MockedStatic<Transport> transport = mockStatic(Transport.class);
                 MockedConstruction<MimeMessage> mockMimeMessage = Mockito.mockConstruction(MimeMessage.class);
                  ) {
            session.when(() -> Session.getInstance(any(), isNull())).thenReturn(sessionMock);
            when(sessionMock.getTransport()).thenReturn(transportMock);

            assertTrue(EmailApp.sendText(from, recipients, cc, bcc, subject, body, attachments, smtp, true));
        } catch (Exception e) {
            fail("must not fail test", e);
        }
    }

    @Test
    public void sendHTMLEmailWithAttachment() {
        EmailAddress from = new EmailAddress.Builder("noreply@foo.com").build();
        List<EmailAddress> recipients = List.of(new EmailAddress.Builder("jason@bar.com").build());
        List<EmailAddress> cc = null;
        List<EmailAddress> bcc = null;
        String subject = "email  好";
        EmailBody body = new EmailBody.Builder("<p>Hello, this is a test.</p>", "text/html; charset=\"UTF-8\"").build();
        SMTP smtp = new SMTP.Builder("mymail.smtp.com", 587).smtpAuth(true).smtpUsername("jason@foo.com").smtpPassword("").build();
        List<EmailAttachment> attachments = List.of(new EmailAttachment.Builder("this is attachment").name("attachment_filename.txt").contentType("text/plain").build());

        try (MockedStatic<Session> session = mockStatic(Session.class);
                MockedStatic<Transport> transport = mockStatic(Transport.class);
                 MockedConstruction<MimeMessage> mockMimeMessage = Mockito.mockConstruction(MimeMessage.class);
                  ) {
             session.when(() -> Session.getDefaultInstance(Mockito.any(), Mockito.any())).thenReturn(sessionMock);
             when(sessionMock.getTransport()).thenReturn(transportMock);

            assertTrue(EmailApp.sendHtml(from, recipients, cc, bcc, subject, body, attachments, smtp, true));
         } catch (Exception e) {
             fail("must not fail test", e);
         }
    }

    @Test
    public void sendTextEmailWithImgAttachment(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("temp_file.gif");
        // https://shoonia.github.io/pixel-gif/#1f85e3
        // xxd 1x1_#1F85E3.gif
        Files.write(file, Hex.parseHexBinary("474946383961010001008000001f85e300000021f90400000000002c00000000010001000002024401003b"));

        SMTP smtp = new SMTP.Builder("localhost", 25).build();
        EmailAddress from = new EmailAddress.Builder("noreply@foo.com").name("No Reply").build();
        List<EmailAddress> recipients = List.of(new EmailAddress.Builder("jason@bar.com").build());
        List<EmailAddress> cc = null;
        List<EmailAddress> bcc = null;
        String subject = "email subject";
        EmailBody body = new EmailBody.Builder("Hello, this is a test.", "text/plain; charset=us-ascii").build();
        List<EmailAttachment> attachments = List.of(new EmailAttachment.Builder(file.toFile()).name("temp_file.gif").build());

        try (MockedStatic<Session> session = mockStatic(Session.class);
                MockedStatic<Transport> transport = mockStatic(Transport.class);
                 MockedConstruction<MimeMessage> mockMimeMessage = Mockito.mockConstruction(MimeMessage.class);
                  ) {
            session.when(() -> Session.getInstance(any(), isNull())).thenReturn(sessionMock);
            when(sessionMock.getTransport()).thenReturn(transportMock);

            assertTrue(EmailApp.sendText(from, recipients, cc, bcc, subject, body, attachments, smtp, true));
        } catch (Exception e) {
            fail("must not fail test", e);
        }
    }

    @Test
    public void sendHTMLEmailWithImgAttachment(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("temp_file.gif");
        Files.writeString(file, "content inside temp file");
        Files.write(file, Hex.parseHexBinary("474946383961010001008000001f85e300000021f90400000000002c00000000010001000002024401003b"));

        EmailAddress from = new EmailAddress.Builder("noreply@foo.com").build();
        List<EmailAddress> recipients = List.of(new EmailAddress.Builder("jason@bar.com").build());
        List<EmailAddress> cc = null;
        List<EmailAddress> bcc = null;
        String subject = "email  好";
        EmailBody body = new EmailBody.Builder("<p>Hello, this is a test.</p>", "text/html; charset=\"UTF-8\"").build();
        SMTP smtp = new SMTP.Builder("mymail.smtp.com", 587).smtpAuth(true).smtpUsername("jason@foo.com").smtpPassword("").build();
        List<EmailAttachment> attachments = List.of(new EmailAttachment.Builder(file.toFile()).name("temp_file.gif").build());

        try (MockedStatic<Session> session = mockStatic(Session.class);
                MockedStatic<Transport> transport = mockStatic(Transport.class);
                 MockedConstruction<MimeMessage> mockMimeMessage = Mockito.mockConstruction(MimeMessage.class);
                  ) {
             session.when(() -> Session.getDefaultInstance(Mockito.any(), Mockito.any())).thenReturn(sessionMock);
             when(sessionMock.getTransport()).thenReturn(transportMock);

            assertTrue(EmailApp.sendHtml(from, recipients, cc, bcc, subject, body, attachments, smtp, true));
         } catch (Exception e) {
             fail("must not fail test", e);
         }
    }

    @Test
    public void sendTextAndHtml() {
        EmailAddress from = new EmailAddress.Builder("noreply@foo.com").build();
        List<EmailAddress> recipients = List.of(new EmailAddress.Builder("jason@bar.com").build());
        List<EmailAddress> cc = null;
        List<EmailAddress> bcc = null;
        String subject = "email  好";
        EmailBody msgHtml = new EmailBody.Builder("<p>Hello, this is a test.</p>", "text/html; charset=\"UTF-8\"").build();
        EmailBody msgText = new EmailBody.Builder("Hello, this is a test", "text/plain; charset=\"UTF-8\"").build();
        SMTP smtp = new SMTP.Builder("mymail.smtp.com", 587).smtpAuth(true).smtpUsername("jason@foo.com").smtpPassword("").build();


        try (MockedStatic<Session> session = mockStatic(Session.class);
                MockedStatic<Transport> transport = mockStatic(Transport.class);
                 MockedConstruction<MimeMessage> mockMimeMessage = Mockito.mockConstruction(MimeMessage.class);
                  ) {
             session.when(() -> Session.getDefaultInstance(Mockito.any(), Mockito.any())).thenReturn(sessionMock);
             when(sessionMock.getTransport()).thenReturn(transportMock);

            assertTrue(EmailApp.sendTextAndHtml(from, recipients, cc, bcc, subject, msgText, msgHtml, null, smtp, true));
         } catch (Exception e) {
             fail("must not fail test", e);
         }
    }

}
