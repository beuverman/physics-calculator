package physics;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nuclides {
    private static String[][] data;
    private static final int DATA_SIZE = 3367;
    private static final String GROUND_STATE_PATH = "src/main/resources/nuclides/ground_states.csv";

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

    public static Quantity getBindingEnergy(String str) {
        int[] temp = parseNuclide(str);
        return getBindingEnergy(temp[0], temp[1]);
    }

    public static Quantity getBindingEnergy(int Z, int A) {
        return (new Quantity(getData(Z, A, 44) + "keV")).multiply(new Quantity(String.valueOf(A)));
    }

    public static Quantity getMass(String str) {
        int[] temp = parseNuclide(str);
        return getMass(temp[0], temp[1]);
    }

    public static Quantity getMass(int Z, int A) {
        //stored in µamu
        return new Quantity(getData(Z, A, 46) + "uDa");
    }

    public static Quantity getHalfLife(String str) {
        int[] temp = parseNuclide(str);
        return getHalfLife(temp[0], temp[1]);
    }

    public static Quantity getHalfLife(int Z, int A) {
        return new Quantity(getData(Z, A, 16) + "s");
    }

    private static String getData(int Z, int A, int column) {
        int i = searchData(Z, A);

        if (i == -1)
            throw new RuntimeException("Missing data on nuclide Z=" + Z + ", A=" + A);

        return data[i][column];
    }

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