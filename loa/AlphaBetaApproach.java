import java.util.*;
/**
 * Write a description of class AlphaBetaApproach here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AlphaBetaApproach {
    public int treeDepth;
    public int nodeCount;
    public int maxECount = 0;
    public int minECount = 0;
    public int AIPlayer = 0;
    public int prunMinCount = 0;
    public int prunMaxCount = 0;

    public AlphaBetaApproach() {
    }
    // MIN, MAX move, only for 5*5 game
    private static final Move MAX_MOVE = new Move(new Coordinate(0,0), new Coordinate(0,0), Integer.MAX_VALUE);
    private static final Move MIN_MOVE = new Move(new Coordinate(4,4), new Coordinate(4,4), Integer.MIN_VALUE);
    private static final int CURRENT_WIN = 100;
    private static final int OPPONENT_WIN = -100;

    /**
     *
     * @param currentState
     * @return
     */
    public Move runAlphaBeta(State currentState, int p) {
        // Base case
        this.AIPlayer = p;
        currentState.getBoard().printMap();

        nodeCount = 1; // base value
        Move result = new Move();

        //currentState.setV(maxValue(currentState, Integer.MIN_VALUE, Integer.MAX_VALUE, 0));
        int v = maxValue(currentState, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        for (Move move : currentState.getAction()) {
            if (move.getUtility() == v) {
                result = move;
            }
        }

        return result;
    }

    private int maxValue(State state, int alpha, int beta, int depth) {
        State newState = new State();
        newState.copy(state);
        nodeCount++;
        if (isTerminated(newState)) {
            // if currentPlayer win
            //treeDepth = depth;
            if (newState.getBoard().isWin(state.getCurrentPlayer())) {
                return CURRENT_WIN;
            }
            if (newState.getBoard().isWin(state.getOpponentPlayer())) {
                return OPPONENT_WIN;
            }
        }
        if (cutOffTest(newState, depth)) {
            findBestMove(newState);
            maxECount++;
            state.setUtility(newState.getUtility());
            return state.getUtility();
        }
        newState.setV(Integer.MIN_VALUE); // max node, set v -infinity
        for (Move move : newState.getAction()) {
            int childV = minValue(result(newState, move), alpha, beta, depth + 1);
            newState.setV(Math.max(newState.getV(), childV));
            // set the utility of this move
            move.setUtility(childV);

            if (newState.getV() >= beta) {
                prunMaxCount++;
                state.setUtility(newState.getUtility());
                return newState.getV();
            }
            alpha = Math.max(alpha, state.getV());
            //newState.getBoard().undoLastMove(AIPlayer); // because the max will always be the move of AI
        }
        state.setV(newState.getV());
        return state.getV();
    }
    /**
     * minValue is for AI, so when meet the terminate condition, we return 100 if AI win, -100 if human win
     */
    private int minValue(State state, int alpha, int beta, int depth) {
        State newState = new State();
        newState.copy(state);
        nodeCount++;
        if (isTerminated(newState)) {
            if (newState.getBoard().isWin(state.getCurrentPlayer())) {
                return OPPONENT_WIN;
            }
            if (newState.getBoard().isWin(state.getOpponentPlayer())) {
                return CURRENT_WIN;
            }
        }
        if (cutOffTest(newState, depth)) {
            findBestMove(newState);
            minECount++;
            state.setUtility(newState.getUtility());
            return state.getUtility();
        }
        state.setV(Integer.MAX_VALUE);
/*        System.out.println("in depth " + depth);
        for (Move move : state.getAction()) {
            System.out.println(move.getOriginalPos().getX() + ", " + move.getOriginalPos().getY() + "=>"+move.getTargetPos().getX() + ", " + move.getTargetPos().getY());
        }*/
        for (Move move : newState.getAction()) {
            int childV = maxValue(result(newState, move), alpha, beta, depth + 1);
            move.setUtility(childV);
            newState.setV(Math.min(state.getV(), childV));

            if (newState.getV() <= alpha) {
                prunMinCount++;
                state.setUtility(newState.getUtility());
                state.setV(newState.getV());
                return state.getV();

            }
            beta = Math.min(beta, newState.getV());
            //newState.getBoard().undoLastMove(AIPlayer == 1 ? 2 : 1);
        }
        state.setV(newState.getV());
        return state.getV();
    }

    /**
     * Find the result of next move
     * @param state current state of the game
     * @param move the move player wants to make
     * @return the state after making that move
     */
    private State result(State state, Move move) {
        State newState = new State();
        newState.copy(state);
        newState.makeMove(move);// 1. board change 2. action change

        //nodeCount[newState.getDepth()]++;
        return newState;
    }

    /**
     * When the utility reaches 1 for current state, then it means that this state contains a winner
     * @param state
     * @return
     */
    private boolean isTerminated(State state) {
        // terminal condition
        if (state.getBoard().hasWinner()) {
            return true;
        } else {
            return false;
        }
    }

    //Relates to the difficulty choice human player made
    private boolean cutOffTest(State state, int depth) {
        if(depth == treeDepth){
            return true;
        }else{
            return false;
        }
    }
    // evaluation the distance of all checkers belong to player p
    public int evalDistance(State state, int p) {
        int result = 0;
        ArrayList<Coordinate> list = state.getBoard().getAllAvailableCheckers(p);
        for(Coordinate coor1 : list) {
            for(Coordinate coor2 : list) {
                // two different coordinates
                if(coor1.getX() != coor2.getX() && coor1.getY() != coor2.getY()) {
                    double dist = Math.sqrt(Math.pow(coor1.getX() - coor2.getX(), 2) + Math.pow(coor1.getY() - coor2.getY(), 2));
                    result += (int) dist;
                }
            }
        }
        return result;
    }

    Move findBestMove(State state) {
        Move bestSoFar = MIN_MOVE;
        for(Move move : state.getAction()) {
            State newState = result(state, move);
            int score = Math.abs(evalDistance(state, state.getCurrentPlayer()) - evalDistance(state, state.getOpponentPlayer()) );
            if(state.getCurrentPlayer() != AIPlayer) {// if currentPlayer is not AI player, then score has to be the opposite value
                score = 0 - score;
            }
            move.setUtility(score);
            if(score > bestSoFar.getUtility()) {
                bestSoFar = move;
            }
        }
        return bestSoFar;
    }    
    
    /* Old evaluation function, useless, but it will be kept for reference
    Move findBestMove(State state) {
        Board b = new Board(state.getBoard());

        Move bestSoFar = MIN_MOVE;
        for (Move move : state.getAction()) {

            b.moveChecker(move, state.getCurrentPlayer());
            int score = evaluateFunction(b, state.getCurrentPlayer(), state.getOpponentPlayer());
            move.setUtility(score);

            if (score > bestSoFar.getUtility()) {
                bestSoFar = move;
            }
        }
        return bestSoFar;
    }

    public int evaluateFunction(Board b, int currentPlayer, int opponentPlayer) {
        ArrayList<Move> allOpponentMoves = new ArrayList<>();
        ArrayList<Move> allMyMoves = new ArrayList<>();

        allOpponentMoves = b.getAllPossibleMoves(opponentPlayer);
        allMyMoves = b.getAllPossibleMoves(currentPlayer);

        int myNumChecker = b.CountCheckers(currentPlayer);
        int oppoNumChecker = b.CountCheckers(opponentPlayer);

        int myScore = 0, oppoScore = 0;
        for (int i = 0; i < b.getLength(); i++) {
            for (int j = 0; j < b.getLength(); j++) {
                // At the edge of the board
                if (i == 0 || i == b.getLength() - 1 || j == 0 || j == b.getLength() - 1) {
                    if (b.getCell(i, j) == currentPlayer) {
                        myScore++;
                    } else {
                        oppoScore++;
                    }
                }
                // At one step inwards
                if (i == 1 || i == b.getLength() - 2 || j == 1 || j == b.getLength() - 2) {
                    if (b.getCell(i, j) == currentPlayer) {
                        myScore += 2;
                    } else {
                        oppoScore += 2;
                    }
                }

                // At middle
                if (i == 2 || i == b.getLength() - 3 || j == 2 || j == b.getLength() - 3) {
                    if (b.getCell(i, j) == currentPlayer) {
                        myScore += 4;
                    } else {
                        oppoScore += 4;
                    }
                }
            }
        }
        int finalScore = ((allMyMoves.size() + myScore) * 100 / 6)
                - ((allOpponentMoves.size() + oppoScore) * 100 / 6)
                + (allMyMoves.size() - allOpponentMoves.size()) * 3;

        return finalScore;
    }*/

    // Getter & Setter

    public void setTreeDepth(int treeDepth) {
        this.treeDepth = treeDepth;
    }

    public int getTreeDepth() {

        return treeDepth;
    }

}

