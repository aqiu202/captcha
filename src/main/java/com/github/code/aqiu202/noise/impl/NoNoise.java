package com.github.code.aqiu202.noise.impl;

import com.github.code.aqiu202.noise.NoiseProducer;
import java.awt.image.BufferedImage;

/**
 * Implements of NoiseProducer that does nothing.
 */
public class NoNoise implements NoiseProducer {

    public BufferedImage makeNoise(BufferedImage image) {
        //Do nothing.
        return image;
    }

    @Override
    public NoiseStyle getNoiseStyle() {
        return NoiseStyle.NONE;
    }
}
