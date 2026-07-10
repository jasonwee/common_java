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

/**
 * Represents an HTTP response returned from an HTTP request.
 * 
 * <p>This class encapsulates the essential components of an HTTP response:
 * the status code, the response body content, and any error message if the request failed.</p>
 * 
 */
@SuppressWarnings("doclint:missing")
public class HttpResponse {
	
	/** The HTTP status code (e.g., 200, 404, 500) */
    private int statusCode;
    /** The response body content (usually JSON, HTML, or plain text) */
    private String content;
    /** Error message in case the request failed or an exception occurred */
    private String error;

    /**
     * Returns the HTTP status code of the response.
     * 
     * @return the status code (e.g., 200 for OK, 404 for Not Found)
     */
    public int getStatusCode() {
        return statusCode;
    }
    /**
     * Sets the HTTP status code.
     * 
     * @param statusCode the HTTP status code to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    /**
     * Returns the content/body of the HTTP response.
     * 
     * @return the response content as a String
     */
    public String getContent() {
        return content;
    }
    /**
     * Sets the content/body of the HTTP response.
     * 
     * @param content the response content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * Returns the error message if the HTTP request failed.
     * 
     * @return the error message, or {@code null} if no error occurred
     */
    public String getError() {
        return error;
    }
    /**
     * Sets the error message for a failed HTTP request.
     * 
     * @param error the error message to set
     */
    public void setError(String error) {
        this.error = error;
    }
}
