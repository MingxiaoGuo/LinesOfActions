import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class Player2 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player2 extends Player
{
    protected boolean isUser = false;
    private boolean isMyTurn = false;
    private boolean isDragging = false;
    private Background bg;
    private int movingState;
    
    public Player2(int x, int y, int gX, int gY, boolean isUser) {
        super(x, y, gX, gY);
        this.isUser = isUser;
    }
    
    /**
     * Act - do whatever the Player1 wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        bg = (Background)getWorld();
        
        if(bg.currentTurn == 2) {
             //System.out.println(isUser);
            if(isUser) {
               
                if(!isDragging && Greenfoot.mouseDragged(this)) {
                    isDragging = true;
                    Step newStep = new Step(2, new Coordinate(gridX, gridY), bg.board);
                    for(Coordinate co : newStep.getHints()) {
                        co.printCor();
                    }
                    showHints(newStep);
                }
                if (Greenfoot.mouseDragged(this)) {
                             
                    MouseInfo mouse = Greenfoot.getMouseInfo();
                    List<Hint> hints = bg.getObjectsAt(mouse.getX(), mouse.getY(), Hint.class);
                    if (hints.size() > 0) {
                        setLocation(mouse.getX(), mouse.getY());
                    }
                    
                    
                    //this.setLocation( Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
                }
                if(Greenfoot.mouseDragEnded(this)) {
                    int oldGX = gridX, oldGY = gridY;
                    isDragging = false;
                    int[] a = getCenter( Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
                    if(a[0] == x && a[1] == y) { // move to the same point, doesn't count
                        this.setLocation( x, y);
                        List<Hint> hints = bg.getObjects(Hint.class);
                        bg.removeObjects(hints);
                        return;
                    } else {
                        boolean moveIsValid = true;
                        List<Hint> hints = bg.getObjects(Hint.class);
                        int validCount = 0;
                        for(Hint hint : hints) {
                            if (a[0] == hint.getX() && a[1] == hint.getY()) {
                                validCount++;
                            }
                        }
                        if(validCount != 0) {
                            moveIsValid = true;
                        } else {
                            moveIsValid = false;
                        }
                        if (!moveIsValid) { // if the move is invalid, cancel that move
                            this.setLocation( x, y);
                            bg.removeObjects(hints);
                            return;
                        }
                        
                        
                        List<Player1> list2 = bg.getObjectsAt(a[0], a[1], Player1.class);
                        if(list2 != null && list2.size() != 0) {
                            bg.removeObject(list2.get(0));
                        }
                        
                        this.setLocation( a[0], a[1]);
                        x = a[0];
                        y = a[1];
                        // change board
                        int[] b = matchGrid(a[0], a[1]);
                        bg.board.moveChecker(2, oldGX, oldGY, gridX, gridY);
                        System.out.println(oldGX + "+" + oldGY);
                        bg.board.printMap();
                        System.out.println(gridX + "-" + gridY);
                        hints = bg.getObjects(Hint.class);
                        bg.removeObjects(hints);
                        System.out.println("switch side");
                        bg.currentTurn = 1; 
                    }
                }
            }
        }

    }  
    
    public void showHints(Step newStep) {
        bg = (Background)getWorld();
        int x = getX(), y = getY();
        bg.addObject(new Hint(), x, y);
        for(Coordinate coor : newStep.getHints()) {
            int[] bCoor = getMatchBoradCoor(coor.getX(), coor.getY());
            bg.addObject(new Hint(), bCoor[0], bCoor[1]);
        }
        // add itself on the hints
        bg.removeObject(this);
        bg.addObject(this, x, y);
    }
    
    
    public int[] getCenter(int x, int y) {
        int[] result = new int[2];
        if(x >= 50 && x < 150) {
            result[0] = 100;
            gridY = 0;
        } else if (x >= 150 && x < 250) {
            result[0] = 200;
            gridY = 1;
        } else if (x >= 250 && x < 350) {
            result[0] = 300;
            gridY = 2;
        } else if (x >= 350 && x < 450) {
            result[0] = 400;
            gridY = 3;
        } else {
            result[0] = 500;
            gridY = 4;
        }

        if(y >= 70 && y < 170) {
            result[1] = 120;
            gridX = 0;
        } else if (y >= 170 && y < 270) {
            result[1] = 220;
            gridX = 1;
        } else if (y >= 270 && y < 370) {
            result[1] = 320;
            gridX = 2;
        } else if (y >= 370 && y < 470) {
            result[1] = 420;
            gridX = 3;
        } else {
            result[1] = 520;
            gridX = 4;
        }
        System.out.println(result[0] + ", " + result[1]);
        return result;
    }
    
    
    public int[] getMatchBoradCoor(int gridX, int gridY) {
        int boardX = 0, boardY = 0;
        if(gridX == 0) {
            boardY = 120;
        } else if(gridX == 1) {
            boardY = 220;
        } else if(gridX == 2) {
            boardY = 320;
        } else if(gridX == 3) {
            boardY = 420;
        } else {
            boardY = 520;
        }
        
        if(gridY == 0) {
            boardX = 100;
        } else if(gridY == 1) {
            boardX = 200;
        } else if(gridY == 2) {
            boardX = 300;
        } else if(gridY == 3) {
            boardX = 400;
        } else {
            boardX = 500;
        }
        System.out.println(boardX + "," + boardY);
        return new int[]{boardX, boardY};
    }
    
    public int[] matchGrid(int boardX, int boardY) {
        if(boardY == 120) {
            gridX = 0;
        } else if(boardY == 220) {
            gridX = 1;
        } else if(boardY == 320) {
            gridX = 2;
        } else if(boardY == 420) {
            gridX = 3;
        } else {
            gridX = 4;
        }
        
        if(boardX == 100) {
            gridY = 0;
        } else if(boardX == 200) {
            gridY = 1;
        } else if(boardX == 300) {
            gridY = 2;
        } else if(boardX == 400) {
            gridY = 3;
        } else {
            gridY = 4;
        }
        return new int[]{gridX, gridY};
    }
    

}
