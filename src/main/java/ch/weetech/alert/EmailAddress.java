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

public class EmailAddress extends Email {

    private String address;
    private String name;

    private EmailAddress(Builder builder) {
        this.address = builder.address;
        this.name = builder.name;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public static class Builder {

        private String address;
        private String name;

        public Builder(String address) {
            this.address = address;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public EmailAddress build() {
            validate();
            return new EmailAddress(this);
        }

        private void validate() throws IllegalStateException {
            if (address == null || address.trim().isEmpty()) {
                throw new IllegalStateException("address must not be null nor empty");
            }
            if (!Email.isValidEmail(address)) {
                throw new IllegalStateException("invalid email address");
            }
        }

    }
}
