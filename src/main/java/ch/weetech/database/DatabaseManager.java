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

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages database lifecycle operations including connection initialization, 
 * query execution, and resource cleanup.
 * Implements {@link Closeable} to support try-with-resources blocks.
 */
public class DatabaseManager implements Closeable {

	/**
     * Logger instance for recording database lifecycle events and errors.
     */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    /**
     * The active connection instance to the database.
     */
    private Connection con;

    /**
     * The database user account name.
     */
    private String username;
    
    /**
     * The password corresponding to the database user.
     */
    private String password;

    /**
     * The JDBC connection URL string.
     */
    private String url;

    /**
     * Constructs a new {@code DatabaseManager} with the specified credentials and URL.
     *
     * @param username the database user account name
     * @param password the password corresponding to the database user
     * @param url      the JDBC connection URL string
     */
    public DatabaseManager(String username, String password, String url) {
        this.username = username;
        this.password = password;

        this.url = url;

    }

    /**
     * Initializes the database connection using the configured credentials and URL.
     *
     * @throws Exception if a database access error occurs during connection establishment
     */
    public void init() throws Exception {
        con = Jdbc.getConnection(url, username, password);
    }

    /**
     * Releasing the active database connection and logs any cleanup errors.
     */
    public void shutdown() {
        try {
            logger.info("releasing database connection");
            con.close();
        } catch (SQLException e) {
            logger.info("", e);
        }
    }

    /**
     * Closes the database manager resources by delegating to {@link #shutdown()}.
     * This method enables compatibility with try-with-resources statements.
     *
     * @throws IOException if an I/O error occurs during resource closure
     */
    @Override
    public void close() throws IOException {
        shutdown();
    }

    /**
     * Executes a given SQL query string and transforms the resulting {@link ResultSet} 
     * into a list-of-maps layout wrapped inside a {@code Result} container.
     *
     * @param query the SQL query string to be executed
     * @return a {@code Result} object detailing operation success and matching records
     */
    public Result execute(String query) {
        Result res = new Result();
        try (Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery(query);) {

            List<Map<String, String>> dbRes = RStoListMap.toMapList(results);

            res.setResultsList(dbRes);
            res.setSuccess(true);
            return res;
        } catch (SQLException e) {
            logger.error("unable to execute query", e);
        }
        return res;
    }

}
