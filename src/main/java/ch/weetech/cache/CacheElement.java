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
package ch.weetech.cache;

/**
 * Represents a basic key-value pair container used as an element within a cache.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class CacheElement<K, V> {
	/**
     * The key associated with this cache element.
     */
    private K key;
    
    /**
     * The value associated with this cache element.
     */
    private V value;

    /**
     * Constructs a new {@code CacheElement} with the specified key and value.
     *
     * @param key   the key to associate with this element
     * @param value the value to associate with this element
     */
    public CacheElement(K key, V value) {
        this.value = value;
        this.key = key;
    }

    /**
     * Returns the key of this cache element.
     *
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * Sets or updates the key of this cache element.
     *
     * @param key the new key to assign
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Returns the value of this cache element.
     *
     * @return the value
     */
    public V getValue() {
        return value;
    }

    /**
     * Sets or updates the value of this cache element.
     *
     * @param value the new value to assign
     */
    public void setValue(V value) {
        this.value = value;
    }
}
