import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class btnP2 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class btnP2 extends Button
{
    /**
     * Act - do whatever the btnP2 wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(Greenfoot.mouseClicked(this)) {
            ChooseDifficulty cf =new ChooseDifficulty(1);
           // Background bg = new Background(1);
            Greenfoot.setWorld(cf);
        }
    }    
}
