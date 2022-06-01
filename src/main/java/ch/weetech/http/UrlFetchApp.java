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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// https://developers.google.com/apps-script/reference/url-fetch
public class UrlFetchApp {

	public static HttpResponse<String> get(String url, HttpClient client, Map<String, String> headers) throws Exception {

		if (client == null) {
			throw new IllegalArgumentException("please create a valid HttpClient object");
		}
		
		if (url == null || url.isEmpty()) {
			throw new IllegalArgumentException("url is required");
		}

		final HttpClient httpClient = client;
		
		Builder builder = HttpRequest.newBuilder().GET();
		
		builder.uri(URI.create(url));
		
		if (headers != null) {
			for (Entry<String, String> sets : headers.entrySet()) {
				builder.setHeader(sets.getKey(), sets.getValue());
			}
		}

        HttpRequest request = builder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        return response;
	}
	
	public static String getResponse(String url, Map<String, String> data) throws Exception {
		// convert data
		
		String u = url;
		if (data != null) {
			u = String.format("%s?%s", url, getParamsString(data));
		}

		// always follow redirect with 2 seconds connect timeout.
		final HttpClient httpClient = HttpClient.newBuilder()
				.followRedirects(HttpClient.Redirect.ALWAYS)
				.connectTimeout(Duration.ofSeconds(2))
				// read timeout?
				.version(HttpClient.Version.HTTP_2)
				.build();
		
		HttpResponse<String> response = get(u, httpClient, null);
		
		return response.body();
	}
	
	// we only care about the response.
	public static String getResponse(String url) throws Exception {
		return getResponse(url, null);
	}
	
	public static HttpResponse<?>[] getAll(List<String> urls, ExecutorService executorService) throws Exception {
		final HttpClient httpClient = HttpClient.newBuilder()
				.executor(executorService)
				.version(HttpClient.Version.HTTP_2).build();

		List<HttpResponse<String>> results = new ArrayList<HttpResponse<String>>();
		
		// changed type
		List<URI> targets = urls.stream().map(URI::create).collect(Collectors.toList());

        List<CompletableFuture<HttpResponse<String>>> result = 
        		targets.stream()
        		           .map(url -> httpClient.sendAsync(
        		        		   HttpRequest
        		        		     .newBuilder(url)
        		        		     .GET()
        		        		     .build(), 
        		        		   HttpResponse.BodyHandlers.ofString())
        		           ).collect(Collectors.toList());
        
        for (CompletableFuture<HttpResponse<String>> future : result) {
        	results.add(future.get());
        }
        
		return results.stream().toArray(HttpResponse[]::new);
	}
	
	public static String asyncGet(String url, int timeout) throws Exception {
		
		final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
		
		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(url))
				.build();

		CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request,
				HttpResponse.BodyHandlers.ofString());

		return response.thenApply(HttpResponse::body).get(timeout, TimeUnit.SECONDS);
	}

	public static HttpResponse<String> post(String url, Map<Object, Object> data, String contentType) throws Exception {
		final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(ofFormData(data))
                .uri(URI.create(url))
                .header("Content-Type", contentType)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		return response;
	}


    private static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
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

	private static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		String resultString = result.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
	}

}
