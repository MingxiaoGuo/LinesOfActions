package loa;

import static loa.Side.BLACK;

import java.util.List;

/** An automated Player.
 *  @author Weier Wan */
class MachinePlayer1 extends MachinePlayer {

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer1(Side side, Game game) {
        super(side, game);
    }

    @Override
    Move makeMove() {
        copy = new MutableBoard(getGame().getBoard());
        Move m;
        Side mySide = getGame().getBoard().turn();
        if (!getGame().hasTimeLimit()
                || getGame().timeRemaining(mySide) > 120000) {
            m = max(MIN_MOVE, MAX_MOVE, 3);
        } else if (getGame().timeRemaining(mySide) > 10000) {
            m = max(MIN_MOVE, MAX_MOVE, 2);
        } else if (getGame().timeRemaining(mySide) > 3000) {
            m = max(MIN_MOVE, MAX_MOVE, 1);
        } else if (getGame().timeRemaining(mySide) > 1000) {
            m = max(MIN_MOVE, MAX_MOVE, 0);
        } else {
            m = randomMove();
        }
        System.out.printf("%s::%s\n", mySide == BLACK ? "B" : "W", m);
        return m;
    }

    /** An evaluation function based on the position of the pieces and
     *  the mobility of the other side.
     */
    int evaluateFunction() {
        List<Move> allMyMoves, allOppoMoves;
        if (copy.turn() == getGame().getBoard().turn()) {
            allMyMoves = copy.legalMoves();
            copy.reverseTurn();
            allOppoMoves = copy.legalMoves();
            copy.reverseTurn();
        } else {
            allOppoMoves = copy.legalMoves();
            copy.reverseTurn();
            allMyMoves = copy.legalMoves();
            copy.reverseTurn();
        }
        int myPieceNum = copy.countPieces(getGame().getBoard().turn());
        int oppoPieceNum = copy.countPieces(
                getGame().getBoard().turn().opponent());
        int myPosScore = 0;
        int oppoPosScore = 0;
        for (int r = 0; r < Board.LENGTH; r++) {
            for (int c = 0; c < Board.LENGTH; c++) {
                if (r == 1 || r == 6 || c == 1 || c == 6) {
                    if (copy.getBoard()[r][c].side()
                            == getGame().getBoard().turn()) {
                        myPosScore += 1;
                    } else if (copy.getBoard()[r][c].side()
                            == getGame().getBoard().turn().opponent()) {
                        oppoPosScore += 1;
                    }
                }
                if (r == 2 || r == 5 || c == 2 || c == 5) {
                    if (copy.getBoard()[r][c].side()
                            == getGame().getBoard().turn()) {
                        myPosScore += 3;
                    } else if (copy.getBoard()[r][c].side()
                            == getGame().getBoard().turn().opponent()) {
                        oppoPosScore += 3;
                    }
                }
                if (r == 3 || r == 4 || c == 3 || c == 4) {
                    if (copy.getBoard()[r][c].side()
                            == getGame().getBoard().turn()) {
                        myPosScore += 6;
                    } else if (copy.getBoard()[r][c].side()
                            == getGame().getBoard().turn().opponent()) {
                        oppoPosScore += 6;
                    }
                }
            }
        }
        int score = ((allMyMoves.size() + myPosScore) * 100 / myPieceNum)
                - ((allOppoMoves.size() + oppoPosScore) * 100 / oppoPieceNum)
                + (allMyMoves.size() - allOppoMoves.size()) * 3;
        return score;
    }


}
