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
package ch.weetech.database;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;

import org.mockito.Mock;
import org.mockito.MockedStatic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JdbcTest {

    // Class cannot be mock
    //@Mock
    //Class<?> clazz;

    @Mock
    Connection connection;

    @Test
    void testUnit() {

        try (MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class);) {
            driverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(connection);

            Connection c = Jdbc.getConnection("jdbc:mysql://localhost:3306/", "testusername", "testpassword");
            assertNotNull(c);
            //verify(Class.class, times(1)).forName(anyString());
        } catch (Exception e) {

        }

    }

}
