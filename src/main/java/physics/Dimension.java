package physics;

import org.jscience.mathematics.number.Rational;

/**
 * Represents the dimensions of a quantity in terms of the SI fundamental units
 */
public class Dimension {
    private static final Dimension DIMENSIONLESS = new Dimension(0, 0, 0, 0, 0, 0,0);
    private static final Dimension SECOND = new Dimension(1, 0, 0, 0, 0, 0, 0);
    private static final Dimension METRE = new Dimension(0, 1, 0, 0, 0, 0, 0);
    private static final Dimension KILOGRAM = new Dimension(0, 0, 1, 0, 0, 0, 0);
    private static final Dimension AMPERE = new Dimension(0, 0, 0, 1, 0, 0, 0);
    private static final Dimension KELVIN = new Dimension(0, 0, 0, 0, 1, 0, 0);
    private static final Dimension MOLE = new Dimension(0, 0, 0, 0, 0, 1, 0);
    private static final Dimension CANDELA = new Dimension(0, 0, 0, 0, 0, 0, 1);
    private static final Dimension HERTZ = new Dimension(-1, 0, 0, 0, 0, 0, 0);
    private static final Dimension NEWTON = new Dimension(-2, 1, 1, 0, 0, 0, 0);
    private static final Dimension PASCAL = new Dimension(-2, -1, 1, 0, 0, 0, 0);
    private static final Dimension JOULE = new Dimension(-2, 2, 1, 0, 0, 0, 0);
    private static final Dimension WATT = new Dimension(-3, 2, 1, 0, 0, 0, 0);
    private static final Dimension COULOMB = new Dimension(1, 0, 0, 1, 0, 0, 0);
    private static final Dimension VOLT = new Dimension(-3, 2, 1, -1, 0, 0, 0);
    private static final Dimension FARAD = new Dimension(4, -2, -1, 2, 0, 0, 0);
    private static final Dimension OHM = new Dimension(-3, 2, 1, -2, 0, 0, 0);
    private static final Dimension SIEMENS = new Dimension(3, -2, -1, 2, 0, 0, 0);
    private static final Dimension WEBER = new Dimension(-2, 2, 1, -1, 0, 0, 0);
    private static final Dimension TESLA = new Dimension(-2, 0, 1, -1, 0, 0, 0);
    private static final Dimension HENRY = new Dimension(-2, 2, 1, -2, 0, 0, 0);
    //private static final Unit LUMEN = new Unit(0, 0, 0, 0, 0, 0, 0);
    //private static final Unit LUX = new Unit(0, 0, 0, 0, 0, 0, 0);
    private static final Dimension BECQUEREL = new Dimension(-1, 0, 0, 0, 0, 0, 0);
    private static final Dimension SIEVERT = new Dimension(-2, 2, 0, 0, 0, 0, 0);

    private Rational[] dimensions = new Rational[7];

    /**
     * Creates a dimensionless Dimension
     */
    public Dimension() {
        for (int i = 0; i < 7; i++) {
            dimensions[i] = Rational.ZERO;
        }
    }

    /**
     * Creates a Dimension with the specified value in each SI base quantity.
     * Each dimension is treated as having a value equal to the appropriate SI fundamental unit
     * @param T Time
     * @param L Length
     * @param M Mass
     * @param I Electric current
     * @param theta Thermodynamic temperature
     * @param N Amount of substance
     * @param J Luminous intensity
     */
    public Dimension(int T, int L, int M, int I, int theta, int N, int J) {
        dimensions[0] = Rational.valueOf(T,1);
        dimensions[1] = Rational.valueOf(L,1);
        dimensions[2] = Rational.valueOf(M,1);
        dimensions[3] = Rational.valueOf(I,1);
        dimensions[4] = Rational.valueOf(theta,1);
        dimensions[5] = Rational.valueOf(N,1);
        dimensions[6] = Rational.valueOf(J,1);
    }

