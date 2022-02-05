package utils;

public class MapFiller {

    public static char[][] fillMap(char[][] map, char charToFill) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = charToFill;
            }
        }
        return map;
    }
}
