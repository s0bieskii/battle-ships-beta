package logic;

import java.util.Scanner;
import java.util.SplittableRandom;
import java.util.logging.Logger;
import java.util.random.RandomGenerator;
import utils.Direction;
import utils.MapFiller;
import utils.ShipType;

public class BattleshipsGame {

    public static final Logger LOGGER = Logger.getLogger(BattleshipsGame.class.getName());
    private final int mapDimension;
    private final int battleShipsNumber;
    private final int destroyerNumber;
    private Player computer;
    private Player player;
    private int moveCounter = 0;
    private Direction currentDirection=Direction.DOWN;
    private ShipType currentShip;



    public BattleshipsGame(int mapDimension, int battleShipsNumber, int destroyerNumber) {
        LOGGER.info("BattleshipsGame(" + mapDimension + ", " + battleShipsNumber + ", " + destroyerNumber + ")");
        this.mapDimension = mapDimension;
        this.battleShipsNumber = battleShipsNumber;
        this.destroyerNumber = destroyerNumber;
        prepareGame();
    }

    public void prepareGame() {
        LOGGER.info("BattleshipsGame started");
        computer = new Player("Computer", battleShipsNumber, destroyerNumber, createMap());
        player = new Player("Player", battleShipsNumber, destroyerNumber, createMap());
        randomSetShipsOnMap(computer);
        LOGGER.info("Created players. computer: " + computer + ", player: " + player);
        System.out.println("Game prepared");
        printMap(player.getMap());
    }

    public void start() {
        playerMapCreator();
        while (player.getHealth() > 0 || computer.getHealth() > 0) {
            moveCounter++;
            while (true) {
                printMap(computer.showShotsMap());
                Coordinates playerCoordinates = getCoordinatesToShot();
                if (takeShot(computer, playerCoordinates)) {
                    break;
                }
            }
            while (true) {
                Coordinates computerCoordinate = getRandomCoordinates();
                if (takeShot(player, computerCoordinate)) {
                    break;
                }
            }
        }
    }

    private char[][] createMap() {
        LOGGER.info("createMap() with dimension: " + mapDimension + "x" + mapDimension);
        char[][] map = new char[mapDimension][mapDimension];
        map = MapFiller.fillMap(map, 'o');
        return map;
    }

    private void playerMapCreator() {
        int attemptsNumber = 0;
        int battleShipsToSet = player.getBattleShipsAmount();
        int destroyerShipsToSet = player.getDestroyerShipAmount();
        int successAddShips = 0;
        char[][] tempMap = player.getMap();
        ShipType shipType = ShipType.BATTLESHIP;
        while (successAddShips < getTotalShips() && attemptsNumber < 1000) {
            if (battleShipsToSet == 0) {
                LOGGER.info("All battleShips successfully set. Now set destroyers");
                System.out.println(
                        "All battleShips successfully set. Now set destroyers in amount: " + destroyerShipsToSet);
                shipType = ShipType.DESTROYER;
            }
            Coordinates coordinates = getCoordinateFromUser();
            Direction direction = getDirectionFromUser();
            if (checkShipFit(tempMap, coordinates, direction, shipType)) {
                LOGGER.info("Setting ship " + shipType + " on " + coordinates + " on " + direction + " direction");
                System.out.println("Successfully add ship to map!");
                tempMap = addShipToMap(tempMap, coordinates, direction, shipType);
                battleShipsToSet--;
                successAddShips++;
            } else {
                System.out.println("Pleas choose another place for your ship!");
            }
            printMap(tempMap);
            attemptsNumber++;
        }
        LOGGER.info("Settings ships on map finish in " + attemptsNumber + " attempts");
    }

    private boolean takeShot(Player player, Coordinates coordinates) {
        char[][] tempMap = player.getMap();
        if (coordinates.getX() < mapDimension - 1 && coordinates.getX() >= 0 &&
                coordinates.getY() < mapDimension - 1 && coordinates.getY() >= 0) {
            if (player.checkShot(coordinates)) {
                System.out.println(player.getName() + " You already shot there");
                return false;
            }
            switch (tempMap[coordinates.getX()][coordinates.getY()]) {
                case 'o':
                    System.out.println(this.player.getName() + " You hit the water!");
                    player.addShot(coordinates);
                    return true;
                case 's':
                    System.out.println(computer.getName() + " You hit the ship!");
                    player.addShot(coordinates);
                    player.takeHit();
                    return true;
            }
        }
        return false;
    }

