package scanner;

import eval.ComicFilmEvaluator;
import utils.YearInterval;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.*;

public class WikipediaComicFilmScannerMain {
    static final String RESSOURCE_DIR = "out\\production\\A2-WS1920";

    public static void main(String[] args) throws MalformedURLException, IOException {
        /*
         * Wir arbeiten mit einer lokalen Datei / Kopie einer Wikipediaseite.
         * Referenz auf die Datei wird als URI String übergeben: file:///<absoluter Pfad zur Datei>
         * Path wiki3DFilmLocal = Paths.get("Listevon3D-Filmen–Wikipedia.html") erzeugt einen relativen
         * Pfad zur der Datei (Bezug das aktuelle Projekt)
         * wiki3DFilmLocal.toAbsolutePath() erzeugt den absoluten Pfad, der als
         * Argument übergeben wird.
         */
        Path wikiComicHTML = Paths.get(RESSOURCE_DIR + "\\Liste von Comicverfilmungen.html");
        ComicFilmScanner wp1 = new ComicFilmScanner("file:///" + wikiComicHTML.toAbsolutePath());


        ComicFilmEvaluator eval = new ComicFilmEvaluator();


//        1
        System.out.println("1 : Berechnen Sie eine Liste der Comics");
        System.out.println(eval.allComics());
        System.out.println("#############################################");
//        2
        System.out.println("2 : Berechnen Sie eine Liste aller Filme.");
        System.out.println(eval.alleFilme());
        System.out.println("#############################################");
//        3
        System.out.println("3 : Berechnen Sie eine Liste aller Filme die in einem gegebenen Jahr liegen.");
        System.out.println(eval.filmeImJahr(Year.of(1992)));
        System.out.println("#############################################");
//        4
        System.out.println("4 : Prüfen Sie ob es einen Film in einem gegebenen Intervall gibt. ");
        System.out.println(eval.filmeImIntervall(new YearInterval(Year.of(1992),Year.of(1998))));
        System.out.println("#############################################");
//        5
        System.out.println("5 : Comics im Jahr -Berechnen Sie eine Liste aller Filme/Comics, die in einem gegebenen Zeitintervall liegen.");
        System.out.println(eval.comicsImJahr(Year.of(2013)));
        System.out.println("#############################################");
//        6
        System.out.println("6 : Berechnen Sie eine Liste aller Filme/Comics, die in einem gegebenen Jahr liegen.");
        System.out.println(eval.comicsImInterval(new YearInterval(Year.of(1990),Year.of(1998))));
        System.out.println("#############################################");
//        7
        System.out.println("7 : Berechnen Sie eine Liste aller Comics, die miteinem gegebenen Präfix beginnen");
        String pre = "Der";
        System.out.println("Präfix: " + pre);
        System.out.println(eval.alleComicsMitPraefix(pre));
        System.out.println("#############################################");
//        maxFilme
        System.out.println("Bilde auf Anzahl aller Verfilmungen ab");
        System.out.println(eval.mapToFilmsTotal(eval.getMapValue()));
        System.out.println("#############################################");
//        8
        System.out.println("gibt den Comic mit den meisten Verfilmungen");
        System.out.println(eval.comicMitDenMeistenVerfilmungen());
        System.out.println("gibt die Comics mit den meisten Verfilmungen");
        System.out.println(eval.comicsMitDenMeistenVerfilmungen());
        System.out.println("#############################################");
//        9
        System.out.println("Prüfen Sie ob es einen Film in einemgegebenen Intervall gibt");
        System.out.println(eval.anyInInterval(new YearInterval(Year.of(1992),Year.of(1998)), eval.getMapValue()));
        System.out.println("#############################################");
//        11
        System.out.println("Gibt es einen Film/Comic vor dem Jahr");
        System.out.println(eval.anyFilmBefore(Year.of(1875)));
        System.out.println("#############################################");
        System.out.println(eval.filmsPast(Year.of(1992)));
        System.out.println("#############################################");
        System.out.println(eval.comicsPast(Year.of(1992)));
        System.out.println("#############################################");
        System.out.println(eval.comicsBefore(Year.of(1992)));
        System.out.println("#############################################");
//        12
        System.out.println(eval.gruppiereComicsNachInterval());
        System.out.println("#############################################");

        System.out.println(eval.);

        /*
         * Um direkt von der Webseite zu lesen (BITTE VORSICHT BEI NICHT
         * GETESTETEN PROGRAMMEN) wird der Parser mit der URL der Seiten
         * erzeugt. WebseitenParser wp1 = new
         * WebseitenParser("https://de.wikipedia.org/wiki/Liste_von_3D-Filmen");
         */

        /*
         * Parsen der Seite und Einsammeln der Liste von 3D-Filmen eines Jahres / eines Zeitraums
         * in einem Verzeichnis (einer Java-Map).
         */
//        long start = System.currentTimeMillis();
//        Map<String, Map<YearInterval, List<String>>> comicFilmMap = wp1.contentToComicMap();
//        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
//        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//        ppMap(comicFilmMap);
    }

    private static <K extends Comparable<? super K>> void ppMap(Map<K, Map<YearInterval, List<String>>> aMap) {
        List<K> al = new ArrayList<K>(aMap.keySet());
        Collections.sort(al);
        for (K key : al) {
            System.out.printf("%s->", key);
            for (Map.Entry<YearInterval, List<String>> entry : aMap.get(key).entrySet()) {
                System.out.printf("%s %s ", entry.getKey(), entry.getValue().toString());
            }
            System.out.println();
        }
    }


}
