/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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

/**
 * Utility class for making HTTP requests using Java's {@link HttpClient}.
 * 
 * https://developers.google.com/apps-script/reference/url-fetch
 * 
 * <p>Provides convenient static methods for GET and POST requests with support for
 * query parameters, headers, redirects, timeouts, and asynchronous operations.</p>
 *
 */
@SuppressWarnings("doclint:missing")
public class UrlFetchApp {

	/**
     * Sends a GET request using the provided {@link HttpClient} and headers.
     * 
     * @param url      the target URL
     * @param client   the HttpClient to use for the request
     * @param headers  optional headers to include in the request
     * @return the {@link HttpResponse} containing status code and body
     * @throws Exception if the request fails or parameters are invalid
     * @throws IllegalArgumentException if client is null or URL is empty
     */
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
	
	/**
     * Sends a GET request with optional query parameters and default client settings.
     * 
     * <p>Automatically appends query parameters if provided and configures the client
     * with redirect following and a 2-second connect timeout.</p>
     * 
     * @param url  the base URL
     * @param data optional query parameters to append to the URL
     * @return the response body as a String
     * @throws Exception if the request fails
     */
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

	/**
     * Sends a simple GET request with default settings.
     * 
     * we only care about the response.
     * 
     * @param url the target URL
     * @return the response body as a String
     * @throws Exception if the request fails
     */
	public static String getResponse(String url) throws Exception {
		return getResponse(url, null);
	}
	
	/**
     * Sends multiple GET requests concurrently using the provided executor.
     * 
     * @param urls             list of URLs to fetch
     * @param executorService  executor service for concurrent execution
     * @return array of {@link HttpResponse} objects
     * @throws Exception if any request fails
     */
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
	
	/**
     * Sends an asynchronous GET request with a specified timeout.
     * 
     * @param url     the target URL
     * @param timeout timeout in seconds
     * @return the response body as a String
     * @throws Exception if the request fails or times out
     */
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

	/**
     * Sends a POST request with form data.
     * 
     * @param url         the target URL
     * @param data        the form data to send
     * @param contentType the Content-Type header value (e.g. "application/x-www-form-urlencoded")
     * @return the {@link HttpResponse} containing status code and body
     * @throws Exception if the request fails
     */
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

	/**
     * Converts a map into a form-urlencoded body publisher.
     * 
     * @param data the data to encode
     * @return a {@link BodyPublisher} containing the encoded form data
     */
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

    /**
     * Builds a query string from a map of parameters.
     * 
     * @param params the parameters to encode
     * @return a properly encoded query string (without leading '?')
     * @throws UnsupportedEncodingException if encoding fails
     */
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
