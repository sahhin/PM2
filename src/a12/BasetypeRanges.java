package a12;

import java.util.Random;
import java.util.SortedMap;

public class BasetypeRanges {


    public static void main(String[] args) {

        // TODO Aufruf der Methode isShort

        //  TODO Aufruf der beiden add-Methoden, die einen Überlauf erzeugen

        // TODO Test, ob einen Addition einen Überlauf erzeugt. Aufruf 2'er Methoden

        // TODO Aufruf der Methoden für die korrekte Darstellung als binäre Zeichenkette byte, short char

        System.out.println(Integer.toBinaryString((Character.MAX_VALUE+Character.MAX_VALUE)));
        // TODO Ergebnisse erklären
        floatPrecision();
        doubleUnderflow();
    }


    // TODO Schreiben Sie eine Methode, die zwei byte Werte addiert
    // TODO Schreiben Sie eine Methode, die zwei short Werte addiert
    public static  short add(short a, short b){
        short result;
        result = a + b;
        return result;
    }

    // TODO Methode isShort
    public static boolean isShort(long val){
        return false;
    }

    // TODO Schreiben Sie Methoden, die prüfen, ob eine Addition einen Überlauf erzeugt hat

    // TODO Schreiben Sie eine Methode, die ein byte in korrekte Binärdarstellung umwandelt
    // TODO Schreiben Sie die Methoden für short und char





    // Gegeben. Was macht die Methode
    // Welches Ergebnis beobachten Sie? Erklären Sie das Ergebnis
    public static void floatPrecision(){
        for(int i = 1; i <=100;i++) {
            double q = 1.0/i;
            double p = q*i;
            if (p != 1.0) {
                System.out.printf("%d %1.3f\n", i, p);
            }
        }
    }

    // Gegeben. Was macht die Methode?
    // Welches Ergebnis beobachten Sie? Erklären Sie das Ergebnis
    public static void doubleUnderflow(){
        double d = 1e-305 * Math.PI;
        System.out.println("gradual underflow: " + d);
        for (int i = 0; i < 4; i++)
            System.out.println(d /= 100000);
    }
}
