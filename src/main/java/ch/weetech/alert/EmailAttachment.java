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

import java.io.File;

/**
 * Represents an email attachment, holding either a binary file or raw text content 
 * along with associated MIME metadata.
 * 
 * <p>Instances of this class are immutable and must be constructed using the 
 * nested {@link Builder} class to ensure proper data validation.</p>
 */
public class EmailAttachment extends Email {

    /** The binary file asset associated with this attachment. 
     * file object or just text content
     */
    private File file;
    /** The raw text payload data for this attachment. */
    private String content;
    /** The display name or filename of the attachment. */
    private String name;
    /** The MIME media type string (e.g., "text/plain", "application/pdf"). */
    private String contentType;
    /** The encoding applied to the payload (e.g., "base64", "quoted-printable"). */
    private String contentTransferEncoding;

    /**
     * Constructs an EmailAttachment instance copies fields from the builder.
     *
     * @param builder the configured builder instance containing property states
     */
    private EmailAttachment(Builder builder) {
        this.file = builder.file;
        this.content = builder.content;
        this.name = builder.name;
        this.contentType = builder.contentType;
        this.contentTransferEncoding = builder.contentTransferEncoding;
    }

    /**
     * Retrieves the physical file associated with this attachment.
     *
     * @return the {@link File} object, or {@code null} if text content is used instead
     */
    public File getFile() {
        return file;
    }

    /**
     * Retrieves the raw string text content payload.
     *
     * @return the text content string, or {@code null} if a file asset is used instead
     */
    public String getContent() {
        return content;
    }

    /**
     * Retrieves the display or target filename assigned to this attachment.
     *
     * @return the filename string
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the configured MIME media type string.
     *
     * @return the content-type identifier
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Retrieves the transport encoding format for this attachment element.
     *
     * @return the encoding scheme keyword string
     */
    public String getContentTransferEncoding() {
        return contentTransferEncoding;
    }

    /**
     * A fluent API Builder pattern engine dedicated to creating valid, 
     * well-formed {@link EmailAttachment} instances.
     */

    public static class Builder {
        // either file or content, so no need builder method. it should be at constructor
        private File file;
        private String content;

        private String name;
        private String contentType;
        private String contentTransferEncoding;

        /**
         * Initializes a builder initialized with a file-backed asset target.
         *
         * @param file the resource file to attach; must be readable and exist
         */
        public Builder(File file) {
            this.file = file;
        }

        /**
         * Initializes a builder initialized with a raw text string payload.
         *
         * @param content the raw text sequence string; must not be blank
         */
        public Builder(String content) {
            this.content = content;
        }

        /**
         * Binds a target display name or system filename to the attachment.
         *
         * @param name the display string used in email headers
         * @return this builder instance for method chaining
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Declares the specific content type category definition for the resource payload.
         *
         * @param contentType the MIME format identifier string
         * @return this builder instance for method chaining
         */
        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * Dictates the serialization transmission format configuration context.
         *
         * @param contentTransferEncoding the header encoding keyword scheme
         * @return this builder instance for method chaining
         */
        public Builder contentTransferEncoding(String contentTransferEncoding) {
            this.contentTransferEncoding = contentTransferEncoding;
            return this;
        }

        /**
         * Executes validation filters and constructs a finished {@link EmailAttachment} object.
         *
         * @return a verified, immutable EmailAttachment instance
         * @throws IllegalStateException if data requirements fail integrity and state checks
         */
        public EmailAttachment build() {
            validate();
            return new EmailAttachment(this);
        }

        /**
         * Inspects internal states to secure compliance against configuration flaws.
         *
         * @throws IllegalStateException if payloads are empty, missing, or file targets are invalid
         */
        private void validate() throws IllegalStateException {
            if (file == null && content == null) {
                 throw new IllegalStateException("either File object or text content must be set");
            }
            if (content != null && content.trim().isEmpty()) {
                throw new IllegalStateException("either File object or text content must be set");
            }
            if (file != null) {
                if (!file.exists() && file.isDirectory()) {
                    throw new IllegalStateException("file must be exists and not a directory");
                }
                if (!file.canRead()) {
                    throw new IllegalStateException("file must be readable");
                }
            }
        }
    }

}
