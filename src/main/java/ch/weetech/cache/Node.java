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

public class Node<T> implements LinkedListNode<T> {

    private T value;
    private DoublyLinkedList<T> list;
    private LinkedListNode<T> next;
    private LinkedListNode<T> prev;

    public Node(T value, LinkedListNode<T> next, DoublyLinkedList<T> list) {
        this.value = value;
        this.next = next;
        this.setPrev(next.getPrev());
        this.prev.setNext(this);
        this.next.setPrev(this);
        this.list = list;
    }

    @Override
    public boolean hasElement() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public T getElement() throws NullPointerException {
        return value;
    }

    @Override
    public void detach() {
        this.prev.setNext(this.getNext());
        this.next.setPrev(this.getPrev());
    }

    @Override
    public DoublyLinkedList<T> getListReference() {
        return this.list;
    }

    @Override
    public LinkedListNode<T> setPrev(LinkedListNode<T> prev) {
        this.prev = prev;
        return this;
    }

    @Override
    public LinkedListNode<T> setNext(LinkedListNode<T> next) {
        this.next = next;
        return this;
    }

    @Override
    public LinkedListNode<T> getPrev() {
        return this.prev;
    }

    @Override
    public LinkedListNode<T> getNext() {
        return this.next;
    }

    @Override
    public LinkedListNode<T> search(T value) {
        return this.getElement() == value ? this : this.getNext().search(value);
    }

}
