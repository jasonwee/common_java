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

// https://developers.google.com/apps-script/reference/jdbc
// https://developers.google.com/apps-script/guides/jdbc
public class Jdbc {

    public static Connection getConnection(String url, String user, String password) throws Exception {
        if (url.contains("mysql")) {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } else if (url.contains("postgresql")) {
            Class.forName("org.postgresql.Driver");
        }
        return DriverManager.getConnection(url, user, password);
    }

}
