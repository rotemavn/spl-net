package PL.EditLevel;

import BL.*;
import PL.GameBoard;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static BL.Orientation.VERTICAL;
import static BL.Role.Target;

/**
 * Created by liorbass on 20/05/2016.
 */
public class EditGrid extends GameBoard {

    private Piece _toAdd;
    private JButton[][] EmptyButtons;

    /**
     * Empty constractor
     * build a level from scratch
     */
    public EditGrid() throws IOException {
        super();
        _l = new Level();
        fillBoard();
        setTargetPiece();
        //region fill board
        revalidate();

    }


    /**
     * level constractor
     * edit existing level
     *
     * @param l
     */
    public EditGrid(Level l) throws IOException {
        super();
        this.setFocusable(true);
        this._l = new Level(l);
        this.setPreferredSize(new Dimension(41 * 6, 41 * 6));

        for (Piece p : l.get_Pieces()) {
            //get piece and JButton
            Position place = p.get_start();
            JButton b = super.getJButton(p);

            //key action listeners
            b.addActionListener(this);
            b.addKeyListener(this);
            GridBagConstraints c = getConstrains(p);  //key constrains
            add(b, c, 2); // add key with constrains
            buttonArr[place.getX()][place.getY()] = b;

        }
        //mark endspot
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = _l.get_finish().getX();
        c.gridy = _l.get_finish().getY();
        c.gridwidth = 1;
        c.gridwidth = 1;
        JPanel cont = new JPanel();
        cont.setPreferredSize(new Dimension(40, 40));
        cont.setBackground(Color.black);
        cont.setBackground(Color.black);
        this.add(cont, c);
        //end mark endspot
        setVisible(true);
    }

