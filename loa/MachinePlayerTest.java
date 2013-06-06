package loa;

import static org.junit.Assert.*;
import static loa.Side.*;

import org.junit.Test;

public class MachinePlayerTest {
    
    Game game = new Game(1, BLACK, 42, 60000);
    MachinePlayer1 p = new MachinePlayer1(BLACK, game);

    @Test
    public void testEvaluation() {
        System.out.println(p.evaluate());
    }

}
