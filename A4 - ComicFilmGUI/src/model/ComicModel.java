package model;

import scanner.ComicFilmScanner;
import utils.YearInterval;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// Die Klasse verwaltet die comicMap (Aufgabe 2) und ruft dazu den ComicFilmScanner auf.
// Sie stellt Methoden für das Filtern der Comic-Map nach verschiedenen Kriterien bereit
// Desweiteren enthält sie Methoden, um  Comic-Maps in eine Jahresintervall Map zu transformieren
// Wird vom ComicViewModel genutzt

public class ComicModel {

    private Map<String, Map<YearInterval, List<String>>> comicMap;
    private Path pathLocal;
    static final String RESSOURCE_DIR = "out\\production\\A4 - ComicFilmGUI";

    // TODO An dieser Stelle müssen Sie Ihren Scanner integrieren
    public ComicModel() throws IOException {
        pathLocal = Paths.get(RESSOURCE_DIR + "\\Liste von Comicverfilmungen.html");
        ComicFilmScanner scanner = new ComicFilmScanner("file:///" + pathLocal.toAbsolutePath());
        comicMap = scanner.contentToComicMap();

    }

    public static Map<String, Map<YearInterval, List<String>>> filterYears(
            Map<String, Map<YearInterval, List<String>>> aComicMap,
            String sub) {
        return aComicMap
                .entrySet()
                .stream()
                .map(e1 -> Map.entry(e1.getKey(),
                        e1.getValue()
                                .entrySet()
                                .stream()
                                .filter(e2 -> e2.getKey().toString().contains(sub))
                                .collect(Collectors.toMap(e3 -> e3.getKey(), e3 -> e3.getValue()))))
                .collect(Collectors.toMap(e4 -> e4.getKey(), e4 -> e4.getValue()));
    }

    public Map<String, Map<YearInterval, List<String>>> comicMap() {
        return comicMap;
    }


    public Map<YearInterval, Map<String, List<String>>> yearMap() {
        return ComicModel.yearMap(comicMap);
    }

    public static Map<YearInterval, Map<String, List<String>>> yearMap(Map<String, Map<YearInterval, List<String>>> aComicMap) {
        Map<YearInterval, Map<String, List<String>>> yearMap = new HashMap<>();

        aComicMap.forEach((comic, yearFilmList) ->
                yearFilmList.forEach((year, filmList) -> {
                            if (!yearMap.containsKey(year)) {
                                yearMap.put(year, new HashMap<String, List<String>>());
                            }
                            yearMap.get(year).put(comic, filmList);
                        }
                ));
        return yearMap;
    }

    public Map<String, Map<YearInterval, List<String>>> filterComics(String sub) {
        return comicMap
                .entrySet()
                .stream()
                .filter(e -> e.getKey().contains(sub))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public Map<String, Map<YearInterval, List<String>>> filterYears(String sub) {
        return ComicModel.filterYears(comicMap, sub);
    }

    public Map<String, Map<YearInterval, List<String>>> filterYearsBetween(Integer start, Integer end) {

        Map<String, Map<YearInterval, List<String>>> comicsFiltered = new HashMap<>();
        comicMap.forEach((comic, yearFilmMap) -> {
            yearFilmMap.forEach((year, films) ->
            {
                if (new YearInterval(Year.of(start), Year.of(end)).contains(year)) {
                    comicsFiltered.computeIfAbsent(comic, v -> new HashMap<>()).put(year, films);
                }
            });
        });
        return comicsFiltered;
    }


    public Map<String, Map<YearInterval, List<String>>> filterYearsBefore(Integer val) {
        Map<String, Map<YearInterval, List<String>>> comicsFiltered = new HashMap<>();
        comicMap.forEach((comic, yearFilmMap) -> {
            yearFilmMap.forEach((year, films) ->
            {
                if (year.before(Year.of(val))) {
                    comicsFiltered.computeIfAbsent(comic, v -> new HashMap<>()).put(year, films);
                }
            });
        });
        return comicsFiltered;
    }

    public Map<String, Map<YearInterval, List<String>>> filterYearsAfter(Integer val) {
        Map<String, Map<YearInterval, List<String>>> comicsFiltered = new HashMap<>();
        comicMap.forEach((comic, yearFilmMap) -> {
            yearFilmMap.forEach((year, films) ->
            {
                if (year.after(Year.of(val))) {
                    comicsFiltered.computeIfAbsent(comic, v -> new HashMap<>()).put(year, films);
                }
            });
        });
        return comicsFiltered;
    }
}
