package PL.PlayLevel;

import BL.*;
import PL.GameBoard;
import javafx.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Created by liorbass on 17/05/2016.
 */
public class BoardGrid extends GameBoard {

    private boolean _isFinished;
    protected final Position END_POSITION;
    private GameWindow gw;

    public BoardGrid(Level l,GameWindow gw) throws IOException {
        super();
        this.gw=gw;
        _isFinished=false;
        END_POSITION=new Position(5,2);
        this.setFocusable(true);
        this._l = new Level(l);
        this.setPreferredSize(new Dimension(41 * 6, 41 * 6));


        for (Piece p : l.get_Pieces()) {
            //get piece and JButton
            Position place = p.get_start();
            JButton b =getJButton(p);
            //key action listeners
            b.addActionListener(this);
            b.addKeyListener(this);
            GridBagConstraints c=getConstrains(p);  //key constrains
            add(b, c,2); // add key with constrains
            buttonArr[place.getX()][place.getY()] = b;

        }

        //end mark endspot
        setVisible(true);
    }

    /**
     * what to do when a button is pressed, mark piece as selected
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressed = (JButton) e.getSource();
        Piece tp = _l.getPieceByPosition(GetButtonIndex(pressed));
        _selected = new Pair<Piece, JButton>(tp, (JButton) e.getSource());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }


    //public void keyPressed(KeyEvent e) is implemented at super

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Moves a button from current position to position pos
     * @param b button to remove
     * @param pos the new position of the button
     */
    protected void moveButton(JButton b, Position pos) {
        Piece p= _selected.getKey();
        GridBagConstraints c= new GridBagConstraints();
        GridBagLayout gbl=(GridBagLayout)this.getLayout();
        c=gbl.getConstraints(b);
        c.gridx=pos.getX();
        c.gridy=pos.getY();
        Position bpos=GetButtonIndex(b);
        buttonArr[pos.getX()][pos.getY()]=b;
        buttonArr[bpos.getX()][bpos.getY()]=null;
        _selected = new Pair<Piece, JButton>(p, b);
        gbl.setConstraints(b,c);
        this.revalidate();
        isFinished(_selected.getKey());
        /*if(isFinished(_selected.getKey()))
        {
            _isFinished=true;
          //  ((GameWindow)SwingUtilities.getWindowAncestor(this)).finished();
        }*/
    }

    /**
     * Revert last move
     * @return true if succeeded, false otherwise
     */
    public boolean undoMove()
    {
        Move m= _l.undoMove();
        if(m!=null) {
            moveButton(buttonArr[m.GetEnd().getX()][m.GetEnd().getY()], m.GetStart());
            return true;
        }
        return false;
    }

    /**
     * Checks if the piece pic is at the spot endPos
     * @param pic piece to check location of
     * @return  true if piece is at location, false otherwise
     */
   /* private boolean isFinished(Piece pic)
    {
        boolean ans;
        Position picEndPos=pic.get_end();
        ans=picEndPos.equals(END_POSITION)||pic.get_start().equals(END_POSITION);
        return ans;
    }*/

    private void isFinished(Piece pic)
    {
        boolean ans;
        Position picEndPos=pic.get_end();
        ans=picEndPos.equals(END_POSITION)||pic.get_start().equals(END_POSITION);
        if(ans==true) {
            endGame();
        }
    }

    public void endGame(){
        _isFinished = true;
        gw.finished();

    }



    /**
     * returns true if the stage has been finished
     * @return true if the stage has been finished, false otherwise
     */
    public boolean isFinished() {
        return _isFinished;
    }
}
