package com.yeyanxiang.util.entry;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Entry<T> {
    private String key;
    private T value;
    private Type type;

    public Entry(String key, T value) {
        super();
        this.key = key;
        this.value = value;
    }

    public Entry(String key, T value, Type type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public static enum Type {
        CLASS, PACKAGE, ACTION
    }
}
