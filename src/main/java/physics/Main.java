package physics;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Equation eq;

        while (true) {
            System.out.print("Equation: ");

            eq = new Equation(in.nextLine());

            System.out.println(eq.compute().toString());
            System.out.println();
        }
    }
}