    private void randomSetShipsOnMap(Player player) {
        LOGGER.info("randomSetShipsOnMap( " + player + " )");
        int attemptsNumber = 0;
        int successAddShips = 0;
        int battleShipAmount = 0;
        char[][] temporaryMap = player.getMap();
        ShipType shipType = ShipType.BATTLESHIP;
        while (successAddShips < getTotalShips() && attemptsNumber < 1000) {
            Coordinates coordinates = getRandomCoordinates();
            Direction direction = Direction.fromValue(getRandomInt(1, 4));
            if (battleShipAmount == battleShipsNumber) {
                LOGGER.info("All battleShips successfully set. Now set destroyers");
                shipType = ShipType.DESTROYER;
            }
            if (checkShipFit(temporaryMap, coordinates, direction, shipType)) {
                LOGGER.info("Setting ship " + shipType + " on " + coordinates + " on " + direction + " direction");
                temporaryMap = addShipToMap(temporaryMap, coordinates, direction, shipType);
                battleShipAmount++;
                successAddShips++;
            }
            attemptsNumber++;
        }
        LOGGER.info("Randomly settings ships on map finish in " + attemptsNumber + " attempts");
    }


    private Direction getDirectionFromUser() {
        Scanner scanner = new Scanner(System.in);
        int direction = 0;

        while (true) {
            System.out.println("Please choose ship direction UP[1], DOWN[2], LEFT[3], RIGHT[4]");
            direction = scanner.nextInt();
            if (direction == 1 || direction == 2 || direction == 3 || direction == 4) {
                return Direction.fromValue(direction);
            }
        }
    }

    private Coordinates getCoordinatesToShot() {
        Scanner scanner = new Scanner(System.in);
        int x = -1;
        int y = -1;
        while (true) {
            System.out.println("Please chose your X coordinate to hit:");
            x = scanner.nextInt();
            if (x >= 0 && x < mapDimension) {
                break;
            }
        }
        while (true) {
            System.out.println("Please chose your Y coordinate to hit:");
            y = scanner.nextInt();
            if (x >= 0 && x < mapDimension) {
                break;
            }
        }
        return new Coordinates(x, y);
    }

    private Coordinates getCoordinateFromUser() {
        Scanner scanner = new Scanner(System.in);
        int x;
        int y;
        while (true) {
            System.out.println("Please chose you coordinate where will be your ship head");
            System.out.println("Row coordinate X:");
            x = scanner.nextInt();
            System.out.println("Column coordinate Y");
            y = scanner.nextInt();
            if (x >= 0 && x < mapDimension && y >= 0 &&
                    y < mapDimension) {
                return new Coordinates(x, y);
            }
            System.out.println("Cant place your ship there!");
        }
    }

    public boolean checkShipFit(char[][] map, Coordinates coordinates, Direction direction, ShipType shipType) {
        boolean checkFit = false;
        Coordinates copyCoordinates = new Coordinates(coordinates.getX(), coordinates.getY());
        if (map[copyCoordinates.getX()][copyCoordinates.getY()] != 'o') {
            return false;
        }
        switch (direction) {
            case UP:
                for (int x = 0; x < shipType.getLength(); x++) {
                    if (copyCoordinates.getX() >= 0 && map[copyCoordinates.getX()][copyCoordinates.getY()] == 'o') {
                        checkFit = true;
                    } else {
                        return false;
                    }
                    copyCoordinates.setX(copyCoordinates.getX() - 1);
                }
                return checkFit;
            case DOWN:
                for (int x = 0; x < shipType.getLength(); x++) {
                    if (copyCoordinates.getX() < mapDimension &&
                            map[copyCoordinates.getX()][copyCoordinates.getY()] == 'o') {
                        checkFit = true;
                    } else {
                        return false;
                    }
                    copyCoordinates.setX(copyCoordinates.getX() + 1);
                }
                return checkFit;
            case LEFT:
                for (int x = 0; x < shipType.getLength(); x++) {
                    if (copyCoordinates.getY() >= 0 && map[copyCoordinates.getX()][copyCoordinates.getY()] == 'o') {
                        checkFit = true;
                    } else {
                        return false;
                    }
                    copyCoordinates.setY(copyCoordinates.getY() - 1);
                }
                return checkFit;
            case RIGHT:
                for (int x = 0; x < shipType.getLength(); x++) {
                    if (copyCoordinates.getY() < mapDimension &&
                            map[copyCoordinates.getX()][copyCoordinates.getY()] == 'o') {
                        checkFit = true;
                    } else {
                        return false;
                    }
                    copyCoordinates.setY(copyCoordinates.getY() + 1);
                }
                return checkFit;
        }
        return checkFit;
    }

