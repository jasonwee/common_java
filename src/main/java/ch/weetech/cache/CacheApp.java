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
 * 
 *       https://explainjava.com/simple-in-memory-cache-java/
 *       https://www.alibabacloud.com/blog/java-high-performance-local-cache-practices_599804
 *       https://commons.apache.org/proper/commons-jcs/
 * done https://dzone.com/refcardz/java-caching
 * done https://crunchify.com/how-to-create-a-simple-in-memory-cache-in-java-lightweight-cache/
 * done https://stackoverflow.com/questions/575685/looking-for-simple-java-in-memory-cache
 * done https://medium.com/analytics-vidhya/how-to-implement-cache-in-java-d9aa5e9577f2
 * done https://developers.google.com/apps-script/reference/cache 
 */
public class CacheApp {
	
	/**
	 * default constructor
	 */
	public CacheApp() {
		
	}

	/**
	 * https://www.baeldung.com/java-lru-cache
	 * 
	 * Creates and returns a new instance of an LRU (Least Recently Used) cache with the specified capacity.
     * @param <K>  the type of keys maintained by the cache
     * @param <V>  the type of mapped values
     * @param size the maximum number of elements the cache can hold before eviction occurs
     * @return a new {@link LRUCache} instance configured with the given size bound
     * @throws IllegalArgumentException if the specified size is less than or equal to zero
	 */
    public static <K, V> LRUCache<K, V> getLRUCache(int size) {
        return new LRUCache<K, V>(size);
    }

}
