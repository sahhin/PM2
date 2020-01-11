package sample;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import sample.viewmodel.ComicViewModel;
import uimodelhelper.TreeModelHelper;

import java.io.IOException;
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
    private TreeTableView<Comparable> filmListView;

    @FXML
    private TreeTableColumn<String, String> listYear= new TreeTableColumn<>("Jahr");

    @FXML
    private TreeTableColumn<String, String> listName;

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

        private Pattern pattern = Pattern.compile("\\d\\d\\d\\d");
        private String year1 = "";
        private String year2 = "";



        public void initialize() {
            //Erzeugen der TreeView auf Basis alle Comics
            TreeItem<Comparable> treeItemRoot = TreeModelHelper.createComicTree(new TreeItem<>("Comics"), comicViewModel.comicMap());


////            klick auf linkes element
//            comicTree.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {
//
//                @Override
//                public void changed(ObservableValue observable, Object oldValue,
//                                    Object newValue) {
//
//                    TreeItem<String> selectedItem = (TreeItem<String>) newValue;
//                    System.out.println("Selected Text : " + selectedItem.getValue());
//                    // do what ever you want
//                    ObservableList<String> )
//                }
//
//            });

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

                            if(!(year1.isEmpty() && year2.isEmpty()) && pattern.matcher(year1).matches() && pattern.matcher(year2).matches() ) {
                                comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>("1926 6548484 Suchergebnisse vor (Jahr): (" + year1 + " - " + year2 + ")"), comicViewModel.filterYearsBetween(Integer.parseInt(year1),Integer.parseInt(year2))));
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


            //Verknupfung
            comicTree.setRoot(treeItemRoot);
            comicTree.setShowRoot(true);
            comicTree.getRoot().setExpanded(true);
            comicTree.getRoot().getChildren();
        }
    }

}


