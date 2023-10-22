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

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EmailTest {

    @Test
    public void testValidEmail() {
        assertFalse(Email.isValidEmail(null));
        assertFalse(Email.isValidEmail(""));
        assertFalse(Email.isValidEmail("i-am-a-long-character-1i-am-a-long-character-2i-am-a-long-character-3i-am-a-long-character-4i-am-a-long-character-5i-am-a-long-character-6i-am-a-long-character-7i-am-a-long-character-8i-am-a-long-character-9i-am-a-long-character-10i-am-a-long-character-11i-am-a-long-character-12i-am-a-long-character-13i-am-a-long-character-14@foo-bar-1foo-bar-2foo-bar-3foo-bar-4foo-bar-5foo-bar-6foo-bar-7foo-bar-8foo-bar-9foo-bar-10.com"));
        assertTrue(Email.isValidEmail("foo@bar.com"));
        assertTrue(Email.isValidEmail("foo+jason@bar.com"));
    }

}
