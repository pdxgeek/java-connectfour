package com.gonzobeans.connectfour.engine;

import com.gonzobeans.connectfour.model.GamePiece;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.gonzobeans.connectfour.model.GamePiece.Color;

public class GameBoard {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final int WIN_TARGET = 4;

    private final GamePiece[][] gameBoard ;
    @Getter
    private final List<GamePiece> gamePieces;
    private List<GamePiece> winningPieces;

    public GameBoard() {
        this.gameBoard = new GamePiece[ROWS][COLUMNS];
        this.gamePieces = new ArrayList<>();
    }

    public Optional<GamePiece> getPiece(int row, int column) {
        return outOfBounds(row, column)
            ? Optional.empty()
            : Optional.ofNullable(gameBoard[row][column]);
    }

    public Optional<List<GamePiece>> getWinningPieces() {
        return winningPieces != null ? Optional.of(winningPieces) : Optional.empty();
    }

    public void clearBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                gameBoard[row][column] = null;
            }
        }
    }

    public void dropPiece(GamePiece gamePiece, int column) {
        var row = new AtomicInteger();
        var placed = false;
        while (row.get() < ROWS && !placed) {
            placed = getPiece(row.get(), column).map(gp -> {
                row.incrementAndGet();
                return false;
            }).orElseGet(() -> {
                gameBoard[row.get()][column] = gamePiece;
                gamePiece.setCoordinates(row.get(), column);
                gamePieces.add(gamePiece);
                return true;
            });
        }
    }

    public Optional<List<GamePiece>> checkWin() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                var winList = checkWinTarget(row, column);
                    if (winList.isPresent()) {
                        this.winningPieces = winList.get();
                        return winList;
                }
            }
        }
        return Optional.empty();
    }

    public int getColumns() {
        return COLUMNS;
    }

    public int getRows() {
        return ROWS;
    }

    public Optional<List<GamePiece>> checkWinTarget(final int row, final int column) {
        if (getPiece(row, column).isEmpty()) {
            return Optional.empty();
        }
        var color = getPiece(row, column).get().getColor();

        var winList = Stream.of(
            checkRight(color, row, column),
            checkDiagonalUp(color, row, column),
            checkUp(color, row, column),
            checkDiagonalDown(color, row, column))
            .filter(Optional::isPresent)
            .findFirst().orElse(Optional.empty());

        winList.ifPresent(list -> list.forEach(gamePiece -> gamePiece.setHighlight(true)));

        return winList;
    }

    private Optional<List<GamePiece>> checkRight(Color color, final int row, final int column) {
        var currentColumn = new AtomicInteger(column);
        var foundPiece = true;
        var pieces = new ArrayList<GamePiece>();
        while (foundPiece && currentColumn.get() - column < WIN_TARGET) {
            foundPiece = getPiece(row, currentColumn.getAndIncrement())
                .map(piece -> checkGamePieceColor(piece, color, pieces))
                .orElse(false);
        }
        return pieces.size() == WIN_TARGET ? Optional.of(pieces) : Optional.empty();
    }

    private Optional<List<GamePiece>> checkDiagonalUp(Color color, final int row, final int column) {
        var currentRow = new AtomicInteger(row);
        var currentColumn = new AtomicInteger(column);
        var foundPiece = true;
        var pieces = new ArrayList<GamePiece>();
        while (foundPiece && currentColumn.get() - column < WIN_TARGET) {
            foundPiece = getPiece(currentRow.getAndIncrement(), currentColumn.getAndIncrement())
                .map(piece -> checkGamePieceColor(piece, color, pieces))
                .orElse(false);
        }
        return pieces.size() == WIN_TARGET ? Optional.of(pieces) : Optional.empty();
    }

    private Optional<List<GamePiece>> checkDiagonalDown(Color color, final int row, final int column) {
        var currentRow = new AtomicInteger(row);
        var currentColumn = new AtomicInteger(column);
        var foundPiece = true;
        var pieces = new ArrayList<GamePiece>();
        while (foundPiece && currentColumn.get() - column < WIN_TARGET) {
            foundPiece = getPiece(currentRow.getAndDecrement(), currentColumn.getAndIncrement())
                .map(piece -> checkGamePieceColor(piece, color, pieces))
                .orElse(false);
        }
        return pieces.size() == WIN_TARGET ? Optional.of(pieces) : Optional.empty();
    }

    private Optional<List<GamePiece>> checkUp(Color color, final int row, final int column) {
        var currentRow = new AtomicInteger(row);
        var foundPiece = true;
        var pieces = new ArrayList<GamePiece>();
        while (foundPiece && currentRow.get() - row < WIN_TARGET) {
            foundPiece = getPiece(currentRow.getAndIncrement(), column)
                .map(piece -> checkGamePieceColor(piece, color, pieces))
                .orElse(false);
        }
        return pieces.size() == WIN_TARGET ? Optional.of(pieces) : Optional.empty();
    }

    private boolean outOfBounds(int row, int column) {
        return (row < 0 || row >= ROWS) || (column < 0 || column >= COLUMNS);
    }

    private boolean checkGamePieceColor(GamePiece gamePiece, Color color, List<GamePiece> pieces) {
        if (gamePiece.getColor().equals(color)) {
            pieces.add(gamePiece);
            return true;
        }
        return false;
    }
}
