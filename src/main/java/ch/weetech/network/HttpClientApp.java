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
package ch.weetech.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper class around Java's {@link java.net.http.HttpClient} that simplifies
 * making HTTP GET and POST requests with built-in retry logic and consistent error handling.
 * 
 * <p>This class provides convenient methods for sending HTTP requests and returns
 * a standardized {@link HttpResponse} object containing the status code, response body,
 * and error details if applicable.</p>
 * 
 */
public class HttpClientApp {

    private HttpClient httpClient = null;

    /**
     * Constructs an HttpClientApp with a pre-configured {@link HttpClient}.
     * 
     * @param httpClient the HttpClient instance to use
     */
    public HttpClientApp(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Constructs an HttpClientApp with a new HttpClient configured with the specified
     * connect timeout and HTTP/2 version preference.
     * 
     * @param connectTimeout the connect timeout in seconds
     */
    public HttpClientApp(int connectTimeout) {
        httpClient = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(connectTimeout))
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    /**
     * Sends a GET request to the specified URL with retry capability.
     * 
     * @param url           the target URL
     * @param userAgent     the User-Agent header value
     * @param readTimeout   the read timeout in seconds
     * @param retriesCount  number of retry attempts if the request fails due to timeout or I/O errors
     * @return an {@link HttpResponse} containing the result of the request
     */
    public HttpResponse get(String url, String userAgent, int readTimeout, int retriesCount) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(readTimeout))
                .setHeader("User-Agent", userAgent)
                .build();

        HttpResponse httpResponse = new HttpResponse();
        int tries = retriesCount;

        do {
            try {
                java.net.http.HttpResponse<String> response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
                httpResponse.setStatusCode(response.statusCode());
                httpResponse.setContent(response.body());
                break;
            } catch (HttpConnectTimeoutException e) {
                httpResponse.setStatusCode(HttpResponseCode.CLIENT_CONNECT_TIMEOUT);
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                httpResponse.setError(sw.toString());
            } catch (HttpTimeoutException e) {
                httpResponse.setStatusCode(HttpResponseCode.CLIENT_READ_TIMEOUT);
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                httpResponse.setError(sw.toString());
            } catch (IOException e) {
                httpResponse.setStatusCode(HttpResponseCode.CLIENT_IO_ERROR);
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                httpResponse.setError(sw.toString());
            } catch (InterruptedException e) {
                httpResponse.setStatusCode(HttpResponseCode.CLIENT_INTERRUPTED_EXCEPTION);
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                httpResponse.setError(sw.toString());
            }
            tries--;
        } while (tries > 0);

        return httpResponse;
    }

    /**
     * Sends a simple GET request using default settings.
     * 
     * <p>Uses default values: User-Agent = "HttpClientApp", read timeout = 5 seconds, no retries.</p>
     * 
     * @param url the target URL
     * @return an {@link HttpResponse} containing the result of the request
     */
    public HttpResponse get(String url) {
        return get(url, "HttpClientApp", 5, 0);
    }

    /**
     * Sends a POST request with form data and custom headers with retry capability.
     * 
     * @param url           the target URL
     * @param data          the form data to send (key-value pairs). Can be null.
     * @param headers       additional HTTP headers to include in the request
     * @param userAgent     the User-Agent header value
     * @param readTimeout   the read timeout in seconds
     * @param retriesCount  number of retry attempts on failure
     * @return an {@link HttpResponse} containing the result of the request
     */
    public HttpResponse post(String url, Map<Object, Object> data, Map<String, String> headers, String userAgent, int readTimeout, int retriesCount) {

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        requestBuilder.uri(URI.create(url));
        requestBuilder.timeout(Duration.ofSeconds(readTimeout));
        requestBuilder.setHeader("User-Agent", userAgent);

        for (var entry : headers.entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue());
        }

        if (data != null) {
            requestBuilder.POST(HttpUtil.buildFormDataFromMap(data));
        } else {
            requestBuilder.POST(HttpRequest.BodyPublishers.noBody());
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse httpResponse = new HttpResponse();
        int tries = retriesCount;

        do {
            java.net.http.HttpResponse<String> response;
            try {
                response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
                httpResponse.setStatusCode(response.statusCode());
                httpResponse.setContent(response.body());
                break;
            } catch (HttpConnectTimeoutException e) {
                httpResponse.setStatusCode(HttpResponseCode.CLIENT_CONNECT_TIMEOUT);
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                httpResponse.setError(sw.toString());
            } catch (HttpTimeoutException e) {
                httpResponse.setStatusCode(HttpResponseCode.CLIENT_READ_TIMEOUT);
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                httpResponse.setError(sw.toString());
            } catch (IOException e) {
                httpResponse.setStatusCode(HttpResponseCode.CLIENT_IO_ERROR);
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                httpResponse.setError(sw.toString());
            } catch (InterruptedException e) {
                httpResponse.setStatusCode(HttpResponseCode.CLIENT_INTERRUPTED_EXCEPTION);
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                httpResponse.setError(sw.toString());
            }
            tries--;
        } while (tries > 0);

        return httpResponse;
    }

    /**
     * Sends a simple POST request with form data using default settings.
     * 
     * <p>Uses default values: Content-Type = application/x-www-form-urlencoded,
     * User-Agent = "HttpClientApp", read timeout = 5 seconds, no retries.</p>
     * 
     * @param url  the target URL
     * @param data the form data to send
     * @return an {@link HttpResponse} containing the result of the request
     */
    public HttpResponse post(String url, Map<Object, Object> data) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return post(url, data, headers, "HttpClientApp", 5, 0);
    }

}
