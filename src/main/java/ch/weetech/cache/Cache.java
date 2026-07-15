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
package ch.weetech.cache;

import java.util.Optional;

/**
 * A key-value caching interface for temporary data storage and retrieval.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public interface Cache<K, V> {

	/**
     * Associates the specified value with the specified key in this cache.
     * If the cache previously contained a mapping for the key, the old value
     * is replaced by the specified value.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return {@code true} if the operation succeeded, {@code false} otherwise
     * @throws NullPointerException if the specified key or value is null
     *                              (depending on the implementation)
     */
    boolean put(K key, V value);

    /**
     * Returns an {@link Optional} containing the value to which the specified key is mapped,
     * or an empty {@code Optional} if this cache contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return an {@code Optional} containing the value, or empty if not found
     * @throws NullPointerException if the specified key is null 
     *                              (depending on the implementation)
     */
    Optional<V> get(K key);

    /**
     * Returns the number of key-value mappings in this cache.
     *
     * @return the number of key-value mappings in this cache
     */
    int size();

    /**
     * Returns {@code true} if this cache contains no key-value mappings.
     *
     * @return {@code true} if this cache contains no key-value mappings
     */
    boolean isEmpty();

    /**
     * Removes all of the mappings from this cache.
     * The cache will be empty after this call returns.
     */
    void clear();

}
