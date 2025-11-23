package physics;

import java.util.HashMap;

/**
 * Encapsulates some relevant information for chemistry-related functions.
 */
public class Chemistry {
    private static final HashMap<String, Double> molarMasses = new HashMap<>();

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
        return new String[]
                {"H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne",
                "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca",
                "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn",
                "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr",
                "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn",
                "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd",
                "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb",
                "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg",
                "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th",
                "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm",
                "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds",
                "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts", "Og"};
    }

    static {
        molarMasses.put("H",  1.008);
        molarMasses.put("He", 4.0026);
        molarMasses.put("Li", 6.94);
        molarMasses.put("Be", 9.0122);
        molarMasses.put("B",  10.81);
        molarMasses.put("C",  12.011);
        molarMasses.put("N",  14.007);
        molarMasses.put("O",  15.999);
        molarMasses.put("F",  18.998);
        molarMasses.put("Ne", 20.180);
        molarMasses.put("Na", 22.990);
        molarMasses.put("Mg", 24.305);
        molarMasses.put("Al", 26.982);
        molarMasses.put("Si", 28.085);
        molarMasses.put("P",  30.974);
        molarMasses.put("S",  32.06);
        molarMasses.put("Cl", 35.45);
        molarMasses.put("Ar", 39.948);
        molarMasses.put("K",  39.098);
        molarMasses.put("Ca", 40.078);
        molarMasses.put("Sc", 44.956);
        molarMasses.put("Ti", 47.867);
        molarMasses.put("V",  50.942);
        molarMasses.put("Cr", 51.996);
        molarMasses.put("Mn", 54.938);
        molarMasses.put("Fe", 55.845);
        molarMasses.put("Co", 58.933);
        molarMasses.put("Ni", 58.693);
        molarMasses.put("Cu", 63.546);
        molarMasses.put("Zn", 65.38);
        molarMasses.put("Ga", 69.723);
        molarMasses.put("Ge", 72.630);
        molarMasses.put("As", 74.922);
        molarMasses.put("Se", 78.971);
        molarMasses.put("Br", 79.904);
        molarMasses.put("Kr", 83.798);
        molarMasses.put("Rb", 85.468);
        molarMasses.put("Sr", 87.62);
        molarMasses.put("Y",  88.906);
        molarMasses.put("Zr", 91.224);
        molarMasses.put("Nb", 92.906);
        molarMasses.put("Mo", 95.95);
        molarMasses.put("Tc", 96.906);
        molarMasses.put("Ru", 101.07);
        molarMasses.put("Rh", 102.91);
        molarMasses.put("Pd", 106.42);
        molarMasses.put("Ag", 107.87);
        molarMasses.put("Cd", 112.41);
        molarMasses.put("In", 114.82);
        molarMasses.put("Sn", 118.71);
        molarMasses.put("Sb", 121.76);
        molarMasses.put("Te", 127.60);
        molarMasses.put("I",  126.90);
        molarMasses.put("Xe", 131.29);
        molarMasses.put("Cs", 132.91);
        molarMasses.put("Ba", 137.33);
        molarMasses.put("La", 138.91);
        molarMasses.put("Ce", 140.12);
        molarMasses.put("Pr", 140.91);
        molarMasses.put("Nd", 144.24);
        molarMasses.put("Pm", 144.91);
        molarMasses.put("Sm", 150.36);
        molarMasses.put("Eu", 151.96);
        molarMasses.put("Gd", 157.25);
        molarMasses.put("Tb", 158.93);
        molarMasses.put("Dy", 162.50);
        molarMasses.put("Ho", 164.93);
        molarMasses.put("Er", 167.26);
        molarMasses.put("Tm", 168.93);
        molarMasses.put("Yb", 173.05);
        molarMasses.put("Lu", 174.97);
        molarMasses.put("Hf", 178.49);
        molarMasses.put("Ta", 180.95);
        molarMasses.put("W",  183.84);
        molarMasses.put("Re", 186.21);
        molarMasses.put("Os", 190.23);
        molarMasses.put("Ir", 192.22);
        molarMasses.put("Pt", 195.08);
        molarMasses.put("Au", 196.97);
        molarMasses.put("Hg", 200.59);
        molarMasses.put("Tl", 204.38);
        molarMasses.put("Pb", 207.2);
        molarMasses.put("Bi", 208.98);
        molarMasses.put("Po", 208.98);
        molarMasses.put("At", 209.99);
        molarMasses.put("Rn", 222.02);
        molarMasses.put("Fr", 223.02);
        molarMasses.put("Ra", 226.03);
        molarMasses.put("Ac", 227.03);
        molarMasses.put("Th", 232.04);
        molarMasses.put("Pa", 231.04);
        molarMasses.put("U",  238.03);
        molarMasses.put("Np", 237.05);
        molarMasses.put("Pu", 244.06);
        molarMasses.put("Am", 243.06);
        molarMasses.put("Cm", 247.07);
        molarMasses.put("Bk", 247.07);
        molarMasses.put("Cf", 251.08);
        molarMasses.put("Es", 252.08);
        molarMasses.put("Fm", 257.10);
        molarMasses.put("Md", 258.10);
        molarMasses.put("No", 259.10);
        molarMasses.put("Lr", 262.11);
        molarMasses.put("Rf", 267.12);
        molarMasses.put("Db", 270.13);
        molarMasses.put("Sg", 269.13);
        molarMasses.put("Bh", 270.13);
        molarMasses.put("Hs", 269.13);
        molarMasses.put("Mt", 278.16);
        molarMasses.put("Ds", 281.17);
        molarMasses.put("Rg", 281.17);
        molarMasses.put("Cn", 285.18);
        molarMasses.put("Nh", 286.18);
        molarMasses.put("Fl", 289.19);
        molarMasses.put("Mc", 289.20);
        molarMasses.put("Lv", 293.20);
        molarMasses.put("Ts", 293.21);
        molarMasses.put("Og", 294.21);
    }
}