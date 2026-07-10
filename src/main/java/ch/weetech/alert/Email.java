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

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Abstract blueprint defining core structures, formatting configurations, 
 * and utility validation tools for handling email messages.
 */
public abstract class Email {

	/**
     * Defines the standard MIME multipart subtype structures used to assemble 
     * and render various layout combinations within an email body.
     */
    public enum Type {
    	/**
         * Used when body parts are independent and need to be bundled sequentially 
         * (e.g., combining plain body text alongside independent file attachments).
         */
        mixed,
        
        /**
         * Used when content is duplicated across different formats, allowing the client 
         * to render the best option (e.g., providing both Plain Text and HTML versions).
         */
        alternative,
        
        /**
         * Used when body parts reference each other internally to display compound content 
         * (e.g., an HTML body displaying an embedded inline image attachment).
         */
        related,
    }
    
    /**
     * Initializes core abstract email configurations.
     * <p>
     * This constructor is invoked implicitly or explicitly by concrete subclass 
     * constructors to establish baseline email structures.
     * </p>
     */
    protected Email() {
        // Initialization code if needed
    }


    /**
     * Validates if a given string adheres to standard email structure constraints.
     * <p>
     * The evaluation filters out blank references, enforces a strict maximum length rule 
     * of 320 characters to optimize processing boundaries, and leverages RFC compliance 
     * verification checks via {@link InternetAddress#validate()}.
     * </p>
     *
     * @param email the raw text email address string to evaluate
     * @return {@code true} if the input string is a structurally sound email address format, 
     *         {@code false} otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        if (email.length() >= 320) {
            return false;
        }

        boolean result = false;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            result = true;
        } catch (AddressException ex) {

        }
        return result;
    }

}
