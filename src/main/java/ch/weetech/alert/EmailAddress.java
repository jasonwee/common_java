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
 * Represents an immutable email address mapping that pairs a functional email routing address 
 * with an optional display name.
 * <p>
 * This class extends {@link Email} and enforces object construction invariants using an internal 
 * {@link Builder} pattern to guarantee validity before instance creation.
 * </p>
 */
public class EmailAddress extends Email {

	/**
     * The verified destination email address routing string.
     */
    private String address;
    
    /**
     * The optional friendly display name associated with this email address entry.
     */
    private String name;

    /**
     * Private constructor invoked exclusively by the inner {@link Builder} sequence.
     *
     * @param builder the configured builder configuration mapping source values
     */
    private EmailAddress(Builder builder) {
        this.address = builder.address;
        this.name = builder.name;
    }

    /**
     * Retrieves the validated email routing address string.
     *
     * @return the destination email address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Retrieves the display name associated with this address mapping.
     *
     * @return the display name value, or {@code null} if none was specified
     */
    public String getName() {
        return name;
    }

    /**
     * Builder class for assembling and validating structural {@link EmailAddress} instances.
     */
    public static class Builder {

    	/**
         * The temporary staging reference for the destination email routing string.
         */
        private String address;
        
        /**
         * The temporary staging reference for the user's friendly display name.
         */
        private String name;

        /**
         * Constructs a new Builder initialization sequence anchored by a required target address.
         *
         * @param address the baseline email address string configuration candidate
         */
        public Builder(String address) {
            this.address = address;
        }

        /**
         * Configures an optional user friendly display name context onto the target address structure.
         *
         * @param name the display name string layout to merge
         * @return a self reference pointer to support consecutive chaining sequences
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Evaluates structure criteria validations and instantiates an immutable {@link EmailAddress} instance.
         *
         * @return a fully populated and verified {@code EmailAddress} object instance
         * @throws IllegalStateException if the configured address is blank, null, or fails RFC structural validation rules
         */
        public EmailAddress build() {
            validate();
            return new EmailAddress(this);
        }

        /**
         * Asserts data invariants by screening for null fields, empty spacing issues, 
         * and testing format verification states through the base parent application utility check.
         *
         * @throws IllegalStateException if structural validation errors or blank addresses are encountered
         */
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
