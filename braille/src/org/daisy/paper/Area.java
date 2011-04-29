package org.daisy.paper;

/**
 *
 * @author Bert Frees
 */
public class Area implements Dimensions {

    private final double width, height, offsetX, offsetY;

    public Area(double width,
                double height,
                double offsetX,
                double offsetY) {

        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }
}
