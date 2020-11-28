package com.eventbus.impl;


import com.google.common.base.Preconditions;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ObserverRegistry {
    // Map: 消息类型 -> 可处理的该消息类型的所有的观察者方法列表
    private ConcurrentMap<Class<?>, CopyOnWriteArraySet<ObserverAction>> registry = new ConcurrentHashMap<>();

    public void register(Object observer) {

        // 获得当前observer中所有待处理的消息类型，以及对应待处理该消息类型的方法列表
        Map<Class<?>, Collection<ObserverAction>> observerActions = findAllObserverActions(observer);

        // 将当前observer获得的map，合并到所有注册的map registry中
        for (Map.Entry<Class<?>, Collection<ObserverAction>> entry : observerActions.entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<ObserverAction> eventActions = entry.getValue();
            CopyOnWriteArraySet<ObserverAction> registeredEventActions = registry.get(eventType);
            if (registeredEventActions == null) {
                registry.putIfAbsent(eventType, new CopyOnWriteArraySet<>());
                registeredEventActions = registry.get(eventType);
            }
            registeredEventActions.addAll(eventActions);
        }
    }

    // getMatchedObserverActions 用于获取该event 可接收消息的函数列表
    // 这里不能是直接从registry map中get，其原因是我们还需要支持该event类型的父类对应的处理函数列表
    public List<ObserverAction> getMatchedObserverActions(Object event) {
        List<ObserverAction> matchedObservers = new ArrayList<>();
        Class<?> postedEventType = event.getClass();
        for (Map.Entry<Class<?>, CopyOnWriteArraySet<ObserverAction>> entry : registry.entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<ObserverAction> eventActions = entry.getValue();
            // isAssignableFrom 判断类或接口是否相同，或是否是其超类或超接口
            if (postedEventType.isAssignableFrom(eventType)) {
                matchedObservers.addAll(eventActions);
            }
        }
        return matchedObservers;
    }

    private Map<Class<?>, Collection<ObserverAction>> findAllObserverActions(Object observer) {
        // Map: 消息类型 -> 可处理的该消息类型的所有的观察者方法列表
        Map<Class<?>, Collection<ObserverAction>> observerActions = new HashMap<>();

        // 获取当前observer的class类型
        Class<?> clazz = observer.getClass();
        // 寻找当前observer中所有带有@Subscribe注解的方法，并循环
        for (Method method : getAnnotatedMethods(clazz)) {
            // 方法的参数，就是该方法可以接收的消息类型
            Class<?>[] parameterTypes = method.getParameterTypes();
            // 固定只有一个参数
            Class<?> eventType = parameterTypes[0];
            if (!observerActions.containsKey(eventType)) {
                observerActions.put(eventType, new ArrayList<>());
            }
            observerActions.get(eventType).add(new ObserverAction(observer, method));
        }

        return observerActions;
    }

    // 这个函数就是获取到当前clazz中，所有带有注解@Subscribe的方法
    private List<Method> getAnnotatedMethods(Class<?> clazz) {
        List<Method> annotatedMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                // 保证被注解的方法只有一个参数
                Preconditions.checkArgument(parameterTypes.length == 1,
                        "Method %s has @Subscribe annotation but has %s parameters."
                                + "Subscriber methods must have exactly 1 parameter.",
                        method, parameterTypes.length);

                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }
}
