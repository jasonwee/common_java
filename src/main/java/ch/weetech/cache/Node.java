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
 * A concrete implementation of a node in a doubly linked list.
 * 
 * <p>This class represents a standard non-empty node containing an element and
 * references to its previous and next nodes. It is used internally by
 * {@link DoublyLinkedList} to maintain the list structure.</p>
 * 
 * @param <T> the type of element stored in this node
 * @see DoublyLinkedList
 * @see LinkedListNode
 * @see DummyNode
 */
public class Node<T> implements LinkedListNode<T> {

    private T value;
    private DoublyLinkedList<T> list;
    private LinkedListNode<T> next;
    private LinkedListNode<T> prev;

    /**
     * Creates a new node and inserts it into the doubly linked list.
     * 
     * <p>The node is inserted before the given {@code next} node, properly
     * updating the links of the surrounding nodes.</p>
     * 
     * @param value the element to store in this node
     * @param next  the node that will become the next node after this one
     * @param list  reference to the parent {@link DoublyLinkedList}
     */
    public Node(T value, LinkedListNode<T> next, DoublyLinkedList<T> list) {
        this.value = value;
        this.next = next;
        this.setPrev(next.getPrev());
        this.prev.setNext(this);
        this.next.setPrev(this);
        this.list = list;
    }

    /**
    * {@inheritDoc}
    * 
    * @return always {@code true} for {@link Node}
    */
    @Override
    public boolean hasElement() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @return always {@code false} for {@link Node}
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @return the value stored in this node
     * @throws NullPointerException if the value is null (though usually avoided)
     */
    @Override
    public T getElement() throws NullPointerException {
        return value;
    }

    /**
     * Removes this node from the linked list by updating its previous and next nodes.
     */
    @Override
    public void detach() {
        this.prev.setNext(this.getNext());
        this.next.setPrev(this.getPrev());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DoublyLinkedList<T> getListReference() {
        return this.list;
    }

    /**
     * Sets the previous node and returns this node for method chaining.
     */    
    @Override
    public LinkedListNode<T> setPrev(LinkedListNode<T> prev) {
        this.prev = prev;
        return this;
    }

    /**
     * Sets the next node and returns this node for method chaining.
     */
    @Override
    public LinkedListNode<T> setNext(LinkedListNode<T> next) {
        this.next = next;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedListNode<T> getPrev() {
        return this.prev;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedListNode<T> getNext() {
        return this.next;
    }

    /**
     * Searches for a node containing the specified value starting from this node.
     * 
     * @param value the value to search for
     * @return this node if it contains the value, otherwise continues searching
     *         in the next nodes
     */
    @Override
    public LinkedListNode<T> search(T value) {
        return this.getElement() == value ? this : this.getNext().search(value);
    }

}
