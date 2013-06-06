package loa;

import java.io.IOException;

import ucb.util.CommandArgs;
import java.util.*;
import static loa.Board.*;
import static loa.Piece.BP;
import static loa.Piece.EMP;
import static loa.Piece.WP;
import static loa.Side.BLACK;
import static loa.Side.WHITE;
import java.util.HashSet;
import java.util.Set;

public class SomeTests {

    public static void main(String...args) {
        /**try {
            CommandArgs options =
                    new CommandArgs("--white --ai=([0|1|2]) --seed=(\\d+) --time=(\\d+(?:.\\d)*) --debug=(\\d+) --display", args);
            if(!options.ok()) {
                System.out.println("not ok.");
            }

                int numHuman = options.containsKey("--ai")? 2 - options.getInt("--ai") : 1;
                System.out.println("here.");
                Side side0 = options.containsKey("--white")? Side.WHITE : Side.BLACK;
                long seed = options.containsKey("--seed")? options.getLong("--seed") : -1;
                double timeInM = options.containsKey("--time")? options.getDouble("--time") : Double.POSITIVE_INFINITY;
                if (timeInM < 0) {
                    throw new IOException("Error: time parameter cannot be negative.");
                }
                long timeInMillis = (long) timeInM * 60000;
                
                System.out.printf("%d %s %d %d", numHuman, side0, seed, timeInMillis);

        } catch(IOException ex) {
            System.err.println(ex);
            System.exit(1);
        } catch(IllegalArgumentException ex) {
            System.err.println("Error: the command time is errorneous.");
        }
        
        MutableBoard b = new MutableBoard();
        Move m = Move.create(1, 2, 2, 3);
        Move m2 = Move.create(3, 1, 1, 1);
        Move m3 = Move.create(1, 6, 1, 1);
        b.makeMove(m);
        b.makeMove(m2);
        b.makeMove(m3);

        System.out.print(b);*/
        
        /**Piece[][] pieces = {
                { EMP,  BP, EMP,  BP,  BP, EMP, EMP, EMP },
                { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
                { WP,  EMP, EMP, EMP,  BP,  BP, EMP, WP  },
                { WP,  EMP,  BP, EMP, EMP,  WP, EMP, EMP },
                { WP,  EMP,  WP,  WP, EMP,  WP, EMP, EMP },
                { WP,  EMP, EMP, EMP,  BP, EMP, EMP, WP  },
                { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
                { EMP, BP,  BP,  BP,  EMP,  BP,  BP, EMP }
        };
        Piece[][] pieces = {
                { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
                { EMP, EMP, EMP,  WP, EMP, EMP, EMP, EMP },
                { EMP, EMP,  BP,  WP, EMP,  WP, EMP, EMP },
                { EMP, EMP, EMP, EMP,  BP, EMP,  BP, EMP },
                { EMP, EMP,  WP, EMP, EMP,  BP, EMP, EMP },
                { EMP, EMP, EMP,  BP,  BP,  BP, EMP, EMP },
                { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
                { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP }
        };
        MutableBoard board = new MutableBoard(pieces, WHITE);
        Game game = new Game(2,BLACK, -1, 10000);
        game._board = board;
        MachinePlayer p = new MachinePlayer(WHITE, game);
        System.out.println(p.makeMove());
        
        
        
        //System.out.println(board.getBoard()[1][0].side() == WHITE);
        
        /**Game game = new Game(1, BLACK, 42, 60000);
        Piece[][] pieces = {
                { EMP, BP, EMP,  BP,  BP, EMP, EMP, EMP },
                { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
                { WP,  EMP, EMP, EMP,  WP,  EMP, EMP, WP  },
                { WP,  EMP,  BP, WP,   BP,  WP, EMP, EMP },
                { BP,  EMP,  BP,  BP, EMP,  WP, EMP, EMP },
                { WP,  EMP, EMP, EMP,  WP, EMP, EMP, WP  },
                { EMP, WP,  EMP, EMP,  WP, EMP, WP,  EMP },
                { EMP, BP,  BP,  BP,  EMP,  BP, EMP, EMP }
        };
        MutableBoard board = new MutableBoard(pieces, BLACK);
        game._board = board;
        MachinePlayer p = new MachinePlayer(BLACK, game);
        p.copy = new MutableBoard(game.getBoard());
        System.out.println(p.evaluate());
        
        System.out.println(MachinePlayer.MAX_MOVE.getScore());*/
        
        Gui frame = new Gui();
        //frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Move move = Move.create(2,1,2,2 );
        frame.makeMove(move);
        
    }
}
