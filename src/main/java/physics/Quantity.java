package physics;

import ch.obermuhlner.math.big.BigDecimalMath;
import physics.exceptions.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Quantity {
    private static final RoundingMode RM = RoundingMode.HALF_EVEN;
    private static final MathContext MC = new MathContext(100, RM);
    private static final int SCALE = 100;
    private static final int OUTPUT_PRECISION = 6;

    private BigDecimal value;
    private Dimension dimension;

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

    protected Quantity(String value, Dimension dimensions) {
        this.value = new BigDecimal(value);
        this.value = this.value.setScale(SCALE, RM);
        dimension = dimensions;
    }

    public Quantity(String str) {
        int separator = -1;
        Quantity a, b;

        for (int i = str.length() - 1; i >= 0; i--) {
            if (Character.isDigit(str.charAt(i))) {
                separator = i + 1;
                break;
            }
        }

        //just unit
        if (separator == -1) {
            b = Units.getUnit(str);

            //Check for prefix
            if (b == null) {
                //For the only 2 character prefix: da
                if (str.length() == 1)
                    throw new RuntimeException("Unrecognized unit " + str);
                a = Units.getPrefix(str.substring(0, 2));

                if (a == null) {
                    a = Units.getPrefix(str.substring(0, 1));

                    if (a == null)
                        throw new RuntimeException("Unrecognized unit " + str);

                    str = str.substring(1);
                }
                else
                    str = str.substring(2);

                b = Units.getUnit(str);
            }
            else
                a = new Quantity("1", new Dimension());

            value = a.multiply(b).value;
            dimension = b.dimension;

            if (str.equals("g"))
                value = value.movePointLeft(3);
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

            value = a.multiply(b).value;
            dimension = b.dimension;
        }

        value = value.setScale(SCALE, RM);
    }

    /**
     * Returns the negative of this quantity
     * @return Returns a quantity that has the negative value of this quantity
     */
    public Quantity negate() {
        return new Quantity(value.negate(), dimension);
    }

    /**
     * Returns a quantity that is the sum of the given quantities
     * @param augend The quantity to be added
     * @return Returns the sum
     */
    public Quantity add(Quantity augend) {
        if (!dimension.equals(augend.dimension))
            throw new IncompatibleUnitsException(dimension.toString(), augend.dimension.toString());

        return new Quantity(value.add(augend.value), dimension);
    }

    /**
     * Returns a quantity that is the difference of the given quantities
     * @param subtrahend The quantity to be subtracted
     * @return Returns the difference
     */
    public Quantity subtract(Quantity subtrahend) {
        if (!dimension.equals(subtrahend.dimension))
            throw new IncompatibleUnitsException(dimension.toString(), subtrahend.dimension.toString());

        return new Quantity(value.subtract(subtrahend.value), dimension);
    }

    /**
     * Returns a quantity that is the product of the given quantities
     * @param multiplicand The quantity to be multiplied by
     * @return Returns the product
     */
    public Quantity multiply(Quantity multiplicand) {
        return new Quantity(value.multiply(multiplicand.value), dimension.add(multiplicand.dimension));
    }

    /**
     * Returns a quantity that is the quotient of the given quantities
     * @param divisor The quantity to be divided by
     * @return Returns the quotient
     */
    public Quantity divide(Quantity divisor) {
        return new Quantity(value.divide(divisor.value, RM), dimension.subtract(divisor.dimension));
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
        if (!BigDecimalMath.isIntValue(n.value) && !this.isDimensionless())
            throw new InvalidDimensionException();

        return new Quantity(BigDecimalMath.pow(value, n.value, MC),
                this.isDimensionless() ? dimension : dimension.multiply(n.value.intValue()));
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
     * @param quantity The quantity to be compared against
     * @return Returns true for the same value and dimensions and false otherwise
     */
    public boolean equals(Quantity quantity) {
        return (value.equals(quantity.value) && dimension.equals(quantity.dimension));
    }

    /**
     * Checks whether the quantity has dimensions
     * @return Returns true if dimensionless and false otherwise
     */
    public boolean isDimensionless() {
        return dimension.isDimensionless();
    }

    /**
     * Creates a string representation of this quantity
     * @return Returns a string representation of this quantity
     */
    public String toString() {
        return value.round(new MathContext(OUTPUT_PRECISION, RoundingMode.HALF_UP)) + dimension.toString();
    }
}