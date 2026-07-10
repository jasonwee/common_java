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
 * Represents a node contract within a structural {@link DoublyLinkedList}.
 * <p>
 * This abstraction outlines structural behaviors required for elements to properly 
 * link, decouple, navigate, and search sequentially across both concrete data-carrying nodes 
 * and terminal sentinel nodes.
 * </p>
 *
 * @param <V> the type of value payload held by elements of this node sequence
 */
public interface LinkedListNode<V> {
	
	/**
     * Determines whether this node encapsulates an active, non-null value payload element.
     *
     * @return {@code true} if a value element is available, {@code false} if this node represents a boundary marker
     */
    boolean hasElement();

    /**
     * Identifies if this node corresponds to an empty structural state or a sentinel edge boundary.
     *
     * @return {@code true} if the node contains no usable value payload, {@code false} otherwise
     */
    boolean isEmpty();

    /**
     * Retrieves the structural payload data element currently wrapped within this node context.
     *
     * @return the value payload element instances
     * @throws NullPointerException if the underlying implementation is an empty element node or sentinel boundary
     */
    V getElement() throws NullPointerException;

    /**
     * Detaches this specific node context from its preceding and succeeding adjacent neighbors, 
     * cleanly bridging the surrounding structural chain gaps together.
     */
    void detach();

    /**
     * Obtains the managing core structural container reference tracking this node placement point.
     *
     * @return the parent {@link DoublyLinkedList} instance pointer tracking this element context
     */
    DoublyLinkedList<V> getListReference();

    /**
     * Sets the structural predecessor link pointer located prior to this node sequence point.
     *
     * @param prev the upcoming node context target designated to sit before this instance
     * @return the modified node instance configuration or the provided input reference
     */
    LinkedListNode<V> setPrev(LinkedListNode<V> prev);

    /**
     * Sets the structural successor link pointer located directly behind this node sequence point.
     *
     * @param next the upcoming node context target designated to sit after this instance
     * @return the modified node instance configuration or the provided input reference
     */
    LinkedListNode<V> setNext(LinkedListNode<V> next);

    /**
     * Navigates backward to return the node instance positioned immediately prior to this step sequence.
     *
     * @return the preceding {@code LinkedListNode} context instance link
     */
    LinkedListNode<V> getPrev();

    /**
     * Navigates forward to return the node instance positioned immediately after this step sequence.
     *
     * @return the succeeding {@code LinkedListNode} context instance link
     */
    LinkedListNode<V> getNext();

    /**
     * Searches recursively or sequentially starting from this node tracking step onward for a matching target value.
     *
     * @param value the target data element value match criteria to scan downstream for
     * @return the matching {@code LinkedListNode} container holding that value, or an empty node context boundary if absent
     */
    LinkedListNode<V> search(V value);
}
