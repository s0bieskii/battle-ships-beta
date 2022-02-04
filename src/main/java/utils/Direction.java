package utils;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Direction {
    UP(1), DOWN(2), LEFT(3), RIGHT(4);

    private static Map<Integer, Direction> reverseLookup =
            Arrays.stream(Direction.values()).collect(Collectors.toMap(Direction::getValue, Function.identity()));
    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static Direction fromValue(int value) {
        switch (value) {
            case 1:
                return Direction.UP;
            case 2:
                return Direction.DOWN;
            case 3:
                return Direction.LEFT;
            case 4:
                return Direction.RIGHT;
            default:
                return null;
        }
    }


}
