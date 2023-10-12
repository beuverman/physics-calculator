package physics;

/**
 * Represents the dimensions of a quantity in terms of the SI fundamental units
 */
public class Dimension {
    private static final Dimension DIMENSIONLESS = new Dimension(new int[]{ 0, 0, 0, 0, 0, 0,0});
    private static final Dimension SECOND = new Dimension(new int[]{1, 0, 0, 0, 0, 0, 0});
    private static final Dimension METRE = new Dimension(new int[]{0, 1, 0, 0, 0, 0, 0});
    private static final Dimension KILOGRAM = new Dimension(new int[]{0, 0, 1, 0, 0, 0, 0});
    private static final Dimension AMPERE = new Dimension(new int[]{0, 0, 0, 1, 0, 0, 0});
    private static final Dimension KELVIN = new Dimension(new int[]{0, 0, 0, 0, 1, 0, 0});
    private static final Dimension MOLE = new Dimension(new int[]{0, 0, 0, 0, 0, 1, 0});
    private static final Dimension CANDELA = new Dimension(new int[]{0, 0, 0, 0, 0, 0, 1});
    private static final Dimension HERTZ = new Dimension(new int[]{-1, 0, 0, 0, 0, 0, 0});
    private static final Dimension NEWTON = new Dimension(new int[]{-2, 1, 1, 0, 0, 0, 0});
    private static final Dimension PASCAL = new Dimension(new int[]{-2, -1, 1, 0, 0, 0, 0});
    private static final Dimension JOULE = new Dimension(new int[]{-2, 2, 1, 0, 0, 0, 0});
    private static final Dimension WATT = new Dimension(new int[]{-3, 2, 1, 0, 0, 0, 0});
    private static final Dimension COULOMB = new Dimension(new int[]{1, 0, 0, 1, 0, 0, 0});
    private static final Dimension VOLT = new Dimension(new int[]{-3, 2, 1, -1, 0, 0, 0});
    private static final Dimension FARAD = new Dimension(new int[]{4, -2, -1, 2, 0, 0, 0});
    private static final Dimension OHM = new Dimension(new int[]{-3, 2, 1, -2, 0, 0, 0});
    private static final Dimension SIEMENS = new Dimension(new int[]{3, -2, -1, 2, 0, 0, 0});
    private static final Dimension WEBER = new Dimension(new int[]{-2, 2, 1, -1, 0, 0, 0});
    private static final Dimension TESLA = new Dimension(new int[]{-2, 0, 1, -1, 0, 0, 0});
    private static final Dimension HENRY = new Dimension(new int[]{-2, 2, 1, -2, 0, 0, 0});
    //private static final Unit LUMEN = new Unit(new int[]{0, 0, 0, 0, 0, 0, 0});
    //private static final Unit LUX = new Unit(new int[]{0, 0, 0, 0, 0, 0, 0})0;
    private static final Dimension BECQUEREL = new Dimension(new int[]{-1, 0, 0, 0, 0, 0, 0});
    private static final Dimension SIEVERT = new Dimension(new int[]{-2, 2, 0, 0, 0, 0, 0});

    private int[] dimensions = new int[7];

    /**
     * Creates a dimensionless Dimension
     */
    public Dimension() {
        for (int i = 0; i < 7; i++) {
            dimensions[i] = 0;
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
        dimensions[0] = T;
        dimensions[1] = L;
        dimensions[2] = M;
        dimensions[3] = I;
        dimensions[4] = theta;
        dimensions[5] = N;
        dimensions[6] = J;
    }

    /**
     * Creates a Dimension with the specified values in each SI base quantity
     * @param dimensions Array representing the dimensions in the following order:
     *                   time, length, mass, current, temperature, amount, intensity
     */
    private Dimension(int[] dimensions) {
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
            case "g" -> setDimensions(KILOGRAM.getDimensions());
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
    private void setDimensions(int[] dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Returns the dimensions of this Dimension
     * @return Returns an array with the dimensions in the following order:
     *         time, length, mass, current, temperature, amount, intensity
     */
    private int[] getDimensions() {
        return dimensions.clone();
    }

    /**
     * Compares whether the Dimensions have the same value for each of the dimensions
     * @param units The Dimension to be compared against
     * @return Returns true if they have the same value for each dimension, false otherwise
     */
    public boolean equals(Dimension units) {
        for (int i = 0; i < dimensions.length; i++) {
            if (dimensions[i] != units.dimensions[i])
                return false;
        }

        return true;
    }

    /**
     * Returns a new Dimension whose dimensions are the inverse of this object
     * @return Returns a new Dimension where each dimension is the negative of the respective dimension in this object
     */
    public Dimension invert() {
        int[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] *= -1;
        }

        return new Dimension(ret);
    }

    /**
     * Returns a new Dimension whose dimensions are the sum of two Dimensions
     * @param units The Dimension to be added to this Dimension
     * @return Returns a new Dimension whose dimensions are the sum of this Dimension and the argument
     */
    public Dimension add(Dimension units) {
        int[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] += units.dimensions[i];
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
        int[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] *= n;
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
     * Returns a string representation of this Dimension
     * @return Returns the SI symbol of the unit if there is a direct match.
     *         If there is no match, gives each base unit and its respective power
     */
    public String toString() {
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
            if (dimensions[i] != 0) {
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

                sb.append("^").append(dimensions[i]);
            }
        }

        return sb.toString();
    }
}