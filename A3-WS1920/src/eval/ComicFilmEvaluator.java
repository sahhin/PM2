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
        System.out.println("Berechnen Sie eine Liste der Comics");
        return new ArrayList<>(comicFilmMap.keySet());
    }

    // Alle Filme
    public Set<String> alleFilme() {
        System.out.println("Berechnen Sie eine Liste aller Filme.");
        return comicFilmMap
                .values()
                .stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }


    // Alle Filme in Jahr
    public List<String> filmeImJahr(Year year) {
        System.out.println("Berechnen Sie eine Liste aller Filme/Comics, die in einem gegebenen Jahr liegen.");
        YearInterval intervalYear = new YearInterval(year);
        return comicFilmMap
                .values()
                .stream()
                .filter(e -> e.containsKey(intervalYear))
                .map(e -> e.values()).flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    // Alle Filme im Intervall
    public List<String> filmeImIntervall(YearInterval interval) {
        System.out.println("Prüfen Sie ob es einen Film in einem gegebenen Intervall gibt. ");
        return comicFilmMap
                .values()
                .stream()
                .map(yearFilmeEntry -> yearFilmeEntry.entrySet())
                .flatMap(Set::stream)
                .filter(entry -> interval.contains(entry.getKey()))
                .map(entry -> entry.getValue())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    // Comics im Jahr
    public List<String> comicsImJahr(Year year) {
        System.out.println(" !!!!!!!!!!!!!!!!!!! Comics im Jahr -Berechnen Sie eine Liste aller Filme/Comics, die in einem gegebenen Zeitintervall liegen.");
        return comicFilmMap
                .entrySet()
                .stream()
                .filter(comic -> comic.getValue().keySet().contains(year))
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }

    private ArrayList<String> getComic(String comic) {
        ArrayList<String> comicList = new ArrayList<>();
        comicList.add(comic);
        return comicList;
    }

    private int filmAnzahl(Map.Entry<String, Map<YearInterval, List<String>>> comicEntry) {
        // return comicEntry.getValue().values().stream().map(List::size).reduce(0, Integer::sum);
        return comicEntry.getValue().values().stream().mapToInt(List::size).sum();
    }

    // Comics im Intervall
    public List<String> comicsImInterval(YearInterval interval) {
//        System.out.println("Berechnen Sie eine Liste aller Filme/Comics, die in einem gegebenen Jahr liegen.");
//        ArrayList<String> comicImIntervalList = new ArrayList<>();
//        System.out.println(comicFilmMap.keySet().stream().map(e -> getYearComic(e).stream().
//                filter(interval::contains).map(m -> getComic(e)).
//                collect(Collectors.toList())).filter(r -> !r.isEmpty()).collect(Collectors.toList()));
        return null;
    }

    // Alle Comics mit Präfix pre
    public List<String> alleComicsMitPraefix(String pre) {
        System.out.println("Berechnen Sie eine Liste aller Comics, die miteinem gegebenen Präfix beginnen");
        //ArrayList<String> allComicsList = new ArrayList<>();
        return comicFilmMap.keySet().stream().filter(comic -> comic.startsWith(pre)).collect(Collectors.toList());
        //return allComicsList;
    }

    // Comic mit den meisten Verfilmungen
    private String comicMitDenMeistenVerfilmungen() {
        //return comicFilmMap.entrySet().stream().max(Comparator.comparingInt(this::filmAnzahl)).orElse("");
        return null;
    }

    // Comics mit den meisten Verfilmungen
    public List<String> comicsMitDenMeistenVerfilmungen() {
        System.out.println("Berechnen Sie einen Comic mit den meisten Verfilmungen.");
        int max = maxVerfilmungen();

        return null; //comicFilmMap.keySet().stream().filter(e -> filmAnzahl(e) == max).collect(Collectors.toList());
    }

    public void getYear() {
        comicFilmMap.keySet().forEach((k) -> comicFilmMap.get(k));
    }

    //Gibt den Map-Value zuruck
    public Map<YearInterval, List<String>> getMapValue() {
        HashMap<YearInterval, List<String>> map = new HashMap<>();
        for (Map<YearInterval, List<String>> pair : comicFilmMap.values()) {
            for (YearInterval pair2 : pair.keySet()) {
                for (String pair3 : pair.get(pair2)) {
                    if (!map.containsKey(pair2)) {
                        map.put(pair2, new ArrayList<>());
                    }
                    map.get(pair2).add(pair3);
                }
            }
        }
        return map;
    }

    // Gibt es eine Verfilmung im Interval
    public boolean anyInInterval(YearInterval interval, Map<YearInterval, List<String>> value) {
        System.out.println("Prüfen Sie ob es einen Film in einemgegebenen Intervall gibt");
        return value.keySet().stream().anyMatch(y -> interval.contains(y));
    }

    // Bilde auf Anzahl aller Verfilmungen ab
    public int mapToFilmsTotal(Map<YearInterval, List<String>> m) {
        System.out.println("Bilden Sie ein Intervall-Filme Map auf die Gesamtzahl der Verfilmungen ab.");
        return m.entrySet().stream().map((jahr) -> jahr.getValue().size()).reduce(0, (film1, film2) -> film1 + film2);
    }

    // Maximale Anzahl an Verfilmungen
    public int maxVerfilmungen() {
        return 0;
    }

    // Gibt es einen Film/Comic vor dem Jahr
    public boolean anyFilmBefore(Year year) {
        System.out.println("Prüfen Sie, ob es einen Comic/Film vor/nach einem gegebenen Jahr gibt.");
        return false;//comicFilmMap.keySet().stream().map(e -> getYearComic(e).stream().
        // filter(y -> y.before(year)).collect(Collectors.toList())).anyMatch(r -> !r.isEmpty());
    }

    // Gibt es einen Film/Comic nach dem Jahr j
    public List<String> filmsPast(Year year) {
//        System.out.println("Prüfen Sie, ob es einen Comic/Film vor/nach einem gegebenen Jahr gibt." +
//                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!                           !!!!!!!!!!!! ");
//        System.out.println(comicFilmMap.values().stream().filter(c -> c.keySet().stream().
//                anyMatch(y -> y.after(year))).collect(Collectors.toList()));
//
//        System.out.println(comicFilmMap.keySet().stream().map(e -> getYearComic(e).stream().
//                filter(y -> y.after(year)).map(m -> comicFilmMap.get(e).get(m)).
//                collect(Collectors.toList())).filter(r -> !r.isEmpty()).collect(Collectors.toList()));
        return null;
    }

    // Comics nach dem Jahr j
    public List<String> comicsPast(Year year) {
//        System.out.println("Prüfen Sie, ob es einen Comic/Film vor/nach einem gegebenen Jahr gibt."
//                + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(comicFilmMap.keySet().stream().map(e -> getYearComic(e).stream().
//                filter(y -> y.after(year)).map(m -> getComic(e)).
//                collect(Collectors.toList())).filter(r -> !r.isEmpty()).collect(Collectors.toList()));
        return null;
    }

    // Comics vor dem Jahr j
    public List<String> comicsBefore(Year year) {
//        System.out.println("Prüfen Sie, ob es einen Comic/Film vor/nach einem gegebenen Jahr gibt.");
//        System.out.println(comicFilmMap.keySet().stream().map(e -> getYearComic(e).stream().
//                filter(y -> y.before(year)).map(m -> getComic(e)).
//                collect(Collectors.toList())).filter(r -> !r.isEmpty()).collect(Collectors.toList()));
        return null;
    }

    public Stream<YearInterval> getYearStream(String comic) {
        return comicFilmMap.get(comic).keySet().stream();
    }

    // Gruppiere Comics nach Interval
    public Map<YearInterval, List<String>> gruppiereComicsNachInterval() {
        System.out.println("Gruppieren Sie die Comics nach Jahresintervall.");

        Map<YearInterval, List<String>> mapYearComic = new HashMap<>();
        comicFilmMap.entrySet().forEach(comic -> comic.getValue().entrySet().forEach(y -> {
            if (!mapYearComic.containsKey(y.getKey())) {
                mapYearComic.put(y.getKey(), new ArrayList<>());
            } else {
                y.getValue().forEach(f -> mapYearComic.get(y.getKey()).add(f));
            }
        }));
        return mapYearComic;
    }

}
