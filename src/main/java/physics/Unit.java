package physics;

public class Unit {
    private static final Unit SECOND = new Unit(new int[]{1, 0, 0, 0, 0, 0, 0});
    private static final Unit METRE = new Unit(new int[]{0, 1, 0, 0, 0, 0, 0});
    private static final Unit KILOGRAM = new Unit(new int[]{0, 0, 1, 0, 0, 0, 0});
    private static final Unit AMPERE = new Unit(new int[]{0, 0, 0, 1, 0, 0, 0});
    private static final Unit KELVIN = new Unit(new int[]{0, 0, 0, 0, 1, 0, 0});
    private static final Unit MOLE = new Unit(new int[]{0, 0, 0, 0, 0, 1, 0});
    private static final Unit CANDELA = new Unit(new int[]{0, 0, 0, 0, 0, 0, 1});
    private static final Unit HERTZ = new Unit(new int[]{-1, 0, 0, 0, 0, 0, 0});
    private static final Unit NEWTON = new Unit(new int[]{-2, 1, 1, 0, 0, 0, 0});
    private static final Unit PASCAL = new Unit(new int[]{-2, -1, 1, 0, 0, 0, 0});
    private static final Unit JOULE = new Unit(new int[]{-2, 2, 1, 0, 0, 0, 0});
    private static final Unit WATT = new Unit(new int[]{-3, 2, 1, 0, 0, 0, 0});
    private static final Unit COULOMB = new Unit(new int[]{1, 0, 0, 1, 0, 0, 0});
    private static final Unit VOLT = new Unit(new int[]{-3, 2, 1, -1, 0, 0, 0});
    private static final Unit FARAD = new Unit(new int[]{4, -2, -1, 2, 0, 0, 0});
    private static final Unit OHM = new Unit(new int[]{-3, 2, 1, -2, 0, 0, 0});
    private static final Unit SIEMENS = new Unit(new int[]{3, -2, -1, 2, 0, 0, 0});
    private static final Unit WEBER = new Unit(new int[]{-2, 2, 1, -1, 0, 0, 0});
    private static final Unit TESLA = new Unit(new int[]{-2, 0, 1, -1, 0, 0, 0});
    private static final Unit HENRY = new Unit(new int[]{-2, 2, 1, -2, 0, 0, 0});
    //private static final Unit LUMEN = new Unit(new int[]{0, 0, 0, 0, 0, 0, 0});
    //private static final Unit LUX = new Unit(new int[]{0, 0, 0, 0, 0, 0, 0})0;
    private static final Unit BECQUEREL = new Unit(new int[]{-1, 0, 0, 0, 0, 0, 0});
    private static final Unit SIEVERT = new Unit(new int[]{-2, 2, 0, 0, 0, 0, 0});

    private int[] dimensions = new int[7];

    public Unit() {
        for (int i = 0; i < 7; i++) {
            dimensions[i] = 0;
        }
    }

    public Unit(int T, int L, int M, int I, int theta, int N, int J) {
        dimensions[0] = T;
        dimensions[1] = L;
        dimensions[2] = M;
        dimensions[3] = I;
        dimensions[4] = theta;
        dimensions[5] = N;
        dimensions[6] = J;
    }

    private Unit(int[] dimensions) {
        this.dimensions = dimensions;
    }

    public Unit(String unit) {
        switch (unit) {
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
            default -> throw new RuntimeException("Unrecognized unit");
        }
    }

    private void setDimensions(int[] dimensions) {
        this.dimensions = dimensions;
    }

    private int[] getDimensions() {
        return dimensions.clone();
    }

    public boolean equals(Unit units) {
        for (int i = 0; i < dimensions.length; i++) {
            if (dimensions[i] != units.dimensions[i])
                return false;
        }

        return true;
    }

    public Unit invert() {
        int[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] *= -1;
        }

        return new Unit(ret);
    }

    public Unit add(Unit units) {
        int[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] += units.dimensions[i];
        }

        return new Unit(ret);
    }

    public Unit subtract(Unit units) {
        return add(units.invert());
    }

    public Unit multiply(int n) {
        int[] ret = dimensions.clone();

        for (int i = 0; i < dimensions.length; i++) {
            ret[i] *= n;
        }

        return new Unit(ret);
    }

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
