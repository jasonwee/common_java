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

/**
 * A sentinel node implementation of {@link LinkedListNode} representing an empty or boundary state.
 * <p>
 * This class applies the Null Object Pattern to eliminate null-checks within the 
 * {@link DoublyLinkedList} logic. It throws exceptions for operations that access 
 * data payload values and returns self references or performs no-ops for structural transitions.
 * </p>
 *
 * @param <T> the type of elements stored in the linked list structure
 */
public class DummyNode<T> implements LinkedListNode<T> {

	/**
     * The structural reference back to the owner doubly-linked list instance.
     */
    private DoublyLinkedList<T> list;

    /**
     * Constructs a new sentinel {@code DummyNode} bound to its parent doubly-linked list.
     *
     * @param list the managing {@link DoublyLinkedList} instance
     */
    public DummyNode(DoublyLinkedList<T> list) {
        this.list = list;
    }

    /**
     * Indicates whether this node wraps a valid data element payload.
     *
     * @return always {@code false} as a sentinel node never wraps active data
     */
    @Override
    public boolean hasElement() {
        return false;
    }

    /**
     * Evaluates if this node acts as an empty structural container.
     *
     * @return always {@code true}
     */
    @Override
    public boolean isEmpty() {
        return true;
    }

    /**
     * Retrieves the inner data element payload assigned to this node wrapper.
     *
     * @return none because this operation is invalid on a sentinel node
     * @throws NullPointerException always, indicating an attempt to fetch data from an empty node boundary
     */
    @Override
    public T getElement() throws NullPointerException {
        throw new NullPointerException();
    }

    /**
     * Disconnects this node from its surrounding list sequence elements.
     * This operation acts as a safe no-op on a sentinel marker.
     */
    @Override
    public void detach() {
        return;
    }

    /**
     * Returns the parent data structure reference managing this node.
     *
     * @return the managing {@link DoublyLinkedList} reference configuration
     */
    @Override
    public DoublyLinkedList<T> getListReference() {
        return list;
    }

    /**
     * Assigns the preceding node placement sequence tracking point.
     *
     * @param prev the upcoming fallback sequence node reference point
     * @return the provided node reference point without modifying internal state
     */
    @Override
    public LinkedListNode<T> setPrev(LinkedListNode<T> prev) {
        return prev;
    }

    /**
     * Assigns the succeeding node placement sequence tracking point.
     *
     * @param next the upcoming forward sequence node reference point
     * @return the provided node reference point without modifying internal state
     */
    @Override
    public LinkedListNode<T> setNext(LinkedListNode<T> next) {
        return next;
    }

    /**
     * Retrieves the node located directly before this element placement step.
     *
     * @return a self reference pointer indicating a structural terminal boundary
     */
    @Override
    public LinkedListNode<T> getPrev() {
        return this;
    }

    /**
     * Retrieves the node located directly after this element placement step.
     *
     * @return a self reference pointer indicating a structural terminal boundary
     */
    @Override
    public LinkedListNode<T> getNext() {
        return this;
    }

    /**
     * Evaluates and searches downstream entries looking for a specific target match.
     *
     * @param value the data payload target value to seek out
     * @return a self reference pointer indicating the value could not be found within the structure
     */
    @Override
    public LinkedListNode<T> search(T value) {
        return this;
    }

}
