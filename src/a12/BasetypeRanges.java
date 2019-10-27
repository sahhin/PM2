package a12;

import sun.nio.cs.ext.SJIS;

import java.util.Random;
import java.util.SortedMap;

public class BasetypeRanges {

    static final byte valueByte = 127;
    static final short valueShort = -30;
    static final char c = '\u00FF';

    public static void main(String[] args) {

        // TODO Aufruf der Methode isShort

        System.out.println(isShort2(-32769));
        System.out.println(isShort(32768));

        //  TODO Aufruf der beiden add-Methoden, die einen Überlauf erzeugen
        // neg + neg = pos Ergebnis
        System.out.println(Short.MIN_VALUE + " + " + "(" + Short.MIN_VALUE + ") = " + addShort(Short.MIN_VALUE, Short.MIN_VALUE));

        // pos + pos = neg Ergebnis
        System.out.println(Short.MAX_VALUE + " + " + Short.MAX_VALUE + " = " + addShort(Short.MAX_VALUE, Short.MAX_VALUE));

        // pos + pos = neg Ergebnis
        System.out.println(Byte.MAX_VALUE + " + " + Byte.MAX_VALUE + " = " + addByte(Byte.MAX_VALUE, Byte.MAX_VALUE));

        // neg + neg = pos Ergebnis
        System.out.println(Byte.MIN_VALUE + " + " + "(" + Byte.MIN_VALUE + ") = " + addByte(Byte.MIN_VALUE, Byte.MIN_VALUE));

        // TODO Test, ob einen Addition einen Überlauf erzeugt. Aufruf 2'er Methoden
        System.out.println(Byte.MIN_VALUE + " + " + Byte.MAX_VALUE + " = " + overflowAddByte(Byte.MIN_VALUE, Byte.MAX_VALUE));
        System.out.println(Short.MIN_VALUE + " + " + Short.MAX_VALUE + " = " + overflowAddShort(Short.MIN_VALUE, Short.MAX_VALUE));

        // TODO Aufruf der Methoden für die korrekte Darstellung als binäre Zeichenkette byte, short char
        System.out.println(toBinaryStringByte(valueByte));
        System.out.println("hier");
        System.out.println(toBinaryStringShort(valueShort));
        System.out.println(toBinaryStringChar(c));
        System.out.println(Integer.toBinaryString((Character.MAX_VALUE + Character.MAX_VALUE)));

        // TODO Ergebnisse erklären
        floatPrecision();
        doubleUnderflow();
    }

    // TODO Schreiben Sie eine Methode, die zwei byte Werte addiert
    // TODO Schreiben Sie eine Methode, die zwei short Werte addiert
    private static int addShort(short a, short b) {
        return ((short) (a + b));
    }

    private static byte addByte(byte a, byte b) {
        return ((byte) (a + b));
    }

    // TODO Methode isShort
    private static boolean isShort(long val) {
        return (val >= Short.MIN_VALUE) && (val <= Short.MAX_VALUE);
    }

    private static boolean isShort2(long val) {
        Long longObj = val;
        Short shortValue = longObj.shortValue();
        Long longValue = (long) (longObj.shortValue());

        return longValue.equals(longObj);
    }

    // TODO Schreiben Sie Methoden, die prüfen, ob eine Addition einen Überlauf erzeugt hat
    private static boolean overflowAddShort(short s1, short s2) {
        return !isShort2(addShort(s1, s2));
    }

    private static boolean overflowAddByte(byte b1, byte b2) {
        short sumBytes = addByte(b1, b2);
        return (sumBytes >= Byte.MIN_VALUE) && (sumBytes <= Byte.MAX_VALUE);
    }

    // TODO Schreiben Sie eine Methode, die ein byte in korrekte Binärdarstellung umwandelt
    private static String toBinaryStringByte(byte byteValue) {
        String binInt = Integer.toBinaryString((int) byteValue);

        if (byteValue < 0) {
            return binInt.substring(binInt.length() - 8);
        } else {
            return String.format("%08d", Integer.parseInt(binInt));
        }
    }

    // TODO Schreiben Sie die Methoden für short und char
    private static String toBinaryStringShort(short valShort) {
        String binInt = Integer.toBinaryString(valShort);

        if (valShort < 0) {
            return binInt.substring(binInt.length() - 16);
        } else {
            //         return binInt;
            return String.format("%016d", Integer.parseInt(binInt));
        }
    }

    private static String toBinaryStringChar(char c) {
        return Integer.toBinaryString(c);
    }

    // Gegeben. Was macht die Methode
    // Welches Ergebnis beobachten Sie? Erklären Sie das Ergebnis
    private static void floatPrecision() {
        for (int i = 1; i <= 100; i++) {
            double q = 1.0 / i; // q = 1.0 / 10**2 = 1.0*10**-2= 0.01
            double p = q * i; // p = 1.0*10**-2 * 10**2
            if (p != 1.0) {
                System.out.printf("%d %1.3f\n", i, p);
            }
        }
    }

    // Gegeben. Was macht die Methode?
    // Welches Ergebnis beobachten Sie? Erklären Sie das Ergebnis
    private static void doubleUnderflow() {
        double d = 1e-305 * Math.PI;
        System.out.println("gradual underflow: " + d);
        for (int i = 0; i < 4; i++)
            System.out.println(d /= 100000);
    }
}
