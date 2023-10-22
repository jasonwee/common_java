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

public abstract class Email {

    public enum Type {
        mixed,
        alternative,
        related,
    }


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
