package loa;

import java.util.List;
import java.util.Iterator;

import static loa.Piece.*;
import static loa.Side.*;
import static loa.Board.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTest {

    private Piece[][] pieces = {
        { EMP,  BP, EMP,  BP,  BP, EMP, EMP, EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP,  BP,  BP, EMP, WP  },
        { WP,  EMP,  BP, EMP, EMP,  WP, EMP, EMP },
        { WP,  EMP,  WP,  WP, EMP,  WP, EMP, EMP },
        { WP,  EMP, EMP, EMP,  BP, EMP, EMP, WP  },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, BP,  BP,  BP,  EMP,  BP,  BP, EMP }
    };
    private MutableBoard board = new MutableBoard(pieces, BLACK);
    private Move move1 =
            Move.create(col("f3"), row("f3"), col("h1"), row("h1"));
    private Move move2 =
            Move.create(col("f3"), row("f3"), col("h5"), row("h5"));
    private Move move3 =
            Move.create(col("f3"), row("f3"), col("b3"), row("b3"));
    private Move move4 =
            Move.create(col("f3"), row("f3"), col("d5"), row("d5"));


    @Test
    public void isLegalTest() {
        Move move5 = Move.create(col("f3"), row("f3"), col("d1"), row("d1"));
        Move move6 = Move.create(col("f3"), row("f3"), col("f7"), row("f7"));
        Move move7 = Move.create(col("f3"), row("f3"), col("c6"), row("c6"));
        Move move8 = Move.create(col("f3"), row("f3"), col("c3"), row("c3"));
        Move move9 = Move.create(col("a6"), row("a6"), col("d6"), row("d6"));
        Move move10 = Move.create(col("f3"), row("f3"), col("e5"), row("e5"));
        assertTrue(board.isLegal(move1));
        assertTrue(board.isLegal(move2));
        assertTrue(board.isLegal(move3));
        assertTrue(board.isLegal(move4));
        assertFalse(board.isLegal(move5));
        assertFalse(board.isLegal(move6));
        assertFalse(board.isLegal(move7));
        assertFalse(board.isLegal(move8));
        assertFalse(board.isLegal(move9));
        assertFalse(board.isLegal(move10));
    }

    @Test
    public void legalMovesTest() {
        int[] num = board.accumNumInLine();
        assertEquals(3, num[0]);
        assertEquals(4, num[4]);
        assertEquals(0, num[6]);
        assertEquals(5, num[8]);
        assertEquals(3, num[0]);
        assertEquals(1, num[14]);
        assertEquals(2, num[20]);
        assertEquals(2, num[23]);
        assertEquals(2, num[35]);
        assertEquals(3, num[40]);
        List<Move> moves = board.findMoves(2, 5, num);
        assertEquals(4, moves.size());
        assertTrue(moves.contains(move1));
        assertTrue(moves.contains(move2));
        assertTrue(moves.contains(move3));
        assertTrue(moves.contains(move4));
        for (Iterator<Move> itr =
                board.legalMoves().iterator(); itr.hasNext();) {
            board.makeMove(itr.next());
            for (Iterator<Move> i =
                    board.legalMoves().iterator(); i.hasNext();) {
                assertTrue(board.isLegal(i.next()));
            }
            board.retract();
        }
    }

    @Test
    public void piecesContiguousTest() {
        assertEquals(1, board.findAdjacent(2, 5).size());
        assertEquals(2, board.findAdjacent(3, 0).size());
        assertEquals(0, board.findAdjacent(5, 4).size());
        assertFalse(board.piecesContiguous(BLACK));
        assertFalse(board.piecesContiguous(WHITE));
        MutableBoard board1 = new MutableBoard();
        board1.makeMove(Move.create(col("d8"), row("d8"),
                col("d6"), row("d6")));
        assertFalse(board1.piecesContiguous(BLACK));
        assertFalse(board1.piecesContiguous(WHITE));
        Piece[][] pieces2 = {
            {  WP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP,  WP,  WP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP,  BP,  WP, EMP, EMP, EMP, EMP },
            { EMP,  BP,  WP,  BP, EMP, EMP, EMP, EMP },
            { EMP,  WP, EMP,  WP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP,  BP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        };
        Board board2 = new Board(pieces2, WHITE);
        assertTrue(board2.piecesContiguous(WHITE));
        assertFalse(board2.piecesContiguous(BLACK));
        Piece[][] pieces3 = {
            {  WP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP,  WP,  WP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP,  BP,  WP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP,  WP, EMP,  WP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        };
        Board board3 = new Board(pieces3, WHITE);
        assertTrue(board3.piecesContiguous(BLACK));
        assertFalse(board3.piecesContiguous(WHITE));
    }

    @Test
    public void makeMoveTest() {
        board.makeMove(move4);
        assertEquals(1, board.movesMade());
        assertEquals(WHITE, board.turn());
        assertEquals(board.getBoard()[2][5], EMP);
        assertEquals(board.getBoard()[4][3], BP);
    }

    @Test
    public void retractTest() {
        board.makeMove(move4);
        board.retract();
        assertEquals(0, board.movesMade());
        assertEquals(BLACK, board.turn());
        assertEquals(board.getBoard()[2][5], BP);
        assertEquals(board.getBoard()[4][3], WP);
    }

}
