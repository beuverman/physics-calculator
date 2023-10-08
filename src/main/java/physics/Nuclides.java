package physics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles interactions with the saved Nuclide data
 */
public class Nuclides {
    private static String[][] data;
    private static final int DATA_SIZE = 3367;

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
        int i = searchData(Z, A);

        if (i == -1)
            throw new RuntimeException("No nuclide found with Z=" + Z + ", A=" + A);

        return (new Quantity(data[i][44] + "keV")).multiply(new Quantity(String.valueOf(A)));
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
        int i = searchData(Z, A);

        if (i == -1)
            throw new RuntimeException("No nuclide found with Z=" + Z + ", A=" + A);

        //stored in µamu
        return new Quantity(data[i][46] + "uDa");
    }

    /**
     * Searches the saved for an entry matching the arguments
     * @param Z Proton number of target
     * @param A Atomic number of target
     * @return Returns the index of the target in the data, -1 if not found
     */
    private static int searchData(int Z, int A) {
        if (data == null) {
            try {
                loadData();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < DATA_SIZE; i++) {
            if (Integer.parseInt(data[i][0]) == Z && Integer.parseInt(data[i][1]) == A-Z) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Loads data from .csv file
     * @throws FileNotFoundException If data is not found
     */
    private static void loadData() throws FileNotFoundException {
        data = new String[DATA_SIZE][];
        Scanner scanner = new Scanner(new File("src/main/resources/nuclides/ground_states.csv"));

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
        return switch (str.toUpperCase()) {
            case "H" -> 1;
            case "HE" -> 2;
            case "LI" -> 3;
            case "BE" -> 4;
            case "B" -> 5;
            case "C" -> 6;
            case "N" -> 7;
            case "O" -> 8;
            case "F" -> 9;
            case "NE" -> 10;
            case "NA" -> 11;
            case "MG" -> 12;
            case "AL" -> 13;
            case "SI" -> 14;
            case "P" -> 15;
            case "S" -> 16;
            case "CL" -> 17;
            case "AR" -> 18;
            case "K" -> 19;
            case "CA" -> 20;
            case "SC" -> 21;
            case "TI" -> 22;
            case "V" -> 23;
            case "CR" -> 24;
            case "MN" -> 25;
            case "FE" -> 26;
            case "CO" -> 27;
            case "NI" -> 28;
            case "CU" -> 29;
            case "ZN" -> 30;
            case "GA" -> 31;
            case "GE" -> 32;
            case "AS" -> 33;
            case "SE" -> 34;
            case "BR" -> 35;
            case "KR" -> 36;
            case "RB" -> 37;
            case "SR" -> 38;
            case "Y" -> 39;
            case "ZR" -> 40;
            case "NB" -> 41;
            case "MO" -> 42;
            case "TC" -> 43;
            case "RU" -> 44;
            case "RH" -> 45;
            case "PD" -> 46;
            case "AG" -> 47;
            case "CD" -> 48;
            case "IN" -> 49;
            case "SN" -> 50;
            case "SB" -> 51;
            case "TE" -> 52;
            case "I" -> 53;
            case "XE" -> 54;
            case "CS" -> 55;
            case "BA" -> 56;
            case "LA" -> 57;
            case "CE" -> 58;
            case "PR" -> 59;
            case "ND" -> 60;
            case "PM" -> 61;
            case "SM" -> 62;
            case "EU" -> 63;
            case "GD" -> 64;
            case "TB" -> 65;
            case "DY" -> 66;
            case "HO" -> 67;
            case "ER" -> 68;
            case "TM" -> 69;
            case "YB" -> 70;
            case "LU" -> 71;
            case "HF" -> 72;
            case "TA" -> 73;
            case "W" -> 74;
            case "RE" -> 75;
            case "OS" -> 76;
            case "IR" -> 77;
            case "PT" -> 78;
            case "AU" -> 79;
            case "HG" -> 80;
            case "TL" -> 81;
            case "PB" -> 82;
            case "BI" -> 83;
            case "PO" -> 84;
            case "AT" -> 85;
            case "RN" -> 86;
            case "FR" -> 87;
            case "RA" -> 88;
            case "AC" -> 89;
            case "TH" -> 90;
            case "PA" -> 91;
            case "U" -> 92;
            case "NP" -> 93;
            case "PU" -> 94;
            case "AM" -> 95;
            case "CM" -> 96;
            case "BK" -> 97;
            case "CF" -> 98;
            case "ES" -> 99;
            case "FM" -> 100;
            case "MD" -> 101;
            case "NO" -> 102;
            case "LR" -> 103;
            case "RF" -> 104;
            case "DB" -> 105;
            case "SG" -> 106;
            case "BH" -> 107;
            case "HS" -> 108;
            case "MT" -> 109;
            case "DS" -> 110;
            case "RG" -> 111;
            case "CN" -> 112;
            case "NH" -> 113;
            case "FL" -> 114;
            case "MC" -> 115;
            case "LV" -> 116;
            case "TS" -> 117;
            case "OG" -> 118;
            default -> throw new RuntimeException("Unrecognized element: " + str);
        };
    }
}