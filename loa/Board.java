package loa;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import static loa.Piece.*;
import static loa.Side.*;

/** Represents the state of a game of Lines of Action. A Board is immutable.
 *  Its MutableBoard subclass allows moves to be made.
 *  @author Weier Wan
 */
class Board {

    /** A Board whose initial contents are taken from
     *  INITIALCONTENTS and in which it is PLAYER's move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row-1][col-1]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     */
    Board(Piece[][] initialContents, Side player) {
        assert player != null && initialContents.length == 8;
        _pieces = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                _pieces[i][j] = initialContents[i][j];
            }
        }
        _turn = player;
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BLACK);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this(board.getBoard(), board.turn());
    }

    /** Return the contents of column C, row R, where 1 <= C,R <= 8,
     *  where column 1 corresponds to column 'a' in the standard
     *  notation. */
    Piece get(int c, int r) {
        assert 1 <= c && c <= 8 && 1 <= r && r <= 8;
        return _pieces[r - 1][c - 1];
    }

    /** Return the contents of the square SQ.  SQ must be the
     *  standard printed designation of a square (having the form cr,
     *  where c is a letter from a-h and r is a digit from 1-8). */
    Piece get(String sq) {
        return get(col(sq), row(sq));
    }

    /** Return the column number (a value in the range 1-8) for SQ.
     *  SQ is as for {@link get(String)}. */
    static int col(String sq) {
        return sq.charAt(0) - 'a' + 1;
    }

    /** Return the row number (a value in the range 1-8) for SQ.
     *  SQ is as for {@link get(String)}. */
    static int row(String sq) {
        return sq.charAt(1) - '1' + 1;
    }

    /** Return the Side that is currently next to move. */
    Side turn() {
        return _turn;
    }

    /** Return true iff MOVE is legal for the player currently on move. */
    boolean isLegal(Move move) {
        int r0 = move.getRow0() - 1;
        int c0 = move.getCol0() - 1;
        if (_pieces[r0][c0].side() != _turn) {
            return false;
        }
        int ver = move.getRow1() - 1 - r0;
        int hor = move.getCol1() - 1 - c0;
        int count = 0;
        if (ver == 0 && hor != 0) {
            for (Piece p : _pieces[r0]) {
                if (p != EMP) {
                    count++;
                }
            }
            if (Math.abs(hor) != count) {
                return false;
            }
        } else if (hor == 0 && ver != 0) {
            for (Piece[] p : _pieces) {
                if (p[c0] != EMP) {
                    count++;
                }
            }
            if (Math.abs(ver) != count) {
                return false;
            }
        } else if (hor == ver && hor != 0) {
            for (int i = 0; i < 8; i++) {
                int c = c0 - r0 + i;
                if (c >= 0 && c < 8 && _pieces[i][c] != EMP) {
                    count++;
                }
            }
            if (Math.abs(hor) != count) {
                return false;
            }
        } else if (hor == -ver && hor != 0) {
            for (int i = 0; i < 8; i++) {
                int c = r0 + c0 - i;
                if (c >= 0 && c < 8 && _pieces[i][c] != EMP) {
                    count++;
                }
            }
            if (Math.abs(hor) != count) {
                return false;
            }
        } else {
            return false;
        }
        return isLegal(r0, c0, ver, hor, count);
    }

    /** Return true if the move from _pieces[ROW][COL] to
     *  _pieces[ROW+VER][COL+HOR] is legal. NUM is the number of moves,
     */
    boolean isLegal(int row, int col, int ver, int hor, int num) {
        if (_pieces[row + ver][col + hor].side() == _turn) {
            return false;
        } else {
            int count = Math.max(Math.abs(hor), Math.abs(ver));
            for (int i = 1; i < count; i++) {
                if (_pieces[row + i * (ver / count)][col
                            + i * (hor / count)].side() == _turn.opponent()) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Return a sequence of all legal from this position. */
    List<Move> legalMoves() {
        List<Move> lm = new ArrayList<Move>();
        List<Move> m;
        int[] numPInLine = accumNumInLine();
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                if (_pieces[i][j].side() == _turn) {
                    m = findMoves(i, j, numPInLine);
                    lm.addAll(m);
                }
            }
        }
        return lm;
    }

    /** Return the number of pieces in each line as
     *  a sequence of integer. */
    int[] accumNumInLine() {
        int[] result = new int[LENGTH * 2 + (LENGTH * 2 - 1) * 2];
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                if (_pieces[i][j] != EMP) {
                    result[i]++;
                    result[LENGTH + j]++;
                    result[LENGTH * 2 + i + j]++;
                    result[LENGTH * 4 - 1 + (i - j + LENGTH - 1)]++;
                }
            }
        }
        return result;
    }

    /** Return all legal moves from _pieces[R][C]. P is a sequence
     * of integer returned by accumNumInLine().
     */
    List<Move> findMoves(int r, int c, int[] p) {
        assert r >= 0 && r < LENGTH && c >= 0 && c < LENGTH;
        List<Move> moves = new ArrayList<Move>();
        int hor = p[r];
        int ver = p[LENGTH + c];
        int up = p[LENGTH * 2 + r + c];
        int down = p[LENGTH * 4 - 1 + (r - c + LENGTH - 1)];
        findMoves(r, c, hor, 0, moves);
        findMoves(r, c, -hor, 0, moves);
        findMoves(r, c, 0, ver, moves);
        findMoves(r, c, 0, -ver, moves);
        findMoves(r, c, up, -up, moves);
        findMoves(r, c, -up, up, moves);
        findMoves(r, c, down, down, moves);
        findMoves(r, c, -down, -down, moves);
        return moves;
    }

    /** Add the move from _pieces[ROW][COL] to _pieces[ROW+VER][COL+HOR]
     *  to MOVES if the move is legal.
     */
    void findMoves(int row, int col, int hor, int ver, List<Move> moves) {
        boolean legal = true;
        if (col + hor > 7 || row + ver > 7 || col + hor < 0 || row + ver < 0
                || _pieces[row + ver][col + hor].side() == _turn) {
            legal = false;
        } else {
            int count = Math.max(Math.abs(hor), Math.abs(ver));
            int hIncr = hor / count;
            int vIncr = ver / count;
            for (int k = 1; k < count; k++) {
                if (_pieces[row + (vIncr * k)][col + (hIncr * k)].side()
                        == _turn.opponent()) {
                    legal = false;
                    break;
                }
            }
            if (legal) {
                moves.add(Move.create
                    (col + 1, row + 1, col + hor + 1, row + ver + 1));
            }
        }
    }

    /** Return true iff the game is currently over.  A game is over if
     *  either player has all his pieces continguous. */
    boolean gameOver() {
        return piecesContiguous(BLACK) || piecesContiguous(WHITE);
    }

    /** Return true iff PLAYER's pieces are continguous. */
    boolean piecesContiguous(Side player) {
        int totalPieces = countPieces(player);
        Set<int[]> contiguousPieces = new HashSet<int[]>();
        int r = 0;
        int c = 0;
    outerloop:
        for (r = 0; r < LENGTH; r++) {
            for (c = 0; c < LENGTH; c++) {
                if (_pieces[r][c].side() == player) {
                    break outerloop;
                }
            }
        }
        findAllContiguous(r, c, contiguousPieces);
        return contiguousPieces.size() == totalPieces;
    }

    /** find all pieces which are contiguous with _pieces[R][C], and
     *  add them into FOUND. */
    void findAllContiguous(int r, int c, Set<int[]> found) {
        int[] pair = {r, c};
        if (!contains(found, pair)) {
            found.add(pair);
            for (Iterator<int[]> itr
                    = findAdjacent(r, c).iterator(); itr.hasNext();) {
                int[] next = itr.next();
                findAllContiguous(next[0], next[1], found);
            }
        }
    }

    /** Return true if SET contains PAIR. */
    static boolean contains(Set<int[]> set, int[] pair) {
        for (Iterator<int[]> itr = set.iterator(); itr.hasNext();) {
            int[] next = itr.next();
            if (next[0] == pair[0] && next[1] == pair[1]) {
                return true;
            }
        }
        return false;
    }

    /** Return the total number of pieces remaining of Side PLAYER. */
    int countPieces(Side player) {
        int result = 0;
        for (Piece[] row : _pieces) {
            for (Piece piece : row) {
                if (piece.side() == player) {
                    result++;
                }
            }
        }
        return result;
    }

    /** Return true if the piece at _pieces[R][C] of THIS has
     * adjacent PIECE of the same side.
     */
    Set<int[]> findAdjacent(int r, int c) {
        Side side = _pieces[r][c].side();
        Set<int[]> result = new HashSet<int[]>();
        if (r == 0 && c == 0) {
            addAdjacent(0, 1, side, result);
            addAdjacent(1, 1, side, result);
            addAdjacent(1, 0, side, result);
        } else if (r == 0 && c == 7) {
            addAdjacent(0, 6, side, result);
            addAdjacent(1, 6, side, result);
            addAdjacent(1, 7, side, result);
        } else if (r == 7 && c == 0) {
            addAdjacent(7, 1, side, result);
            addAdjacent(6, 1, side, result);
            addAdjacent(6, 0, side, result);
        } else if (r == 7 && c == 7) {
            addAdjacent(7, 6, side, result);
            addAdjacent(6, 6, side, result);
            addAdjacent(6, 7, side, result);
        } else if (r == 0) {
            addAdjacent(0, c - 1, side, result);
            addAdjacent(0, c + 1, side, result);
            addAdjacent(1, c - 1, side, result);
            addAdjacent(1, c, side, result);
            addAdjacent(1, c + 1, side, result);
        } else if (r == 7) {
            addAdjacent(7, c - 1, side, result);
            addAdjacent(7, c + 1, side, result);
            addAdjacent(6, c - 1, side, result);
            addAdjacent(6, c, side, result);
            addAdjacent(6, c + 1, side, result);
        } else if (c == 0) {
            addAdjacent(r - 1, 0, side, result);
            addAdjacent(r + 1, 0, side, result);
            addAdjacent(r - 1, 1, side, result);
            addAdjacent(r, 1, side, result);
            addAdjacent(r + 1, 1, side, result);
        } else if (c == 7) {
            addAdjacent(r - 1, 7, side, result);
            addAdjacent(r + 1, 7, side, result);
            addAdjacent(r - 1, 6, side, result);
            addAdjacent(r, 6, side, result);
            addAdjacent(r + 1, 6, side, result);
        } else {
            addAdjacent(r - 1, c - 1, side, result);
            addAdjacent(r - 1, c, side, result);
            addAdjacent(r - 1, c + 1, side, result);
            addAdjacent(r, c - 1, side, result);
            addAdjacent(r, c + 1, side, result);
            addAdjacent(r + 1, c - 1, side, result);
            addAdjacent(r + 1, c, side, result);
            addAdjacent(r + 1, c + 1, side, result);
        }
        return result;
    }

    /** add _pieces[R][C] to PAIRS if its SIDE is the same as the
     * piece we are looking at. */
    void addAdjacent(int r, int c, Side side, Set<int[]> pairs) {
        if (_pieces[r][c].side() == side) {
            pairs.add(new int[]{r, c});
        }
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    /** Returns move #K used to reach the current position, where
     *  0 <= K < movesMade().  Does not include retracted moves. */
    Move getMove(int k) {
        return _moves.get(k);
    }

    /** Return the configuration of the BOARD. */
    Piece[][] getBoard() {
        return _pieces;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = LENGTH - 1; i >= 0; i--) {
            str.append(" ");
            for (Piece j : _pieces[i]) {
                str.append(" " + j.textName());
            }
            str.append("\n");
        }
        str.append("Next move: " + _turn.toString().toLowerCase());
        str.append("\nMoves: " + movesMade());
        return str.toString();
    }


    /** The Side currently in turn. */
    protected Side _turn;

    /** The configuration of the board. */
    protected Piece[][] _pieces;

    /** A list of moves has been make on THIS board. */
    protected List<Move> _moves = new ArrayList<Move>();

    /** The side length of the board. */
    static final int LENGTH = 8;

    /** The standard initial configuration for Lines of Action. */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

}
