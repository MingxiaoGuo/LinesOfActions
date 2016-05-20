import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ChooseDifficulty here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ChooseDifficulty extends World
{
    public int whoIsAI = 0;
    /**
     * Constructor for objects of class ChooseDifficulty.
     * 
     */
    public ChooseDifficulty(int AIRole)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1); 
        CFText cfText = new CFText();
        btnEasy easy = new btnEasy();
        btnMedium medium = new btnMedium();
        btnHard hard = new btnHard();
        addObject(cfText, 300, 100);
        addObject(easy, 150, 250);
        addObject(medium, 300, 250);
        addObject(hard, 450, 250);
        this.whoIsAI = AIRole;
    }
}
