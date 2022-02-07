package gui.controller;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import logic.BattleshipsGame;
import utils.ShipType;

public class BattleShipsController {

    private ObservableList<Integer> battleShipsAmount =
            FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6);
    private ObservableList<Integer> destroyerAmount =
            FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7);
    private ObservableList<Integer> dimensionList =
            FXCollections.observableArrayList(10);

    @FXML
    private ChoiceBox<Integer> mapDimension;

    @FXML
    private ChoiceBox<Integer> battleshipsAmount;

    @FXML
    private ChoiceBox<Integer> destroyersAmount;

    @FXML
    private void initialize() {
        mapDimension.setItems(dimensionList);
        battleshipsAmount.setItems(battleShipsAmount);
        destroyersAmount.setItems(destroyerAmount);
        mapDimension.setValue(10);
        battleshipsAmount.setValue(1);
        destroyersAmount.setValue(2);
    }

    @FXML private AnchorPane mainAnchor;


    @FXML
    private void start() throws IOException {
        int dimension = mapDimension.getValue();
        int battleships = battleshipsAmount.getValue();
        int destroyers = destroyersAmount.getValue();
        if (checkCreateMapIsPossible(dimension, battleships, destroyers)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
            Parent root=(Parent) loader.load();
            System.out.println("");
            GameLogicController logicController= loader.getController();
            logicController.setGame(new BattleshipsGame(dimension, battleships, destroyers));
            Stage stage = (Stage) mainAnchor.getScene().getWindow();
            stage.setScene(new Scene(root));
            return;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING, "To small map for this ships amount", ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    private boolean checkCreateMapIsPossible(int mapDimension, int battleShipsNumber, int destroyerNumber) {
        int totalFields = mapDimension * mapDimension;
        int totalShipsFields =
                battleShipsNumber * ShipType.BATTLESHIP.getLength() + destroyerNumber * ShipType.DESTROYER.getLength();
        double percentage = (totalShipsFields * 100) / totalFields;
        if (mapDimension < 10 || percentage > 25) {
            return false;
        }
        return true;
    }


}