    /**
     * Creates a Dimension with its value in each SI quantity given by the array.
     */
    public Dimension(int[] dims) {
        this(dims[0], dims[1], dims[2], dims[3], dims[4], dims[5], dims[6]);

        if (dims.length != 7)
            throw new RuntimeException("Cannot create Dimension with anything but 7 integers.");
    }

    /**
     * Creates a Dimension with the specified values in each SI base quantity
     * @param dimensions Array representing the dimensions in the following order:
     *                   time, length, mass, current, temperature, amount, intensity
     */
    private Dimension(Rational[] dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Creates a Dimension with the same dimensions as the given SI unit
     * @param dimension Symbol of the SI unit (e.g., Hz for Hertz, W for Watt). Case-sensitive, official SI units only
     */
    public Dimension(String dimension) {
        switch (dimension) {
            case "s" -> setDimensions(SECOND.getDimensions());
            case "m" -> setDimensions(METRE.getDimensions());
            case "kg" -> setDimensions(KILOGRAM.getDimensions());
            case "A" -> setDimensions(AMPERE.getDimensions());
            case "K" -> setDimensions(KELVIN.getDimensions());
            case "mol" -> setDimensions(MOLE.getDimensions());
            case "cd" -> setDimensions(CANDELA.getDimensions());
            case "Hz" -> setDimensions(HERTZ.getDimensions());
            case "N" -> setDimensions(NEWTON.getDimensions());
            case "Pa" -> setDimensions(PASCAL.getDimensions());
            case "J" -> setDimensions(JOULE.getDimensions());
            case "W" -> setDimensions(WATT.getDimensions());
            case "C" -> setDimensions(COULOMB.getDimensions());
            case "V" -> setDimensions(VOLT.getDimensions());
            case "F" -> setDimensions(FARAD.getDimensions());
            case "O" -> setDimensions(OHM.getDimensions());
            case "S" -> setDimensions(SIEMENS.getDimensions());
            case "Wb" -> setDimensions(WEBER.getDimensions());
            case "T" -> setDimensions(TESLA.getDimensions());
            case "H" -> setDimensions(HENRY.getDimensions());
            case "Bq" -> setDimensions(BECQUEREL.getDimensions());
            case "SV" -> setDimensions(SIEVERT.getDimensions());
            default -> throw new RuntimeException("Unrecognized dimension");
        }
    }

    /**
     * Overwrites the dimensions of this Dimension with the given dimensions
     * @param dimensions Array representing the dimensions in the following order:
     *                   time, length, mass, current, temperature, amount, intensity
     */
    private void setDimensions(Rational[] dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Returns the dimensions of this Dimension
     * @return Returns an array with the dimensions in the following order:
     *         time, length, mass, current, temperature, amount, intensity
     */
    private Rational[] getDimensions() {
        return dimensions.clone();
    }

    /**
     * Compares whether the Dimensions have the same value for each of the dimensions
     * @param units The Dimension to be compared against
     * @return Returns true if they have the same value for each dimension, false otherwise
     */
    public boolean equals(Dimension units) {
        for (int i = 0; i < dimensions.length; i++) {
            if (!dimensions[i].equals(units.dimensions[i]))
                return false;
        }

        return true;
    }

    /**
     * Returns a new Dimension whose dimensions are the inverse of this object
     * @return Returns a new Dimension where each dimension is the negative of the respective dimension in this object
     */
    public Dimension invert() {
        Rational[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] = ret[i].opposite();
        }

        return new Dimension(ret);
    }

    /**
     * Returns a new Dimension whose dimensions are the sum of two Dimensions
     * @param units The Dimension to be added to this Dimension
     * @return Returns a new Dimension whose dimensions are the sum of this Dimension and the argument
     */
    public Dimension add(Dimension units) {
        Rational[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] = ret[i].plus(units.dimensions[i]);
        }

        return new Dimension(ret);
    }

