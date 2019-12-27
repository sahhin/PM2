package sample.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.ComicModel;
import utils.YearInterval;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComicViewModel {

    private StringProperty comicProperty;

    private Map<String, Map<YearInterval, List<String>>> currentComicMap;
    private ComicModel comicModel;

    // Property, die anzeigt, dass ein neues Comic selektiert wurde
    public StringProperty comicProperty() {
        return comicProperty;
    }

    public String getComic() {
        return comicProperty.get();
    }

    public void setComic(String comic) {
        System.out.println("ComicProperty setting value " + comic);
        comicProperty.set(comic);
    }

    private ObjectProperty<YearInterval> yearIntervalProperty;

    // Property, die anzeigt, dass ein neues Jahresintervall selektiert wurde
    public ObjectProperty<YearInterval> yearIntervalProperty() {return yearIntervalProperty;}

    public void setYearInterval(YearInterval value) {
        System.out.println("YearIntervalProperty setting value " + value);

        this.yearIntervalProperty.set(value);
    }
    public YearInterval getYearInterval() {
        return yearIntervalProperty.get();
    }


    // Erzeugt das ComicViewModel, initialisiert die Properties für die Synchronisation der Views
    // erzeugt ein ComicModel Objekt, dass die ComicMap verwaltet
    public ComicViewModel() throws IOException {
        comicModel = new ComicModel();
        comicProperty = new SimpleStringProperty();
        yearIntervalProperty = new SimpleObjectProperty<>();
    }


    // Liefert eine Map der Comics, die nach Jahren organisiert ist, berücksichtigt die zuletzt eingestellten Filter in
    // der ComicSicht
     public Map<YearInterval, Map<String, List<String>>> currentYearMap() {
        return ComicModel.yearMap(currentComicMap());
    }

    // Liefert die ungefilterte ComicMap
    public Map<String, Map<YearInterval, List<String>>> comicMap() {
        return comicModel.comicMap();
    }


    // Liefert die gefilterte ComicMap
    public Map<String, Map<YearInterval, List<String>>> currentComicMap() {
        if (currentComicMap == null) currentComicMap = comicModel.comicMap();
        return currentComicMap;
    }


    // Prädikate, damit die View entscheiden kann, in welcher Ansicht sie sich befindet
    public boolean isComic(Comparable<?> aComp) {
        return aComp instanceof String && comicModel.comicMap().containsKey(aComp);
    }

    public boolean isYearInterval(Comparable<?> aComp) {
        return aComp instanceof YearInterval;
    }


    // Das Filtern der Comics über Teilstringsuche
    public Map<String, Map<YearInterval, List<String>>> filterComics(String sub) {
        System.out.println("Filtering Comics containing " + sub);
        if (sub == null || sub.isEmpty()) currentComicMap = null;
        else {
            currentComicMap = comicModel.filterComics(sub.toUpperCase());
        }
        System.out.println(currentComicMap);
        return currentComicMap();
    }

    // Das Filtern der Jahresangaben über Teilstringsuche
    // Die Filterlogik steckt im ComicModel
    public Map<YearInterval, Map<String, List<String>>> filterYears(String sub) {
        System.out.println("Filtering Year Intervals containing " + sub);
        if (sub == null || sub.isEmpty()) currentComicMap=null;
        else {
            currentComicMap = ComicModel.filterYears(currentComicMap,sub);
        }
        return currentYearMap();
    }

    // Das Filtern der Jahresangaben über Intervalle
    public Map<YearInterval, Map<String, List<String>>> filterYearsBetween(Integer start, Integer end) {
        currentComicMap = comicModel.filterYearsBetween(start,end);
        System.out.println(currentComicMap);
        return currentYearMap();
    }

    // Das Filtern der Jahresangaben, die vor einem Zeitpunkt liegen
    public Map<String, Map<YearInterval, List<String>>> filterYearsBefore(Integer val) {

        currentComicMap = comicModel.filterYearsBefore(val);
        System.out.println(currentComicMap);
        return currentComicMap();
    }

    // Das Filtern der Jahresangaben, die nach einem Zeitpunkt liegen
    public Map<YearInterval, Map<String, List<String>>> filterYearsAfter(Integer val) {
        currentComicMap = comicModel.filterYearsAfter(val);
        return currentYearMap();
    }


    // Bereitet für ein Comic die Einträge als Map<String, String> auf.
    // Die Liste der Filme wird in einen String umgewandelt
    // Wird für die Tabellensicht benötigt
    public Map<String, String> get(String comic) {
        Map<String, String> stringMap = new HashMap<>();
        if (isComic(comic)) {
            Map<YearInterval, List<String>> map = currentComicMap().get(comic);
            stringMap =
                    map.entrySet().stream().map(entry ->
                            Map.entry(entry.getKey().toString(),
                                    entry.getValue().stream().collect(
                                            Collectors.joining("\n")
                                    ))).collect(Collectors.toMap(entry -> entry.getKey(),
                            entry -> entry.getValue()));
        }
        System.out.println("get comic");
        System.out.println(stringMap);
        return stringMap;
    }


    // Bereitet für ein Jahresintervall die Einträge als Map<String,String> auf.
    // Die Liste der Filme wird in einen String umgewandelt
    // Wird für die Tabellensicht benötigt
    public Map<String, String> get(YearInterval yi) {
        Map<String, List<String>> mss = currentYearMap().get(yi);
        if (mss != null) {
            return mss.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                    e -> e.getValue().stream().collect(Collectors.joining("\n"))));

        }
        return new HashMap<>();
    }
}
