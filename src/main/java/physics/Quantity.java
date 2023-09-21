package physics;

import ch.obermuhlner.math.big.BigDecimalMath;
import physics.exceptions.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Quantity {
    private static final RoundingMode RM = RoundingMode.HALF_EVEN;
    private static final MathContext MC = new MathContext(100, RM);
    private static final int SCALE = 10;

    private BigDecimal value;
    private Unit unit;

    /**
     * Creates a dimensionless quantity of value 0
     */
    public Quantity() {
        value = new BigDecimal(0, MC);
        value = value.setScale(SCALE, RM);
        unit = new Unit(0, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Creates a dimensionless quantity with a given value
     * @param value The value of the quantity
     */
    private Quantity(BigDecimal value) {
        this.value = value;
        this.unit = new Unit();
    }

    /**
     * Creates a quantity with given value and dimensions
     * @param value Value of the quantity
     * @param unit Dimensions of the quantity
     */
    private Quantity(BigDecimal value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public Quantity(String str) {
        for (int i = str.length() - 1; i >= 0; i--) {
            if (Character.isDigit(str.charAt(i))) {
                value = new BigDecimal(str.substring(0, i + 1), MC);
                value = value.setScale(SCALE, RM);

                if (i == str.length() - 1)
                    unit = new Unit();
                else
                    unit = new Unit(str.substring(i + 1));

                return;
            }
        }

        value = new BigDecimal(1, MC);
        value = value.setScale(SCALE, RM);
        unit = new Unit(str);
    }

    /**
     * Returns a quantity that is the sum of the given quantities
     * @param augend The quantity to be added
     * @return Returns the sum
     */
    public Quantity add(Quantity augend) {
        if (!unit.equals(augend.unit))
            throw new IncompatibleUnitsException(unit.toString(), augend.unit.toString());

        return new Quantity(value.add(augend.value), unit);
    }

    /**
     * Returns a quantity that is the difference of the given quantities
     * @param subtrahend The quantity to be subtracted
     * @return Returns the difference
     */
    public Quantity subtract(Quantity subtrahend) {
        if (!unit.equals(subtrahend.unit))
            throw new IncompatibleUnitsException(unit.toString(), subtrahend.unit.toString());

        return new Quantity(value.subtract(subtrahend.value), unit);
    }

    /**
     * Returns a quantity that is the product of the given quantities
     * @param multiplicand The quantity to be multiplied by
     * @return Returns the product
     */
    public Quantity multiply(Quantity multiplicand) {
        return new Quantity(value.multiply(multiplicand.value), unit.add(multiplicand.unit));
    }

    /**
     * Returns a quantity that is the quotient of the given quantities
     * @param divisor The quantity to be divided by
     * @return Returns the quotient
     */
    public Quantity divide(Quantity divisor) {
        return new Quantity(value.divide(divisor.value, RM), unit.subtract(divisor.unit));
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
                this.isDimensionless() ? unit : unit.multiply(n.value.intValue()));
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
        return (value.equals(quantity.value) && unit.equals(quantity.unit));
    }

    /**
     * Checks whether the quantity has dimensions
     * @return Returns true if dimensionless and false otherwise
     */
    public boolean isDimensionless() {
        return unit.isDimensionless();
    }

    /**
     * Creates a string representation of this quantity
     * @return Returns a string representation of this quantity
     */
    public String toString() {
        return value.stripTrailingZeros().toPlainString() + unit.toString();
    }
}