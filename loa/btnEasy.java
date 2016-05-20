import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class btnEasy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class btnEasy extends Button
{
    /**
     * Act - do whatever the btnEasy wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Easy is 1
        if(Greenfoot.mouseClicked(this)) {
            int whoIsAI = ((ChooseDifficulty)getWorld()).whoIsAI;
            Background bg = new Background(whoIsAI, 1);
            Greenfoot.setWorld(bg);
        }
    }    
}
