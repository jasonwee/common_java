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

import java.io.File;

public class EmailAttachment extends Email{

    // file object or just text content
    private File file;
    private String content;

    private String name;
    private String contentType;
    private String contentTransferEncoding;

    private EmailAttachment(Builder builder) {
        this.file = builder.file;
        this.content = builder.content;
        this.name = builder.name;
        this.contentType = builder.contentType;
        this.contentTransferEncoding = builder.contentTransferEncoding;
    }

    public File getFile() {
        return file;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentTransferEncoding() {
        return contentTransferEncoding;
    }

    public static class Builder {
        // either file or content, so no need builder method. it should be at constructor
        private File file;
        private String content;

        private String name;
        private String contentType;
        private String contentTransferEncoding;

        public Builder(File file) {
            this.file = file;
        }

        public Builder(String content) {
            this.content = content;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder contentTransferEncoding(String contentTransferEncoding) {
            this.contentTransferEncoding = contentTransferEncoding;
            return this;
        }

        public EmailAttachment build() {
            validate();
            return new EmailAttachment(this);
        }

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
