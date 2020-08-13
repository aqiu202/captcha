package com.github.code.aqiu202.text;

public class StringWrapper {

    private StringWrapper() {
    }

    public static StringWrapper newInstance() {
        return new StringWrapper();
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
