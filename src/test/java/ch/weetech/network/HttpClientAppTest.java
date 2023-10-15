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


import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ExtendWith(MockitoExtension.class)
public class HttpClientAppTest {

    @Mock
    HttpClient httpClient;

    @Mock
    java.net.http.HttpResponse<Object> httpResponse;

    @Test
    public void testHttpGet() throws IOException, InterruptedException {
        String mockResponse = "{ \"hello\": \"world\"}" ;
        when(httpClient.send(any(), any())).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(mockResponse);
        HttpClientApp tester = new HttpClientApp(httpClient);
        HttpResponse response = tester.get("https://dummyjson.com/products");
        assertNotNull(response);
        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getContent().length() > 0);
        assertEquals(response.getContent(), mockResponse);
    }
    
    @Test
    public void testHttpPost() throws IOException, InterruptedException {
        String mockResponse = "{ \"id\": \"101\"}" ;
        when(httpClient.send(any(), any())).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(mockResponse);
        Map<Object, Object> data = new HashMap<>();
        data.put("username", "abc");
        data.put("password", "123");
        data.put("custom", "secret");
        data.put("ts", System.currentTimeMillis());

        HttpClientApp tester = new HttpClientApp(httpClient);
        HttpResponse response = tester.post("https://dummyjson.com/products/add", data);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getContent().length() > 0);
        assertEquals(response.getContent(), mockResponse);
    }

}
