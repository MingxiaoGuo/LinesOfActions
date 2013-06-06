package loa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import static loa.Side.*;
import static loa.Main.*;
import static loa.Board.*;

/** The class that provides graphic game user interface support.
 * @author Weier Wan
 */
public class Gui extends JFrame implements MouseMotionListener, MouseListener {

    /** The default constructor. */
    public Gui() {
        initialize();

        for (int i = 0; i < LENGTH * LENGTH; i++) {
            JPanel square = new JPanel(new BorderLayout());
            actionBoard.add(square);
            int row = (i / 8) % 2;
            if (row == 0) {
                square.setBackground(i % 2 == 0
                        ? Color.white : Color.lightGray);
            } else {
                square.setBackground(i % 2 == 0
                        ? Color.lightGray : Color.white);
            }
        }
        placeStuff();
    }

    /** Link THIS to NEWGAME. */
    public void setBoard(Game newgame) {
        board = (MutableBoard) newgame.getBoard();
        game = newgame;
        blackTime.setText(game._hasTimeLimit ? ("Black left time: "
                + (game._remainingTimeB / SEC_MILLI) + "s") : "No time limit");
        whiteTime.setText(game._hasTimeLimit ? ("White left time: "
                + (game._remainingTimeW / SEC_MILLI) + "s") : "No time limit");
    }

