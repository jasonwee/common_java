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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class EmailAttachmentTest {

    @Test
    public void validTextContent() {
        EmailAttachment tested = new EmailAttachment.Builder("this is content").name("mycontent.txt").build();
        assertNotNull(tested);
        assertEquals("this is content", tested.getContent());
        assertEquals("mycontent.txt", tested.getName());
        assertNull("mycontent.txt", tested.getFile());
    }

    @Test
    public void validFileContent(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("temp_file.txt");
        Files.writeString(file, "content inside temp file");

        EmailAttachment tested = new EmailAttachment.Builder(file.toFile()).name("mycustom_filename.txt").build();
        assertNotNull(tested);
        assertEquals("content inside temp file", Files.readString(file));
        assertEquals("mycustom_filename.txt", tested.getName());
        assertNull(tested.getContent());
    }

    @Test
    public void testValidation() throws IOException {
        EmailAttachment.Builder builder = new EmailAttachment.Builder("");
        assertThrows(IllegalStateException.class, builder::build);
    }

}
