package a15;

import java.util.Random;

public class RingReader {

    public static void main(String[] args) {
        int length = 10; // TODO Größe des Arrays aus den Eingabedaten lesen!!!!
        int[] a = new int[length];
        Random rand = new Random(9999);
        for (int i = 0; i< length; i++) {
            a[i] = rand.nextInt(length);
        }
        int ct =0;
        while (true) {
            System.out.println(a[ct++ % a.length]); // 0%10=0, 1%10=1, .., 9%10=9, 10%10=0
        }
    }
}
