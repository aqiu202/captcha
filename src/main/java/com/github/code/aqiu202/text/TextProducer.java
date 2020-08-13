package com.github.code.aqiu202.text;


/**
 * {@link TextProducer} is responsible for creating text.
 */
public interface TextProducer {

    String getText();

    String getText(int length);
}