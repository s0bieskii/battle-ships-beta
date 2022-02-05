package logic;

import java.util.HashMap;
import utils.MapFiller;

public class Player {
    private int battleShipsAmount;
    private int destroyerShipAmount;
    private char[][] map;
    private HashMap<Coordinates, Boolean> shots = new HashMap<>();
    private int health;
    private String name;

    Player(String name, int battleShipsAmount, int destroyerShipAmount, char[][] map) {
        this.name = name;
        this.battleShipsAmount = battleShipsAmount;
        this.destroyerShipAmount = destroyerShipAmount;
        this.map = map;
        health = calculateHealth();
    }

    private int calculateHealth() {
        int battleShips = battleShipsAmount * 5;
        int destroyerShips = destroyerShipAmount * 4;
        return battleShips + destroyerShips;
    }

    public char[][] getMap() {
        return map;
    }

    public boolean checkShot(Coordinates coordinates) {
        Boolean check = shots.get(coordinates);
        if (check == null) {
            return false;
        }
        if (check == true) {
            return true;
        }
        return false;
    }

    public void addShot(Coordinates coordinates) {
        shots.put(coordinates, true);
    }

    public char[][] showShotsMap() {
        char[][] shotMap = new char[map.length][map.length];
        MapFiller.fillMap(shotMap, 'o');
        for (Coordinates coordinates : shots.keySet()) {
            shotMap[coordinates.getX()][coordinates.getY()] = '*';
        }
        return shotMap;
    }

    public void setMap(char[][] map) {
        this.map = map;
    }

    public void takeHit() {
        this.health--;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getBattleShipsAmount() {
        return battleShipsAmount;
    }

    public int getDestroyerShipAmount() {
        return destroyerShipAmount;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "battleShipsAmount=" + battleShipsAmount +
                ", destroyerShipAmount=" + destroyerShipAmount +
                ", shots=" + shots +
                ", health=" + health +
                ", name='" + name + '\'' +
                '}';
    }
}
