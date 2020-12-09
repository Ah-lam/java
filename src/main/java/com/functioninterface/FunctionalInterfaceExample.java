package com.functioninterface;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FunctionalInterfaceExample {

    public static void main( String[] args ) {

        AddInterface<Integer> addInt = (Integer a, Integer b) -> a + b;
        AddInterface<Double> addDouble = (Double a, Double b) -> a + b;

        log.info(addInt.add(1, 2).toString());
        log.info(addDouble.add(1.1d, 2.2d).toString());
    }
}
