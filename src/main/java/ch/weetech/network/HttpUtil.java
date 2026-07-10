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

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * A utility class providing common helper routines for managing HTTP client communications.
 * <p>
 * This class abstracts lower-level processing operations such as structural content transformation 
 * and network transport stream data formatting.
 * </p>
 */
@SuppressWarnings("doclint:missing")
public class HttpUtil {

	/**
     * Converts a map of key-value properties into a URL-encoded form data byte stream payload.
     * <p>
     * Iterates through the given key-value collection, stringifies all entries, and safely encodes them 
     * matching the {@code application/x-www-form-urlencoded} specification using UTF-8 formatting rules.
     * The resulting encoded key-value pairs are bound together sequentially via ampersand flags.
     * </p>
     * <p>
     * For details regarding the format standards of form post requests, see: 
     * {@code https://w3.org}
     * </p>
     *
     * @param data a map containing the original data parameters to serialize into the form structure
     * @return a stateful reactive byte stream publisher that contains the final serialized key-value form layout
     * @throws NullPointerException if the input data map context stringifies null fields
     */
    public static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
         var builder = new StringBuilder();
            for (Map.Entry<Object, Object> entry : data.entrySet()) {
                if (builder.length() > 0) {
                    builder.append("&");
                }
                builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
                builder.append("=");
                builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
            }
            return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

}
