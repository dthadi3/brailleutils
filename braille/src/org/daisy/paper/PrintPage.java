package org.daisy.paper;

/**
 *
 * @author Bert Frees
 */
public class PrintPage implements Dimensions {

    /**
     *  Direction of print
     */
    public enum PrintDirection {
        /**
         *  Direction of print is equal to direction of feeding paper
         */
        UPRIGHT,
        /**
         *  Direction of print is opposite to direction of feeding paper
         */
        SIDEWAYS
    }

    /**
     *  Regular printing or multi-page printing
     */
    public enum PrintMode {
        /**
         *  One print page per input page
         */
        REGULAR,
        /**
         *  Two print pages per input page
         */
        MAGAZINE
    }

    private final PageFormat inputPage;
    private final PrintDirection direction;
    private final PrintMode mode;

    public PrintPage(PageFormat inputPage,
                     PrintDirection direction,
                     PrintMode mode) {

        this.inputPage = inputPage;
        this.direction = direction;
        this.mode = mode;
    }

    public PrintPage(PageFormat inputPage) {
        this(inputPage, PrintDirection.UPRIGHT, PrintMode.REGULAR);
    }

    public double getWidth() {

        double width;

        switch (direction) {
            case SIDEWAYS:
                width = inputPage.getHeight();
                break;
            case UPRIGHT:
            default:
                width = inputPage.getWidth();
        }

        switch (mode) {
            case MAGAZINE:
                return width/2;
            case REGULAR:
            default:
                return width;
        }
    }

    public double getHeight() {

        switch (direction) {
            case SIDEWAYS:
                return inputPage.getWidth();
            case UPRIGHT:
            default:
                return inputPage.getHeight();
        }
    }
}
