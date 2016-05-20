import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class btnMedium here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class btnMedium extends Button
{
    /**
     * Act - do whatever the btnMedium wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Medium is 2
        if(Greenfoot.mouseClicked(this)) {
            int whoIsAI = ((ChooseDifficulty)getWorld()).whoIsAI;
            Background bg = new Background(whoIsAI, 2);
            Greenfoot.setWorld(bg);
        }
    }    
}
