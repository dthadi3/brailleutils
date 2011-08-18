package org.daisy.braille.tools;

public class Length {
	public enum UnitsOfLength {MILLIMETER, CENTIMETER, INCH};
	public final static double INCH_IN_MM = 25.4;
	
	private final double originalValue;
	private final double mmValue;
	private final UnitsOfLength unit;
	
	private Length(double originalValue, UnitsOfLength unit) {
		this.originalValue = originalValue;
		this.unit = unit;
		switch (unit) {
		case INCH:
			mmValue = originalValue * INCH_IN_MM;
			break;
		case CENTIMETER:
			mmValue = originalValue * 10;
			break;
		case MILLIMETER: default:
			mmValue = originalValue;
			break;
		}
	}

	/**
	 * Gets the length, in the original units of length.
	 * @return returns the length
	 */
	public double getLength() {
		return originalValue;
	}

	/**
	 * Gets the units of length.
	 * @return returns the units of length
	 */
	public UnitsOfLength getUnitsOfLength() {
		return unit;
	}

	public double asMillimeter() {
		return mmValue;
	}

	public double asInches() {
		return mmValue / INCH_IN_MM;
	}

	/**
	 * millimeter
	 * @param value
	 * @return
	 */
	public static Length newMillimeterValue(double value) {
		return new Length(value, UnitsOfLength.MILLIMETER);
	}
	
	public static Length newCentimeterValue(double value) {
		return new Length(value, UnitsOfLength.CENTIMETER);
	}
	
	public static Length newInchValue(double value) {
		return new Length(value, UnitsOfLength.INCH);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return originalValue + " " + unit.toString().toLowerCase();
	}

}
