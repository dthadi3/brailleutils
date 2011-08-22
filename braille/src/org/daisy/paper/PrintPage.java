package org.daisy.paper;

import org.daisy.braille.embosser.EmbosserProperties.PrintMode;
import org.daisy.braille.tools.Length;

/**
 *
 * @author Bert Frees
 * @author Joel Håkansson
 */
public class PrintPage implements Dimensions {

    /**
     *  Direction of print
     */
    public enum PrintDirection {
        /**
         *  Direction of embosser head is equal to direction of feeding paper
         */
        UPRIGHT,
        /**
         *  Direction of embosser head is opposite to direction of feeding paper
         */
        SIDEWAYS
    }

    /**
	 * The shape of the paper
	 */
	public enum Shape {
		/**
		 *  Represents portrait shape, that is to say that getWidth()<getHeight()
		 */
		PORTRAIT,
		/**
		 *  Represents landscape shape, that is to say that getWidth>getHeight()
		 */
		LANDSCAPE,
		/**
		 *  Represents square shape, that is to say that getWidth()==getHeight()
		 */
		SQUARE
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
    
    public Length getLengthAcrossFeed() {
    	switch (inputPage.getPageFormatType()) {
	    	case SHEET: {
	    		switch (direction) {
		    		case SIDEWAYS:
		    			return ((SheetPaperFormat)inputPage).getPageHeight();
		    		case UPRIGHT: default:
		    			return ((SheetPaperFormat)inputPage).getPageWidth();
	    		}
	    	}
	    	case ROLL:
	    		return ((RollPaperFormat)inputPage).getLengthAcrossFeed();
	    	case TRACTOR:
	    		return ((TractorPaperFormat)inputPage).getLengthAcrossFeed();
	    	default:
	    		throw new RuntimeException("Coding error");
    	}
    }

    public Length getLengthAlongFeed() {
    	switch (inputPage.getPageFormatType()) {
	    	case SHEET: {
	    		switch (direction) {
		    		case SIDEWAYS:
		    			return ((SheetPaperFormat)inputPage).getPageWidth();
		    		case UPRIGHT: default:
		    			return ((SheetPaperFormat)inputPage).getPageHeight();
	    		}
	    	}
	    	case ROLL:
	    		return ((RollPaperFormat)inputPage).getLengthAlongFeed();
	    	case TRACTOR:
	    		return ((TractorPaperFormat)inputPage).getLengthAlongFeed();
	    	default:
	    		throw new RuntimeException("Coding error");
		}
    }

    public double getWidth() {
        double width;

        switch (direction) {
            case SIDEWAYS:
                width = getLengthAlongFeed().asMillimeter();
                break;
            case UPRIGHT:
            default:
                width = getLengthAcrossFeed().asMillimeter();
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
                return getLengthAcrossFeed().asMillimeter();
            case UPRIGHT:
            default:
                return getLengthAlongFeed().asMillimeter();
        }
    }
    
    public Shape getShape() {
		if (getWidth()<getHeight()) {
			return Shape.PORTRAIT;
		} else if (getWidth()>getHeight()) {
			return Shape.LANDSCAPE;
		} else {
			return Shape.SQUARE;
		}
    }
}
