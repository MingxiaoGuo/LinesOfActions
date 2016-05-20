import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Welcome here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Welcome extends World
{

    /**
     * Constructor for objects of class Welcome.
     * 
     */
    public Welcome()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1); 
        btnP1 p1 = new btnP1();
        btnP2 p2 = new btnP2();
        addObject(p1, 200, 220);
        addObject(p2, 400, 220);
    }
}
