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

public class DatabaseManager implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    private Connection con;

    private String username;
    private String password;

    private String url;

    public DatabaseManager(String username, String password, String url) {
        this.username = username;
        this.password = password;

        this.url = url;

    }

    public void init() throws Exception {
        con = Jdbc.getConnection(url, username, password);
    }

    public void shutdown() {
        try {
            logger.info("releasing database connection");
            con.close();
        } catch (SQLException e) {
            logger.info("", e);
        }
    }

    @Override
    public void close() throws IOException {
        shutdown();
    }

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
