package com.dependencyinjection;

public interface ApplicationContext {
    Object getBean(String beanId);
}