    /** initialize the board. */
    void initialize() {
        backgroundPane = new JLayeredPane();
        backgroundPane.setLayout(new BorderLayout());

        JPanel info = new JPanel(new GridLayout(4, 2));
        info.setPreferredSize(INFO_SIZE);
        info.setBackground(Color.yellow);
        usage.setPreferredSize(LABEL_SIZE);
        usage.setFont(INST_FONT);
        prompt = new JLabel();
        prompt.setPreferredSize(LABEL_SIZE);
        nextMove = new JLabel("Next move: Black");
        nextMove.setPreferredSize(LABEL_SIZE);
        moveNum = new JLabel("Total moves: 0");
        moveNum.setPreferredSize(LABEL_SIZE);
        blackTime = new JLabel("");
        blackTime.setPreferredSize(LABEL_SIZE);
        whiteTime = new JLabel("");
        blackTime.setPreferredSize(LABEL_SIZE);
        play.setPreferredSize(LABEL_SIZE);
        quitGame.setPreferredSize(LABEL_SIZE);
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                moveMade = true;
                humanMove = null;
            }
        });
        quitGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });

        info.add(usage);
        info.add(prompt);
        info.add(nextMove);
        info.add(moveNum);
        info.add(blackTime);
        info.add(whiteTime);
        info.add(play);
        info.add(quitGame);

        getContentPane().add(backgroundPane);
        backgroundPane.setPreferredSize(GAME_SIZE);
        backgroundPane.addMouseListener(this);
        backgroundPane.addMouseMotionListener(this);
        actionBoard = new JPanel();
        backgroundPane.add(actionBoard, BorderLayout.CENTER);
        backgroundPane.add(info, BorderLayout.SOUTH);
        actionBoard.setLayout(new GridLayout(8, 8));
        actionBoard.setPreferredSize(BOARD_SIZE);
        actionBoard.setBounds(0, 0, BOARD_SIZE.width, BOARD_SIZE.height);
    }

    /** Set the initial configuration of the board. */
    void placeStuff() {
        JLabel piece;
        JPanel panel;

        int inc = LENGTH * LENGTH - 8;
        for (int i = 1; i < 7; i++) {
            piece = new JLabel(new ImageIcon("./images/black.jpg"));
            piece.setBackground(Color.blue);
            panel = (JPanel) actionBoard.getComponent(i);
            panel.add(piece);
            piece = new JLabel(new ImageIcon("./images/black.jpg"));
            panel = (JPanel) actionBoard.getComponent(i + inc);
            panel.add(piece);
        }

        for (int i = 1; i < 7; i++) {
            piece = new JLabel(new ImageIcon("./images/white.jpg"));
            panel = (JPanel) actionBoard.getComponent(i * 8);
            panel.add(piece);
            piece = new JLabel(new ImageIcon("./images/white.jpg"));
            panel = (JPanel) actionBoard.getComponent(i * 8 + 7);
            panel.add(piece);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        pieceLabel = null;
        Component c = actionBoard.findComponentAt(e.getX(), e.getY());

        if (c instanceof JPanel) {
            return;
        }

        Point parentLocation = c.getParent().getLocation();
        p0 = getCoor(indexOf(c.getParent()));
        System.out.println(p0);
        xCoord = parentLocation.x - e.getX();
        yCoord = parentLocation.y - e.getY();
        pieceLabel = (JLabel) c;
        pieceLabel.setLocation(e.getX() + xCoord, e.getY() + yCoord);
        pieceLabel.setSize(pieceLabel.getWidth(), pieceLabel.getHeight());
        backgroundPane.add(pieceLabel, JLayeredPane.DRAG_LAYER);
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (pieceLabel == null) {
            return;
        }
        pieceLabel.setLocation(me.getX() + xCoord, me.getY() + yCoord);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (pieceLabel == null) {
            return;
        }

        pieceLabel.setVisible(false);
        Component c = actionBoard.findComponentAt(e.getX(), e.getY());

        if (c instanceof JLabel) {

            Container parent = c.getParent();
            p1 = getCoor(indexOf(parent));
            Move move = Move.create((int) p0.getY() + 1, 8 - (int) p0.getX(),
                    (int) p1.getY() + 1, 8 - (int) p1.getX());
            if (board.isLegal(move)) {
                parent.remove(0);
                parent.add(pieceLabel);
                nextMove.setText("Next move: " + board.turn().opponent());
                moveNum.setText("Total moves: " + (board.movesMade() + 1));
                humanMove = move;
                moveMade = true;
                prompt.setText((board.turn() == BLACK ? "B::" : "W::") + move);
            } else {
                moveBack(pieceLabel);
                prompt.setText("Illegal move.");
            }
        } else {
            Container parent = (Container) c;
            int index = indexOf(parent);
            if (index != -1) {
                p1 = getCoor(index);
                Move move = Move.create((int) p0.getY() + 1,
                        8 - (int) p0.getX(), (int) p1.getY() + 1,
                        8 - (int) p1.getX());
                if (board.isLegal(move)) {
                    parent.add(pieceLabel);
                    nextMove.setText("Next move: " + board.turn().opponent());
                    moveNum.setText("Total moves: " + (board.movesMade() + 1));
                    humanMove = move;
                    moveMade = true;
                    prompt.setText((board.turn() == BLACK
                            ? "B::" : "W::") + move);
                } else {
                    moveBack(pieceLabel);
                    prompt.setText("Illegal move.");
                }
            } else {
                moveBack(pieceLabel);
                prompt.setText("Illegal move.");
            }

        }

        pieceLabel.setVisible(true);

    }

    /** Move the PIECE back to its original place. */
    void moveBack(JLabel piece) {
        int index = getIndex((int) p0.getX(), (int) p0.getY());
        JPanel temp = (JPanel) actionBoard.getComponent(index);
        temp.add(piece);

    }

    /** Make MOVE to this GUI. */
    public void makeMove(Move move) {
        int row0 = 8 - move.getRow0();
        int col0 = move.getCol0() - 1;
        int row1 = 8 - move.getRow1();
        int col1 = move.getCol1() - 1;
        JPanel panel0, panel1;
        JLabel piece;
        panel0 = (JPanel) actionBoard.getComponent(getIndex(row0, col0));
        if (panel0.getComponentCount() == 0) {
            return;
        }
        piece = (JLabel) panel0.getComponent(0);
        piece.setVisible(false);
        panel0.remove(0);
        panel1 = (JPanel) actionBoard.getComponent(getIndex(row1, col1));
        if (panel1.getComponentCount() != 0) {
            panel1.remove(0);
        }
        panel1.add(piece);
        piece.setVisible(true);
        nextMove.setText("Next move: " + board.turn());
        moveNum.setText("Total moves: " + board.movesMade());
        prompt.setText((board.turn() == BLACK ? "W::" : "B::") + move);
    }

    /** Return the corresponding index of ROW and COL in the actionBoard. */
    static int getIndex(int row, int col) {
        return row * 8 + col;
    }

    /** Return the Coordinate corresponding to INDEX in the actionBoard. */
    Point getCoor(int index) {
        Point p = new Point();
        p.setLocation(index / 8, index % 8);
        return p;
    }

    /** Return the index of component C in the actionBoard. -1 if C is not
     *  in the board. */
    int indexOf(Component c) {
        for (int i = 0; i < LENGTH * LENGTH; i++) {
            if (actionBoard.getComponent(i) == c) {
                return i;
            }
        }
        return -1;
    }

    /** Let the Board display the winner SIDE. */
    void gameOver(Side side) {
        prompt.setText(side + " wins!");
        prompt.setFont(WIN_FONT);
    }


    /** Empty overriden moethods E. */
    public void mouseClicked(MouseEvent e) {
    }

    /** They supress complaint E. */
    public void mouseMoved(MouseEvent e) {
    }

    /** MouseEntered E. */
    public void mouseEntered(MouseEvent e) {
    }

    /** MouseExited E. */
    public void mouseExited(MouseEvent e) {
    }

    /** Return whether expected move has been made. */
    boolean moveMade() {
        return moveMade;
    }

    /** Set moveMade to MADE. */
    void setMoveMade(boolean made) {
        moveMade = made;
    }

    /** Return the move made by a human player. */
    Move getMove() {
        return humanMove;
    }

    /** Set the humanMove to MOVE. */
    void setMove(Move move) {
        humanMove = move;
    }

    /** Return the time remaining for SIDE. */
    JLabel getTime(Side side) {
        if (side == BLACK) {
            return blackTime;
        } else {
            return whiteTime;
        }
    }

    /** Layer configuration. */
    private JLayeredPane backgroundPane;

    /** The main chess board. */
    private JPanel actionBoard;

    /** A single piece. */
    private JLabel pieceLabel;

    /** X Coordinate. */
    private int xCoord;

    /** Y Coordinate. */
    private int yCoord;

    /** Indicate the side of the next move. */
    private JLabel nextMove;

    /** Indicate the total number of moves made. */
    private JLabel moveNum;

    /** Prompt text. */
    private JLabel prompt;

    /** Remaining time for BLACK. */
    private JLabel blackTime;

    /** Remaining time for WHITE. */
    private JLabel whiteTime;

    /** Display the instruction. */
    private static JLabel usage = new JLabel("Drag pieces to make move!");

    /** Button used to control the initialization of the AI. */
    private static JButton play = new JButton("AI start");

    /** Button used to quit the game. */
    private static JButton quitGame = new JButton("Quit");

    /** The dimension of the chess board. */
    static final Dimension BOARD_SIZE = new Dimension(500, 500);

    /** The dimension of the whole game board. */
    static final Dimension GAME_SIZE = new Dimension(500, 640);

    /** The dimension of the info bar. */
    static final Dimension INFO_SIZE = new Dimension(500, 140);

    /** The dimension of the single label. */
    static final Dimension LABEL_SIZE = new Dimension(250, 35);

    /** The font of winning message. */
    static final Font WIN_FONT = new Font("Serif", Font.PLAIN, 30);

    /** The font of instruction. */
    static final Font INST_FONT = new Font("Serif", Font.PLAIN, 17);

    /** The board corresponds to THIS. */
    private MutableBoard board;

    /** The game associated with THIS. */
    private Game game;

    /** A Point. */
    private Point p0;

    /** A Point. */
    private Point p1;

    /** Indicates an expected move has been made. */
    private boolean moveMade = false;

    /** The move made by human player. */
    private Move humanMove;

    /** Autogenerated serial version ID. */
    private static final long serialVersionUID = 4127589752473251360L;

}
