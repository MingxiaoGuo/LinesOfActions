package loa;

import static loa.Move.*;
import static loa.Board.*;

/** A Player that prompts for moves and reads them from its Game.
 *  @author Weier Wan */
class HumanPlayer extends Player {

    /** A HumanPlayer that plays the SIDE pieces in GAME.  It uses
     *  GAME.getMove() as a source of moves.  */
    HumanPlayer(Side side, Game game) {
        super(side, game);
    }

    @Override
    Move makeMove() {
        while (true) {
            String moveStr = getGame().getMove();
            if (moveStr.equals("p")) {
                return null;
            } else {
                String pos0 = moveStr.substring(0, 2);
                String pos1 = moveStr.substring(3);
                Move newMove = create(col(pos0), row(pos0),
                        col(pos1), row(pos1));
                if (getGame().getBoard().isLegal(newMove)) {
                    return newMove;
                } else {
                    System.out.println("The move you enter is"
                            + " not allowed, please enter a new move.");
                }
            }
        }
    }

}
