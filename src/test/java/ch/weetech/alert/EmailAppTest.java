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

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/*
 * TODO
 * re-enable sendTextViaSMTP
 * re-enable sendTextViaLocalhost
 * test email with text attachment
 * test email with binary attachment
 *
 */
public class EmailAppTest {


    @Disabled("write when powermockito support junit5")
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

        try {
            //when(EmailApp.sendText(from, recipients, cc, subject, body, attachments, smtp, true)).thenReturn(true);

            assertTrue(EmailApp.sendText(from, recipients, cc, bcc, subject, body, attachments, smtp, true));
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            fail("must not fail");
        }
    }

    @Disabled("write when powermockito support junit5")
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

        try {
            assertTrue(EmailApp.sendText(from, recipients, cc, bcc, subject, body, attachments, smtp, true));
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            fail("must not fail");
        }
    }

    @Disabled("write when powermockito support junit5")
    @Test
    public void sendHTML() {
        EmailAddress from = new EmailAddress.Builder("noreply@foo.com").build();
        List<EmailAddress> recipients = List.of(new EmailAddress.Builder("jason@bar.com").build());
        List<EmailAddress> cc = null;
        List<EmailAddress> bcc = null;
        String subject = "email  好";
        EmailBody body = new EmailBody.Builder("<p>Hello, this is a test.</p>", "text/html; charset=\"UTF-8\"").build();
        SMTP smtp = new SMTP.Builder("mymail.smtp.com", 587).smtpAuth(true).smtpUsername("jason@foo.com").smtpPassword("").build();

        try {
            assertTrue(EmailApp.sendHtml(from, recipients, cc, bcc, subject, body, null, smtp, true));
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            fail("must not fail");
        }

    }

}
