package physics;

import java.io.File;
import java.util.Map;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

/**
 * Encapsulates some relevant information for chemistry-related functions.
 */
public class Chemistry {
    private static final Map<String, Double> molarMasses;
    private static final String[] elementStrings;

    /**
     * Gets the molar mass of a given element in g/mol.
     * Information obtained from <a href="https://webelements.com">Web Elements</a>.
     * @param element Element to get molar mass of
     * @return Returns the molar mass of the given element in g/mol.
     */
    public static double getMolarMass(String element) {
        return molarMasses.get(element);
    }

    /**
     * Gets the symbols of each element
     * @return Returns an array of symbols, so that the symbol for atomic number Z is found at index Z-1
     */
    public static String[] getElementStrings() {
        return elementStrings;
    }

    static {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/resources/data/periodic_table.json");
        molarMasses = mapper.readValue(file, new TypeReference<>() {});
        elementStrings = molarMasses.keySet().toArray(new String[0]);
    }
}