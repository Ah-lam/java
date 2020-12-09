package com.functioninterface;

@FunctionalInterface
interface AddInterface<T> {
    T add(T a, T b);
}
