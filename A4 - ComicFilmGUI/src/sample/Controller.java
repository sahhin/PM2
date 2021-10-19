package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import model.ComicModel;
import sample.viewmodel.ComicViewModel;
import uimodelhelper.TreeModelHelper;
import utils.YearInterval;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

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
    private TableView<Comic> filmListView = new TableView<>();

    @FXML
    private TableColumn<Comic, String> listYear = new TableColumn<>();

    @FXML
    private TableColumn<Comic, String> listName = new TableColumn<>();

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

    private boolean isTreeViewMode = true;

    private boolean treeModeActive(){
        return isTreeViewMode;
    }
    private final String switchToComicsText = "nach Namen sortieren";
    private final String switchToYearText = "nach Jahren sortieren";

    private TableViewComic tableViewComic;

    //im initialize werden nur die Daten der Applikation
    //generiert und Objekte der inneren Klassen erzeugt
    @FXML
    protected void initialize() throws IOException {
        //erzeugen der Daten für die Applikation
        ComicViewModel comicViewModel = new ComicViewModel();

        ComicTreeView comicTreeView = new ComicTreeView(comicViewModel);
        comicTreeView.initialize();
        comicTreeView.initializeGUI();
        tableViewComic = new TableViewComic(comicViewModel);
        tableViewComic.initialize();

    }

    private class TableViewComic {
        private ComicViewModel comicViewModel;
        private ObservableList<Comic> data = FXCollections.observableArrayList();

        public TableViewComic(ComicViewModel comicViewModel){
            this.comicViewModel = comicViewModel;
            this.initialize();

            this.comicViewModel.comicProperty().addListener((((observableValue, oldValue, newValue) -> {
                if(oldValue == null || (newValue != null && !newValue.equals(oldValue))){
                    updateView();
                }
            })));

            this.comicViewModel.yearIntervalProperty().addListener((((observableValue, oldValue, newValue) -> {
                if(oldValue == null || (newValue != null && !newValue.equals(oldValue))){
                    updateView();
                }
            })));
        }
        private void initialize(){
            listName.setText("Film");
            listYear.setText("Jahr");
        }

        private void updateView(){
            filmListView.getItems().clear();

            if(treeModeActive()){
                listName.setText("Jahr");
                listYear.setText("Film");
            } else{
                listName.setText("Comic");
                listYear.setText("Film");
            }

            if(treeModeActive()){
                for(Map.Entry element : comicViewModel.get(comicViewModel.comicProperty().getValue()).entrySet()){
                    data.add(new Comic((String) element.getKey(), (String) element.getValue()));
                }
                filmListView.setItems(data);

            } else{
                for(Map.Entry element : comicViewModel.get(comicViewModel.yearIntervalProperty().getValue()).entrySet()){
                    data.add(new Comic((String) element.getKey(), (String) element.getValue()));
                }
                filmListView.setItems(data);

            }

            listYear.setCellValueFactory(new PropertyValueFactory<Comic, String>("first"));
            listName.setCellValueFactory(new PropertyValueFactory<Comic, String>("last"));
            filmListView.refresh();
        }
    }

    public static class Comic {
        private final SimpleStringProperty first;
        private final SimpleStringProperty last;

        private Comic(String year, String film){
            this.first = new SimpleStringProperty(year);
            this.last = new SimpleStringProperty(film);
        }
        public String getFirst(){
            return first.get();
        }
        public void setFirst(String first){
            this.first.set(first);
        }
        public String getLast(){
            return last.get();
        }
        public void setLast(String last){
            this.last.set(last);
        }
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

            comicTree.setCellFactory((TreeView<Comparable> tv) -> new TextFieldTreeCell<>() {
                private final MenuItem switchItem;

                private final ContextMenu switchViewMenu = new ContextMenu();

                {
                    switchItem = new MenuItem();
                    switchViewMenu.getItems().add(switchItem);
                    switchItem.setOnAction((ActionEvent e) -> {
                        if(treeModeActive()){
                            comicTree.setRoot(TreeModelHelper.createYearTree(new TreeItem<>("nach Comics"), comicViewModel.currentYearMap()));
                            isTreeViewMode = false;
                        } else{
                            comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>("nach Jahren"), comicViewModel.currentComicMap()));
                            isTreeViewMode = true;
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
                        switchItem.setText(isTreeViewMode ? switchToYearText : switchToComicsText);
                    }
                }
            });

// Listener für die Tableview auf dem Comic Tree
            comicTree.getSelectionModel().selectedItemProperty().addListener(
                    (observableValue, oldValue, newValue) -> {
                        if (newValue != null && newValue.getValue() instanceof String) {
                            comicViewModel.setComic((String) newValue.getValue());
                        }
                    });

            // Listener fuer die Tableview auf dem Year Tree
            comicTree.getSelectionModel().selectedItemProperty().addListener(
                    (observableValue, oldValue, newValue) -> {
                        if (newValue != null && newValue.getValue() instanceof YearInterval) {
                            comicViewModel.setYearInterval((YearInterval) newValue.getValue());
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

            comicTree.setEditable(true);

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
    private void setInitialTree(){
        if (isTreeViewMode) {
            comicTree.setRoot(TreeModelHelper.createYearTree(new TreeItem<>("nach Jahren"), comicViewModel.currentYearMap()));
            isTreeViewMode = false;
        } else {
            comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>("nach Comics"), comicViewModel.comicMap()));
            isTreeViewMode = true;
        }
    }

    public void initializeGUI() {
        //Radio Buttons gruppieren und Textfelder ausblenden (damit nur ein Button ausgewählt werden kann):
        ToggleGroup radioGroup = new ToggleGroup();
        vorRadioButton.setToggleGroup(radioGroup);
        nachRadioButton.setToggleGroup(radioGroup);
        vonbisRadioButton.setToggleGroup(radioGroup);
        vorRadioButton.setSelected(false); //Per Default ist kein Button ausgewählt
        nachRadioButton.setSelected(false); //Per Default ist kein Button ausgewählt
        vonbisRadioButton.setSelected(false); //Per Default ist kein Button ausgewählt
        //beim Start müssen die zugehörigen Textfelder deaktiviert werden:
        vorText.setDisable(true);
        nachText.setDisable(true);
        vonbisText1.setDisable(true);
        vonbisText2.setDisable(true);

        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                if (radioGroup.getSelectedToggle() != null) {
                    RadioButton buttontmp = (RadioButton) radioGroup.getSelectedToggle();
                    System.out.println("Ausgewählter Button: " + buttontmp);
                    if (buttontmp.equals(vorRadioButton)) {
                        setInitialTree();
                        vorText.setDisable(false);
                        nachText.setDisable(true);//new
                        searchByText.clear();
                        vonbisText1.setDisable(true); //new
                        vonbisText2.setDisable(true); //new
                        nachText.clear(); //new
                        vonbisText1.clear(); //new
                        vonbisText2.clear(); //new
                    } else if (buttontmp.equals(nachRadioButton)) {//new
                        setInitialTree();
                        vorText.setDisable(true);
                        nachText.setDisable(false);//new
                        searchByText.clear();
                        vonbisText1.setDisable(true); //new
                        vonbisText2.setDisable(true); //new
                        nachText.clear(); //new
                        vonbisText1.clear(); //new
                        vonbisText2.clear(); //new
                    } else if (buttontmp.equals(vonbisRadioButton)) {//new
                        setInitialTree();
                        vorText.setDisable(true);
                        vorText.clear();
                        searchByText.clear();
                        nachText.clear(); //new
                        vonbisText1.clear(); //new
                        vonbisText2.clear(); //new
                        nachText.setDisable(true);
                        vonbisText1.setDisable(false);
                        vonbisText2.setDisable(false);
                    }
                }
            }
        });

        searchByText.textProperty().addListener((observableValue, old_value, new_value) -> {
            if (!new_value.equals(old_value)) {
                // System.out.println(new_value);
                vorText.clear();
                nachText.clear();
                vonbisText1.clear();
                vonbisText2.clear();
                comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>(new_value), comicViewModel.filterComics(new_value)));
            }

        });

        vorText.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));

        vorText.textProperty().addListener((observableValue, old_value, new_value) -> {

            if (!old_value.equals(new_value) && !new_value.isEmpty()) {
                //System.out.println("new value: " + new_value);
                comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>(new_value), comicViewModel.filterYearsBefore(Integer.valueOf(new_value))));
            }
        });

        nachText.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));

        nachText.textProperty().addListener((observableValue, old_value, new_value) -> {

            if (!new_value.equals(old_value) && !new_value.isEmpty()) {
                System.out.println("new value: " + new_value);
                comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>(new_value), comicViewModel.filterYearsAfter(Integer.valueOf(new_value))));
            }
        });

        vonbisText1.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        vonbisText2.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));

        vonbisText1.textProperty().addListener((observableValue, old_value, new_value) -> {
            vonbisText2.textProperty().addListener((observableValue1, old_value1, new_value1) -> {


                if (!new_value.equals(old_value) && !new_value.isEmpty() && !new_value1.equals(old_value1) && !new_value1.isEmpty()) {

                    System.out.println("new value: " + new_value + " " + new_value1);
                    comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>(new_value), comicViewModel.filterYearsBetween(Integer.valueOf(new_value), Integer.valueOf(vonbisText2.textProperty().getValue()))));
                    comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>(new_value1), comicViewModel.filterYearsBetween(Integer.valueOf(vonbisText1.textProperty().getValue()), Integer.valueOf(new_value1))));


                }
            });
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


