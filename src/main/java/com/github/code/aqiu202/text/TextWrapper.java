package com.github.code.aqiu202.text;

public class TextWrapper {

    private TextWrapper() {
    }

    public static TextWrapper newInstance() {
        return new TextWrapper();
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
