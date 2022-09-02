package engine;

import com.gonzobeans.connectfour.engine.GameBoard;
import com.gonzobeans.connectfour.model.GamePiece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    GameBoard board;

    @BeforeEach
    void setup() {
        board = new GameBoard();
    }

    @Test
    void dropPieces() {
        board.dropPiece(GamePiece.red(), 0);
        board.dropPiece(GamePiece.black(), 0);
        board.dropPiece(GamePiece.red(), 1);
        board.dropPiece(GamePiece.black(), 4);
        board.dropPiece(GamePiece.red(), 2);
        board.dropPiece(GamePiece.black(), 3);
        assertFalse(board.checkWin());
        board.dropPiece(GamePiece.red(), 1);
        board.dropPiece(GamePiece.black(), 1);
        board.dropPiece(GamePiece.red(), 0);
        board.dropPiece(GamePiece.black(), 0);
        board.dropPiece(GamePiece.red(), 3);
        assertFalse(board.checkWin());
        board.dropPiece(GamePiece.black(), 2);
        assertTrue(board.checkWin());
        //assertTrue(board.checkWin());
        printBoard();
    }



    private void printBoard() {
        var rowList = new LinkedList<String>();
        for (int r = 0; r < board.getRows(); r++) {
            var builder = new StringBuilder();
            for (int c = 0; c < board.getColumns(); c++) {
                builder.append(board.getPiece(r, c)
                        .map(piece -> piece.equals(GamePiece.red()) ? " R " : " B ")
                        .orElse(" _ "));
            }
            rowList.addFirst(builder.toString());
        }
        rowList.forEach(System.out::println);
    }

}