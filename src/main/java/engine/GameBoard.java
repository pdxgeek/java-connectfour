package engine;

import model.GamePiece;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class GameBoard {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final int WIN_TARGET = 4;

    private static final GamePiece[][] gameBoard = new GamePiece[ROWS][COLUMNS];

    public Optional<GamePiece> getPiece(int row, int column) {
        return outOfBounds(row, column)
            ? Optional.empty()
            : Optional.ofNullable(gameBoard[row][column]);
    }

    public void clearBoard() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                gameBoard[r][c] = null;
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
                return true;
            });
        }
    }

    public boolean checkWin() {
        var win = false;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                if (checkWinTarget(r, c)) {
                    win = true;
                    break;
                }
            }
        }
        return win;
    }

    public int getColumns() {
        return COLUMNS;
    }

    public int getRows() {
        return ROWS;
    }

    public boolean checkWinTarget(final int row, final int column) {
        if (getPiece(row, column).isEmpty()) {
            return false;
        }
        var color = getPiece(row, column).get();
        return checkRight(color, row, column)
            || checkDiagonalUp(color, row, column)
            || checkUp(color, row, column)
            || checkDiagonalDown(color, row, column);
    }

    private boolean checkRight(GamePiece color, final int row, final int column) {
        var currentColumn = new AtomicInteger(column);
        var foundPiece = true;
        while (foundPiece && currentColumn.get() - column < WIN_TARGET) {
            foundPiece = getPiece(row, currentColumn.getAndIncrement())
                .map(piece -> piece.equals(color))
                .orElse(false);
        }
        return foundPiece;
    }

    private boolean checkDiagonalUp(GamePiece color, final int row, final int column) {
        var currentRow = new AtomicInteger(row);
        var currentColumn = new AtomicInteger(column);
        var foundPiece = true;
        while (foundPiece && currentColumn.get() - column < WIN_TARGET) {
            foundPiece = getPiece(currentRow.getAndIncrement(), currentColumn.getAndIncrement())
                .map(piece -> piece.equals(color))
                .orElse(false);
        }
        return foundPiece;
    }

    private boolean checkDiagonalDown(GamePiece color, final int row, final int column) {
        var currentRow = new AtomicInteger(row);
        var currentColumn = new AtomicInteger(column);
        var foundPiece = true;
        while (foundPiece && currentColumn.get() - column < WIN_TARGET) {
            foundPiece = getPiece(currentRow.getAndDecrement(), currentColumn.getAndIncrement())
                .map(piece -> piece.equals(color))
                .orElse(false);
        }
        return foundPiece;
    }

    private boolean checkUp(GamePiece color, final int row, final int column) {
        var currentRow = new AtomicInteger(row);
        var foundPiece = true;
        while (foundPiece && currentRow.get() - row < WIN_TARGET) {
            foundPiece = getPiece(currentRow.getAndIncrement(), column)
                .map(piece -> piece.equals(color))
                .orElse(false);
        }
        return foundPiece;
    }

    private boolean outOfBounds(int row, int column) {
        return (row < 0 || row >= ROWS) || (column < 0 || column >= COLUMNS);
    }
}
