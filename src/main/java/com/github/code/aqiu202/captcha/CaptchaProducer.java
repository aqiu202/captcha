package com.github.code.aqiu202.captcha;

import com.github.code.aqiu202.text.StringWrapper;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;

/**
 * Responsible for creating captcha image with a text drawn on it.
 */
public interface CaptchaProducer {

    BufferedImage createImage(@Nonnull StringWrapper stringWrapper);

    BufferedImage createImage(@Nonnull StringWrapper stringWrapper, int width, int height);

    /**
     * Create an image which will have written a distorted text.
     *
     * @param text
     *            the distorted characters
     * @return image with the text
     */
    BufferedImage createImage(String text);

    BufferedImage createImage(String text, int width, int height);

    /**
     * @return the text to be drawn
     */
    String createText();

    void writeToResponse(String text, int width, int height, HttpServletResponse response)
            throws IOException;

    void writeToResponse(String text, HttpServletResponse response) throws IOException;

    String writeToResponse(HttpServletResponse response) throws IOException;

    void writeToResponse(StringWrapper stringWrapper, int width, int height,
            HttpServletResponse response) throws IOException;

    void writeToResponse(StringWrapper stringWrapper, HttpServletResponse response)
            throws IOException;

    void writeToResponse(BufferedImage image, HttpServletResponse response) throws IOException;
}