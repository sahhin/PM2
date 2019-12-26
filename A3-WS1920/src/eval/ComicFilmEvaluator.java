package eval;

import scanner.ComicFilmScanner;
import utils.YearInterval;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.*;
import java.util.function.Function;
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
        return new ArrayList<>(comicFilmMap.keySet());
    }

    // Alle Filme
    public Set<String> alleFilme() {
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

        return comicFilmMap.values()
                .stream()
                .map(yearMap -> yearMap.entrySet()
                        .stream()
                        .filter(yearEntry -> yearEntry.getKey().contains(year)))
                .map(entryStream -> entryStream.map(mapYearFilm -> mapYearFilm.getValue())
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .flatMap(List::stream)
                .collect(Collectors.toList());

    }

    // Alle Filme im Intervall
    public List<String> filmeImIntervall(YearInterval interval) {
        return comicFilmMap
                .values()
                .stream()
                .map(yearFilmeEntry -> yearFilmeEntry.entrySet())
                .flatMap(Set::stream)
                .filter(entry -> interval.contains(entry.getKey()))
                .map(filmList -> filmList.getValue())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    // Comics im Jahr
    public List<String> comicsImJahr(Year year) {
        return comicFilmMap.entrySet()
                .stream()
                .filter(comicMapFilm -> comicMapFilm.getValue()
                        .entrySet()
                        .stream()
                        .anyMatch(yearIntervalListEntry -> yearIntervalListEntry.getKey().contains(year)))
                .map(map -> map.getKey())
                .collect(Collectors.toList());

    }

    // Comics im Intervall
    public List<String> comicsImInterval(YearInterval interval) {
        return comicFilmMap.entrySet()
                .stream()
                .filter(comicMap -> comicMap.getValue()
                        .entrySet()
                        .stream()
                        .anyMatch(yearIntervalListEntry -> interval.contains(yearIntervalListEntry.getKey())))
                .map(map -> map.getKey())
                .collect(Collectors.toList());

    }

    // Alle Comics mit Präfix pre
    public List<String> alleComicsMitPraefix(String pre) {
        return comicFilmMap.keySet()
                .stream()
                .filter(comic -> comic.startsWith(pre))
                .collect(Collectors.toList());
    }

    //gibt den Anzahl an Films von einem Comic
    public int filmsperComic(Map<YearInterval, List<String>> m) {
        return m.entrySet()
                .stream()
                .mapToInt(film -> film.getValue().size()).sum();
    }

    // Comic mit den meisten Verfilmungen
    public String comicMitDenMeistenVerfilmungen() {
        Optional<Map.Entry<String,Map<YearInterval,List<String>>>> opt =  comicFilmMap.entrySet()
                        .stream()
                        .max(Comparator.comparing(comicMap -> filmsperComic(comicMap.getValue())));
        if (opt.isPresent()) {
            return opt.get().getKey();
        }
        return null;
    }

    // Comics mit den meisten Verfilmungen
    public List<String> comicsMitDenMeistenVerfilmungen() {
        int max = maxVerfilmungen();

        return comicFilmMap.entrySet()
                .stream()
                .filter(mapOfComic -> filmsperComic(mapOfComic.getValue()) == max)
                .map(mapComicFilm -> mapComicFilm.getKey())
                .collect(Collectors.toList());
    }


    // Bilde auf Anzahl aller Verfilmungen ab
    public int mapToFilmsTotal(Map<YearInterval, List<String>> m) {
        return m.values().stream().mapToInt(filmList -> filmList.size()).sum();
    }

    // Maximale Anzahl an Verfilmungen
    public int maxVerfilmungen() {
        return comicFilmMap.values()
                .stream()
                .mapToInt(yearMap -> filmsperComic(yearMap))
                .max()
                .orElse(0);
    }

    //10 BErechnen Sie die maximale Anzahl an Verfilmungen eines Comics
    public int anzahlFilmforComic(String comicName) {
        return filmsperComic(comicFilmMap.get(comicName));
    }

    // Gibt es eine Verfilmung im Interval
    public boolean anyInInterval(YearInterval interval, Map<YearInterval, List<String>> value) {
        return value.keySet()
                .stream()
                .anyMatch(yearIntervalEntry -> interval.contains(yearIntervalEntry));
    }

    // Gibt es einen Film vor dem Jahr
    public boolean anyFilmBefore(Year year) {
        return comicFilmMap.values()
                .stream()
                .anyMatch(yearIntervalListMap -> yearIntervalListMap.entrySet()
                        .stream()
                        .anyMatch(film -> film.getKey().before(year)));
    }

    // Gibt es einen Film/Comic nach dem Jahr j
    public List<String> filmsPast(Year year) {
        return comicFilmMap
                .values()
                .stream()
                .map(yearFilmeEntry -> yearFilmeEntry.entrySet())
                .flatMap(Set::stream)
                .filter(entry -> entry.getKey().after(year))
                .map(entry -> entry.getValue())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    //Filme nach dem Jahr
    public List<String> filmsBefore(Year year) {
        return comicFilmMap
                .values()
                .stream()
                .map(yearFilmeEntry -> yearFilmeEntry.entrySet())
                .flatMap(Set::stream)
                .filter(entry -> entry.getKey().before(year))
                .map(entry -> entry.getValue())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    // Comics nach dem Jahr j
    public List<String> comicsPast(Year year) {
        return comicFilmMap.entrySet()
                .stream()
                .filter(mapComicFIlm -> mapComicFIlm.getValue()
                        .entrySet()
                        .stream()
                        .anyMatch(yearIntervalListEntry -> yearIntervalListEntry.getKey().after(year)))
                .map(map -> map.getKey())
                .collect(Collectors.toList());
    }

    // Comics vor dem Jahr j
    public List<String> comicsBefore(Year year) {
        return comicFilmMap.entrySet()
                .stream()
                .filter(mapComicFIlm -> mapComicFIlm.getValue()
                        .entrySet()
                        .stream()
                        .anyMatch(yearIntervalListEntry -> yearIntervalListEntry.getKey().before(year)))
                .map(map -> map.getKey())
                .collect(Collectors.toList());
    }

    // Gruppiere Comics nach Interval
    public Map<YearInterval, List<String>> gruppiereComicsNachInterval() {
        Map<YearInterval, List<String>> mapYearComic = new HashMap<>();

        comicFilmMap.entrySet()
                .stream()
                .forEach(mapOfComic -> mapOfComic.getValue()
                        .forEach((yearEntry, filmList) -> {
//                            if (!mapYearComic.containsKey(yearEntry)) {
//                                mapYearComic.put(yearEntry, new ArrayList<>());
//                            }
//                            mapYearComic.get(yearEntry).add(mapOfComic.getKey());
                              mapYearComic.computeIfAbsent(yearEntry, list -> new ArrayList<>()).add(mapOfComic.getKey());
                        }));

        return mapYearComic;
    }
}