    private char[][] addShipToMap(char[][] map, Coordinates coordinates, Direction direction, ShipType shipType) {
        switch (direction) {
            case UP:
                for (int x = 0; x < shipType.getLength(); x++) {
                    map[coordinates.getX()][coordinates.getY()] = 's';
                    coordinates.setX(coordinates.getX() - 1);
                }
                break;
            case DOWN:
                for (int x = 0; x < shipType.getLength(); x++) {
                    map[coordinates.getX()][coordinates.getY()] = 's';
                    coordinates.setX(coordinates.getX() + 1);
                }
                break;
            case LEFT:
                for (int x = 0; x < shipType.getLength(); x++) {
                    map[coordinates.getX()][coordinates.getY()] = 's';
                    coordinates.setY(coordinates.getY() - 1);
                }
                break;
            case RIGHT:
                for (int x = 0; x < shipType.getLength(); x++) {
                    map[coordinates.getX()][coordinates.getY()] = 's';
                    coordinates.setY(coordinates.getY() + 1);
                }
                break;
        }
        return map;
    }

    private void printMap(char[][] mapToPrint) {
        System.out.print("   ");
        for (int j = 0; j < mapToPrint.length; j++) {
            System.out.print(j + " ");
        }
        System.out.println("");
        System.out.print("   ");
        for (int j = 0; j < mapToPrint.length; j++) {
            System.out.print("- ");
        }
        System.out.println("");
        for (int i = 0; i < mapToPrint.length; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < mapToPrint[i].length; j++) {
                System.out.print(mapToPrint[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.print("  ");
        for (int j = 0; j < mapToPrint.length + 1; j++) {
            System.out.print("- ");
        }
        System.out.println("");
    }

    private Coordinates getRandomCoordinates() {
        int x = getRandomInt(0, mapDimension - 1);
        int y = getRandomInt(0, mapDimension - 1);
        while (x == y) {
            y = getRandomInt(0, mapDimension - 1);
        }
        return new Coordinates(x, y);
    }

    private void checkCreateMapIsPossible(int mapDimension, int battleShipsNumber, int destroyerNumber) {
        int totalFields = mapDimension * mapDimension;
        int totalShipsFields =
                battleShipsNumber * ShipType.BATTLESHIP.getLength() + destroyerNumber * ShipType.DESTROYER.getLength();
        double percentage = (totalShipsFields * 100) / totalFields;
        if (mapDimension < 10 || percentage > 25) {
            System.out.println("The game map cant be created!");
            System.out.println("To small game board for this ships amount");
            System.exit(0);
        }
    }

    private int getRandomInt(int min, int max) {
        RandomGenerator random = new SplittableRandom();
        return min + random.nextInt(max);
    }

    public int getTotalShips() {
        return battleShipsNumber + destroyerNumber;
    }

    public Direction getCurrentDirection(){
        return currentDirection;
    }

    public void setCurrentDirection(Direction direction){
        this.currentDirection=direction;
    }

    public ShipType getCurrentShip(){
        return ShipType.BATTLESHIP;
    }

    public void setCurrentShip(ShipType shipType){
        this.currentShip=shipType;
    }
    public int getPlayerHp() {
        return player.getHealth();
    }

    public int getComputerHp() {
        return computer.getHealth();
    }

    public int getCurrentMove() {
        return moveCounter;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
