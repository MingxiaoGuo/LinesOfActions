/**
 * Write a description of class Move here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Move {
    private Coordinate originalPos;
    private Coordinate targetPos;
    //private int player = 0;  // still don't know if this is needed, just in case
    private int utility; // after take this move, the utility player will get
    /**
     * Move contains three parameters, old position, new position and current player making the move
     */
    public Move() {}

    /**
     * From old position to new position
     * @param oldPos
     * @param newPos
     */
    public Move(Coordinate oldPos, Coordinate newPos) {
        this.originalPos = new Coordinate(oldPos.getX(), oldPos.getY());
        this.targetPos = new Coordinate(newPos.getX(), newPos.getY());
    }


    public Move(Coordinate oldPos,  Coordinate newPos, int utility) {
        this.originalPos = new Coordinate(oldPos.getX(), oldPos.getY());
        this.targetPos = new Coordinate(newPos.getX(), newPos.getY());
        this.setUtility(utility);
    }

    public Coordinate getTargetPos() {
        return targetPos;
    }

    public Coordinate getOriginalPos() {
        return originalPos;
    }

    public void setTargetPos(Coordinate targetPos) {
        this.targetPos = targetPos;
    }

    public void setOriginalPos(Coordinate originalPos) {
        this.originalPos = originalPos;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    public int getUtility() {
        return utility;
    }
}
