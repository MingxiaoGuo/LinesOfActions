package loa;

public class MachinePlayerRandom extends MachinePlayer {
    
    MachinePlayerRandom(Side side, Game game) {
        super(side, game);
    }

    @Override
    Move makeMove() {
        return randomMove();
    }

    @Override
    int evaluateFunction() {
        return 0;
    }

}
