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
package ch.weetech.http;

import static org.junit.jupiter.api.Assertions.*;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

public class UrlFetchAppTest {
	
	@Test
    public void testGet() {

    }
	
	@Test
    public void testGetResponse() {
		try {
			String result = UrlFetchApp.getResponse("https://httpbin.org/get");
			assertTrue(result != null);
			assertTrue(!result.isEmpty());
		} catch (Exception e) {
			e.printStackTrace();
			fail("should not fail");
		}
	}
	
	@Test
	public void testGetAll() {
		
        List<String> targets = Arrays.asList(
                "https://httpbin.org/get?name=jasonwee1",
                "https://httpbin.org/get?name=jasonwee2",
                "https://httpbin.org/get?name=jasonwee3",
                "https://httpbin.org/get?name=jasonwee4",
                "https://httpbin.org/get?name=jasonwee5");
        
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        
        try {
			HttpResponse<?>[] results = UrlFetchApp.getAll(targets, executorService);
			assertEquals(5, results.length);
		} catch (Exception e) {
			e.printStackTrace();
			fail("should not fail to get all");
		} finally {
			executorService.shutdown();
		}
	}
	
	@Test
    public void testAsyncGet() {
		try {
			String result = UrlFetchApp.asyncGet("https://httpbin.org/get", 5);
			System.out.println(result);
			assertTrue(result != null);
			assertTrue(!result.isEmpty());
		} catch (Exception e) {
			e.printStackTrace();
			fail("should not fail");
		}
	}
	
	@Test
	public void testPost() {
		
        Map<Object, Object> data = new HashMap<>();
        data.put("username", "abc");
        data.put("password", "123");
        data.put("custom", "secret");
        data.put("ts", System.currentTimeMillis());
        
        String url = "https://httpbin.org/post";
        String contentType =  "application/x-www-form-urlencoded";
        
		try {
			HttpResponse<String> result = UrlFetchApp.post(url, data, contentType);
			assertNotNull(result);
			assertEquals(200, result.statusCode());
		} catch (Exception e) {
			e.printStackTrace();
			fail("should not fail to get all");
		}
	}

}
