package com.github.code.aqiu202.background.impl;

import com.github.code.aqiu202.background.BackgroundProducer;
import com.github.code.aqiu202.props.CaptchaProperties.BackgroundProperties;
import com.github.code.aqiu202.util.ColorUtils;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;

/**
 * Default implementation of {@link BackgroundProducer}, adds a gradient
 * background to an image. The gradient color is diagonal and made of Color From
 * (top left) and Color To (bottom right).
 */
public class DefaultBackgroundProducer implements BackgroundProducer {

    private Color from;
    private Color to;

    private int rgbStart = ColorUtils.COLOR_RGB_LIGHT_START;
    private int rgbEnd = ColorUtils.COLOR_RGB_END;

    public DefaultBackgroundProducer(Color color) {
        this.from = this.to = color;
    }

    public DefaultBackgroundProducer(@Nonnull BackgroundProperties properties) {
        Integer colorFrom = properties.getColorFrom();
        if (colorFrom != null) {
            this.from = new Color(colorFrom);
        }
        Integer colorTo = properties.getColorTo();
        if (colorTo != null) {
            this.to = new Color(colorTo);
        }
        Integer start = properties.getRgbStart();
        if (start != null) {
            this.rgbStart = start;
        }
        Integer end = properties.getRgbEnd();
        if (end != null) {
            this.rgbEnd = end;
        }
    }

    public DefaultBackgroundProducer(Color from, Color to) {
        this.from = from;
        this.to = to;
    }

    public DefaultBackgroundProducer() {
    }

    public DefaultBackgroundProducer setRgbStart(int rgbStart) {
        this.rgbStart = rgbStart;
        return this;
    }

    public DefaultBackgroundProducer setRgbEnd(int rgbEnd) {
        this.rgbEnd = rgbEnd;
        return this;
    }

    private void prepareColor() {
        if (this.from == null) {
            this.from = ColorUtils.randomColor(this.rgbStart, this.rgbEnd);
        }
        if (this.to == null) {
            this.to = ColorUtils.randomColor(this.rgbStart, this.rgbEnd);
        }
    }

    /**
     * @param baseImage the base image
     * @return an image with a gradient background added to the base image.
     */
    public BufferedImage addBackground(BufferedImage baseImage) {
        this.prepareColor();
        int width = baseImage.getWidth();
        int height = baseImage.getHeight();

        // create an opaque image
        BufferedImage imageWithBackground = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D graph = imageWithBackground.createGraphics();
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

        hints.add(new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY));
        hints.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));

        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));

        graph.setRenderingHints(hints);

        GradientPaint paint = new GradientPaint(0, 0, this.from, width, height,
                this.to);
        graph.setPaint(paint);
        graph.fill(new Rectangle2D.Double(0, 0, width, height));
        // draw the transparent image over the background
        graph.drawImage(baseImage, 0, 0, null);
        graph.dispose();
        return imageWithBackground;
    }
}