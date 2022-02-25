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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public class DatabaseStatementManager {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseStatementManager.class);

    private static List<Map<String, Object>> getMapList(ResultSet resultSet) throws SQLException, NullPointerException {

        if (resultSet == null)
            throw new NullPointerException("resultset is null");

        List<Map<String, Object>> hashResults = null;
        String[] columnNames = null;

        resultSet.last();

        int numberOfRows = resultSet.getRow();

        hashResults = new ArrayList<>(numberOfRows);

        // set up columnNames
        ResultSetMetaData resultSetMega = resultSet.getMetaData();
        int numberOfColumns = resultSetMega.getColumnCount();

        if (numberOfRows == 0 && numberOfColumns != 0) {

            if (resultSetMega.getColumnLabel(1).equals("GENERATED_KEY")) {
                hashResults = new ArrayList<>(1);
                hashResults.add(new LinkedHashMap<>());
                return hashResults;
            } else {
                return null;
            }
        }

        columnNames = new String[numberOfColumns];

        for (int i = 1; i <= numberOfColumns; i++) {
            columnNames[i - 1] = resultSetMega.getColumnLabel(i);
        }

        // setup resultset to first row
        resultSet.first();

        // fill HashMap with put(columnName, data)
        if (numberOfRows != 0)
            do {

                SQLWarning warning = resultSet.getWarnings();

                while (warning != null) {

                    // Process connection warning
                    // For information on these values, see e241 Handling a SQL Exception
                    String message = warning.getMessage();
                    String sqlState = warning.getSQLState();
                    int errorCode = warning.getErrorCode();

                    logger.warn("message: {} \nsqlState: {} \nerrorCode: {}", message, sqlState, errorCode);
                    warning = warning.getNextWarning();

                }

                LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();

                for (int i = 0; i < numberOfColumns; i++) {

                    Object myObject = null;
                    try {
                        myObject = resultSet.getObject(columnNames[i]);
                        linkedHashMap.put(columnNames[i], myObject);
                    } catch (NullPointerException e) {
                        linkedHashMap.put(columnNames[i], null);
                    }
                    hashResults.add(linkedHashMap);
                }

            } while (resultSet.next());

        return hashResults;

    }

    public static List<Map<String, Object>> getHashMapList(String preparedStatement, Map<String, Object> hashmap, DataSource ds) {

        List<Map<String, Object>> result = null;
        long t0 = System.currentTimeMillis();

        try (Connection con = ds.getConnection();
                PreparedStatement stmt = con.prepareStatement(preparedStatement, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ) {

            StatementUtil.setStatement(hashmap, stmt);

            try (ResultSet rs = stmt.executeQuery();) {
                result = getMapList(rs);
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
