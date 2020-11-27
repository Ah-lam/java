package com.dependencyinjection;

public class RateLimiter {
    private com.dependencyinjection.RedisCounter redisCounter;
    public RateLimiter(com.dependencyinjection.RedisCounter redisCounter) {
        this.redisCounter = redisCounter;
    }
    public boolean isValid() {
        this.redisCounter.increamentAndGet();
        return true;
    }
}
