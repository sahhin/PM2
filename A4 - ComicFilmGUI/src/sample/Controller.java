package sample;

import com.sun.jdi.Value;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.text.Text;
import model.ComicModel;
import sample.viewmodel.ComicViewModel;
import uimodelhelper.TreeModelHelper;
import utils.YearInterval;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Controller {

    private ComicTreeView comicTreeview;

    //hier wird für jedes UI Element mit fx:id
    // eine Instanz-Variable, die mit @FXML annotiert ist
    @FXML
    private TreeView<Comparable> comicTree;

    @FXML
    private Text searchByFilmText;

    @FXML
    private TextField searchByText;

    @FXML
    private TableView<String> filmListView = new TableView<>();

    @FXML
    private TableColumn<String, String> listYear = new TableColumn<>();

    @FXML
    private TableColumn<String, String> listName = new TableColumn<>();

    @FXML
    private RadioButton vorRadioButton;

    @FXML
    private TextField vorText;

    @FXML
    private RadioButton nachRadioButton;

    @FXML
    private TextField nachText;

    @FXML
    private RadioButton vonbisRadioButton;

    @FXML
    private TextField vonbisText1;

    @FXML
    private TextField vonbisText2;

    private Pattern pattern = Pattern.compile("\\d\\d\\d\\d");
    private String year1 = "";
    private String year2 = "";

    private boolean toggleView = true;
    private final String switchToComicsText = "nach Namen sortieren";
    private final String switchToYearText = "nach Jahren sortieren";


    //im initialize werden nur die Daten der Applikation
    //generiert und Objekte der inneren Klassen erzeugt
    @FXML
    protected void initialize() throws IOException {
        //erzeugen der Daten für die Applikation
        ComicViewModel comicViewModel = new ComicViewModel();

        ComicTreeView comicTreeView = new ComicTreeView(comicViewModel);
        comicTreeView.initialize();

    }

//private innere Klassen

    private class ComicTreeView {
        private ComicViewModel comicViewModel;
        private ComicModel comicModel;

        public ComicTreeView(ComicViewModel comicViewModel) {
            this.comicViewModel = comicViewModel;
            this.comicViewModel.comicProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                    if (oldValue == null || (newValue != null && !oldValue.equals(newValue))) {
                        System.out.println("hey");
                    }
                }
            });
        }


        public void initialize() {
            //Erzeugen der TreeView auf Basis alle Comics
            TreeItem<Comparable> treeItemRoot = TreeModelHelper.createComicTree(new TreeItem<>("nach Comics"), comicViewModel.comicMap());

            //Verknupfung
            comicTree.setRoot(treeItemRoot);
            comicTree.setShowRoot(true);
            comicTree.getRoot().setExpanded(true);
            comicTree.getRoot().getChildren();


//            klick auf linkes element
            comicTree.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        if (newValue != null && newValue.getValue() instanceof String) {
                            comicViewModel.setComic((String) newValue.getValue());
                        }
                        if (newValue != null && newValue.getValue() instanceof YearInterval) {
                            comicViewModel.setYearInterval((YearInterval) newValue.getValue());
                        }

                        List<String> namesList = new ArrayList<>(comicViewModel.get(comicViewModel.getComic()).values());
                        ObservableList<String> namesObs = FXCollections.observableArrayList(namesList);

                        List<String> yearsList = new ArrayList<>(comicViewModel.get(comicViewModel.getComic()).values());
                        ObservableList<String> yearsObs = FXCollections.observableArrayList(yearsList);

                        yearsObs.forEach(k ->  listYear.setCellValueFactory(p -> new
                                ReadOnlyObjectWrapper<>(yearsObs.get(yearsObs.indexOf(k)))));


                        namesObs.forEach(k ->  listName.setCellValueFactory(p -> new
                                ReadOnlyObjectWrapper<>(namesObs.get(namesObs.indexOf(k)))));

                        filmListView.setItems(yearsObs);
                        filmListView.setItems(namesObs);
                        filmListView.getColumns().setAll(listYear,listName);
                    });

            comicTree.setEditable(true);

            comicTree.setCellFactory((TreeView<Comparable> tv) -> new TextFieldTreeCell<>() {
                private final MenuItem switchItem;

                private final ContextMenu switchViewMenu = new ContextMenu();

                {
                    switchItem = new MenuItem();
                    switchViewMenu.getItems().add(switchItem);
                    switchItem.setOnAction((ActionEvent e) -> {
                        if (toggleView) {
                            comicTree.setRoot(TreeModelHelper.createYearTree(new TreeItem<>("nach Jahren"), comicViewModel.currentYearMap()));
                            toggleView = false;
                        } else {
                            comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>("nach Comics"), comicViewModel.comicMap()));
                            toggleView = true;
                        }

                        comicViewModel.setComic(null);
                        comicViewModel.setYearInterval(null);
                        comicTree.setShowRoot(true);
                        comicTree.getRoot().setExpanded(true);
                        return;
                    });
                    setEditable(false);
                    setContextMenu(switchViewMenu);
                }

                @Override
                public void updateSelected(boolean selected) {
                    if (switchItem != null) {
                        switchItem.setText(toggleView ? switchToYearText : switchToComicsText);
                    }
                }
            });


            vonbisRadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {

                    if (isNowSelected) {
                        vonbisText1.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!((newValue.equals("") || newValue.isEmpty()))) {
                                year1 = newValue;
                            }
                        });

                        vonbisText2.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!((newValue.equals("") || newValue.isEmpty()))) {
                                year2 = newValue;
                            }

                            if (!(year1.isEmpty() && year2.isEmpty()) && pattern.matcher(year1).matches() && pattern.matcher(year2).matches()) {
                                comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>("Suchergebnisse vor (Jahr): (" + year1 + " - " + year2 + ")"), comicViewModel.filterYearsBetween(Integer.parseInt(year1), Integer.parseInt(year2))));
                                comicTree.getRoot().setExpanded(true);
                            } else {
                                comicTree.setRoot(treeItemRoot);
                            }
                        });


                    }
                }
            });

            vorRadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                    if (isNowSelected) {
                        vorText.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!((newValue.equals("") || newValue.isEmpty())) && pattern.matcher(newValue).matches()) {
                                comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>("Suchergebnisse vor (Jahr): " + newValue), comicViewModel.filterYearsBefore(Integer.parseInt(newValue))));
                                comicTree.getRoot().setExpanded(true);
                            } else {
                                comicTree.setRoot(treeItemRoot);
                            }

                        });
                    }
                }
            });

            nachRadioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                    if (isNowSelected) {
                        nachText.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!((newValue.equals("") || newValue.isEmpty())) && pattern.matcher(newValue).matches()) {
                                comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>("Suchergebnisse nach (Jahr): " + newValue), comicViewModel.filterYearsAfter(Integer.parseInt(newValue))));
                                comicTree.getRoot().setExpanded(true);
                            } else {
                                comicTree.setRoot(treeItemRoot);

                            }

                        });
                    }
                }
            });

            searchByText.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!((newValue.equals("") || newValue.isEmpty()))) {
                    comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>("Suchergebnisse für : " + newValue), comicViewModel.filterComics(newValue)));
                    comicTree.getRoot().setExpanded(true);
                } else {
                    comicTree.setRoot(treeItemRoot);
                }
            });


        }
    }
    public void updateView() throws IOException {
        int currentSelection = comicTree.getSelectionModel().getSelectedIndex();
        ComicViewModel comicViewModel = new ComicViewModel();
        comicTree.getSelectionModel().getSelectedItem().setValue(comicViewModel.getComic());
        comicTree.refresh();
        comicTree.getSelectionModel().select(currentSelection);
    }
}


