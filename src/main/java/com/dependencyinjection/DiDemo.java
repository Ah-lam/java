package com.dependencyinjection;

public class DiDemo {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new com.dependencyinjection.ClassPathXmlApplicationContext("beans.xml");
        RateLimiter rateLimiter = (RateLimiter) applicationContext.getBean("rateLimiter");
        Boolean isValid = rateLimiter.isValid();
        System.out.println("RateLimiter call isValid method, result: " + isValid);
    }
}
