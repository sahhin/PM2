package utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class WikiFilmParserMain {
    static final String RESSOURCE_DIR = "out\\production\\A2-WS1920";

    public static void main(String[] args) throws IOException, ParseException {
        Path wikiComicHTML = Paths.get(RESSOURCE_DIR + "\\Liste von Comicverfilmungen.html");
        WikiFilmParser wiki = new WikiFilmParser("file:///" + wikiComicHTML.toAbsolutePath());
//        System.out.println(wiki.readTableComicFilm2());
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
