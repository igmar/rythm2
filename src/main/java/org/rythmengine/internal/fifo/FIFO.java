package org.rythmengine.internal.fifo;

public interface FIFO<T> {
    void push(T t);
    T pop();
    T pop(int idx);
    T peek();
    T peek(int idx);
    int size();
    void clear();
}
