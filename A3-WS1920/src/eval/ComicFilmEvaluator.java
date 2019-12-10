package eval;

import scanner.ComicFilmScanner;
import utils.YearInterval;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComicFilmEvaluator {

    private Path pathLocal;
    public static Map<String, Map<YearInterval, List<String>>> comicFilmMap;
    static final String RESSOURCE_DIR = "out\\production\\A2-WS1920";

    public ComicFilmEvaluator() throws IOException {
        pathLocal = Paths.get(RESSOURCE_DIR + "\\Liste von Comicverfilmungen.html");
        ComicFilmScanner scanner = new ComicFilmScanner("file:///" + pathLocal.toAbsolutePath());

        comicFilmMap = scanner.contentToComicMap();
    }


    // Alle Comics
    public ArrayList<String> allComics() {
        ArrayList<String> allComicsList = new ArrayList<>();
        comicFilmMap.keySet().forEach((k) -> allComicsList.add(k));
        return allComicsList;
        //System.out.println(comicFilmMap.keySet().stream().map(e -> e.toString()).collect(Collectors.toList()));
    }

    // Alle Filme

    public Set<String> alleFilme() {
        Set<String> allFilms = new HashSet<>();

        comicFilmMap.forEach((k, v) -> v.forEach((k1, v1) -> allFilms.addAll(v1)));

        return allFilms;
    }


    // Alle Filme in Jahr

    public List<String> filmeImJahr(Year year) {
        YearInterval intervalYear = new YearInterval(year);

        return comicFilmMap.keySet().stream().filter(e -> comicFilmMap.get(e).containsKey(intervalYear)).map(e -> comicFilmMap.get(e).get(intervalYear).toString())
                .collect(Collectors.toList());

    }

    public ArrayList<YearInterval> getYearComic(String comic) {
        ArrayList<YearInterval> yearFilmList = new ArrayList<>();

        comicFilmMap.get(comic).forEach((k, v) -> yearFilmList.add(k));
        return yearFilmList;
    }

    // Alle Filme im Intervall
    public List<String> filmeImIntervall(YearInterval interval) {

        System.out.println(comicFilmMap.keySet().stream().map(e -> getYearComic(e).stream().
                filter(y -> interval.contains(y)).map(m -> comicFilmMap.get(e).get(m)).
                collect(Collectors.toList())).filter(r -> !r.isEmpty()).collect(Collectors.toList()));
        return null;
    }

    // Comics im Jahr
    public List<String> comicsImJahr(Year year) {
        YearInterval intervalYear = new YearInterval(year);

        return comicFilmMap.keySet().stream().filter(e -> comicFilmMap.get(e).containsKey(intervalYear)).collect(Collectors.toList());
    }

    // Comics im Intervall
    public List<String> comicsImInterval(YearInterval interval) {
        System.out.println(comicFilmMap.keySet().stream().map(e -> getYearComic(e).stream().
                filter(y -> interval.contains(y)).map(m -> comicFilmMap.get(e).get(m))));

        return null;
    }


    // Alle Comics mit Präfix pre
    public List<String> alleComicsMitPraefix(String pre) {
        ArrayList<String> allComicsList = new ArrayList<>();
        comicFilmMap.keySet().stream().filter(y -> y.contains(pre)).forEach((k) -> allComicsList.add(k));
        return allComicsList;
    }

    // Comic mit den meisten Verfilmungen
    public String comicMitDenMeistenVerfilmungen() {
        return null;
    }

    // Comics mit den meisten Verfilmungen
    public List<String> comicsMitDenMeistenVerfilmungen() {
        int max = maxVerfilmungen();
        return null;
    }

    // Gibt es eine Verfilmung im Interval
    public boolean anyInInterval(YearInterval interval, Map<YearInterval, List<String>> value) {
        return false;
    }

    // Bilde auf Anzahl aller Verfilmungen ab
    private int mapToFilmsTotal(Map<YearInterval, List<String>> m) {
        return 0;
    }

    // Maximale Anzahl an Verfilmungen
    public int maxVerfilmungen() {
        return 0;
    }

    // Gibt es einen Film/Comic vor dem Jahr
    public boolean anyFilmBefore(Year year) {
        return false;
    }

    // Gibt es einen Film/Comic nach dem Jahr j
    public List<String> filmsPast(Year year) {
        return null;
    }

    // Comics nach dem Jahr j
    public List<String> comicsPast(Year year) {
        return null;
    }

    // Comics vor dem Jahr j
    public List<String> comicsBefore(Year year) {
        return null;
    }

    // Gruppiere Comics nach Interval
    public Map<YearInterval, List<String>> gruppiereComicsNachInterval() {
        return null;
    }


}
