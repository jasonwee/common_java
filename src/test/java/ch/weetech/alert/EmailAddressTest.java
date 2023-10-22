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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class EmailAddressTest {

    @Test
    public void valid() {
        EmailAddress tested = new EmailAddress.Builder("john@gmail.com").name("John Smith").build();
        assertNotNull(tested);
        assertEquals("john@gmail.com", tested.getAddress());
        assertEquals("John Smith", tested.getName());

        tested = new EmailAddress.Builder("marry@gmail.com").build();
        assertNotNull(tested);
        assertEquals("marry@gmail.com", tested.getAddress());
        assertNull(tested.getName());
    }

    @Test
    public void testValidation() {
        EmailAddress.Builder builder = new EmailAddress.Builder("john").name("John Smith");
        assertThrows(IllegalStateException.class, builder::build);

        builder = new EmailAddress.Builder("");
        assertThrows(IllegalStateException.class, builder::build);

    }

}
