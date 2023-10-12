package physics;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles interactions with the saved Nuclide data
 */
public class Nuclides {
    private static String[][] data;
    private static final int DATA_SIZE = 3367;
    private static final String GROUND_STATE_PATH = "src/main/resources/nuclides/ground_states.csv";

    /**
     * Returns a pair of integers that are the Z and A values of the given nuclide
     * @param str Nuclide to be parsed. Recognized variations include (14C, 14-C, C14, 14 C, and similar)
     * @return Returns an integer array where the first element is the Z value and the second is the A value
     */
    public static int[] parseNuclide(String str) {
        int Z, A;
        Pattern pattern = Pattern.compile("[0-9]+|[a-zA-Z]+");
        Matcher matcher = pattern.matcher(str);

        String a, b;
        matcher.find();
        a = matcher.group();
        matcher.find();
        b = matcher.group();

        if (Character.isAlphabetic(a.charAt(0))) {
            Z = getAtomicNumber(a);
            A = Integer.parseInt(b);
        }
        else {
            Z = getAtomicNumber(b);
            A = Integer.parseInt(a);
        }

        return new int[]{Z, A};
    }

    /**
     * Returns the ground state binding energy of a given nuclide
     * @param str String representation of nuclide
     * @return Returns a Quantity that is the ground state binding energy of the given nuclide
     */
    public static Quantity getBindingEnergy(String str) {
        int[] temp = parseNuclide(str);
        return getBindingEnergy(temp[0], temp[1]);
    }

    /**
     * Returns the ground state binding energy of a given nuclide
     * @param Z Number of protons in nuclide
     * @param A Atomic number of nuclide
     * @return Returns a Quantity that is the ground state binding energy of the given nuclide
     */
    public static Quantity getBindingEnergy(int Z, int A) {
        return (new Quantity(getData(Z, A, 44) + "keV")).multiply(new Quantity(String.valueOf(A)));
    }

    /**
     * Returns the mass of a given nuclide
     * @param str String representation of nuclide
     * @return Returns a Quantity that is the mass of the given nuclide
     */
    public static Quantity getMass(String str) {
        int[] temp = parseNuclide(str);
        return getMass(temp[0], temp[1]);
    }

    /**
     * Returns the mass of a given nuclide
     * @param Z Number of protons in nuclide
     * @param A Atomic number of nuclide
     * @return Returns a Quantity that is the mass of the given nuclide
     */
    public static Quantity getMass(int Z, int A) {
        //stored in µamu
        return new Quantity(getData(Z, A, 46) + "uDa");
    }

    /**
     * Returns the half-life of a given nuclide
     * @param str String representation of nuclide
     * @return Returns a Quantity that is the half-life of the given nuclide
     */
    public static Quantity getHalfLife(String str) {
        int[] temp = parseNuclide(str);
        return getHalfLife(temp[0], temp[1]);
    }

    /**
     * Returns the half-life of a given nuclide
     * @param Z Number of protons in nuclide
     * @param A Atomic number of nuclide
     * @return Returns a Quantity that is the half-life of the given nuclide
     */
    public static Quantity getHalfLife(int Z, int A) {
        return new Quantity(getData(Z, A, 16) + "s");
    }

    /**
     * Finds the entry associate with Z and A in
     * the data and returns the information in the specified column
     * @param Z Number of protons in nuclide
     * @param A Atomic number of nuclide
     * @param column Index of column where requested information is stored
     * @return Returns information in specified column in row where Z and A match
     */
    private static String getData(int Z, int A, int column) {
        int i = searchData(Z, A);

        if (i == -1)
            throw new RuntimeException("Missing data on nuclide Z=" + Z + ", A=" + A);

        return data[i][column];
    }

    /**
     * Searches the saved for an entry matching the arguments
     * @param Z Proton number of target
     * @param A Atomic number of target
     * @return Returns the index of the target in the data, -1 if not found
     */
    private static int searchData(int Z, int A) {
        if (data == null)
            loadData();

        for (int i = 0; i < DATA_SIZE; i++) {
            if (Integer.parseInt(data[i][0]) == Z && Integer.parseInt(data[i][1]) == A-Z) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Loads data from .csv file
     */
    private static void loadData() {
        data = new String[DATA_SIZE][];
        Scanner scanner;

        try {
            scanner = new Scanner(new File(GROUND_STATE_PATH));
        }
        catch (Exception e) {
            throw new RuntimeException("Missing or invalid file " + GROUND_STATE_PATH);
        }

        //Drop header
        scanner.nextLine();
        for (int i = 0; i < DATA_SIZE; i++) {
            data[i] = scanner.nextLine().split(",");
        }
    }

    /**
     * Determines atomic number from element symbol. Not case-sensitive
     * @param str Element symbol
     * @return Returns the atomic number of the given element
     */
    public static int getAtomicNumber(String str) {
        if (data == null)
            loadData();

        for (int i = 0; i < DATA_SIZE; i++) {
            if (str.equals(data[i][2])) {
                return Integer.parseInt(data[i][0]);
            }
        }

        throw new RuntimeException("Unrecognized element: " + str);
    }
}