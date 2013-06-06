package loa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Random;
import ucb.util.Stopwatch;

import static loa.Side.*;
import static loa.Main.*;

/** Represents one game of Lines of Action.
 *  @author Weier Wan */
class Game {

    /** A new Game between NUMHUMAN humans and 2-NUMHUMAN AIs.  SIDE0
     *  indicates which side the first player (known as ``you'') is
     *  playing.  SEED is a random seed for random-number generation.
     *  TIME is the time limit each side has to make its moves (in seconds).
     *  A TIME value of <=0 means there is no time limit.  A SEED value <= 0
     *  means to use a randomly seeded generator.
     */
    Game(int numHuman, Side side0, long seed, long time) {
        if (seed <= 0) {
            _randomSource = new Random();
        } else {
            _randomSource = new Random(seed);
        }
        _numHuman = numHuman;
        _aiStarted = false;
        _player1 = new HumanPlayer(BLACK, this);
        _player2 = new HumanPlayer(WHITE, this);
        if (time > 0) {
            _remainingTimeB = time;
            _remainingTimeW = time;
            _hasTimeLimit = true;
        } else {
            _hasTimeLimit = false;
        }
        _board = new MutableBoard();
        _side0 = side0;
    }

    /** Return the current board. */
    Board getBoard() {
        return _board;
    }

    /** Return a move from the terminal.  Processes any intervening commands
     *  as well. */
    String getMove() {
        while (true) {
            try {
                System.out.print(">  ");
                System.out.flush();
                String command = input.readLine();
                if (command == null || command.startsWith("q")) {
                    System.exit(0);
                }
                command = command.trim();
                if (command.startsWith("p")) {
                    if (_numHuman == 0) {
                        _player1 = new MachinePlayer1(_player1.side(), this);
                        _player2 = new MachinePlayer1(_player2.side(), this);
                        _aiStarted = true;
                        return "p";
                    } else if (_numHuman == 1) {
                        if (_side0.opponent() == _player1.side()) {
                            _player1 = new MachinePlayer1(_player1.side(), this);
                        } else {
                            _player2 = new MachinePlayer1(_player2.side(), this);
                        }
                        _aiStarted = true;
                        return "p";
                    }
                } else if (command.startsWith("s")) {
                    System.out.println();
                    System.out.println("===");
                    System.out.println(_board);
                    System.out.println("===");
                } else if (command.startsWith("t")) {
                    if (!_hasTimeLimit) {
                        System.out.println("Does not have time limit");
                    } else {
                        System.out.printf("Time remaining for BLACK: "
                                + "%d seconds\n", _remainingTimeB / SEC_MILLI);
                        System.out.printf("Time remaining for WHITE: "
                                + "%d seconds\n", _remainingTimeW / SEC_MILLI);
                    }
                } else if (command.startsWith("#")
                        || command.isEmpty() || command.matches("\\s*")) {
                    continue;
                } else if (command.matches("([a-h][1-8]"
                        + "-[a-h][1-8])(\\s.*$)*")) {
                    return command.substring(0, 5);
                } else {
                    System.out.println("Input is invalid."
                            + " Please enter a new move.");
                }
            } catch (IOException e) {
                System.exit(0);
            }
        }
    }

    /** Play this game, printing any transcript and other results. */
    public void play() {
        input = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            if (_board.gameOver()) {
                if (!_board.piecesContiguous(BLACK)) {
                    System.out.println("White wins.");
                } else if (!_board.piecesContiguous(WHITE)) {
                    System.out.println("Black wins.");
                } else {
                    System.out.println(_board.turn() == BLACK
                            ? "White wins." : "Black wins.");
                }
                break;
            }
            Player inTurn = _board.turn() == _player1.side()
                    ? _player1 : _player2;
            Stopwatch timer = new Stopwatch();
            if (_hasTimeLimit && (_numHuman == 2 || _aiStarted)) {
                timer.start();
            }
            Move nextMove = inTurn.makeMove();
            if (nextMove == null) {
                continue;
            }
            _board.makeMove(nextMove);
            if (_hasTimeLimit && (_numHuman == 2 || _aiStarted)) {
                long timeUsed = timer.stop();
                if (inTurn.side() == BLACK) {
                    _remainingTimeB = _remainingTimeB - timeUsed;
                    if (_remainingTimeB < 0) {
                        _board.retract();
                        System.out.println("White wins.");
                        break;
                    }
                } else {
                    _remainingTimeW = _remainingTimeW - timeUsed;
                    if (_remainingTimeW < 0) {
                        _board.retract();
                        System.out.println("Black wins.");
                        break;
                    }
                }
            }
        }
        while (true) {
            getMove();
        }
    }

    /** Return time remaining for SIDE (in seconds). */
    long timeRemaining(Side side) {
        if (side == BLACK) {
            return _remainingTimeB;
        } else {
            return _remainingTimeW;
        }
    }

    /** Return the random number generator for this game. */
    Random getRandomSource() {
        return _randomSource;
    }

    /** Return the Player who is denoted by NUM . */
    Player getPlayer(int num) {
        assert num == 1 || num == 2;
        if (num == 1) {
            return _player1;
        } else {
            return _player2;
        }
    }

    /** Return whether this game has time limit. */
    boolean hasTimeLimit() {
        return _hasTimeLimit;
    }

    /** The official game board. */
    protected MutableBoard _board;

    /** A source of random numbers, primed to deliver the same sequence in
     *  any Game with the same seed value. */
    private Random _randomSource;

    /** The first player. */
    protected Player _player1;

    /** The second player. */
    protected Player _player2;

    /** Number of human player in the game. */
    protected int _numHuman;

    /** Remaining time for BLACK side. */
    protected long _remainingTimeB;

    /** Remaining time for WHITE side. */
    protected long _remainingTimeW;

    /** Indicate whether Machine Player(s) (if exist(s)) starts to play. */
    protected boolean _aiStarted;

    /** The side of first player. */
    protected Side _side0;

    /** Indicate whether the game has time limit. */
    protected boolean _hasTimeLimit;

    /** The bufferdreader used to parse input. */
    private BufferedReader input;


}
