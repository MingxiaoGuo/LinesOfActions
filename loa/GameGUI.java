package loa;

import static loa.Main.SEC_MILLI;
import static loa.Side.*;

import javax.swing.JFrame;

import ucb.util.Stopwatch;

/** A Game class which uses GUI.
 *  @author Weier Wan
 */
public class GameGUI extends Game {

    /** Constructor of a new game use GUI. The game has NUMHUMAN human players,
     *  with "you" play the side SIDE0. TIME is the time limit for each player,
     *  SEED is used for generating random number. */
    public GameGUI(int numHuman, Side side0, long seed, long time) {
        super(numHuman, side0, seed, time);
        _display = new Gui();
        _display.setBoard(this);
    }

    @Override
    String getMove() {
        try {
            while (true) {
                Thread.sleep(TIME_SLEEP);
                if (_display.moveMade()) {
                    _display.setMoveMade(false);
                    Move nextMove = _display.getMove();
                    if (nextMove == null) {
                        if (_numHuman == 0) {
                            _player1 = new MachinePlayer1(_player1.side(), this);
                            _player2 = new MachinePlayer1(_player2.side(), this);
                        } else if (_numHuman == 1) {
                            if (_side0.opponent() == _player1.side()) {
                                _player1 = new MachinePlayer1(_player1.side(),
                                        this);
                            } else {
                                _player2 = new MachinePlayer1(_player2.side(),
                                        this);
                            }
                        }
                        _aiStarted = true;
                        return "p";
                    }
                    _display.setMove(null);
                    return nextMove.toString();
                }
            }
        } catch (InterruptedException e) {
            return "p";
        }
    }

    @Override
    public void play() {
        _display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _display.pack();
        _display.setResizable(true);
        _display.setLocationRelativeTo(null);
        _display.setVisible(true);
        while (true) {
            if (_board.gameOver()) {
                if (!_board.piecesContiguous(BLACK)) {
                    _display.gameOver(WHITE);
                    System.out.println("White wins.");
                } else if (!_board.piecesContiguous(WHITE)) {
                    _display.gameOver(BLACK);
                    System.out.println("Black wins.");
                } else {
                    _display.gameOver(_board.turn() == BLACK
                            ? WHITE : BLACK);
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
            if (inTurn instanceof MachinePlayer1) {
                _display.makeMove(nextMove);
            }
            if (_hasTimeLimit && (_numHuman == 2 || _aiStarted)) {
                long timeUsed = timer.stop();
                if (inTurn.side() == BLACK) {
                    _remainingTimeB = _remainingTimeB - timeUsed;
                    _display.getTime(BLACK).setText("Black left time: "
                            + (_remainingTimeB / SEC_MILLI) + "s");
                    if (_remainingTimeB < 0) {
                        runOutOfTime(BLACK);
                        break;
                    }
                } else {
                    _remainingTimeW = _remainingTimeW - timeUsed;
                    _display.getTime(WHITE).setText("White left time: "
                            + (_remainingTimeW / SEC_MILLI) + "s");
                    if (_remainingTimeW < 0) {
                        runOutOfTime(WHITE);
                        break;
                    }
                }
            }
        }
    }

    /** Actions to make if SIDE runs out of time. */
    void runOutOfTime(Side side) {
        _board.retract();
        _display.gameOver(side);
        System.out.println(side == BLACK ? "White wins" : "Black wins.");
    }

    /** The GUI used in the game. */
    private Gui _display;

    /** Sleeping time of the thread. */
    private static final long TIME_SLEEP = 200;

}
