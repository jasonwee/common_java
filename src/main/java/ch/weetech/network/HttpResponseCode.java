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
package ch.weetech.network;

public class HttpResponseCode {

    // https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
    // custom error code
    public static int CLIENT_IO_ERROR = 480;
    public static int CLIENT_INTERRUPTED_EXCEPTION = 481;
    public static int CLIENT_CONNECT_TIMEOUT = 482;
    public static int CLIENT_READ_TIMEOUT = 483;

}
