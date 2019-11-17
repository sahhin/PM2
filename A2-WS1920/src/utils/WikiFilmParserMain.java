package utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WikiFilmParserMain {
    static final String RESSOURCE_DIR = "out\\production\\A2-WS1920";

    public static void main(String[] args) throws IOException {
        Path wikiComicHTML = Paths.get(RESSOURCE_DIR + "\\Liste von Comicverfilmungen.html");
        WikiFilmParser wiki = new WikiFilmParser("file:///" + wikiComicHTML.toAbsolutePath());
//        System.out.println(wiki.contentTo3DFilmsPerYear());
        System.out.println(wiki.tryReader());
//        long start = System.currentTimeMillis();
//        Map<String, List<String>> threeDeeMap1 = wiki.contentTo3DFilmsPerYear();
//        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
//        ppMap(threeDeeMap1);
    }

    private static <K extends Comparable<? super K>> void ppMap(Map<K, List<K>> aMap) {
        List<K> al = new ArrayList<K>(aMap.keySet());
        Collections.sort(al);
        for (K key : al) {
            System.out.println(key);
            for (K o : aMap.get(key)) {
                System.out.println(o);
            }
            System.out.println();
        }
    }
}
