package a14;

import java.util.Scanner;

public class BasetypeReader {

    public static void main(String[] args) {
        countBaseTypes();
    }

    private static void countBaseTypes() {
        System.out.println("Aufgabe 1.4: Es werden Ihre Eingaben gelesen und ausgewertet");
        Scanner scanner = new Scanner(System.in);
        int i = scanner.nextInt();
        double d = scanner.nextDouble();
        float f = scanner.nextFloat();
        long l = scanner.nextLong();
        boolean b = scanner.hasNextBoolean();

        System.out.println(i);
    }
}
