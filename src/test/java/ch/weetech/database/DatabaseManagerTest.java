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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mockStatic;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DatabaseManagerTest {

    @Mock
    Connection connection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @Test
    void testUnit() {
        // see DatabaseStatementManagerTest
        try (MockedStatic<Jdbc> jdbc = mockStatic(Jdbc.class);
               MockedStatic<RStoListMap> rStoListMap = mockStatic(RStoListMap.class)
                ) {
            // preparing
            jdbc.when(() -> Jdbc.getConnection(anyString(), anyString(), anyString())).thenReturn(connection);
            when(connection.createStatement()).thenReturn(statement);
            List<Map<String, String>> listMap = new LinkedList<>();
            Map<String, String> row1Maps = new HashMap<>();
            row1Maps.put("k1", "v1");
            listMap.add(row1Maps);
            rStoListMap.when(() -> RStoListMap.toMapList(any())).thenReturn(listMap);
            when(statement.executeQuery(anyString())).thenReturn(resultSet);

            // test
            DatabaseManager dbm = new DatabaseManager("testusername", "testpassword", "jdbc:mysql://localhost:3306/");
            dbm.init();

            Result res = dbm.execute("select * from javabase.mytable");
            res = dbm.execute("select * from javabase.mytable");

            dbm.close();

            // verify
            assertNotNull(res);
            assertTrue(res.isSuccess());
            assertEquals("[{k1=v1}]", res.getResultsList().toString());
            assertEquals(1, res.getResultsList().size());
        } catch (Exception e) {
            fail("fail test", e);
        }
    }

}
