package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.ComicModel;
import sample.viewmodel.ComicViewModel;
import uimodelhelper.TreeModelHelper;

import java.io.IOException;

public class Controller {

    private ComicTreeView comicTreeview;

    //hier wird für jedes UI Element mit fx:id
    // eine Instanz-Variable, die mit @FXML annotiert ist
    @FXML
    private TreeView<Comparable> comicTree;

    @FXML
    private TextField searchByText;

    @FXML
    private ListView filmListView;

    @FXML
    private RadioButton vorRadioButton;


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

                    }
                }
            });
        }

        public void initialize() {
            //Erzeugen der TreeView auf Basis alle Comics
            TreeItem<Comparable> treeItemRoot = TreeModelHelper.createComicTree(new TreeItem<>("Comics"), comicViewModel.comicMap());

            searchByText.textProperty().addListener((observable, oldValue, newValue) -> {
                comicTree.setRoot(TreeModelHelper.createComicTree(new TreeItem<>(newValue), comicViewModel.filterComics(newValue)));
                comicTree.getRoot().setExpanded(true);
            });

            

            //Verknupfung
            comicTree.setRoot(treeItemRoot);
            comicTree.setShowRoot(true);
            comicTree.getRoot().setExpanded(true);
            comicTree.getRoot().getChildren();
        }
    }

}


