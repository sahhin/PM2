package a14;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Scanner;

public class BasetypeReader {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
            scan.useLocale(Locale.US);
            scan.useDelimiter(" ");
            countBaseTypes(scan);
    }

    private static void countBaseTypes(Scanner scan) {
        int bool_ct = 0;
        int byte_ct = 0;
        int short_ct = 0;
        int int_ct = 0;
        int long_ct = 0;
        int float_ct = 0;
        int double_ct = 0;
        int notclassified_ct = 0;


        while (scan.hasNext()) {

            if (scan.hasNextBoolean()) {
                bool_ct++;
            } else if (scan.hasNextByte()) {
                byte_ct++;
            } else if (scan.hasNextShort()) {
                short_ct++;
            } else if (scan.hasNextInt()) {
                int_ct++;
            } else if (scan.hasNextLong()) {
                long_ct++;
            } else if (scan.hasNextFloat()) {
                float_ct++;
            } else if (scan.hasNextDouble()) {
                double_ct++;
            } else {
                notclassified_ct++;
            }
            scan.next();

        }
        System.out.printf("bool:%d byte:%d short:%d int:%d long:%d float:%d double:%d not classified:%d\n",
                bool_ct, byte_ct, short_ct, int_ct, long_ct, float_ct, double_ct, notclassified_ct);
    }
}