    /**
     * Returns a new Dimension whose dimensions are the difference of two Dimensions
     * @param units The Dimension to be subtracted from this Dimension
     * @return Returns a new Dimension whose dimensions are those of this Dimension minus those of the argument
     */
    public Dimension subtract(Dimension units) {
        return add(units.invert());
    }

    /**
     * Returns a new Dimension where each dimension has been multiplied by a constant
     * @param n The constant to multiply all dimensions by
     * @return Returns a new Dimension whose dimensions are the product of this Dimension and the argument
     */
    public Dimension multiply(int n) {
        Rational[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] = ret[i].times(n);
        }

        return new Dimension(ret);
    }

    /**
     * Returns a new Dimension where each dimension has been divided by a constant
     * @param n The constant to divide all dimensions by
     * @return Returns a new Dimension whose dimensions are the quotient of this Dimension and the argument
     */
    public Dimension divide(int n) {
        Rational[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] = ret[i].divide(Rational.valueOf(n,1));
        }

        return new Dimension(ret);
    }

    /**
     * Checks whether this dimension is dimensionless
     * @return Returns true if all dimensions are 0, false otherwise
     */
    public boolean isDimensionless() {
        return this.equals(DIMENSIONLESS);
    }

    /**
     * Returns a LaTeX string representation of this Dimension
     * @return Returns the SI symbol of the unit if there is a direct match.
     *         If there is no match, gives each base unit and its respective power in a notation suitable for LaTeX
     */
    public String toLatexString() {
        return toString(true);
    }

    /**
     * Returns a string representation of this Dimension
     * @return Returns the SI symbol of the unit if there is a direct match.
     *         If there is no match, gives each base unit and its respective power
     */
    public String toString() {
        return toString(false);
    }

    /**
     * Identifies SI unit associated with this Dimension.
     * If there is no direct unit, gives each base unit and its respective power
     * @param isLatex If true, powers of base units are given in curly braces to aid LaTeX
     * @return Returns a string representation of this Dimension
     */
    private String toString(boolean isLatex) {
        if (this.equals(SECOND)) return "s";
        else if (this.equals(METRE)) return "m";
        else if (this.equals(KILOGRAM)) return "kg";
        else if (this.equals(AMPERE)) return "A";
        else if (this.equals(KELVIN)) return "K";
        else if (this.equals(MOLE)) return "mol";
        else if (this.equals(CANDELA)) return "cd";
        else if (this.equals(HERTZ)) return "Hz";
        else if (this.equals(NEWTON)) return "N";
        else if (this.equals(PASCAL)) return "Pa";
        else if (this.equals(JOULE)) return "J";
        else if (this.equals(WATT)) return "W";
        else if (this.equals(COULOMB)) return "C";
        else if (this.equals(VOLT)) return "V";
        else if (this.equals(FARAD)) return "F";
        else if (this.equals(OHM)) return "O";
        else if (this.equals(SIEMENS)) return "S";
        else if (this.equals(WEBER)) return "Wb";
        else if (this.equals(TESLA)) return "T";
        else if (this.equals(HENRY)) return "H";
        else if (this.equals(BECQUEREL)) return "Bq";
        else if (this.equals(SIEVERT)) return "Sv";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimensions.length; i++) {
            if (!dimensions[i].equals(Rational.ZERO)) {
                sb.append(switch (i) {
                    case 0 -> "s";
                    case 1 -> "m";
                    case 2 -> "kg";
                    case 3 -> "A";
                    case 4 -> "K";
                    case 5 -> "mol";
                    case 6 -> "cd";
                    default -> "";
                });

                sb.append("^");
                if (isLatex) sb.append("{");
                sb.append(dimensions[i].getDivisor().equals(1) ? dimensions[i].getDividend() : dimensions[i]);
                if (isLatex) sb.append("}");
            }
        }

        return sb.toString();
    }
}