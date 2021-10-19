package a13;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BasetypeGenerator {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        int n = Integer.parseInt(args[0]); // TODO hier die Integergröße aus den Eingabeparametern lesen
        // Gegeben NICHT ÄNDERN!!
        Object[] collector = generate(n);

        PrintWriter writer = new PrintWriter("basetype.data", "UTF-8");
        for (Object o: collector){
            writer.print(o+" ");
        }
        writer.close();

    }

    private static Object[] generate(int n){
        // Gegeben NICHT ÄNDERN!!
        Random rand = new Random(9999);

        int bools = rand.nextInt(n)+1;
        int bytes = rand.nextInt(n)+1;
        int shorts = rand.nextInt(n)+1;
        int ints = rand.nextInt(n)+1;
        int longs = rand.nextInt(n)+1;
        int floats = rand.nextInt(n)+1;
        int doubles = rand.nextInt(n)+1;
        int notClassifiedOnes = rand.nextInt(n)+1;

        Object[] collector = new Object[bools + bytes + shorts + ints + longs + floats + doubles + notClassifiedOnes];
        int startIndex = 0;
        for (int j = startIndex; j < bools; j++) {
            collector[j] = rand.nextBoolean();
        }
        startIndex = bools;
        for (int j = startIndex; j < startIndex + bytes; j++) {
            collector[j] = random(rand, Byte.MIN_VALUE, Byte.MAX_VALUE);
        }
        startIndex = bytes;
        for (int j = startIndex; j < startIndex + shorts; j++) {
            collector[j] = random(rand, Short.MIN_VALUE, Short.MAX_VALUE);
        }
        startIndex = shorts;
        for (int j = startIndex; j < startIndex + ints; j++) {
            collector[j] = random(rand,Short.MAX_VALUE+1, Integer.MAX_VALUE);
        }
        startIndex = ints;
        for (int j = startIndex; j < startIndex + longs; j++) {
            collector[j] = random(rand, Long.MIN_VALUE, Long.MAX_VALUE);
        }
        startIndex = longs;
        for (int j = startIndex; j < startIndex + floats; j++) {
            collector[j] = random(rand, Float.MIN_VALUE, Float.MAX_VALUE);
        }
        startIndex = floats;
        for (int j = startIndex; j < startIndex + doubles; j++) {
            collector[j] = random(rand, Double.MIN_VALUE, Double.MAX_VALUE);
        }
        startIndex = doubles;
        for (int j = startIndex; j < startIndex + notClassifiedOnes; j++) {
            collector[j] = "Str"+1;
        }

        // Gegeben NICHT ÄNDERN!!
        shuffle(collector,rand);
        System.out.println(Arrays.toString(collector));

        System.out.printf("bool:%d byte:%d short:%d int:%d long:%d float:%d double:%d not classified:%d\n",
                bools,bytes,shorts,ints,longs,floats,doubles,notClassifiedOnes);
        return collector;
    }

    // TODO Implementieren für alle Basisdatentypen

    private static byte random(Random rand, byte a, byte b) {
        return (byte) ((byte) (rand.nextInt()*(b-a)));
    }

    private static short random(Random rand, short a, short b) {
        return (short) ((short) (rand.nextInt()*(b-a)));
    }

    private static int random(Random rand, int a, int b) {
        return (int) rand.nextInt(b-a+1)+a;
    }

    private static long random(Random rand, long a, long b) {
        return (long) rand.nextDouble()*(b-a);
    }

    private static float random(Random rand, float a, float b) {
        return (float) rand.nextDouble()*(b-a);
    }

    private static double random(Random rand, double a, double b) {
        return rand.nextDouble()*(b-a);
    }

    private static Object[] shuffle(Object[] a, Random rand) {
        List<Object> lo = Arrays.asList(a);
        Collections.shuffle(lo, rand);
        return lo.toArray(new Object[]{});
    }
}
