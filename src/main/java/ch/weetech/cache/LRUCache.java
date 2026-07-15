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

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
* A thread-safe Least Recently Used (LRU) Cache implementation.
* 
* <p>This cache maintains a fixed maximum size and automatically evicts the least
* recently used item when the capacity is reached. It provides O(1) time complexity
* for both {@code get} and {@code put} operations through a combination of a
* {@link ConcurrentHashMap} and a custom {@link DoublyLinkedList}.</p>
* 
* <p>Thread-safety is achieved using a {@link ReentrantReadWriteLock}, allowing
* concurrent reads while ensuring exclusive writes.</p>
* 
* @param <K> the type of keys maintained by this cache
* @param <V> the type of mapped values
* @see Cache
*/
public class LRUCache<K, V> implements Cache<K, V> {

    private int size;
    private Map<K, LinkedListNode<CacheElement<K, V>>> linkedListNodeMap;
    private DoublyLinkedList<CacheElement<K, V>> doublyLinkedList;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Constructs a new LRUCache with the specified maximum capacity.
     * 
     * @param size the maximum number of entries this cache can hold
     */
    public LRUCache(int size) {
        this.size = size;
        this.linkedListNodeMap = new ConcurrentHashMap<>(size);
        this.doublyLinkedList = new DoublyLinkedList<>();
    }

    /**
     * Associates the specified value with the specified key in this cache.
     * If the key already exists, its value is updated and the entry is moved
     * to the front (most recently used).
     * 
     * <p>If the cache is full, the least recently used entry is evicted before
     * inserting the new entry.</p>
     * 
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return {@code true} if the operation was successful, {@code false} otherwise
     */
    @Override
    public boolean put(K key, V value) {
        this.lock.writeLock().lock();
        try {
            CacheElement<K, V> item = new CacheElement<K, V>(key, value);
            LinkedListNode<CacheElement<K, V>> newNode;
            if (this.linkedListNodeMap.containsKey(key)) {
                LinkedListNode<CacheElement<K, V>> node = this.linkedListNodeMap.get(key);
                newNode = doublyLinkedList.updateAndMoveToFront(node, item);
            } else {
                if (this.size() >= this.size) {
                    this.evictElement();
                }
                newNode = this.doublyLinkedList.add(item);
            }
            if (newNode.isEmpty()) {
                return false;
            }
            this.linkedListNodeMap.put(key, newNode);
            return true;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or empty if this
     * cache contains no mapping for the key.
     * 
     * <p>Accessing an entry via this method moves it to the front of the cache
     * (marking it as most recently used).</p>
     * 
     * @param key the key whose associated value is to be returned
     * @return an {@link Optional} containing the value, or empty if not present
     */
    @Override
    public Optional<V> get(K key) {
        this.lock.readLock().lock();
        try {
            LinkedListNode<CacheElement<K, V>> linkedListNode = this.linkedListNodeMap.get(key);
            if (linkedListNode != null && !linkedListNode.isEmpty()) {
                linkedListNodeMap.put(key, this.doublyLinkedList.moveToFront(linkedListNode));
                return Optional.of(linkedListNode.getElement().getValue());
            }
            return Optional.empty();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Returns the current number of entries in this cache.
     * 
     * @return the number of key-value mappings in this cache
     */
    @Override
    public int size() {
        this.lock.readLock().lock();
        try {
            return doublyLinkedList.size();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Returns {@code true} if this cache contains no entries.
     * 
     * @return {@code true} if this cache is empty
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Removes all entries from this cache.
     */
    @Override
    public void clear() {
        this.lock.writeLock().lock();
        try {
            linkedListNodeMap.clear();
            doublyLinkedList.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * Evicts the least recently used element from the cache.
     * 
     * @return {@code true} if an element was successfully evicted
     */
    private boolean evictElement() {
        this.lock.writeLock().lock();
        try {
            LinkedListNode<CacheElement<K, V>> linkedListNode = doublyLinkedList.removeTail();
            if (linkedListNode.isEmpty()) {
                return false;
            }
            linkedListNodeMap.remove(linkedListNode.getElement().getKey());
            return true;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

}
