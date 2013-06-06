package loa;

import static loa.Piece.*;

/** Represents the state of a game of Lines of Action, and allows making moves.
 *  @author Weier Wan */
class MutableBoard extends Board {

    /** A MutableBoard whose initial contents are taken from
     *  INITIALCONTENTS and in which it is PLAYER's move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row-1][col-1]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     */
    MutableBoard(Piece[][] initialContents, Side player) {
        super(initialContents, player);
    }

    /** A new board in the standard initial position. */
    MutableBoard() {
        super();
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    MutableBoard(Board board) {
        super(board);
    }

    /** Reverse the turn of the current board. */
    void reverseTurn() {
        _turn = _turn.opponent();
    }

    /** Assuming isLegal(MOVE), make MOVE. */
    void makeMove(Move move) {
        Piece pMove = _pieces[move.getRow0() - 1][move.getCol0() - 1];
        _pieces[move.getRow0() - 1][move.getCol0() - 1] = EMP;
        _pieces[move.getRow1() - 1][move.getCol1() - 1] = pMove;
        _moves.add(move);
        _turn = _turn.opponent();
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move lastMove = _moves.get(_moves.size() - 1);
        int r0 = lastMove.getRow0() - 1;
        int c0 = lastMove.getCol0() - 1;
        int r1 = lastMove.getRow1() - 1;
        int c1 = lastMove.getCol1() - 1;
        int ver = r1 - r0;
        int hor = c1 - c0;
        int count = 0;
        if (ver == 0 && hor != 0) {
            for (Piece p : _pieces[r0]) {
                if (p != EMP) {
                    count++;
                }
            }
        } else if (hor == 0 && ver != 0) {
            for (Piece[] p : _pieces) {
                if (p[c0] != EMP) {
                    count++;
                }
            }
        } else if (hor == ver && hor != 0) {
            for (int i = 0; i < 8; i++) {
                int c = c0 - r0 + i;
                if (c >= 0 && c < 8 && _pieces[i][c] != EMP) {
                    count++;
                }
            }
        } else if (hor == -ver && hor != 0) {
            for (int i = 0; i < 8; i++) {
                int c = r0 + c0 - i;
                if (c >= 0 && c < 8 && _pieces[i][c] != EMP) {
                    count++;
                }
            }
        }
        Piece move = _pieces[r1][c1];
        _pieces[r0][c0] = move;
        if (count == Math.max(Math.abs(ver), Math.abs(hor))) {
            _pieces[r1][c1] = EMP;
        } else {
            if (move == BP) {
                _pieces[r1][c1] = WP;
            } else if (move == WP) {
                _pieces[r1][c1] = BP;
            }
        }
        _moves.remove(_moves.size() - 1);
        _turn = _turn.opponent();
    }

}
