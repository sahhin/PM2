package uimodelhelper;

import javafx.scene.control.TreeItem;
import utils.YearInterval;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Methoden der Klasse transformieren Maps in TreeItem-Hierarchien
public class TreeModelHelper {

    // erzeugt die TreeItem-Hierarchie für die Comic-Map 
    public static TreeItem<Comparable> createComicTree(TreeItem<Comparable> root, 
                                                       Map<String, Map<YearInterval, List<String>>> comicMap) {

        List<TreeItem<Comparable>> comicNodes = comicMap.entrySet().stream().map(comicEntry -> {
            TreeItem<Comparable> comicNode = new TreeItem<>(comicEntry.getKey());
            comicNode.getChildren().addAll(
                    comicEntry.getValue()
                            .entrySet()
                            .stream().map(yearFilmsEntry -> {
                        TreeItem<Comparable> yearNode = new TreeItem<Comparable>(yearFilmsEntry.getKey());
                        yearNode.getChildren().addAll(
                                yearFilmsEntry
                                        .getValue()
                                        .stream()
                                        .map(film -> {
                                            TreeItem<Comparable> filmNode = new TreeItem<>(film);
                                            return filmNode;
                                        })
                                        .collect(Collectors.toList()));
                        return yearNode;
                    }).collect(Collectors.toList()));
            return comicNode;
        }).collect(Collectors.toList());
        root.getChildren().addAll((List<TreeItem<Comparable>>)comicNodes.stream().sorted(Comparator.comparing(ti -> ti.getValue())).collect(Collectors.toList()));
        return root;
    }

    // erzeugt die TreeItem-Hierarchie für die Jahresintervall-Map 
    public static TreeItem<Comparable> createYearTree(TreeItem<Comparable> root, Map<YearInterval, Map<String, List<String>>> yearMap) {

        List<TreeItem<Comparable>> yearNodes = yearMap.entrySet().stream().map(comicEntry -> {
            TreeItem<Comparable> yearNode = new TreeItem<>(comicEntry.getKey());
            yearNode.getChildren().addAll(
                    comicEntry.getValue()
                            .entrySet()
                            .stream().map(comicFilmEntries -> {
                        TreeItem<Comparable> comicNode = new TreeItem<Comparable>(comicFilmEntries.getKey());
                        comicNode.getChildren().addAll(
                                comicFilmEntries
                                        .getValue()
                                        .stream()
                                        .map(film -> {
                                            TreeItem<Comparable> filmNode = new TreeItem<>(film);
                                            return filmNode;
                                        })
                                        .collect(Collectors.toList()));
                        return comicNode;
                    }).collect(Collectors.toList()));
            return yearNode;
        }).collect(Collectors.toList());
        root.getChildren().addAll((List<TreeItem<Comparable>>)yearNodes.stream().sorted(Comparator.comparing(ti -> ti.getValue())).collect(Collectors.toList()));
        return root;
    }

}