package loa;

import ucb.util.CommandArgs;
import java.io.IOException;

/** Main class of the Lines of Action program.
 * @author Weier Wan
 */
public class Main {

    /** The main Lines of Action.  ARGS are as described in the
     *  project 3 handout:
     *      [ --white ] [ --ai=N ] [ --seed=S ] [ --time=LIM ] \
     *      [ --debug=D ] [ --display ]
     */
    public static void main(String... args) {
        try {
            CommandArgs options =
                    new CommandArgs("--white --ai=([0|1|2])"
                            + " --seed=(\\d+) --time=(\\d+(\\.\\d+)?)"
                            + " --debug=(\\d+) --display", args);

            if (!options.ok()) {
                usage();
            }

            int numHuman = options.containsKey("--ai")
                    ? 2 - options.getInt("--ai") : 1;
            Side side0 = options.containsKey("--white")
                    ? Side.WHITE : Side.BLACK;
            long seed = options.containsKey("--seed")
                    ? options.getLong("--seed") : -1;
            double timeInM = options.containsKey("--time")
                    ? options.getDouble("--time") : 0;
            if (timeInM < 0) {
                throw new IOException("Error:"
                        + " time parameter cannot be negative.");
            }
            long timeInMillis = (long) (timeInM * MIN_SEC * SEC_MILLI);
            boolean display = options.containsKey("--display");

            Game game;
            if (display) {
                game = new GameGUI(numHuman, side0, seed, timeInMillis);
            } else {
                game = new Game(numHuman, side0, seed, timeInMillis);
            }

            game.play();

        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        } catch (IllegalArgumentException ex) {
            usage();
        }
    }

    /** Print brief description of the command-line format. */
    static void usage() {
        System.out.println("The command line is erroneous."
                + " The correct format is:");
        System.out.println("[ --white ] [ --ai=N ]"
                + " [ --seed=S ] [ --time=LIM ]");
        System.out.println("[ --debug=D ] [ --display ]");
        System.exit(1);
    }

    /** Unit conversion from minute to second. */
    static final int MIN_SEC = 60;

    /** Unit conversion from second to millisecond. */
    static final int SEC_MILLI = 1000;

}
