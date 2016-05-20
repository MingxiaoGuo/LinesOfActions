/**
 * Write a description of class Step here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;

public class Step {
    private int whichPlayer;
    private Coordinate originalPos;
    private ArrayList<Coordinate> hints;
    private Board board;
    
    /**
     * Constructor
     * @param p current player(1 or 2)
     * @param pos current position of checker
     * @param b current board
     */
    public Step(int p, Coordinate pos, Board b) {
        this.setWhichPlayer(p);
        this.setOriginalPos(pos);
        setHints(new ArrayList<>());
        this.setBoard(b);
        generateHints();
    }

    public int getWhichPlayer() {
        return whichPlayer;
    }

    public void setWhichPlayer(int whichPlayer) {
        this.whichPlayer = whichPlayer;
    }

    public Coordinate getOriginalPos() {
        return originalPos;
    }

    public void setOriginalPos(Coordinate originalPos) {
        this.originalPos = originalPos;
    }

    public ArrayList<Coordinate> getHints() {
        return hints;
    }

    public void setHints(ArrayList<Coordinate> hints) {
        this.hints = hints;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
    
    private void generateHints() {
        int upCount = 0, downCount = 0;
        int leftCount = 0, rightCount = 0;
        int dLeftUpCount = 0, dRightUpCount = 0, dLeftDownCount = 0, dRightDownCount = 0;
        //on which direction has opponent
        HashMap<String, ArrayList<Coordinate>> map = new HashMap<>();
        
        int length = board.getLength();
        ArrayList<Coordinate> list;

        //Go up
        list = new ArrayList<>();
        for(int i = originalPos.getX() - 1; i >= 0; i--) {
            int currentCell = board.getCell(i, originalPos.getY());
            if(currentCell != 0) {
                if(currentCell != whichPlayer) {
                    list.add(new Coordinate(i, originalPos.getY()));
                }
                upCount++;
            }
        }
        map.put("Up", list);
        list = new ArrayList<>();
        //Go down
        for(int i = originalPos.getX() + 1; i < length; i++) {
            int currentCell = board.getCell(i, originalPos.getY());
            if(currentCell != 0) {
                if(currentCell != whichPlayer) {
                    list.add(new Coordinate(i, originalPos.getY()));
                }
                downCount++;
            }
        }
        map.put("Down", list);
        list = new ArrayList<>();
        //Go left
        for(int i = originalPos.getY() - 1; i >= 0; i--) {
            int currentCell = board.getCell(originalPos.getX(), i);
            if(currentCell != 0) {
                if(currentCell != whichPlayer) {
                    list.add(new Coordinate(originalPos.getX(), i));
                }
                leftCount++;
            }
        }
        map.put("Left", list);
        list = new ArrayList<>();
        
        //Go right
        for(int i = originalPos.getY() + 1; i < length; i++) {
            int currentCell = board.getCell(originalPos.getX(), i);
            if(currentCell != 0) {
                if(currentCell != whichPlayer) {
                    list.add(new Coordinate(originalPos.getX(), i));
                }
                rightCount++;
            }
        }
        map.put("Right", list);
        list = new ArrayList<>();
        
        //Go diagonal up left
        int upLeftDis = originalPos.getX() > originalPos.getY() ? originalPos.getY() : originalPos.getX(); // distance to the wall
        for(int i = 1; i <= upLeftDis; i++) {
            int currentCell = board.getCell(originalPos.getX() - i, originalPos.getY() - i);
            if(currentCell != 0) {
                if(currentCell != whichPlayer) {
                    list.add(new Coordinate(originalPos.getX() - i, originalPos.getY() - i));
                }
                dLeftUpCount++;
            }
        }
        map.put("dLeftUp", list);
        list = new ArrayList<>();

        //Go diagonal up right
        int upRightDis = originalPos.getX() > (length - 1 - originalPos.getY()) ? (length - 1 - originalPos.getY()) : originalPos.getX(); // distance to the wall
        for(int i = 1; i <= upRightDis; i++) {
            int currentCell = board.getCell(originalPos.getX() - i, originalPos.getY() + i);
            if(currentCell != 0){
                if(currentCell != whichPlayer) {
                    list.add(new Coordinate(originalPos.getX() - i, originalPos.getY() + i));
                }
                dRightUpCount++;
            }
        }
        map.put("dRightUp", list);
        list = new ArrayList<>();
        
        //Go diagonal down left
        int leftDownDis = (length - 1 - originalPos.getX()) > originalPos.getY() ? originalPos.getY() : (length - 1 - originalPos.getX());
        for(int i = 1; i <= leftDownDis; i++) {
            int currentCell = board.getCell(originalPos.getX() + i, originalPos.getY() - i);
            if(currentCell != 0) {
                if(currentCell != whichPlayer) {
                    list.add(new Coordinate(originalPos.getX() + i, originalPos.getY() - i));
                }
                dLeftDownCount++;
            }
        }
        map.put("dLeftDown", list);
        list = new ArrayList<>();
        
        //Go diagonal down right
        int rightDownDis = (length - 1 - originalPos.getX()) > (length - 1 - originalPos.getY()) ? (length - 1 - originalPos.getY()) : (length - 1 - originalPos.getX());
        for(int i = 1; i <= rightDownDis; i++) {
            int currentCell = board.getCell(originalPos.getX() + i, originalPos.getY() + i);
            if(currentCell != 0) {
                if(currentCell != whichPlayer) {
                    list.add(new Coordinate(originalPos.getX() + i, originalPos.getY() + i));
                }
                dRightDownCount++;
            }
        }
        map.put("dRightDown", list);
        
        int verticalTotal = upCount + downCount + 1;
/*        if (whichPlayer == 1) {
            System.out.println("v: " + verticalTotal);
        }*/

        int horizontalTotal = leftCount + rightCount + 1;
        
        int dLeftToRightTotal = dLeftUpCount + dRightDownCount + 1;
        int dRightToLeftTotal = dRightUpCount + dLeftDownCount + 1;
        
        // check along the path, there is opponent
        // check vertical
        int upBoundary = originalPos.getX() - verticalTotal;
        if (upBoundary >= 0 && upBoundary < length - 1) { // didn't go over the boundary
            for(Coordinate coordinate : map.get("Up")) {
                if (coordinate.getX() > upBoundary) {
                    upCount = 0;
                    break;
                }
            }
        } else { // over the upper wall, make upCount 0
            upCount = 0;
        }

        int downBoundary = originalPos.getX() + verticalTotal;
        if (downBoundary < length) {
            for(Coordinate coordinate : map.get("Down")) {
                if (coordinate.getX() < downBoundary) {
                    downCount = 0;
                    break;
                }
            }
        }  else { // over the bottom wall, make downCount 0
            downCount = 0;
        }
        // add hint point into list
        if (upCount > 0) {
            hints.add(new Coordinate(originalPos.getX() - verticalTotal, originalPos.getY()));
        }
        if (downCount > 0) {
            hints.add(new Coordinate(originalPos.getX() + verticalTotal, originalPos.getY()));
        }
        if (verticalTotal == 1) {
            if (originalPos.getX() - verticalTotal >= 0) {
                hints.add(new Coordinate(originalPos.getX() - verticalTotal, originalPos.getY()));
            }
            if (originalPos.getX() + verticalTotal < length) {
                hints.add(new Coordinate(originalPos.getX() + verticalTotal, originalPos.getY()));
            }
        }
        
        
        //check horizontal
        int leftBoundary = originalPos.getY() - horizontalTotal;
        if (leftBoundary >= 0) {
            for(Coordinate coordinate : map.get("Left")) {
                if (coordinate.getY() > leftBoundary) { // opponent is between now and future move
                    leftCount = 0;
                    break;
                }
            }
        } else { // over the left wall
            leftCount = 0;
        }
        
        int rightBoundary = originalPos.getY() + horizontalTotal;
        if (rightBoundary < length) {
            for(Coordinate coordinate : map.get("Right")) {
                if (coordinate.getY() < rightBoundary) {
                    rightCount = 0;
                    break;
                }
            }
        } else {
            rightCount = 0;
        }
        // add horizontal hint points into hints list
        if (leftCount > 0) {
            hints.add(new Coordinate(originalPos.getX(), originalPos.getY() - horizontalTotal));
        }
        if (rightCount > 0) {
            hints.add(new Coordinate(originalPos.getX(), originalPos.getY() + horizontalTotal));
        }
        if (horizontalTotal == 1) {
            if (originalPos.getY() - horizontalTotal >= 0) {
                hints.add(new Coordinate(originalPos.getX(), originalPos.getY() - horizontalTotal));
            }
            if (originalPos.getY() + horizontalTotal < length) {
                hints.add(new Coordinate(originalPos.getX(), originalPos.getY() + horizontalTotal));
            }
        }
        
        
        //check diagonal
        //left up
        Coordinate possiblePoint = new Coordinate(originalPos.getX() - dLeftToRightTotal, originalPos.getY() - dLeftToRightTotal);
        
        if (possiblePoint.getX() >= 0 && possiblePoint.getY() >= 0) {   //check if is out of wall
            for(Coordinate coordinate : map.get("dLeftUp")) {
                if (coordinate.getX() > possiblePoint.getX() && coordinate.getY() > possiblePoint.getY()) {
                    dLeftUpCount = 0;
                    break;
                }
            }   
        } else {
            dLeftUpCount = 0;
        }
        
        //right down
        possiblePoint = new Coordinate(originalPos.getX() + dLeftToRightTotal, originalPos.getY() + dLeftToRightTotal);
        
        if (possiblePoint.getX() < length && possiblePoint.getY() < length) {
            for(Coordinate coordinate : map.get("dRightDown")) {
                if (coordinate.getX() < possiblePoint.getX() && coordinate.getY() < possiblePoint.getY()) {
                    dRightDownCount = 0;
                    break;
                }
            }
        } else {
            dRightDownCount = 0;
        }
        // add left to right diagonal direction to hints
        if (dLeftUpCount > 0) {
            hints.add(new Coordinate(originalPos.getX() - dLeftToRightTotal, originalPos.getY() - dLeftToRightTotal));
        }
        if (dRightDownCount > 0) {
            hints.add(new Coordinate(originalPos.getX() + dLeftToRightTotal, originalPos.getY() + dLeftToRightTotal));
        }
        // when diagonal has only itself
		if (dLeftToRightTotal == 1) {
            int x = originalPos.getX();
            int y = originalPos.getY();
            if (x - dLeftToRightTotal >= 0 && y - dLeftToRightTotal >= 0) {
                hints.add(new Coordinate(x - dLeftToRightTotal, y - dLeftToRightTotal));
            }
            // check whether it's out of wall
            if (x + dLeftToRightTotal < length && y + dLeftToRightTotal < length) {
                hints.add(new Coordinate(x + dLeftToRightTotal, y + dLeftToRightTotal));
            }
        }

        //right up
        possiblePoint = new Coordinate(originalPos.getX() - dRightToLeftTotal, originalPos.getY() + dRightToLeftTotal);
        
        if (possiblePoint.getX() >= 0  && possiblePoint.getY() < length ) { // check if it's in the wall
            for(Coordinate coordinate : map.get("dRightUp")) {
                if (coordinate.getX() > possiblePoint.getX() && coordinate.getY() < possiblePoint.getY()) { // it's between current point and future
                    dRightUpCount = 0;
                    break;
                }
            }
        } else {
            dRightUpCount = 0;
        }
        //left down
        possiblePoint = new Coordinate(originalPos.getX() + dRightToLeftTotal, originalPos.getY() - dRightToLeftTotal);
        
        if (possiblePoint.getX() < length && possiblePoint.getY() >= 0) {
            for(Coordinate coordinate : map.get("dLeftDown")) {
                if (coordinate.getX() < possiblePoint.getX() && coordinate.getY() > possiblePoint.getY()) {
                    dLeftDownCount = 0;
                    break;
                }
            }
        } else {
            dLeftDownCount = 0;
        }
        
        //add hints
        if (dRightUpCount > 0) {
            hints.add(new Coordinate(originalPos.getX() - dRightToLeftTotal, originalPos.getY() + dRightToLeftTotal));
        }
        if (dLeftDownCount > 0) {
            hints.add(new Coordinate(originalPos.getX() + dRightToLeftTotal, originalPos.getY() - dRightToLeftTotal));
        }
        // when diagonal only has itself
        if (dRightToLeftTotal == 1) {
            int x = originalPos.getX();
            int y = originalPos.getY();
            if (x - dRightToLeftTotal >= 0 && y + dRightToLeftTotal < length) {
                hints.add(new Coordinate(x - dRightToLeftTotal, y + dRightToLeftTotal));
            }
            if (x + dRightToLeftTotal < length && y - dRightToLeftTotal >= 0) {
                hints.add(new Coordinate(x + dRightToLeftTotal, y - dRightToLeftTotal));
            }
        }

        //check go over self side
        ArrayList<Coordinate> needToDelete = new ArrayList<>();
        for (Coordinate coordinate : hints) {
            if (board.getCell(coordinate.getX(), coordinate.getY()) == whichPlayer) {
                needToDelete.add(coordinate);
                //hints.remove(coordinate);
            }
        }
        hints.removeAll(needToDelete);
    }
}

