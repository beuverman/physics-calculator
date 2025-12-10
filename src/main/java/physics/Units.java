package physics;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Handles definitions of constants and non-SI units
 * Handles SI prefixes
 */
public class Units extends Quantity {
    private static final String[] smallPrefixes = {"d", "c", "m", "u", "n", "p", "f", "a", "z", "y", "r", "q"};
    private static final String[] bigPrefixes = {"da", "h", "k", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q"};
    private static final HashMap<String, Units> UNITS;
    private static final HashMap<String, Units> CONSTANTS;

    private String alias;

    /**
     * Creates a Unit with a given Quantity, and alias to identify it by
     * @param quantity Quantity of the unit
     * @param alias Alias of the unit
     */
    public Units(String quantity, String alias) {
        super(quantity);
        this.alias = alias;
    }

    /**
     * Creates a Unit with a given value, dimension, and alias to identify it by
     * @param value Value of the unit
     * @param dimensions Dimensions of the unit
     * @param alias Alias of the unit
     */
    private Units(String value, Dimension dimensions, String alias) {
        super(value, dimensions);
        this.alias = alias;
    }

    /**
     * Returns the unit with the given alias.
     * Handles SI prefixes.
     * @param str Alias of the unit to be returned
     * @return Returns the unit with the given alias
     */
    public static Quantity getUnit(String str) {
        // No prefix
        if (str.equals("g")) {
            return new Quantity("0.001", new Dimension("kg"));
        }
        if (UNITS.containsKey(str)) {
            return UNITS.get(str);
        }
        try {
            Dimension dim = new Dimension(str);
            return new Quantity("1", dim);
        }
        catch (Exception ignored) {}

        // Prefix
        // For the only 2 character prefix: da
        if (str.length() == 1)
            throw new RuntimeException("Unrecognized unit " + str);
        Quantity prefix = Units.getPrefix(str.substring(0, 2));
        // 1 character prefix
        if (prefix == null) {
            prefix = Units.getPrefix(str.substring(0, 1));

            if (prefix == null)
                throw new RuntimeException("Unrecognized unit " + str);

            return getUnit(str.substring(1)).multiply(prefix);
        }
        else {
            return getUnit(str.substring(2)).multiply(prefix);
        }
    }

    /**
     * Returns the constants that has the given alias
     * @param str Alias of the constant to be returned
     * @return Returns the quantity that represents the constant
     */
    public static Quantity getConstant(String str) {
        return CONSTANTS.getOrDefault(str, null);
    }

    /**
     * Determines the modifier associated with the given SI prefix
     * @param str The prefix to be used
     * @return Returns a dimensionless quantity with the value associated with the prefix, null if prefix not recognized
     */
    public static Quantity getPrefix(String str) {
        int pow;

        for (int i = 0; i < smallPrefixes.length; i++) {
            if (str.equals(smallPrefixes[i]) || str.equals(bigPrefixes[i])) {
                pow = switch (i) {
                    case 0 -> 1;
                    case 1 -> 2;
                    default -> (i - 1) * 3;
                };

                if (str.equals(smallPrefixes[i]))
                    pow *= -1;
                return new Quantity("1e" + pow);
            }
        }

        return null;
    }

    /**
     * Describes an entry in a JSON file defining a new unit or constant.
     * @param name Name of the entry.
     * @param symbol Symbol for use in equations.
     * @param value Value of entry in base SI units.
     * @param dims Dimensions of entry.
     */
    public record JSONEntry(
            String name,
            String symbol,
            String value,
            int[] dims
    ) { }

    // Initialize new units or constants from their JSON files.
    static {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File("src/main/resources/data/physical_constants.json");
        List<JSONEntry> jsonEntries = mapper.readValue(file, new TypeReference<>() {});
        CONSTANTS = new HashMap<>();
        for (JSONEntry constant : jsonEntries) {
            CONSTANTS.put(constant.symbol(), new Units(constant.value(), new Dimension(constant.dims()), constant.symbol()));
        }

        file = new File("src/main/resources/data/additional_units.json");
        jsonEntries = mapper.readValue(file, new TypeReference<>() {});
        UNITS = new HashMap<>();
        for (JSONEntry constant : jsonEntries) {
            UNITS.put(constant.symbol(), new Units(constant.value(), new Dimension(constant.dims()), constant.symbol()));
        }
    }
}