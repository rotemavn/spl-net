package PL;

import BL.*;
import BL.Position;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static BL.Orientation.VERTICAL;
import static BL.Role.Target;

/**
 * Created by liorbass on 17/05/2016.
 */
public abstract class GameBoard extends JLayeredPane implements KeyListener,ActionListener{

    protected final int SIZE=6;
    protected JButton[][] buttonArr;
    protected Pair<Piece,JButton> _selected;
    protected Level _l;


    public GameBoard() throws IOException {
        super();
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(500,500));

        //JComponent[][]=new JComponent();
        buttonArr=new JButton[SIZE][SIZE];
        GridBagConstraints c= new GridBagConstraints();
        c.fill=GridBagConstraints.BOTH;
        c.gridheight=1;
        c.gridwidth=1;

        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                JButton jp=new JButton();
                c.gridx=i;
                c.gridy=j;
                jp.setPreferredSize(new Dimension(100,100));
                jp.setBackground(Color.white);
                jp.addActionListener(this);
                add(jp,c,0);

                if(i==5&&j==2){ //setting the ending location
                    String currPath= Paths.get(".").toAbsolutePath().normalize().toString();
                    ImageIcon img = new ImageIcon(ImageIO.read(new File(currPath+"/Images/endPos.png")));
                    jp.setIcon(img);
                }

            }
        }

        this.setVisible(true);

    }

    /**
     * Searches for the position of a JButton in the current array
     * @param e button to search for
     * @return the position of the button in the array, if it doesn't exist return null
     */
    public Position GetButtonIndex(JButton e)
    {
        for(int i=0;i<buttonArr.length;i++)
        {
            for(int j=0;j<buttonArr[0].length;j++)
            {
                if(buttonArr[i][j]==(e))
                    return new Position(i,j);
            }
        }
        return null;
    }
    @Override
    public abstract void actionPerformed(ActionEvent e) ;
    @Override
    public abstract void keyTyped(KeyEvent e) ;
    /**
     * what do when a key is pressed
     * check if is a valid move and move a piece
     * @param e the key event
     */
    @Override
    public  void keyPressed(KeyEvent e) {
        if (_selected != null &&_selected.getKey()!=null&&_selected.getKey()!=null) {
            int key = e.getKeyCode();
            JButton b= _selected.getValue();
            Position tempPos = _selected.getKey().get_start();
            GridBagConstraints c = new GridBagConstraints();
            boolean flag=false;
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){

                if (_l.move(_selected.getKey(), new Position(tempPos.moveLeft()))) {
                    //_selected.getValue().add(_selected,c);
                    tempPos = tempPos.moveLeft();
                    flag=true;
                }
            }
            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {

                if (_l.move(_selected.getKey(), new Position(tempPos.moveRight()))) {
                    tempPos = tempPos.moveRight();
                    flag=true;
                }
            }
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {

                if (_l.move(_selected.getKey(), new Position(tempPos.moveUp()))) {
                    tempPos = tempPos.moveUp();
                    flag=true;
                }

            }
            if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {

                if (_l.move(_selected.getKey(), new Position(tempPos.moveDown()))) {
                    tempPos = tempPos.moveDown();
                    flag=true;
                }
            }
            if(flag)
                try {
                    moveButton(b,tempPos);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            //_selected.getValue().setLocation(tempPos.getX(), tempPos.getY());
        }
    }
    protected abstract void moveButton(JButton b, Position pos) throws IOException;
    @Override
    public abstract void keyReleased(KeyEvent e);

    /**
     * Calculate and return GridBagConstrains matching a given piece
     * @param p to account for
     * @return GridBagConstrains matching piece
     */
    protected GridBagConstraints getConstrains(Piece p)
    {
        GridBagConstraints c= new GridBagConstraints();
        Position place =p.get_start();
        c.gridx = place.getX();
        c.gridy = place.getY();

        if (p.get_orientation() == Orientation.HORIZONTAL) {  //piece is horizontal

            c.gridwidth = p.get_size();
            c.gridheight=1;
            c.fill = GridBagConstraints.BOTH;
        }
        else {  //piece is vertical
            c.gridheight = p.get_size();
            c.gridwidth=1;
            c.fill = GridBagConstraints.BOTH;
        }
        return c;
    }

    /**
     * Calculate and create a JButton matching to given piece
     * @param p piece to match
     * @return  a formated JButton matching given piece
     */
    protected JButton getJButton(Piece p) throws IOException {
        String currPath= Paths.get(".").toAbsolutePath().normalize().toString();
        ImageIcon carHor = new ImageIcon(ImageIO.read(new File(currPath+"/Images/carHor1.png")));
        ImageIcon carVer = new ImageIcon(ImageIO.read(new File(currPath+"/Images/carVer1.png")));
        ImageIcon truckHor = new ImageIcon(ImageIO.read(new File(currPath+"/Images/truckHor1.png")));
        ImageIcon truckVer = new ImageIcon(ImageIO.read(new File(currPath+"/Images/TruckVer1.png")));
        ImageIcon targetCar = new ImageIcon(ImageIO.read(new File(currPath+"/Images/TargetCar1.png")));

        JButton b= new JButton();
        b.setBackground(Color.WHITE);
        if (p.get_orientation() == Orientation.HORIZONTAL) {  //piece is horizontal
            b.setPreferredSize(new Dimension(40 * p.get_size(), 40));
            if(p.get_size()==3)
                b.setIcon(truckHor);
            else
                b.setIcon(carHor);
        }
        else {  //piece is vertical
            b.setPreferredSize(new Dimension(40, 40 * p.get_size()));
            if(p.get_size()==3)
                b.setIcon(truckVer);
            else
                b.setIcon(carVer);
        }
        if(p.get_role()==Role.Target)     //if the piece is the targerpiece
        {
            b.setIcon(targetCar);
            b.setOpaque(true);
        }
        if(p.get_start().getX()==5 && p.get_start().getY()==2){
            ImageIcon img = new ImageIcon(ImageIO.read(new File(currPath+"/Images/endPos.png")));
            b.setIcon(img);
        }
        return b;

    }
}
