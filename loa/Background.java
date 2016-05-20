import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Color;
/**
 * Write a description of class Background here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Background extends World
{
    public HashMap<Integer, Integer> selectedPawn = new HashMap<>();
    public Player1 p1 = null;
    public Player2 p2 = null;
    public int difficulty = 2; // default is 2
    public boolean someoneGetSelected = false;
    public Coordinate curSelected = new Coordinate();
    public Board board;
    public int currentTurn = 0; // 1 is player1, 2 is player2
    public int AIRole = 0;
    public Message msgBox = new Message();
    /**
     * Constructor for objects of class Background.
     * 
     */
    public Background(int whoIsAI, int difficulty)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(900, 600, 1);
        this.AIRole = whoIsAI;
        this.difficulty = difficulty;
        init();
    }
    
    public void act() {
        if(board.hasWinner()) {
            if(board.isWin(AIRole)) {
                String text = "You lost";
                Actor messageActor = new Actor(){};
                // when adding message to world
                messageActor.setImage(new GreenfootImage(text, 48, Color.white, new Color(0, 0, 0, 96)));
                addObject(messageActor, 300, 300);
            } else {
                String text = "You win";
                Actor messageActor = new Actor(){};
                // when adding message to world
                messageActor.setImage(new GreenfootImage(text, 48, Color.white, new Color(0, 0, 0, 96)));
                addObject(messageActor, 300, 300);
            }
        }
        if(currentTurn == AIRole) {
            AIMove(AIRole, difficulty);
        }
    }
    
    public void init() {
        board = new Board();
        System.out.println(board.getLength());
        boolean p1IsUser = AIRole == 1 ? false : true;
        Player1 p1_1 = new Player1(200, 120, 0, 1, p1IsUser);
        Player1 p1_2 = new Player1(300, 120, 0, 2, p1IsUser);
        Player1 p1_3 = new Player1(400, 120, 0, 3, p1IsUser);
        Player1 p1_4 = new Player1(200, 520, 4, 1, p1IsUser);
        Player1 p1_5 = new Player1(300, 520, 4, 2, p1IsUser);
        Player1 p1_6 = new Player1(400, 520, 4, 3, p1IsUser);
        
        addObject(p1_1, 200, 120);
        addObject(p1_2, 300, 120);
        addObject(p1_3, 400, 120);
        addObject(p1_4, 200, 520);
        addObject(p1_5, 300, 520);
        addObject(p1_6, 400, 520);
        
        
        Player2 p2_1 = new Player2(100, 220, 1, 0, !p1IsUser);
        Player2 p2_2 = new Player2(100, 320, 2, 0, !p1IsUser);
        Player2 p2_3 = new Player2(100, 420, 3, 0, !p1IsUser);
        Player2 p2_4 = new Player2(500, 220, 1, 4, !p1IsUser);
        Player2 p2_5 = new Player2(500, 320, 2, 4, !p1IsUser);
        Player2 p2_6 = new Player2(500, 420, 3, 4, !p1IsUser);
        
        addObject(p2_1, 100, 220);
        addObject(p2_2, 100, 320);
        addObject(p2_3, 100, 420);
        addObject(p2_4, 500, 220);
        addObject(p2_5, 500, 320);
        addObject(p2_6, 500, 420);
        
        
        currentTurn = 1;
        Greenfoot.start();
    }
    
    public void AIMove(int AISide, int difficulty) {
        AlphaBetaApproach aiMove = new AlphaBetaApproach();
        if (difficulty == 2) {
            aiMove.setTreeDepth(5);
            System.out.println("Medium mode");
        } else if (difficulty == 3) {
            aiMove.setTreeDepth(6);
            System.out.println("Hard mode");
        } else {
            aiMove.setTreeDepth(3);
            System.out.println("Easy mode");
        }
        
        State state = new State(board, AISide);
        state.setDepth(0);
        System.out.println("AI thinking");
        Move move = aiMove.runAlphaBeta(state, AISide);
        
        int[] oldPos = new int[]{move.getOriginalPos().getX(), move.getOriginalPos().getY()};
        int[] newPos = new int[]{move.getTargetPos().getX(), move.getTargetPos().getY()};
        int[] oldWorldPos = getMatchBoradCoor(oldPos[0], oldPos[1]);
        int[] newWorldPos = getMatchBoradCoor(newPos[0], newPos[1]);
        System.out.println("AI move: from " + move.getOriginalPos().getX() + ", " + move.getOriginalPos().getY() + " to " + move.getTargetPos().getX() + ", " + move.getTargetPos().getY());
        System.out.println(oldWorldPos[0] + "~" + oldWorldPos[1]);
        System.out.println(oldPos[0] + "^" + oldPos[1]);
        board.moveChecker(move, AISide);
        if(AISide == 1) {
            List<Player1> list = getObjectsAt(oldWorldPos[0], oldWorldPos[1], Player1.class);
            List<Player2> list2 = getObjectsAt(newWorldPos[0], newWorldPos[1], Player2.class);
            if(list2 != null && list2.size() != 0) {
                removeObject(list2.get(0));
            }
            list.get(0).setLocation(newWorldPos[0], newWorldPos[1]);
        } else {
            List<Player2> list = getObjectsAt(oldWorldPos[0], oldWorldPos[1], Player2.class);
            List<Player1> list2 = getObjectsAt(newWorldPos[0], newWorldPos[1], Player1.class);
            if(list2 != null && list2.size() != 0) {
                removeObject(list2.get(0));
            }
            list.get(0).setLocation(newWorldPos[0], newWorldPos[1]);
        }

        //set turn
        currentTurn = AISide == 1 ? 2 : 1;
        
        Report report = new Report(aiMove.treeDepth, aiMove.nodeCount, aiMove.maxECount, aiMove.minECount, aiMove.prunMaxCount, aiMove.prunMinCount);
        if(getObjectsAt(700, 250, Message.class) != null && getObjectsAt(700, 250, Message.class).size() != 0) {
            removeObject(getObjectsAt(700, 250, Message.class).get(0));
        }
        
        addObject(new Message(report.toString()), 700, 250);
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
        } else if(gridX == 4){
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
        } else if(gridY == 4){
            boardX = 500;
        }
        System.out.println(boardX + "," + boardY);
        return new int[]{boardX, boardY};
    }
    

}
