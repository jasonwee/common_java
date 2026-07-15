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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Utility class for executing prepared database statements with dynamic parameters 
 * and automated execution performance monitoring.
 */
public class DatabaseStatementManager {

	/**
     * Logger instance for tracing SQL query performance and processing errors.
     */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseStatementManager.class);
    
    /**
     * Constructs a new {@code DatabaseStatementManager} instance.
     */
    public DatabaseStatementManager() {
    	
    }

    /**
     * Executes a parametrized SQL query, maps the bind variables from a map container, 
     * and transforms the resulting database cursor into a structured list of maps.
     * <p>
     * This method tracks query execution duration and logs alerts at varying urgency 
     * levels if execution thresholds (1 second and 10 seconds) are breached.
     * </p>
     *
     * @param preparedStatement the template SQL query string containing placeholders
     * @param hashmap           the mapping configuration containing parameter bind values
     * @param ds                the active data source provider supplying connections
     * @return a list containing query records as key-value pairs, or {@code null} if an exception occurs
     */
    public static List<Map<String, Object>> getHashMapList(String preparedStatement, Map<String, Object> hashmap, DataSource ds) {

        List<Map<String, Object>> result = null;
        long t0 = System.currentTimeMillis();

        try (Connection con = ds.getConnection();
                PreparedStatement stmt = con.prepareStatement(preparedStatement, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ) {

            StatementUtil.setStatement(hashmap, stmt);

            try (ResultSet rs = stmt.executeQuery();) {
                result = RStoListMap.getMapList(rs);
            }

            long ms = System.currentTimeMillis() - t0;

            if (logger.isDebugEnabled()) {
                logger.debug("Duration sql: [{}ms]", ms);
            } else if (ms > 1000 && ms <= 10000) {
                logger.warn("Duration slow sql: [{}ms]", ms);
            } else if (ms > 10000) {
                logger.error("Duration slow x2 sql: [{}ms]", ms);
            }

        } catch (Exception e) {
            logger.error("unable to execute sql query", e);
        }

         return result;
    }
}
