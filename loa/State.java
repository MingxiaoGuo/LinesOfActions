/**
 * Write a description of class State here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
public class State {

    private int v;
/*    private int alphaValue;
    private int betaValue;*/
    private int minmax; //min max for AI(min is 2, max is 1)
    private int depth;
    private int utility; //estimated desirability
    private int currentPlayer;
    private int opponentPlayer;

    private Board board;
    private ArrayList<Move> action;

    public State() {}

    /**
     * Initializing state, assign all possible moves
     *
     * @return
     */
    public State(Board currentBoard, int player) {
        this.minmax = player;
        this.currentPlayer = player;
        if (currentPlayer == 1) {
            opponentPlayer = 2;
        } else {
            opponentPlayer = 1;
        }
        this.board = currentBoard;
        this.action = board.getAllPossibleMoves(player);
        // max number of parts minus current number of parts
        // have to change to max notation of getScore()
        initUtility();

    }

    public int evaluate() {
        if (board.isWin(currentPlayer)) {
            return Integer.MAX_VALUE;
        } else if (board.isWin(opponentPlayer)){
            return Integer.MIN_VALUE;
        } else {
            return evaluateFunction();
        }
    }


    public int evaluateFunction() {
        ArrayList<Move> allOpponentMoves = new ArrayList<>();


        allOpponentMoves = board.getAllPossibleMoves(opponentPlayer);

        int myNumChecker = board.CountCheckers(currentPlayer);
        int oppoNumChecker = board.CountCheckers(opponentPlayer);

        int myScore = 0, oppoScore = 0;
        for (int i = 0; i < board.getLength(); i++) {
            for (int j = 0; j < board.getLength(); j++) {
                // At the edge of the board
                if (i == 0 || i == board.getLength() - 1 || j == 0 || j == board.getLength() - 1) {
                    if (board.getCell(i, j) == currentPlayer) {
                        myScore++;
                    } else {
                        oppoScore++;
                    }
                }
                // At one step inwards
                if (i == 1 || i == board.getLength() - 2 || j == 1 || j == board.getLength() - 2) {
                    if (board.getCell(i, j) == currentPlayer) {
                        myScore += 2;
                    } else {
                        oppoScore += 2;
                    }
                }

                // At middle
                if (i == 2 || i == board.getLength() - 3 || j == 2 || j == board.getLength() - 3) {
                    if (board.getCell(i, j) == currentPlayer) {
                        myScore += 4;
                    } else {
                        oppoScore += 4;
                    }
                }
            }
        }
        int finalScore = ((action.size() + myScore) * 100 / myNumChecker)
                - ((allOpponentMoves.size() + oppoScore) * 100 / oppoNumChecker)
                + (action.size() - allOpponentMoves.size()) * 3;

        return finalScore;
    }

    /**
     * if minmax is 1, that means it's AI's turn
     * if minmax is 2, that means it's human's turn
     * @return
     */
    private void initUtility() {
        if (board.isWin(currentPlayer)) {
            if (!board.isWin(opponentPlayer)) {
                this.setUtility(100);
            }
        } else if (!board.isWin(currentPlayer)) {
            if (board.isWin(opponentPlayer)) {
                this.setUtility(-100);
            }

        } else {
            this.setUtility(0);
        }
    }

    public void copy(State state) {
        //this.setAlphaValue(state.getAlphaValue());
        Board newBoard = new Board(state.getBoard());
        this.board = newBoard;
        this.action = state.getAction();
        this.utility = state.getUtility();
        this.currentPlayer = state.getCurrentPlayer();
        this.opponentPlayer = state.getOpponentPlayer();
    }

    /**
     * Make move in this state
     * @param move
     */
    public void makeMove(Move move) {
        this.board.moveChecker(move, currentPlayer);
        // change turn
        int temp = currentPlayer;
        this.currentPlayer = opponentPlayer;
        this.opponentPlayer = temp;
        this.action = board.getAllPossibleMoves(currentPlayer);
        initUtility();
    }



    public Board getBoard() {
        return board;
    }

    public int getMinmax() {

        return minmax;
    }

/*    public int getBetaValue() {

        return betaValue;
    }

    public int getAlphaValue() {

        return alphaValue;
    }*/

    public int getV() {

        return v;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setMinmax(int minmax) {

        this.minmax = minmax;
    }

 /*   public void setBetaValue(int betaValue) {

        this.betaValue = betaValue;
    }

    public void setAlphaValue(int alphaValue) {

        this.alphaValue = alphaValue;
    }
*/
    public void setV(int v) {

        this.v = v;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {

        return depth;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    public int getUtility() {

        return utility;
    }

    public void setAction(ArrayList<Move> action) {
        this.action = action;
    }

    public ArrayList<Move> getAction() {

        return action;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setOpponentPlayer(int opponentPlayer) {
        this.opponentPlayer = opponentPlayer;
    }

    public int getOpponentPlayer() {
        return opponentPlayer;
    }
}
