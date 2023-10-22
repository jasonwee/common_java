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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class EmailBodyTest {


    @Test
    public void validText() {
        EmailBody tested = new EmailBody.Builder("hello world", "text/plain; charset=UTF-8").build();
        assertNotNull(tested);
        assertEquals("hello world", tested.getContent());
        assertEquals("text/plain; charset=UTF-8", tested.getContentType());
    }

    @Test
    public void validHtml() {
        EmailBody tested = new EmailBody.Builder("<p>hello world</p>", "text/html; charset=\"utf-8\"").build();
        assertNotNull(tested);
        assertEquals("<p>hello world</p>", tested.getContent());
        assertEquals("text/html; charset=\"utf-8\"", tested.getContentType());
    }

    @Test
    public void testValidation() {
        EmailBody.Builder builder = new EmailBody.Builder(null, "text/html; charset=\"utf-8\"");
        assertThrows(IllegalStateException.class, builder::build);

        builder = new EmailBody.Builder("hello world", "");
        assertThrows(IllegalStateException.class, builder::build);

    }
}
