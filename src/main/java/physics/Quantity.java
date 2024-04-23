package physics;

import ch.obermuhlner.math.big.BigDecimalMath;
import physics.exceptions.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Associates dimensions with a scalar value
 */
public class Quantity {
    private static final RoundingMode RM = RoundingMode.HALF_EVEN;
    private static final MathContext MC = new MathContext(100, RM);
    private static final int SCALE = 100;
    private static final int SIG_FIGS = 6;

    private BigDecimal value;
    private Dimension dimension;
    private BigDecimal unitScale = BigDecimal.ONE;
    private String unitString;

    /**
     * Creates a dimensionless quantity of value 0
     */
    public Quantity() {
        value = new BigDecimal(0, MC);
        value = value.setScale(SCALE, RM);
        dimension = new Dimension(0, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Creates a dimensionless quantity with a given value
     * @param value The value of the quantity
     */
    private Quantity(BigDecimal value) {
        this.value = value;
        this.dimension = new Dimension();
    }

    /**
     * Creates a quantity with given value and dimensions
     * @param value Value of the quantity
     * @param dimension Dimensions of the quantity
     */
    private Quantity(BigDecimal value, Dimension dimension) {
        this.value = value;
        this.dimension = dimension;
    }

    /**
     * Creates a quantity with given value and dimensions
     * @param value Value of the quantity
     * @param dimensions Dimensions of the quantity
     */
    protected Quantity(String value, Dimension dimensions) {
        this.value = new BigDecimal(value);
        this.value = this.value.setScale(SCALE, RM);
        dimension = dimensions;
    }

    /**
     * Creates a quantity from a string representation
     * String will be used in toString method
     * @param str String representation of the quantity
     */
    public Quantity(String str) {
        int separator = -1;
        Quantity a, b;

        // Find where number ends and unit starts
        for (int i = str.length() - 1; i >= 0; i--) {
            if (Character.isDigit(str.charAt(i))) {
                separator = i + 1;
                break;
            }
        }

        //just unit
        if (separator == -1) {
            b = Units.getUnit(str);

            value = BigDecimal.ONE;
            unitScale = b.value;
            unitString = str;
            dimension = b.dimension;
        }
        //just number
        else if (separator == str.length()) {
            value = new BigDecimal(str, MC);
            dimension = new Dimension();
        }
        //unit and number
        else {
            a = new Quantity(str.substring(0, separator));
            b = new Quantity(str.substring(separator));

            value = a.value;
            unitScale = b.value.multiply(b.unitScale);
            unitString = b.unitString;
            dimension = b.dimension;
        }

        value = value.setScale(SCALE, RM);
    }

    /**
     * Gives the value of this quantity in base SI units
     * @return Returns the product of value and unitScale
     */
    private BigDecimal scaledValue() {
        return value.multiply(unitScale);
    }

    /**
     * Returns the negative of this quantity
     * @return Returns a quantity that has the negative value of this quantity
     */
    public Quantity negate() {
        return new Quantity(scaledValue().negate(), dimension);
    }

    /**
     * Returns a quantity that is the sum of the given quantities
     * @param augend The quantity to be added
     * @return Returns the sum
     */
    public Quantity add(Quantity augend) {
        if (!dimension.equals(augend.dimension))
            throw new IncompatibleUnitsException(dimension.toString(), augend.dimension.toString());

        return new Quantity(scaledValue().add(augend.scaledValue()), dimension);
    }

    /**
     * Returns a quantity that is the difference of the given quantities
     * @param subtrahend The quantity to be subtracted
     * @return Returns the difference
     */
    public Quantity subtract(Quantity subtrahend) {
        if (!dimension.equals(subtrahend.dimension))
            throw new IncompatibleUnitsException(dimension.toString(), subtrahend.dimension.toString());

        return new Quantity(scaledValue().subtract(subtrahend.scaledValue()), dimension);
    }

    /**
     * Returns a quantity that is the product of the given quantities
     * @param multiplicand The quantity to be multiplied by
     * @return Returns the product
     */
    public Quantity multiply(Quantity multiplicand) {
        return new Quantity(scaledValue().multiply(multiplicand.scaledValue()), dimension.add(multiplicand.dimension));
    }

    /**
     * Returns a quantity that is the quotient of the given quantities
     * @param divisor The quantity to be divided by
     * @return Returns the quotient
     */
    public Quantity divide(Quantity divisor) {
        return new Quantity(scaledValue().divide(divisor.scaledValue(), RM), dimension.subtract(divisor.dimension));
    }

    /**
     * Raises this quantity to the nth power
     * @param n Power to be raised by
     * @return Returns this quantity to the nth power
     */
    public Quantity pow(Quantity n) {
        //Check if exponent is dimensionless
        if (!n.isDimensionless())
            throw new InvalidDimensionException();

        int i = isRational(n.value);
        if (i == -1)
            throw new InvalidDimensionException();

        return new Quantity(BigDecimalMath.pow(value, n.value, MC),
                dimension.multiply(n.value.multiply(new BigDecimal(i)).intValue()).divide(new BigDecimal(i).intValue()));
    }

    /**
     * Computes the sin of the given quantity
     * @param x Quantity to compute sin of
     * @return Returns the sin of the given quantity
     */
    public static Quantity sin(Quantity x) {
        if (!x.isDimensionless())
            throw new InvalidDimensionException();

        return new Quantity(BigDecimalMath.sin(x.value, MC));
    }

    /**
     * Computes the cos of the given quantity
     * @param x Quantity to compute cos of
     * @return Returns the cos of the given quantity
     */
    public static Quantity cos(Quantity x) {
        if (!x.isDimensionless())
            throw new InvalidDimensionException();

        return new Quantity(BigDecimalMath.cos(x.value, MC));
    }

    /**
     * Computes the tan of the given quantity
     * @param x Quantity to compute tan of
     * @return Returns the tan of the given quantity
     */
    public static Quantity tan(Quantity x) {
        if (!x.isDimensionless())
            throw new InvalidDimensionException();

        return new Quantity(BigDecimalMath.tan(x.value, MC));
    }

    /**
     * Returns whether these quantities have the same value and dimensions
     * @param o The quantity to be compared against
     * @return Returns true for the same value and dimensions and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        //Allow for scale to be different between BigDecimals
        return scaledValue().compareTo(quantity.scaledValue()) == 0 && dimension.equals(quantity.dimension);
    }

    /**
     * Checks whether the quantity has dimensions
     * @return Returns true if dimensionless and false otherwise
     */
    public boolean isDimensionless() {
        return dimension.isDimensionless();
    }

    /**
     * Determines whether a BidDecimal can be given as a ratio of 2 integers
     * Only checks divisors up to 10
     * @param bd The BigDecimal to be checked
     * @return Returns the divisor if rational, -1 otherwise
     */
    private int isRational(BigDecimal bd) {
        //TODO: Make this actually work
        //Appears to only recognize divisors of 2, 4, 5, and 8
        for (int i = 1; i < 10; i++) {
            if (isInteger(bd.multiply(new BigDecimal(i))))
                return i;
        }

        return -1;
    }

    /**
     * Determines whether a BigDecimal can be given as an integer
     * @param bd The BigDecimal to be checked
     * @return Returns true if integer, false otherwise
     */
    private boolean isInteger(BigDecimal bd) {
        return bd.stripTrailingZeros().scale() <= 0;
    }

    /**
     * Converts the value portion of the Quantity to a String
     * @return Returns a string representing the value of this Quantity, with
     * SIG_FIGS significant figures
     */
    private String valueToString(BigDecimal bd) {
        return stripTrailingZeros(bd.setScale(SIG_FIGS - bd.precision() + bd.scale(), RoundingMode.HALF_UP).toString());
    }

    /**
     * Removes excess zeroes at the end of a number
     * @param str The number to be stripped
     * @return Returns the number with any trailing zeroes after a decimal removed
     */
    private String stripTrailingZeros(String str) {
        int start = -1;
        int end = str.indexOf('E');

        if (str.indexOf('.') == -1 && end == -1) return str;
        if (end == -1) end = str.length();

        for (int i = end - 1; i >= 0; i--) {
            if (str.charAt(i) != '0') {
                start = i;
                break;
            }
        }

        if (start == end - 1) return str;
        if (start == -1) return "0";
        if (start == str.indexOf('.')) start--;

        return str.substring(0, start + 1) + str.substring(end);
    }

    /**
     * Creates a latex representation of this quantity.
     * Uses units given in creation of quantity, if possible.
     * @return Converts toString to a latex representation, replacing engineering notation with scientific notation
     */
    public String toLatexString() {
        String dimString = (unitString == null ? dimension.toLatexString() : unitString);

        if (value.compareTo(BigDecimal.ONE) == 0 && !isDimensionless())
            return dimString;

        String valString = valueToString(value);

        if (valString.contains("E+"))
            valString = valString.replace("E+", "*10^{") + "}";
        else if (valString.contains("E-"))
            valString = valString.replace("E", "*10^{") + "}";

        return valString + dimString;
    }

    /**
     * Creates a string representation of this quantity.
     * Quantity is given in base SI units.
     * @return Returns a string representation of this quantity
     */
    public String toString() {
        return valueToString(scaledValue()) + dimension.toString();
    }
}