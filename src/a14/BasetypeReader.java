package a14;

import java.util.Hashtable;
import java.util.Scanner;

public class BasetypeReader {

    public static void main(String[] args) {
        countBaseTypes();
    }

    private static void countBaseTypes() {
        Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
        ht.put("bool", 0);

        Scanner scanner = new Scanner("false null null null null false 9 null null 8 null null null null null null null null null null null null 9.325229460672683E307 1.1790321132094712E307 null null null 10 1.1220764261495076E308 null null null null null null 13 null 12 null null null null 7.964385643804441E307 false null 1.0676530560771349E308 11 14 ");
        scanner.useDelimiter(" ");

        while (scanner.hasNext()) {
            String scan = scanner.next();
            System.out.println(scan);

            if (Integer.parseInt(scan) > 0){
                int bool = ht.get("bool");
                System.out.println("HIEr");
            }


        }
        System.out.println(ht);
    }
}

