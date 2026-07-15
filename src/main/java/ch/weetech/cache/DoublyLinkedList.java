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

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A thread-safe, generic doubly-linked list implementation optimized for concurrent operations.
 * <p>
 * This list employs a {@link ReentrantReadWriteLock} to protect structure modifications, allowing 
 * multiple concurrent readers while ensuring exclusive access for write operations. It uses a sentinel 
 * {@code DummyNode} to simplify edge cases at the boundaries of the list.
 * </p>
 *
 * @param <T> the type of elements stored in this linked list
 */
public class DoublyLinkedList<T> {

	/**
     * The sentinel node representing the logical end or empty state boundary of the list.
     */
    private DummyNode<T> dummyNode;

    /**
     * The first node in the doubly-linked list.
     */
    private LinkedListNode<T> head;

    /**
     * The last node in the doubly-linked list.
     */
    private LinkedListNode<T> tail;

    /**
     * An atomic counter tracking the total number of non-sentinel nodes in the list.
     */
    private AtomicInteger size;

    /**
     * Lock managing concurrent access to ensure thread-safety across operations.
     */
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Constructs an empty {@code DoublyLinkedList} instance initialized with a sentinel node.
     */
    public DoublyLinkedList() {
        this.dummyNode = new DummyNode<T>(this);
        clear();
    }

    /**
     * Removes all elements from this list, resetting the structure back to its empty configuration.
     */
    public void clear() {
        this.lock.writeLock().lock();
        try {
            head = dummyNode;
            tail = dummyNode;
            size = new AtomicInteger(0);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * Returns the number of elements contained within this linked list.
     *
     * @return the total number of elements
     */
    public int size() {
        this.lock.readLock().lock();
        try {
            return size.get();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Checks whether this linked list contains any data nodes.
     *
     * @return {@code true} if the list contains no elements, {@code false} otherwise
     */
    public boolean isEmpty() {
        this.lock.readLock().lock();
        try {
            return head.isEmpty();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Evaluates if a matching value is present anywhere within the list.
     *
     * @param value the target element value to locate
     * @return {@code true} if a node containing the value exists, {@code false} otherwise
     */
    public boolean contains(T value) {
        this.lock.readLock().lock();
        try {
            return search(value).hasElement();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Traverses the list searching for a node containing the specified value.
     *
     * @param value the target value to seek
     * @return the matching {@link LinkedListNode}, or an empty node/dummy boundary if not found
     */
    public LinkedListNode<T> search(T value) {
        this.lock.readLock().lock();
        try {
            return head.search(value);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Inserts a new value at the beginning (head) of this linked list.
     *
     * @param value the item value to add to the front
     * @return the newly created head node instance
     */
    public LinkedListNode<T> add(T value) {
        this.lock.writeLock().lock();
        try {
            head = new Node<T>(value, head, this);
            if (tail.isEmpty()) {
                tail = head;
            }
            size.incrementAndGet();
            return head;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * Appends a batch collection of values to the linked list sequentially.
     *
     * @param values the collection of elements to introduce into the list
     * @return {@code true} if all elements were added successfully, {@code false} if any insertion failed
     */
    public boolean addAll(Collection<T> values) {
        this.lock.writeLock().lock();
        try {
            for (T value : values) {
                if (add(value).isEmpty()) {
                    return false;
                }
            }
            return true;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * Searches for and unlinks the first node matching the specified target value.
     *
     * @param value the value to find and extract from the sequence
     * @return the unlinked node, or an empty node sequence if the value was not present
     */
    public LinkedListNode<T> remove(T value) {
        this.lock.writeLock().lock();
        try {
            LinkedListNode<T> linkedListNode = head.search(value);
            if (!linkedListNode.isEmpty()) {
                if (linkedListNode == tail) {
                    tail = tail.getPrev();
                }
                if (linkedListNode == head) {
                    head = head.getNext();
                }
                linkedListNode.detach();
                size.decrementAndGet();
            }
            return linkedListNode;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * Unlinks and removes the trailing element (tail node) currently at the end of the list.
     *
     * @return the removed tail node, or an empty node context if the list is already clear
     */
    public LinkedListNode<T> removeTail() {
        this.lock.writeLock().lock();
        try {
            LinkedListNode<T> oldTail = tail;
            if (oldTail == head) {
                tail = head = dummyNode;
            } else {
                tail = tail.getPrev();
                oldTail.detach();
            }
            if (!oldTail.isEmpty()) {
                size.decrementAndGet();
            }
            return oldTail;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * Moves an existing valid node directly to the front position of the list.
     *
     * @param node the structural node to move forward
     * @return the rearranged head node, or a dummy boundary if the source node was empty
     */
    public LinkedListNode<T> moveToFront(LinkedListNode<T> node) {
        return node.isEmpty() ? dummyNode : updateAndMoveToFront(node, node.getElement());
    }

    /**
     * Replaces a specific node's inner value and shifts its structural position to the front of the list.
     * Verify that the node belongs to this list instantiation before applying changes.
     *
     * @param node     the target node to modify and shift
     * @param newValue the new data element payload to apply to the front entry
     * @return the newly modified head entry node, or a dummy marker if the operation is rejected
     */
    public LinkedListNode<T> updateAndMoveToFront(LinkedListNode<T> node, T newValue) {
        this.lock.writeLock().lock();
        try {
            if (node.isEmpty() || (this != (node.getListReference()))) {
                return dummyNode;
            }
            detach(node);
            add(newValue);
            return head;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * Internal helper to disconnect a specific node from the linked context sequence 
     * and accurately update head, tail, and size boundaries.
     *
     * @param node the target structural node to disconnect
     */
    private void detach(LinkedListNode<T> node) {
        if (node != tail) {
            node.detach();
            if (node == head) {
                head = head.getNext();
            }
            size.decrementAndGet();
        } else {
            removeTail();
        }
    }
}
