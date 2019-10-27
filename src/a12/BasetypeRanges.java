package a12;

import sun.nio.cs.ext.SJIS;

import java.util.Random;
import java.util.SortedMap;

public class BasetypeRanges {

    public static void main(String[] args) {
        BasetypeRanges btr = new BasetypeRanges();

        // TODO Aufruf der Methode isShort

        System.out.println(btr.isShort2(-32769));
        System.out.println(btr.isShort(32768));

        //  TODO Aufruf der beiden add-Methoden, die einen Überlauf erzeugen
        // neg + neg = pos Ergebnis
        System.out.println(Short.MIN_VALUE + " + " + "(" + Short.MIN_VALUE + ") = " + btr.addShort(Short.MIN_VALUE, Short.MIN_VALUE));

        // pos + pos = neg Ergebnis
        System.out.println(Short.MAX_VALUE + " + " + Short.MAX_VALUE + " = " + btr.addShort(Short.MAX_VALUE, Short.MAX_VALUE));

        // pos + pos = neg Ergebnis
        System.out.println(Byte.MAX_VALUE + " + " + Byte.MAX_VALUE + " = " + btr.addByte(Byte.MAX_VALUE, Byte.MAX_VALUE));

        // neg + neg = pos Ergebnis
        System.out.println(Byte.MIN_VALUE + " + " + "(" + Byte.MIN_VALUE + ") = " + btr.addByte(Byte.MIN_VALUE, Byte.MIN_VALUE));

        // TODO Test, ob einen Addition einen Überlauf erzeugt. Aufruf 2'er Methoden
        System.out.println(Byte.MIN_VALUE + " + " + Byte.MAX_VALUE + " = " + btr.overflowAddByte(Byte.MIN_VALUE, Byte.MAX_VALUE));
        System.out.println(Short.MIN_VALUE + " + " + Short.MAX_VALUE + " = " + btr.overflowAddShort(Short.MIN_VALUE, Short.MAX_VALUE));

        // TODO Aufruf der Methoden für die korrekte Darstellung als binäre Zeichenkette byte, short char
        byte valueByte = -64;
        System.out.println(btr.toBinaryStringByte(valueByte));

        short valueShort = 32678;
        System.out.println(btr.toBinaryStringShort(valueShort));

        char c = '\u00FF';
        System.out.println(btr.toBinaryStringChar(c));

        System.out.println(Integer.toBinaryString((Character.MAX_VALUE + Character.MAX_VALUE)));

        // TODO Ergebnisse erklären
//        floatPrecision();
//        doubleUnderflow();
    }

    // TODO Schreiben Sie eine Methode, die zwei byte Werte addiert
    // TODO Schreiben Sie eine Methode, die zwei short Werte addiert
    public static int addShort(short a, short b) {
        return ((short) (a + b));
    }

    public static byte addByte(byte a, byte b) {
        return ((byte) (a + b));
    }

    // TODO Methode isShort
    public static boolean isShort(long val) {
        if ((val >= Short.MIN_VALUE) && (val <= Short.MAX_VALUE)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isShort2(long val) {
        Long longObj = new Long(val);
        Short shortValue = longObj.shortValue();
        Long longValue = new Long(longObj.shortValue());

        return (longValue.equals(longObj)) ? true : false;
    }

    // TODO Schreiben Sie Methoden, die prüfen, ob eine Addition einen Überlauf erzeugt hat
    public static boolean overflowAddShort(short s1, short s2) {
        return !isShort2(addShort(s1, s2));
    }

    public static boolean overflowAddByte(byte b1, byte b2) {
        Byte byteObjSum = new Byte(addByte(b1, b2));

        if ((byteObjSum >= Byte.MIN_VALUE) && (byteObjSum <= Byte.MAX_VALUE)) {
            return false;
        } else {
            return true;
        }
    }

    // TODO Schreiben Sie eine Methode, die ein byte in korrekte Binärdarstellung umwandelt
    public static String toBinaryStringByte(byte byteValue) {
        String binInt = Integer.toBinaryString(new Integer(byteValue));
        int stringLength = 8;

        if (byteValue < 0) {
            return binInt.substring(24, 32);
        } else {
            return binInt;
            //   return String.format("%08d"+ stringLength + binInt);
        }
    }

    // TODO Schreiben Sie die Methoden für short und char
    public static String toBinaryStringShort(short valueShort) {
        String binInt = Integer.toBinaryString(new Integer(valueShort));

        if (valueShort < 0) {
            return binInt.substring(15, 32);
        } else {
            return binInt;
        }
    }

    public static String toBinaryStringChar(char c) {
        return Integer.toBinaryString(c);
    }

    // Gegeben. Was macht die Methode
    // Welches Ergebnis beobachten Sie? Erklären Sie das Ergebnis
    public static void floatPrecision() {
        for (int i = 1; i <= 100; i++) {
            double q = 1.0 / i;
            double p = q * i;
            if (p != 1.0) {
                System.out.printf("%d %1.3f\n", i, p);
            }
        }
    }

    // Gegeben. Was macht die Methode?
    // Welches Ergebnis beobachten Sie? Erklären Sie das Ergebnis
    public static void doubleUnderflow() {
        double d = 1e-305 * Math.PI;
        System.out.println("gradual underflow: " + d);
        for (int i = 0; i < 4; i++)
            System.out.println(d /= 100000);
    }
}
