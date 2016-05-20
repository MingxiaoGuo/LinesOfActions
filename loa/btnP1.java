import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class btnP1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class btnP1 extends Button
{
    /**
     * Act - do whatever the btnP1 wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(Greenfoot.mouseClicked(this)) {
            ChooseDifficulty cf =new ChooseDifficulty(2);
            //Background bg = new Background(2);
            Greenfoot.setWorld(cf);
        }
    }    
}
