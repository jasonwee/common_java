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
package ch.weetech.validator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

// https://gist.github.com/cjaoude/fd9910626629b53c4d25
public class EmailValidatorTest {

    @Test
    public void testIsValid() {
        Validator<String> validator = new EmailValidator();

        assertTrue(validator.isValid("foo@bar.com"));
        assertTrue(validator.isValid("foo+20231030@bar.com"));
        assertTrue(validator.isValid("email@example.com"));
        assertTrue(validator.isValid("firstname.lastname@example.com"));
        assertTrue(validator.isValid("email@subdomain.example.com"));
        assertTrue(validator.isValid("firstname+lastname@example.com"));
        assertTrue(validator.isValid("email@123.123.123.123"));
        assertTrue(validator.isValid("email@[123.123.123.123]"));
        assertTrue(validator.isValid("\"email\"@example.com"));
        assertTrue(validator.isValid("1234567890@example.com"));
        assertTrue(validator.isValid("email@example-one.com"));
        assertTrue(validator.isValid("_______@example.com"));
        assertTrue(validator.isValid("email@example.name"));
        assertTrue(validator.isValid("email@example.museum"));
        assertTrue(validator.isValid("email@example.co.jp"));
        assertTrue(validator.isValid("firstname-lastname@example.com"));
        assertTrue(validator.isValid("much.”more\\ unusual”@example.com"));
        // should be valid according to the github gist , make this test pass
        // assertTrue(validator.isValid("very.unusual.”@”.unusual.com@example.com"));
        // assertTrue(validator.isValid("very.”(),:;<>[]”.VERY.”very@\\\\ \"very”.unusual@strange.example.com"));
    }

    @Test
    public void testIsInvalid() {
        Validator<String> validator = new EmailValidator();

        assertFalse(validator.isValid("foo@.com"));
        assertFalse(validator.isValid("plainaddress"));
        assertFalse(validator.isValid("#@%^%#$@#$@#.com"));
        assertFalse(validator.isValid("@example.com"));
        assertFalse(validator.isValid("Joe Smith <email@example.com>"));
        assertFalse(validator.isValid("email.example.com"));
        assertFalse(validator.isValid("email@example@example.com"));
        assertFalse(validator.isValid(".email@example.com"));
        assertFalse(validator.isValid("email.@example.com"));
        assertFalse(validator.isValid("email..email@example.com"));
        // should be invalid according to the github gist , make this test pass
        // and all commented out below
        // assertFalse(validator.isValid("あいうえお@example.com"));
        assertFalse(validator.isValid("email@example.com (Joe Smith)"));
        //assertFalse(validator.isValid("email@example"));
        // assertFalse(validator.isValid("email@-example.com"));
        // assertFalse(validator.isValid("email@example.web"));
        // assertFalse(validator.isValid("email@111.222.333.44444"));
        assertFalse(validator.isValid("email@example..com"));
        assertFalse(validator.isValid("Abc..123@example.com"));
        assertFalse(validator.isValid("”(),:;<>[\\]@example.com"));
        // assertFalse(validator.isValid("just”not”right@example.com"));
        assertFalse(validator.isValid("this\\ is\"really\"not\\allowed@example.com"));
    }

}
