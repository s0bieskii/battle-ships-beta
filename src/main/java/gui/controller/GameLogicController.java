package gui.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import logic.BattleshipsGame;
import logic.Coordinates;
import utils.Direction;

public class GameLogicController {

    private BattleshipsGame game;
    private ObservableList<Integer> addList;

    @FXML
    private AnchorPane gamePane;
    @FXML
    private Button startButton;
    @FXML
    private Button restartButton;
    @FXML
    private GridPane map;
    @FXML
    private Label playerHp;
    @FXML
    private Label computerHp;
    @FXML
    private Label moveCounter;
    @FXML
    private Label messageText;
    @FXML
    private Label directionLabel;


    public void start() {
        map.setDisable(false);
        restartButton.setDisable(false);
        messageText.setText("Chose your coordinates");
        refreshScene();
    }

    private int[] getCoordinate(int x, int y) {
        int[] coordinates = {x, y};
        return coordinates;
    }

    @FXML
    private void refreshScene() {
        playerHp.setText("" + game.getPlayerHp());
        computerHp.setText("" + game.getPlayerHp());
        moveCounter.setText("" + game.getCurrentMove());
        directionLabel.setText("" + game.getCurrentDirection());
    }

    @FXML
    private void initialize() {
        setCoordinateGetEvent();
        setStartButtonMouseClickEvent();
        setRestartButtonMouseClickEvent();
        setDirectionChangeEvent();
        addList = FXCollections.observableArrayList();
        addList.addListener(new ListChangeListener<Integer>() {
            @Override
            public void onChanged(Change<? extends Integer> change) {
                if (addList.size() == 2) {
                    System.out.println("ADD SHIP");
                    Platform.runLater(() -> addList.clear());
                }
            }
        });

    }

    private void setMouseEnterEvent() {
        for (int x = 0; x < map.getRowCount(); x++) {
            for (int y = 0; y < map.getColumnCount(); y++) {
                StackPane stackPane = new StackPane();
                map.add(stackPane, x, y);
                stackPane.setOnMouseEntered((MouseEvent event) ->
                        visualiseShip());
                stackPane.setOnMouseExited((event) ->
                        visualiseShip());
            }
        }
    }

    private void visualiseShip(){
        for (Node node : map.getChildren()) {
            List<Node> editedNode=new ArrayList<>();
            node.setOnMouseEntered((MouseEvent event) -> {
                Integer column = GridPane.getColumnIndex(node);
                Integer row = GridPane.getRowIndex(node);
                Coordinates coordinates=new Coordinates(row, column);
                System.out.println();
                if(game.checkShipFit(game.getPlayer().getMap(), coordinates, game.getCurrentDirection(), game.getCurrentShip())){
                    int columnCopy=column;
                    int rowCopy=row;
                    System.out.println(coordinates);
                    switch (game.getCurrentDirection()) {
                        case UP:
                            for(int x=0;x<game.getCurrentShip().getLength();x++){
                               Node notToEdit =getNodeFromGridPane(columnCopy, rowCopy);
                                notToEdit.setStyle("-fx-background-color: #426b25");
                                editedNode.add(notToEdit);
                                rowCopy--;
                            }
                            break;
                        case DOWN:
                            for(int x=0;x<game.getCurrentShip().getLength();x++){
                                Node notToEdit =getNodeFromGridPane(columnCopy, rowCopy);
                                notToEdit.setStyle("-fx-background-color: #426b25");
                                editedNode.add(notToEdit);
                                rowCopy++;
                            }
                            break;
                        case LEFT:
                            for(int x=0;x<game.getCurrentShip().getLength();x++){
                                Node notToEdit =getNodeFromGridPane(columnCopy, rowCopy);
                                notToEdit.setStyle("-fx-background-color: #426b25");
                                editedNode.add(notToEdit);
                                columnCopy--;
                            }
                            break;
                        case RIGHT:
                            for(int x=0;x<game.getCurrentShip().getLength();x++){
                                Node notToEdit =getNodeFromGridPane(columnCopy, rowCopy);
                                notToEdit.setStyle("-fx-background-color: #426b25");
                                editedNode.add(notToEdit);
                                columnCopy++;
                            }
                            break;
                    }
                }
            });
            node.setOnMouseExited((MouseEvent event)->{
                for(Node nodeToResetStyle:editedNode){
                    nodeToResetStyle.setStyle("");
                }
            });
        }

    }

    private Node getNodeFromGridPane( int col, int row) {
        for (Node node : map.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            if (columnIndex == null)
                columnIndex = 0;
            if (rowIndex == null)
                rowIndex = 0;

            if (columnIndex == col && rowIndex == row) {
                return node;
            }
        }
        return null;
    }

    private void setDirectionChangeEvent(){
        gamePane.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.S || e.getCode() == KeyCode.DOWN){
                game.setCurrentDirection(Direction.DOWN);
            } else if(e.getCode() == KeyCode.W || e.getCode() == KeyCode.UP){
                game.setCurrentDirection(Direction.UP);
            } else if(e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT){
                game.setCurrentDirection(Direction.LEFT);
            } else if(e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT){
                game.setCurrentDirection(Direction.RIGHT);
            }
            directionLabel.setText("" + game.getCurrentDirection());
        });
    }

    private void setRestartButtonMouseClickEvent() {
        restartButton.setOnMouseClicked((MouseEvent event) -> {
            game.prepareGame();
            initialize();
            startButton.setDisable(false);
            restartButton.setDisable(true);
            map.setDisable(true);
            playerHp.setText("0");
            computerHp.setText("0");
            messageText.setText("Press start");
        });
    }

    private void setStartButtonMouseClickEvent() {
        startButton.setOnMouseClicked((MouseEvent event) -> {
            start();
            startButton.setDisable(true);
            restartButton.setDisable(false);
            map.setDisable(false);
            setMouseEnterEvent();
        });
    }

    private void setCoordinateGetEvent() {
        final int x;
        for (Node node : map.getChildren()) {
            node.setOnMouseClicked((MouseEvent event) -> {
                Integer column = GridPane.getColumnIndex(node);
                Integer row = GridPane.getRowIndex(node);
                addList.add(column);
                addList.add(row);
            });
        }
    }

    public void setGame(BattleshipsGame game) {
        this.game = game;
    }

    public void print(String text) {
        System.out.println(text);
    }
}
