package org.example.entity;

public enum SensorType {
    OUTDOOR('O'),
    INDOOR('I');

    private char type;

    SensorType(char type) {
        this.type = type;
    }

    public char getType() {
        return type;
    }
}
