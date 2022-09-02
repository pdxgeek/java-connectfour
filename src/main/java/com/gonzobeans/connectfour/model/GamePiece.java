package com.gonzobeans.connectfour.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public class GamePiece {
    private final Color color;
    private Integer row;
    private Integer column;
    boolean highlight = false;

    public static GamePiece red() {
        return new GamePiece(Color.RED);
    }

    public static GamePiece black() {
        return new GamePiece(Color.BLACK);
    }

    public void setCoordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public enum Color {
        RED, BLACK;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GamePiece)) {
            return false;
        }
        return color.equals(((GamePiece) object).getColor());
    }
}