    /**
     * fills the board with empty buttons/existing buttons
     */
    private void fillBoard() throws IOException {
        JButton b;
        GridBagConstraints c;
        EmptyButtons = new JButton[SIZE][SIZE];
        for (Piece p : _l.get_Pieces()) {
            b = super.getJButton(p);

            b.addActionListener(this);
            b.addKeyListener(this);
            c = getConstrains(p);
            this.add(b, c, 3);
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (_l.getPieceByPosition(new Position(i, j)) == null) {
                    Piece p = new Piece(new Position(i, j), 1, Role.Reg, Orientation.HORIZONTAL);
                    b = getJButton(p);

                    b.addActionListener(this);
                    c = getConstrains(p);
                    EmptyButtons[i][j] = b;
                    this.add(b, c, 1);
                }
            }
        }
        this.revalidate();
    }
    private void setTargetPiece() throws IOException {
        Piece target=new Piece(new Position(0,2),2, Target,Orientation.HORIZONTAL);
        _l.addPiece(target);
        addButtonForNewMainPiece(target);
    }
    /**
     * sets given piece as the piece template to add
     * @param p the template of the piece to add
     */
    public void setSelectedPiece(Piece p) {
        _toAdd = new Piece(p);

    }

    /**
     * deletes the selected piece from the board
     * @return true if succeed, false otherwise
     */
    public boolean deletePiece() throws IOException {
        Position pl = _selected.getKey().get_start();
        Piece p = _l.getPieceByPosition(pl);
        if (p != null &&p.get_role()!= Target&& _l.removePiece(p)) {
            int x = pl.getX();
            int y = pl.getY();
            JButton b = buttonArr[x][y];
            addEmptyButtons(buttonArr[x][y]);
            this.remove(b);

            buttonArr[x][y] = null;

            this.revalidate();
            return true;
        }
        return false;
    }

    //region action listeners
    @Override
    public void actionPerformed(ActionEvent e) { // adds the selected piece to the board
        GridBagLayout gbl = (GridBagLayout) this.getLayout();
        GridBagConstraints c = gbl.getConstraints((JButton) e.getSource());
        int x = c.gridx;
        int y = c.gridy;
        if (_l.getPieceByPosition(new Position(x,y))==null) {

            Position p = new Position(x, y);
            _toAdd=new Piece(_toAdd);
            _toAdd.move(p); //move the selected piece to the selected positon
            _toAdd = new Piece(_toAdd);

            boolean ans = _l.addPiece(_toAdd);    //add the piece to the level if possible
            if (ans) {
                removeEmptyButtons(_toAdd);
                try {
                    addButtonForNewMainPiece(_toAdd);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            this.revalidate();
        }
        else {
            JButton pressed = (JButton) e.getSource();
            Piece tp = _l.getPieceByPosition(GetButtonIndex(pressed));
            _selected = new Pair<Piece, JButton>(tp, (JButton) e.getSource());

        }
        this.setFocusable(true);
    }

    /**
     * adds a JButton that matches given piece to the board
     * @param p piece to add
     */
    private void addButtonForNewMainPiece(Piece p) throws IOException {
        JButton b= super.getJButton(p);
        b.setBackground(Color.white);
        GridBagConstraints c= getConstrains(p);
        b.addActionListener(this);
        b.addKeyListener(this);
        buttonArr[p.get_start().getX()][p.get_start().getY()]=b;
        removeEmptyButtons(p);
        this.add(b,c,3);
    }



    /**
     * removes the empty buttuns that will be underneath p
     * afte p is added to the board
     * @param p piece thtat will be added to the board
     */
    private void removeEmptyButtons(Piece p) {
        int x; int y;
        if(p!=null) {
            for (int i = 0; i < p.get_size(); i++) {

                if (p.get_orientation() == Orientation.HORIZONTAL) {
                    x = p.get_start().getX() + i;
                    y = p.get_start().getY();
                }
                else {
                    x = p.get_start().getX();
                    y = p.get_start().getY() + i;
                }
                if(EmptyButtons[x][y]!=null) {
                    this.remove(EmptyButtons[x][y]);
                    EmptyButtons[x][y] = null;
                }
            }
        }
    }

    /**
     * removes emptyButtons by button
     * @param b button to remove empty buttons underneath
     */
    private void removeEmptyButtons(JButton b) {
        GridBagLayout gbl = (GridBagLayout) this.getLayout();

        GridBagConstraints c = gbl.getConstraints(b);
        int x; int y;
        for (int i = 0; i < c.gridwidth; i++) {
            for(int j=0;j<c.gridheight;j++){
                x=c.gridx + i; y=c.gridy + j;
                if(EmptyButtons[x][y]!=null) {
                    this.remove(EmptyButtons[x][y]);
                    EmptyButtons[x][y] = null;
                }
            }
        }

    }

    /**
     * adds empty buttons to EmptyButton array where b used to be
     * @param b button to be "filled" with empty buttons
     */
    private void addEmptyButtons(JButton b) throws IOException {
        JButton tempB;
        b.setBackground(Color.WHITE);
        GridBagConstraints tempC;
        GridBagLayout gbl=(GridBagLayout)this.getLayout();
        GridBagConstraints oldBConstraints= gbl.getConstraints(b);

        int x; int y;
        for(int i=0;i<oldBConstraints.gridwidth;i++) {
            for (int j=0;j<oldBConstraints.gridheight;j++) {
                x=oldBConstraints.gridx + i; y=oldBConstraints.gridy + j;
                if(EmptyButtons[x][y]==null) {
                    Piece p = new Piece(new Position(x, y), 1, Role.Reg, Orientation.HORIZONTAL);
                    tempB = getJButton(p);
                    tempB.setBackground(Color.WHITE);
                    tempB.addActionListener(this);
                    tempC = getConstrains(p);
                    EmptyButtons[x][y] = tempB;
                    this.add(tempB, tempC, 1);
                }
            }
        }
    }

    protected JButton getJButton(Piece p) throws IOException {
        ImageIcon targetCar = new ImageIcon(ImageIO.read(new File(Paths.get(".").toAbsolutePath().normalize().toString()+"/Images/TargetCar1.png")));
        JButton b= new JButton();
        b.setBackground(Color.white);
        if (p.get_orientation() == Orientation.HORIZONTAL) {  //piece is horizontal
            b.setPreferredSize(new Dimension(40 * p.get_size(), 40));
        }
        else {  //piece is vertical
            b.setPreferredSize(new Dimension(40, 40 * p.get_size()));
        }
        if(p.get_role()==Role.Target)     //if the piece is the targerpiece
        {
            b.setOpaque(true);
            b.setIcon(targetCar);
        }
        if(p.get_start().getX()==5 && p.get_start().getY()==2){
            String currPath= Paths.get(".").toAbsolutePath().normalize().toString();
            ImageIcon img = new ImageIcon(ImageIO.read(new File(currPath+"/Images/endPos.png")));
            b.setIcon(img);
        }


        return b;

    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    //public void keyPressed(KeyEvent e) is implemented at super
    @Override
    public void keyReleased(KeyEvent e) {

    }
    //endregion

    /**
     * Moves a button from current position to position pos
     *
     * @param b      button to remove
     * @param newpos the new position of the button
     */
    protected void moveButton(JButton b, Position newpos) throws IOException {
        addEmptyButtons(b);
        Piece p = _selected.getKey();
        GridBagConstraints c;
        GridBagLayout gbl=(GridBagLayout)this.getLayout();
        c=gbl.getConstraints(b);
        c.gridx=newpos.getX();
        c.gridy=newpos.getY();
        Position bpos=GetButtonIndex(b);

        buttonArr[newpos.getX()][newpos.getY()]=b;
        buttonArr[bpos.getX()][bpos.getY()]=null;
        _selected = new Pair<Piece, JButton>(p, b);
        gbl.setConstraints(b,c);

        removeEmptyButtons(b);

        this.revalidate();
    }
}
