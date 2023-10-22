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

// https://blogs.oracle.com/javamagazine/post/exploring-joshua-blochs-builder-design-pattern-in-java
public class SMTP {

    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String MAIL_SMTP_USERNAME = "mail.smtp.username";
    public static final String MAIL_SMTP_PASSWORD = "mail.smtp.password";

    private String smtpHost;
    private int smtpPort;
    private boolean smtpAuth;
    private boolean smtpStartTlsEnable;
    private String smtpUsername;
    private String smtpPassword;

    private SMTP(Builder builder) {
        this.smtpHost = builder.smtpHost;
        this.smtpPort = builder.smtpPort;
        this.smtpAuth = builder.smtpAuth;
        this.smtpStartTlsEnable = builder.smtpStartTlsEnable;
        this.smtpUsername = builder.smtpUsername;
        this.smtpPassword = builder.smtpPassword;
    }

    public String getSmtpHost() {
        return smtpHost;
    }
    public int getSmtpPort() {
        return smtpPort;
    }
    public boolean isSmtpAuth() {
        return smtpAuth;
    }
    public boolean isSmtpStartTlsEnable() {
        return smtpStartTlsEnable;
    }
    public String getSmtpUsername() {
        return smtpUsername;
    }
    public String getSmtpPassword() {
        return smtpPassword;
    }

    public static class Builder {

        private String smtpHost;
        private int smtpPort;
        private boolean smtpAuth;
        private boolean smtpStartTlsEnable;
        private String smtpUsername;
        private String smtpPassword;

        public Builder(String smtpHost, int smtpPort) {
            this.smtpHost = smtpHost;
            this.smtpPort = smtpPort;
        }

        public Builder smtpAuth(boolean smtpAuth) {
            this.smtpAuth = smtpAuth;
            return this;
        }

        public Builder smtpStartTlsEnable(boolean smtpStartTlsEnable) {
            this.smtpStartTlsEnable = smtpStartTlsEnable;
            return this;
        }

        public Builder smtpUsername(String smtpUsername) {
            this.smtpUsername = smtpUsername;
            return this;
        }

        public Builder smtpPassword(String smtpPassword) {
            this.smtpPassword = smtpPassword;
            return this;
        }

        public SMTP build() {
            validate();
            return new SMTP(this);
        }

        private void validate() throws IllegalStateException {
            if (smtpHost == null || smtpHost.isEmpty()) {
                 throw new IllegalStateException("smtpHost is required");
            }
            if (smtpPort < 0 || smtpPort >= 65535) {
                 throw new IllegalStateException("invalid smtpPort");
            }
            if (smtpAuth && (smtpUsername == null || smtpPassword == null)) {
                 throw new IllegalStateException("smtpAuth is enabled but smtpUsername and smtpPassword is required");
            }

        }
    }

}
