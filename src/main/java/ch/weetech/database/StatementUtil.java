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
package ch.weetech.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Utility class for working with JDBC {@link PreparedStatement} objects.
 * 
 * <p>Provides helper methods to simplify setting parameters on prepared statements
 * using common data structures.</p>
 *
 */
@SuppressWarnings("doclint:missing")
public class StatementUtil {

	/**
     * Sets parameters on a {@link PreparedStatement} from a {@link Map} where keys
     * are sequential string numbers ("1", "2", "3", ...).
     * 
     * <p>This method iterates through the map looking for keys "1", "2", "3", etc.
     * and sets the corresponding values on the PreparedStatement using 
     * {@link PreparedStatement#setObject(int, Object)}.</p>
     * 
     * <p>Example map:</p>
     * <pre>
     * Map&lt;String, Object&gt; params = new HashMap&lt;&gt;();
     * params.put("1", "John Doe");
     * params.put("2", 25);
     * params.put("3", new java.sql.Date(...));
     * 
     * StatementUtil.setStatement(params, stmt);
     * </pre>
     * 
     * @param hashmap the map containing parameters with string keys "1", "2", "3", ...
     * @param stmt    the PreparedStatement to set parameters on
     * @throws SQLException if a database access error occurs while setting parameters
     */
    public static void setStatement(Map<String, Object> hashmap, PreparedStatement stmt) throws SQLException {
        Object value = null;
        int count = 0;
        int i = 1;

        if (hashmap != null) {
            while (hashmap.containsKey("" + i)) {
                value = hashmap.get("" + i);
                stmt.setObject(++count, value);
                i++;
            }
        }
    }

}
