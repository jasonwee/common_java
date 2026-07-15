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
 * Represents the body content of an email, containing the text payload and its media format type.
 * <p>
 * This class inherits from {@link Email} and uses the Builder pattern for object construction.
 * </p>
 */
public class EmailBody extends Email {

    // TODO: 'related' support later
    // alternative, mixed
    // private Type subType;

	/**
     * The MIME content type of the email body (e.g., "text/plain", "text/html").
     * 
     * html / text
     */
    private String contentType;

    /**
     * The actual text or HTML payload of the email.
     * 
     * actual content
     */
    private String content;

    /**
     * Constructs an EmailBody instance using the provided builder.
     *
     * @param builder the builder containing the initialized fields
     */
    private EmailBody(Builder builder) {
        this.contentType = builder.contentType;
        this.content = builder.content;
    }

    /**
     * Gets the content type of this email body.
     *
     * @return a string representing the MIME type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Gets the raw content string of this email body.
     *
     * @return the text or HTML body content
     */
    public String getContent() {
        return content;
    }

    /**
     * A builder class for creating instances of {@link EmailBody}.
     */
    public static class Builder {
        private String contentType;
        private String content;

        /**
         * Initializes a new builder with the mandatory content and content type.
         *
         * @param content     the actual content payload
         * @param contentType the MIME content type (e.g., "text/html")
         */
        public Builder(String content, String contentType) {
            this.contentType = contentType;
            this.content = content;
        }

        /**
         * Validates the fields and constructs the {@link EmailBody} instance.
         *
         * @return a fully constructed and validated {@link EmailBody}
         * @throws IllegalStateException if the content is null, or if the contentType is null or empty
         */
        public EmailBody build() {
            validate();
            return new EmailBody(this);
        }

        /**
         * Validates that all required fields meet the integrity constraints.
         *
         * @throws IllegalStateException if validation rules fail
         */
        private void validate() throws IllegalStateException {
            if (content == null) {
                throw new IllegalStateException("content must not be null");
            }
            if (contentType == null || contentType.trim().isEmpty()) {
                throw new IllegalStateException("contentType must not be null nor empty");
            }
        }
    }

}
