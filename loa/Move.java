package loa;

import java.util.HashMap;

import static loa.Board.*;

/** A move in Lines of Action.
 *  @author Weier Wan */
class Move {

    /* Implementation note: We create moves by means of static "factory
     * methods" all named create, which in turn use the single (private)
     * constructor.  Factory methods have certain advantages over constructors:
     * they allow you to produce results having an arbitrary subtype of Move,
     * and they don't require that you produce a new object each time.  This
     * second advantage is useful when you are trying to speed up the creation
     * of Moves for use in automated searching for moves.  You can (if you
     * want) create just one instance of the Move representing 1-5, for example
     * and return it whenever that move is requested. */

    /** Return a move of the piece at COLUMN0, ROW0 to COLUMN1, ROW1. */
    static Move create(int column0, int row0, int column1, int row1) {
        Move newMove = new Move(column0, row0, column1, row1);
        if (_allMoves.containsKey(newMove)) {
            return _allMoves.get(newMove);
        } else {
            _allMoves.put(newMove, newMove);
            return newMove;
        }
    }

    /** Return a move of the piece at COLUMN0, ROW0 to COLUMN1, ROW1
     *  with SCORE. */
    static Move create(int column0, int row0,
            int column1, int row1, int score) {
        Move move = create(column0, row0, column1, row1);
        move.setScore(score);
        return move;
    }

    /** A new Move of the piece at COL0, ROW0 to COL1, ROW1. */
    private Move(int col0, int row0, int col1, int row1) {
        _col0 = col0;
        _row0 = row0;
        _col1 = col1;
        _row1 = row1;
    }

    /** Return the column at which this move starts, as an index in 1--8. */
    int getCol0() {
        return _col0;
    }

    /** Return the row at which this move starts, as an index in 1--8. */
    int getRow0() {
        return _row0;
    }

    /** Return the column at which this move ends, as an index in 1--8. */
    int getCol1() {
        return _col1;
    }

    /** Return the row at which this move ends, as an index in 1--8. */
    int getRow1() {
        return _row1;
    }

    /** Return the length of this move (number of squares moved). */
    int length() {
        return Math.max(Math.abs(_row1 - _row0), Math.abs(_col1 - _col0));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Move)) {
            return false;
        }
        Move move2 = (Move) obj;
        return _row0 == move2.getRow0() && _col0 == move2.getCol0()
                && _row1 == move2.getRow1() && _col1 == move2.getCol1();
    }

    @Override
    public int hashCode() {
        return label(_row0, _col0) * (LENGTH * LENGTH) + label(_row1, _col1);
    }

    @Override
    public String toString() {
        return String.format("%s%d-%s%d",
                colName(_col0), _row0, colName(_col1), _row1);
    }

    /** Return the hash value of the piece at ROW and COL. */
    int label(int row, int col) {
        return LENGTH * (row - 1) + (col - 1);
    }

    /** Return the label of COL. */
    char colName(int col) {
        return (char) ('a' + col - 1);
    }

    /** Return the score of THIS move. */
    int getScore() {
        return _score;
    }

    /**Set the score of THIS move to SCORE. */
    void setScore(int score) {
        _score = score;
    }

    /** A hashmap used to stored all initialized moves. */
    private static HashMap<Move, Move> _allMoves = new HashMap<Move, Move>();

    /** Column and row numbers of starting and ending points. */
    private int _col0, _row0, _col1, _row1;

    /** The score of the move. */
    private int _score;

}
