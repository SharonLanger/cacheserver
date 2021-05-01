package com.example.cacheserver.utils;

public class CachedException extends Exception {
    public CachedExceptionsType cachedExceptionsType;

    public CachedException(String msg) {
        super(msg);
        cachedExceptionsType = CachedExceptionsType.Default;
    }

    public CachedException(String msg, CachedExceptionsType exceptionsType) {
        super(msg);
        cachedExceptionsType = exceptionsType;
    }
}
