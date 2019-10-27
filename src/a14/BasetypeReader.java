package a14;

import java.util.Hashtable;
import java.util.Scanner;

public class BasetypeReader {

    public static void main(String[] args) {
        countBaseTypes();
    }

    private static void countBaseTypes() {
        System.out.println("Aufgabe 1.4: Es werden Ihre Eingaben gelesen und ausgewertet");
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("");
        Hashtable<Object, Integer> hashtable = new Hashtable<Object, Integer>();
        hashtable.put((int) 4, 0);

        while (scanner.hasNext()) {
            hashtable.put(scanner.nextInt(), 0);
             String c = scanner.next();
            if (c.charAt(0) == '\n' || c.charAt(0) == '\r') {
                System.out.println("_");
            } else {

                hashtable.forEach(
                        (k, v) -> System.out.println("Key : " + k + ", Value : " + v));
            }
        }
    }
}

