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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

public class HttpClientApp {

    private HttpClient httpClient = null;

    public HttpClientApp(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpClientApp(int connectTimeout) {
        httpClient = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(connectTimeout))
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    public HttpResponse get(String url, String userAgent, int readTimeout, int retriesCount) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(readTimeout))
                .setHeader("User-Agent", userAgent)
                .build();

        java.net.http.HttpResponse<String> response;
        HttpResponse httpResponse = new HttpResponse();
        int tries = retriesCount;

        do {
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

    public HttpResponse get(String url) {
        return get(url, "HttpClientApp", 5, 0);
    }

    // TODO IMPLEMENT ME
    public HttpResponse post(String url) {
        return null;
    }



}
