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

import java.util.List;
import java.util.Map;

/**
 * Represents the result of a database operation or query execution.
 * 
 * <p>This class encapsulates the outcome of an operation, including whether it was
 * successful and the list of resulting data rows (each row represented as a map
 * of column name to value).</p>
 * 
 */
@SuppressWarnings("doclint:missing")
public class Result {

	/** Indicates whether the operation completed successfully */
    private boolean isSuccess;

    /**
     * List of result rows, where each row is represented as a {@link Map}
     * with column names as keys and their corresponding values as strings.
     */
    private List<Map<String, String>> resultsList;

    /**
     * Returns whether the operation was successful.
     * 
     * @return {@code true} if the operation succeeded, {@code false} otherwise
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Sets the success status of the operation.
     * 
     * @param isSuccess {@code true} if the operation succeeded
     */
    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * Returns the list of result rows.
     * 
     * <p>Each {@link Map} in the list represents one row, with column names as keys
     * and string representations of the values.</p>
     * 
     * @return list of result rows, or {@code null} if no results or operation failed
     */
    public List<Map<String, String>> getResultsList() {
        return resultsList;
    }

    /**
     * Sets the list of result rows.
     * 
     * @param resultsList the list of result rows to set
     */
    public void setResultsList(List<Map<String, String>> resultsList) {
        this.resultsList = resultsList;
    }

}
