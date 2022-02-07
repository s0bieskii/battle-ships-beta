package gui.controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import logic.BattleshipsGame;

public class GameLogicController {

    private BattleshipsGame game;
    private ObservableList<Integer> addList;

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


    public void start() {
        map.setDisable(false);
        restartButton.setDisable(false);
            refreshScene();
    }

    private void userShipsSet(){
        int x=0;
        int[] coordinate=new int[2];
        while(game.getTotalShips()>x){

        }

    }

    private int[] getCoordinate(int x, int y){
        int[] coordinates={x, y};
        return coordinates;
    }

    @FXML
    private void refreshScene() {
        playerHp.setText("" + game.getPlayerHp());
        computerHp.setText("" + game.getPlayerHp());
        moveCounter.setText("" + game.getCurrentMove());
    }


    @FXML
    private void initialize() {
        setMouseEnterEvent();
        setCoordinateGetEvent();
        setStartButtonMouseClickEvent();
        setRestartButtonMouseClickEvent();
        addList=FXCollections.observableArrayList();
        addList.addListener(new ListChangeListener<Integer>() {
            @Override
            public void onChanged(Change<? extends Integer> change) {
                if(addList.size()==2){
                    System.out.println("ADD SHIP");
                } else if(addList.size()>2){
                    addList.clear();
                }
            }
        });

    }

    private void setRestartButtonMouseClickEvent(){
        restartButton.setOnMouseClicked((MouseEvent event) -> {
            game.prepareGame();
            initialize();
            startButton.setDisable(false);
            restartButton.setDisable(true);
            map.setDisable(true);
            playerHp.setText("0");
            computerHp.setText("0");
        });
    }

    private void setStartButtonMouseClickEvent(){
        startButton.setOnMouseClicked((MouseEvent event) -> {
            start();
            startButton.setDisable(true);
            restartButton.setDisable(false);
            map.setDisable(false);
        });
    }

    private void setMouseEnterEvent() {
        for (int x = 0; x < map.getRowCount(); x++) {
            for (int y = 0; y < map.getColumnCount(); y++) {
                StackPane stackPane = new StackPane();
                stackPane.setOnMouseEntered((MouseEvent event) ->
                        stackPane.setStyle("-fx-background-color: #3f54a1"));
                stackPane.setOnMouseExited((event) ->
                        stackPane.setStyle(""));
                map.add(stackPane, x, y);
            }
        }
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
