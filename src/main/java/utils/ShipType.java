package utils;

public enum ShipType {
    BATTLESHIP(5), DESTROYER(4);

    private int length;

    ShipType(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public static ShipType fromValue(int value) {
        switch (value) {
            case 1:
                return ShipType.BATTLESHIP;
            case 2:
                return ShipType.DESTROYER;
            default:
                return null;
        }
    }
}
