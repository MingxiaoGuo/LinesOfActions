import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class btnHard here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class btnHard extends Button
{
    /**
     * Act - do whatever the btnHard wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Hard is 3
        if(Greenfoot.mouseClicked(this)) {
            int whoIsAI = ((ChooseDifficulty)getWorld()).whoIsAI;
            Background bg = new Background(whoIsAI, 3);
            Greenfoot.setWorld(bg);
        }
    }    
}
