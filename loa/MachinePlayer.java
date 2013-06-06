package loa;

import java.util.Iterator;
import java.util.List;

/** Represents a machine player.  Extensions of this class do the actual playing.
 *  @author Weier Wan
 */
public abstract class MachinePlayer extends Player {

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Side side, Game game) {
        super(side, game);
    }

    @Override
    abstract Move makeMove();


    /** Return a random MOVE from the current stage. */
    Move randomMove() {
        List<Move> l = copy.legalMoves();
        int index = Math.abs(getGame().getRandomSource().nextInt()) % l.size();
        return l.get(index);
    }

    /** Return the move with the highest score from all legal moves from the
     * current stage. This method use ALPHA-BETA pruning with DEPTH given
     * by the parameter.
     */
    Move max(Move alpha, Move beta, int depth) {
        if (copy.piecesContiguous(getGame().getBoard().turn().opponent())) {
            return MIN_MOVE;
        }
        if (copy.piecesContiguous(getGame().getBoard().turn()))  {
            return MAX_MOVE;
        }
        if (depth == 0) {
            return findBestMove();
        }
        for (Iterator<Move> itr
                = copy.legalMoves().iterator(); itr.hasNext();) {
            Move thisMove = itr.next();
            copy.makeMove(thisMove);
            Move scoredMove = min(alpha, beta, depth - 1);
            copy.retract();
            if (scoredMove.getScore() >= beta.getScore()) {
                thisMove.setScore(scoredMove.getScore());
                return thisMove;
            }
            if (scoredMove.getScore() >= alpha.getScore()) {
                thisMove.setScore(scoredMove.getScore());
                alpha = thisMove;
            }
        }
        return alpha;
    }

    /** Return the move with the lowest score from all legal moves from the
     * current stage. This method use ALPHA-BETA pruning with DEPTH given
     * by the parameter.
     */
    Move min(Move alpha, Move beta, int depth) {
        if (copy.piecesContiguous(getGame().getBoard().turn())) {
            return MAX_MOVE;
        }
        if (copy.piecesContiguous(getGame().getBoard().turn().opponent())) {
            return MIN_MOVE;
        }
        if (depth == 0) {
            return findBestMove();
        }

        for (Iterator<Move> itr
                = copy.legalMoves().iterator(); itr.hasNext();) {
            Move thisMove = itr.next();
            copy.makeMove(thisMove);
            Move scoredMove = max(alpha, beta, depth - 1);
            copy.retract();
            if (scoredMove.getScore() <= alpha.getScore()) {
                thisMove.setScore(scoredMove.getScore());
                return thisMove;
            }
            if (scoredMove.getScore() <= beta.getScore()) {
                thisMove.setScore(scoredMove.getScore());
                beta = thisMove;
            }
        }
        return beta;
    }

    /** Return the best move from current state based on evaluation on
     *  all possible legal moves.
     */
    Move findBestMove() {
        Move bestSoFar = MIN_MOVE;
        for (Iterator<Move> itr
                = copy.legalMoves().iterator(); itr.hasNext();) {
            Move pMove = itr.next();
            copy.makeMove(pMove);
            int score = evaluate();
            pMove.setScore(score);
            int temp = copy.turn()
                    == getGame().getBoard().turn() ? -score : score;
            if (temp > bestSoFar.getScore()) {
                bestSoFar = pMove;
            }
            copy.retract();
        }
        return bestSoFar;
    }

    /** Return the score of the current state of the board COPY based on
     *  the static evaluation function. It returns Integer.MAX_VALUE if
     *  the board is at a winning position, and returns MIN_VALUE if the
     *  board is at a losing position.
     */
    int evaluate() {
        if (copy.piecesContiguous(getGame().getBoard().turn())) {
            return Integer.MAX_VALUE;
        } else if (copy.piecesContiguous(
                getGame().getBoard().turn().opponent())) {
            return Integer.MIN_VALUE;
        }
        return evaluateFunction();
    }

    /** The static evaluation function. */
    abstract int evaluateFunction();


    /** The board represents the current stage of this machine player. */
    protected MutableBoard copy;

    /** A fake move with the highest possible score. */
    static final Move MAX_MOVE = Move.create(0, 0, 0, 0, Integer.MAX_VALUE);

    /** A fake move with the lowest possible score. */
    static final Move MIN_MOVE = Move.create(9, 9, 9, 9, Integer.MIN_VALUE);


}

