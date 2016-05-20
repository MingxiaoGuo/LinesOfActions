import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Point;
/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player extends Actor
{
    protected int x = 0;
    protected int y = 0;
    protected int gridX = 0;
    protected int gridY = 0;
    protected boolean isDragging =  false;
    private int fromX = -1;
    private int fromY = -1;
    private Coordinate coordinate;
    
    public Player() {}
    
    public Player(int x, int y, int gX, int gY) {
        this.x = x;
        this.y = y;
        this.gridX = gX;
        this.gridY = gY;
        this.coordinate = new Coordinate(gX, gY);
    }
    
    
    /**
     * Act - do whatever the Player wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(Greenfoot.mouseClicked(this)) {
            ((Background)getWorld()).curSelected.setX(gridX);
            ((Background)getWorld()).curSelected.setY(gridY);
            ((Background)getWorld()).someoneGetSelected = true;
            System.out.println(gridX + "/" + gridY);
        }
    }    
    

    
    
}
