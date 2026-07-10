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
import java.sql.DriverManager;

/**
 * Utility class for obtaining JDBC database connections.
 * 
 * https://developers.google.com/apps-script/reference/jdbc
 * https://developers.google.com/apps-script/guides/jdbc
 * 
 * <p>Provides a simple way to create database connections for MySQL and PostgreSQL
 * by automatically loading the appropriate JDBC driver based on the connection URL.</p>
 * 
 * @author Your Name
 * @version 1.0
 */
@SuppressWarnings("doclint:missing")
public class Jdbc {

	/**
     * Creates and returns a database connection using the given parameters.
     * 
     * <p>Automatically registers the appropriate JDBC driver:</p>
     * <ul>
     *   <li>MySQL driver ({@code com.mysql.cj.jdbc.Driver}) if URL contains "mysql"</li>
     *   <li>PostgreSQL driver ({@code org.postgresql.Driver}) if URL contains "postgresql"</li>
     * </ul>
     * 
     * @param url      the JDBC connection URL 
     *                 (e.g. "jdbc:mysql://localhost:3306/mydb" or "jdbc:postgresql://localhost:5432/mydb")
     * @param user     the database username
     * @param password the database password
     * @return a {@link Connection} to the database
     * @throws Exception if driver loading fails or connection cannot be established
     */
    public static Connection getConnection(String url, String user, String password) throws Exception {
        if (url.contains("mysql")) {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } else if (url.contains("postgresql")) {
            Class.forName("org.postgresql.Driver");
        }
        return DriverManager.getConnection(url, user, password);
    }

}
