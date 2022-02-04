package logic;

import java.util.SplittableRandom;
import java.util.random.RandomGenerator;
import utils.Direction;
import utils.ShipType;

public class BattleshipsGame {

    private final int mapDimension;
    private final int battleShipsNumber;
    private final int destroyerNumber;
    private int[][] computerMap;
    private int[][] userMap;


    public BattleshipsGame(int mapDimension, int battleShipsNumber, int destroyerNumber) {
        this.mapDimension = mapDimension;
        this.battleShipsNumber = battleShipsNumber;
        this.destroyerNumber = destroyerNumber;
        checkCreateMapIsPossible(mapDimension, battleShipsNumber, destroyerNumber);
    }


    public void start() {
        computerMap = createMap();
        userMap = computerMap;
        randomSetShipsOnComputerMap();
        System.out.println();
        System.out.println("************************************");
        System.out.println("*** Welcome in Battleship game! ***");
        System.out.println("************************************");
        printMap(computerMap);
    }

    private int[][] createMap() {
        return new int[mapDimension][mapDimension];
    }

    private void randomSetShipsOnComputerMap() {
        int attemptsNumber = 0;
        int successAddShips = 0;
        int battleShipAmount = 0;
        ShipType shipType = ShipType.BATTLESHIP;
        boolean battleShipIsSet = false;
        while (successAddShips < getTotalShips() && attemptsNumber < 1000) {
            int[] coordinates = getRandomCoordinates();
            Direction direction = Direction.fromValue(getRandomInt(1, 4));
            if (battleShipAmount==battleShipsNumber) {
                shipType = ShipType.DESTROYER;
            }
            if (checkShipFit(computerMap, coordinates, direction, shipType)) {
                addShipToMap(computerMap, coordinates, direction, shipType);
                battleShipAmount++;
                successAddShips++;
            }
            attemptsNumber++;
        }
    }

    /**
     * @param map         map on which you will set a ship
     * @param coordinates coordinates where front of ship will be. coordinates[0]=x, coordinates[1]=y
     * @param direction   direction of the ship
     * @return
     */
    private boolean checkShipFit(int[][] map, int[] coordinates, Direction direction, ShipType shipType) {
        boolean checkFit = false;
        int[] copyCoordinates = new int[2];
        copyCoordinates[0] = coordinates[0];
        copyCoordinates[1] = coordinates[1];
        if (computerMap[copyCoordinates[0]][copyCoordinates[1]] != 0) {
            return false;
        }
        switch (direction) {
            case UP:
                for (int x = 0; x < shipType.getLength(); x++) {
                    if (copyCoordinates[0] >= 0 && computerMap[copyCoordinates[0]][copyCoordinates[1]] == 0) {
                        checkFit = true;
                    } else {
                        return false;
                    }
                    copyCoordinates[0]--;
                }
                return checkFit;
            case DOWN:
                for (int x = 0; x < shipType.getLength(); x++) {
                    if (copyCoordinates[0] < mapDimension && computerMap[copyCoordinates[0]][copyCoordinates[1]] == 0) {
                        checkFit = true;
                    } else {
                        return false;
                    }
                    copyCoordinates[0]++;
                }
                return checkFit;
            case LEFT:
                for (int x = 0; x < shipType.getLength(); x++) {
                    if (copyCoordinates[1] >= 0 && computerMap[copyCoordinates[0]][copyCoordinates[1]] == 0) {
                        checkFit = true;
                    } else {
                        return false;
                    }
                    copyCoordinates[1]--;
                }
                return checkFit;
            case RIGHT:
                for (int x = 0; x < shipType.getLength(); x++) {
                    if (copyCoordinates[1] < mapDimension && computerMap[copyCoordinates[0]][copyCoordinates[1]] == 0) {
                        checkFit = true;
                    } else {
                        return false;
                    }
                    copyCoordinates[1]++;
                }
                return checkFit;
        }
        return checkFit;
    }

    private void addShipToMap(int[][] map, int[] coordinates, Direction direction, ShipType shipType) {
        switch (direction) {
            case UP:
                for (int x = 0; x < shipType.getLength(); x++) {
                    map[coordinates[0]][coordinates[1]] = 1;
                    coordinates[0]--;
                }
                break;
            case DOWN:
                for (int x = 0; x < shipType.getLength(); x++) {
                    map[coordinates[0]][coordinates[1]] = 1;
                    coordinates[0]++;
                }
                break;
            case LEFT:
                for (int x = 0; x < shipType.getLength(); x++) {
                    map[coordinates[0]][coordinates[1]] = 1;
                    coordinates[1]--;
                }
                break;
            case RIGHT:
                for (int x = 0; x < shipType.getLength(); x++) {
                    map[coordinates[0]][coordinates[1]] = 1;
                    coordinates[1]++;
                }
                break;
        }
    }

    private void printMap(int[][] mapToPrint) {
        for (int i = 0; i < mapToPrint.length; i++) {
            for (int j = 0; j < mapToPrint[i].length; j++) {
                System.out.print(mapToPrint[i][j] + " ");
            }
            System.out.println("");
        }
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

    private void resetMap(int[][] mapToFill) {
        int x = 1;
        for (int i = 0; i < mapToFill.length; i++) {
            for (int j = 0; j < mapToFill[i].length; j++) {
                mapToFill[i][j] = 0;
                x++;
            }
        }
        mapToFill = mapToFill;
    }

    private int[] getRandomCoordinates() {
        int[] coordinates = new int[2];
        coordinates[0] = getRandomInt(0, mapDimension - 1);
        coordinates[1] = getRandomInt(0, mapDimension - 1);
        while (coordinates[0] == coordinates[1]) {
            coordinates[1] = getRandomInt(0, mapDimension - 1);
        }
        return coordinates;
    }

    private int getRandomInt(int min, int max) {
        RandomGenerator random = new SplittableRandom();
        return min + random.nextInt(max);
    }

    private int getTotalShips() {
        return battleShipsNumber + destroyerNumber;
    }

}
