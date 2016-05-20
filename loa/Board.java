/**
 * Write a description of class Board here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.ArrayList;

public class Board {
    
    private int[][] map;
    private int length; //The length of map
    private Move lastMove = null;
    //private boolean lastMoveCaptured = false; // A flag which last move is captured a opponent checker
    //private int turn; // current turn(who are making decision on this state)
    
    public Board() {
        init(5);
    }

    /**
     * Copy the layout for some certain state of game to a new board
     * @param board
     */
    public Board(Board board) {
        map = new int[board.getLength()][board.getLength()];
        for (int i = 0; i < board.getLength(); i++) {
            for (int j = 0; j < board.getLength(); j++) {
                this.map[i][j] = board.getCell(i, j);
            }
        }
        length = board.getLength();
        //turn = board.getTurn();
    }
    
    /**
     * initial board width
     * @param n how many cells per row(default 5)
     */
    private void init(int n) {
        map = new int[n][n];
        for(int i = 1; i < n - 1; i++) {
            map[0][i] = 1;
            map[n-1][i] = 1;
            map[i][0] = 2;
            map[i][n-1] = 2;
        }
        setLength(n);
    }
    
    public void printMap() {
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map.length; j++) {
                if (map[i][j] == 0) {
                    System.out.print(" |");
                } else {
                    System.out.print(map[i][j]+ "|");
                }

            }
            System.out.println();
        }
    }

    /**
     * Get the value of (x, y) position
     * @param x
     * @param y
     * @return The value in this cell of the board
     */
    public int getCell(int x, int y) {
        return map[x][y];
    }
    
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Select particular position of checker
     * @param p current player
     * @param x 
     * @param y
     * @return Can this player select this checker or not(Is this checker belongs to this player)
     */
    public boolean selectChecker(int p, int x, int y) {
        if (map[x][y] != p) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Move particular checker
     * @param p Current Player
     * @param x Original x position
     * @param y Original y position
     * @param newX Target x position
     * @param newY Target y position
     * @return True if successfully moved selected checker
     */
    public boolean moveChecker(int p, int x, int y, int newX, int newY) {
        if (selectChecker(p, x, y)) {
            map[x][y] = 0;
            map[newX][newY] = p;
            return true;
        } else {
            return false;
        }
    }

    /**
     * return the current board
     * @return current board
     */
    public int[][] currentBoard() {
        return map;
    }

    /**
     * Check after player made a move
     * @param player
     * @return
     */
    public boolean isWin(int player) {
        boolean result = false;
        int[][] temp = new int[map.length][map.length];
        // assign current layout to temp board
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp.length; j++) {
                if (map[i][j] == player) {
                    temp[i][j] = player;
                }
            }
        }
        int count = 0;
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp.length; j++) {
                if (temp[i][j] == player) {
                    mark(temp, i, j, player);
                    count++;
                }
                if (count > 1) {
                    break;
                }
            }

            if (count > 1) {
                break;
            }
        }
        if (count == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get score(utility) of current layout for current player
     * How many part now the board have on current player's side
     *
     * @param player
     * @return
     */
    public int getScore(int player) {
        int score = 0;
        int[][] temp = new int[map.length][map.length];
        // assign current layout to temp board
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp.length; j++) {
                if (map[i][j] == player) {
                    temp[i][j] = player;
                }
            }
        }
        // count how many separate part
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (temp[i][j] == player) {
                    mark(temp, i, j, player);
                    score++;
                }
            }
        }

        return score;
    }

    private void mark(int[][] grid, int i, int j, int player) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid.length || grid[i][j] != player) {
            return;
        }

        grid[i][j] = 0;
        mark(grid, i+1, j, player);
        mark(grid, i-1, j, player);
        mark(grid, i, j+1, player);
        mark(grid, i, j-1, player);
        mark(grid, i - 1, j - 1, player);
        mark(grid, i - 1, j + 1, player);
        mark(grid, i + 1, j - 1, player);
        mark(grid, i + 1, j + 1, player);
        return;
    }

    /**
     * Get all available checkers for currentPlayer
     */
    public ArrayList<Coordinate> getAllAvailableCheckers(int player) {
        ArrayList<Coordinate> allAvailableChecker = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == player) {
                    allAvailableChecker.add(new Coordinate(i, j));
                }
            }
        }
        return allAvailableChecker;
    }    
    
    
    /**
     * In current layout of game, for current player, generate all possible moves for every available checker
     * @param player current player
     * @return
     */
    public ArrayList<Move> getAllPossibleMoves(int player) {
        ArrayList<Move> result = new ArrayList<>();
        ArrayList<Coordinate> allAvailableChecker = new ArrayList<>();
        // get all available checkers for this player
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == player) {
                    allAvailableChecker.add(new Coordinate(i, j));
                }
            }
        }
        // get all possible moves from available checkers
        Step step;
        for (Coordinate oldCoor : allAvailableChecker) {
            step = new Step(player, oldCoor, this);
            ArrayList<Coordinate> currentCheckerHints = step.getHints();
        for (Coordinate hintCoor : currentCheckerHints) {
                result.add(new Move(oldCoor, hintCoor)); //add every hint to possible move list
            }
        }

        return result;
    }

    /**
     * Move checker by provided move
     * @param move
     */
    public void moveChecker(Move move, int player) {
        lastMove = move;
        
        Coordinate oldPos = move.getOriginalPos();
        Coordinate newPos = move.getTargetPos();

        // set old position to empty
        this.map[oldPos.getX()][oldPos.getY()] = 0;
        // set target position to current player's checker
        this.map[newPos.getX()][newPos.getY()] = player;
    }
    
    
    /**
     * Undo the last move made by a particular player
     * If last move made capture, restore the original checker
     * @param player
     */
    public void undoLastMove(int player) {
        if(lastMove == null) {
            return;
        }
        int opponentChecker = 0;
        if (player == 1) {
            opponentChecker = 2;
        } else {
            opponentChecker = 1;
        }
        // take care of captured situation

        // return to last position
        map[lastMove.getOriginalPos().getX()][lastMove.getOriginalPos().getY()] = player;
    }

    public boolean hasWinner() {
        if (isWin(1) && !isWin(2)) {
            return true;
        } else if (!isWin(1) && isWin(2)) {
            return true;
        } else {
            return false;
        }
    }

    public int CountCheckers(int player) {
        int count = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (map[i][j] == player) {
                    count++;
                }
            }
        }
        return count;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public int[][] getMap() {
        return map;
    }

/*    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }*/
}
