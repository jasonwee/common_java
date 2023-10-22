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

public class EmailBody extends Email {

    // TODO: 'related' support later
    // alternative, mixed
    // private Type subType;

    // html / text
    private String contentType;

    // actual content
    private String content;

    private EmailBody(Builder builder) {
        this.contentType = builder.contentType;
        this.content = builder.content;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

    public static class Builder {
        private String contentType;
        private String content;

        public Builder(String content, String contentType) {
            this.contentType = contentType;
            this.content = content;
        }

        public EmailBody build() {
            validate();
            return new EmailBody(this);
        }

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
