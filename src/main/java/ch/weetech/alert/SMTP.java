/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.weetech.alert;

/**
 * Configuration data object for Simple Mail Transfer Protocol (SMTP) server connections.
 * 
 * https://blogs.oracle.com/javamagazine/post/exploring-joshua-blochs-builder-design-pattern-in-java
 * 
 * <p>
 * This class stores server endpoint coordinates, authentication flags, security configurations, 
 * and user credentials. It provides standard configuration property keys matching JavaMail frameworks 
 * and implements a fluent builder validation pattern.
 * </p>
 */
public class SMTP {

    /** The standard JavaMail property key configuration string for the SMTP server hostname. */
    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    /** The standard JavaMail property key configuration string for the SMTP server port. */
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
    /** The standard JavaMail property key configuration string for enabling SMTP server authentication. */
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    /** The standard JavaMail property key configuration string for enabling opportunistic STARTTLS encryption. */
    public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    /** The configuration string reference name for the server authentication account username. */
    public static final String MAIL_SMTP_USERNAME = "mail.smtp.username";
    /** The configuration string reference name for the server authentication account secret password. */
    public static final String MAIL_SMTP_PASSWORD = "mail.smtp.password";

    private String smtpHost;
    private int smtpPort;
    private boolean smtpAuth;
    private boolean smtpStartTlsEnable;
    private String smtpUsername;
    private String smtpPassword;

    /**
     * Constructs a validated SMTP connection container using state properties set inside a builder.
     *
     * @param builder the configured structural initializer framework context instance
     */
    private SMTP(Builder builder) {
        this.smtpHost = builder.smtpHost;
        this.smtpPort = builder.smtpPort;
        this.smtpAuth = builder.smtpAuth;
        this.smtpStartTlsEnable = builder.smtpStartTlsEnable;
        this.smtpUsername = builder.smtpUsername;
        this.smtpPassword = builder.smtpPassword;
    }

    /**
     * Gets the configured target SMTP server domain name or numeric host network IP address.
     *
     * @return the destination endpoint string host value
     */
    public String getSmtpHost() {
        return smtpHost;
    }

    /**
     * Gets the remote system network port number allocated for handling connections.
     *
     * @return the integer port reference sequence
     */
    public int getSmtpPort() {
        return smtpPort;
    }

    /**
     * Checks if username and password authentication profiles must be explicitly provided to log in.
     *
     * @return {@code true} if connection rules require credentials verification; {@code false} otherwise
     */
    public boolean isSmtpAuth() {
        return smtpAuth;
    }
    
    /**
     * Checks if standard plain-text transport sequences must escalate securely using STARTTLS negotiations.
     *
     * @return {@code true} if transport channel layer upgrade tasks are enabled; {@code false} otherwise
     */
    public boolean isSmtpStartTlsEnable() {
        return smtpStartTlsEnable;
    }
    
    /**
     * Gets the username account identity lookup string used for verifying server authorization rights.
     *
     * @return the profile login identification string payload, or {@code null} if authentication is disabled
     */
    public String getSmtpUsername() {
        return smtpUsername;
    }
    
    /**
     * Gets the raw secret credential verification token or password corresponding to the profile account.
     *
     * @return the access authorization secret payload, or {@code null} if authentication is disabled
     */
    public String getSmtpPassword() {
        return smtpPassword;
    }

    /**
     * A fluent API validation builder designed to safely initialize instances of {@link SMTP}.
     */
    public static class Builder {

        private String smtpHost;
        private int smtpPort;
        private boolean smtpAuth;
        private boolean smtpStartTlsEnable;
        private String smtpUsername;
        private String smtpPassword;

        /**
         * Initializes a new builder workspace configuration layout with mandatory connection endpoint inputs.
         *
         * @param smtpHost the destination SMTP server hostname or network IP destination location
         * @param smtpPort the numeric interface connection entry port value (typically 25, 465, or 587)
         */
        public Builder(String smtpHost, int smtpPort) {
            this.smtpHost = smtpHost;
            this.smtpPort = smtpPort;
        }

        /**
         * Sets whether connection profiles must attempt authentication tracking steps against the target system.
         *
         * @param smtpAuth {@code true} if account validation operations are requested
         * @return a updated self reference to this fluent builder instance
         */
        public Builder smtpAuth(boolean smtpAuth) {
            this.smtpAuth = smtpAuth;
            return this;
        }

        /**
         * Sets whether the server link should trigger an opportunistic channel security upgrade using STARTTLS command verbs.
         *
         * @param smtpStartTlsEnable {@code true} if connection protocols should upgrade securely
         * @return a updated self reference to this fluent builder instance
         */
        public Builder smtpStartTlsEnable(boolean smtpStartTlsEnable) {
            this.smtpStartTlsEnable = smtpStartTlsEnable;
            return this;
        }

        /**
         * Sets the username authentication text payload used during service login phases.
         *
         * @param smtpUsername the user account registration credential identity string
         * @return a updated self reference to this fluent builder instance
         */
        public Builder smtpUsername(String smtpUsername) {
            this.smtpUsername = smtpUsername;
            return this;
        }

        /**
         * Sets the raw password account verification token used to establish authorization clearance rules.
         *
         * @param smtpPassword the user account authorization access password string
         * @return a updated self reference to this fluent builder instance
         */
        public Builder smtpPassword(String smtpPassword) {
            this.smtpPassword = smtpPassword;
            return this;
        }

        /**
         * Validates properties and locks data records into an immutable {@link SMTP} object context layout.
         *
         * @return a fully populated and syntactically clean configuration instance reference
         * @throws IllegalStateException if endpoints are blank, ports are out-of-bounds, or required credentials are omitted
         */
        public SMTP build() {
            validate();
            return new SMTP(this);
        }

        /**
         * Enforces required presence rules, logical alignment, and numeric boundary boundaries.
         * <p>
         * Example: Ensures matching login parameters exist if auth parameters evaluate to true.
         * For specification definitions, see: {@code https://rfc-editor.org}
         * </p>
         *
         * @throws IllegalStateException if constraint parameters fail consistency validations
         */
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
