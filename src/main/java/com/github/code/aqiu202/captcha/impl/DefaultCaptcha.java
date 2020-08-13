package com.github.code.aqiu202.captcha.impl;

import com.github.code.aqiu202.background.BackgroundProducer;
import com.github.code.aqiu202.background.impl.DefaultBackgroundProducer;
import com.github.code.aqiu202.border.BorderProducer;
import com.github.code.aqiu202.border.impl.DefaultBorderProducer;
import com.github.code.aqiu202.captcha.CaptchaProducer;
import com.github.code.aqiu202.noise.NoiseProducer;
import com.github.code.aqiu202.noise.OrderedNoiseProducer;
import com.github.code.aqiu202.noise.impl.CombiningNoiseProducer;
import com.github.code.aqiu202.noise.impl.LineNoise;
import com.github.code.aqiu202.noise.impl.ShadowNoise;
import com.github.code.aqiu202.noise.impl.ShearNoise;
import com.github.code.aqiu202.props.CaptchaProperties;
import com.github.code.aqiu202.props.CaptchaProperties.TextProperties;
import com.github.code.aqiu202.text.StringWrapper;
import com.github.code.aqiu202.text.TextProducer;
import com.github.code.aqiu202.text.WordRenderer;
import com.github.code.aqiu202.text.impl.DefaultTextCreator;
import com.github.code.aqiu202.text.impl.DefaultWordRenderer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * Default {@link CaptchaProducer} implementation which draws a captcha image using
 * {@link WordRenderer}, {@link NoiseProducer}, {@link BackgroundProducer}.
 * Text creation uses {@link TextProducer}.
 */
public class DefaultCaptcha implements CaptchaProducer {

    private int width = WordRenderer.DEFAULT_WIDTH;
    private int height = WordRenderer.DEFAULT_HEIGHT;

    public DefaultCaptcha(CaptchaProperties properties) {
        TextProperties text = properties.getText();
        this.textProducer = new DefaultTextCreator(text.getWord());
        this.wordRenderer = new DefaultWordRenderer(text.getRender());
        this.backgroundProducer = new DefaultBackgroundProducer(properties.getBackground());
        this.noiseProducer = new CombiningNoiseProducer(properties.getNoise());
        this.hasBorder = properties.isHasBorder();
        this.borderProducer = new DefaultBorderProducer(properties.getBorder());
        this.width = properties.getWidth();
        this.height = properties.getHeight();
    }

    public DefaultCaptcha() {
        this.noiseProducer = new CombiningNoiseProducer(
                new LineNoise(),
                new ShearNoise(),
                new ShadowNoise());
    }

    private boolean hasBorder = true;

    private TextProducer textProducer = new DefaultTextCreator();
    private WordRenderer wordRenderer = new DefaultWordRenderer();
    private BorderProducer borderProducer = new DefaultBorderProducer();
    private BackgroundProducer backgroundProducer = new DefaultBackgroundProducer();
    private OrderedNoiseProducer noiseProducer;

    @Override
    public BufferedImage createImage(String text, int width, int height) {
        BufferedImage bi = this.wordRenderer.renderWord(text, width, height);
        bi = this.noiseProducer.makeNoiseBeforeAddBackground(bi);
        bi = this.backgroundProducer.addBackground(bi);
        bi = this.noiseProducer.makeNoiseAfterAddBackground(bi);
        if (hasBorder) {
            this.borderProducer.drawBorder(bi);
        }
        return bi;
    }

    /**
     * Create an image which will have written a distorted text.
     *
     * @param text
     *            the distorted characters
     * @return image with the text
     */
    public BufferedImage createImage(String text) {
        return this.createImage(text, this.width, this.height);
    }

    /**
     * @return the text to be drawn
     */
    public String createText() {
        return this.textProducer.getText();
    }

    @Override
    public BufferedImage createImage(StringWrapper stringWrapper, int width, int height) {
        String text = this.createText();
        stringWrapper.setValue(text);
        return this.createImage(text, width, height);
    }

    @Override
    public BufferedImage createImage(StringWrapper stringWrapper) {
        String text = this.createText();
        stringWrapper.setValue(text);
        return this.createImage(text);
    }

    @Override
    public void writeToResponse(String text, int width, int height, HttpServletResponse response)
            throws IOException {
        BufferedImage image = this.createImage(text, width, height);
        this.writeToResponse(image, response);
    }

    @Override
    public void writeToResponse(String text, HttpServletResponse response) throws IOException {
        this.writeToResponse(text, this.width, this.height, response);
    }

    @Override
    public String writeToResponse(HttpServletResponse response) throws IOException {
        String text = this.createText();
        this.writeToResponse(text, response);
        return text;
    }

    @Override
    public void writeToResponse(StringWrapper stringWrapper, int width, int height,
            HttpServletResponse response) throws IOException {
        BufferedImage image = this.createImage(stringWrapper, width, height);
        this.writeToResponse(image, response);
    }

    @Override
    public void writeToResponse(StringWrapper stringWrapper, HttpServletResponse response)
            throws IOException {
        this.writeToResponse(stringWrapper, this.width, this.height, response);
    }

    @Override
    public void writeToResponse(BufferedImage image, HttpServletResponse response)
            throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    public boolean isHasBorder() {
        return hasBorder;
    }

    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
    }

    public TextProducer getTextProducer() {
        return textProducer;
    }

    public void setTextProducer(TextProducer textProducer) {
        this.textProducer = textProducer;
    }

    public WordRenderer getWordRenderer() {
        return wordRenderer;
    }

    public void setWordRenderer(WordRenderer wordRenderer) {
        this.wordRenderer = wordRenderer;
    }

    public BorderProducer getBorderProducer() {
        return borderProducer;
    }

    public void setBorderProducer(BorderProducer borderProducer) {
        this.borderProducer = borderProducer;
    }

    public BackgroundProducer getBackgroundProducer() {
        return backgroundProducer;
    }

    public void setBackgroundProducer(BackgroundProducer backgroundProducer) {
        this.backgroundProducer = backgroundProducer;
    }

    public OrderedNoiseProducer getNoiseProducer() {
        return noiseProducer;
    }

    public void setNoiseProducer(OrderedNoiseProducer noiseProducer) {
        this.noiseProducer = noiseProducer;
    }

}
