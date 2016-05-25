package PL.EditLevel;

import BL.*;
import PL.MainMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Vector;

public class EditWindow extends JPanel implements ActionListener {

    private Vector<Piece> _vp; //the defult pieces that can be added
    private Vector<JButton> _btnDefultPiece; // the defult JButtons representing _vp
    private EditGrid _eg;  //the main window
    private JButton _btnDelete; //button to delete a piece
    private BufferedImage background;
    private JButton goToMenu;


    public EditWindow() throws IOException {
        super();
        String currPath= Paths.get(".").toAbsolutePath().normalize().toString();
        background=ImageIO.read(new File(currPath+"/Images/background.png"));
        this.setPreferredSize(new Dimension(1409,803));

        this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));

        ImageIcon menu = new ImageIcon(ImageIO.read(new File(currPath+"/Images/back.png")));
        goToMenu = new JButton(menu);
        goToMenu.setBorder(null);
        goToMenu.addActionListener(this);
        add(Box.createHorizontalStrut(25));
        Box left=Box.createVerticalBox();
        left.add(goToMenu);
        left.add(Box.createVerticalGlue());
        add(left);

        Box center=Box.createHorizontalBox();
        Box right=Box.createVerticalBox();


        //main frame
        _eg = new EditGrid();
        center.add(_eg);
        add(center);
        add(right);
        //delete button
        _btnDelete = new JButton();
        _btnDelete.setText("Delete");
        _btnDelete.addActionListener(this);

        right.add(_btnDelete);
        // defult pieces
        _vp=this.setPieces();
        //set buttons
        this._btnDefultPiece = this.getPiecesButtons();
        JPanel jp2=new JPanel();

        int i=0;
        Integer j;
        for (JButton b : _btnDefultPiece) {
            b.addActionListener(this);
            j=_vp.get(i).get_size();
            i++;
            b.requestFocusInWindow();
            setButton(b,j,_vp.get(i-1).get_orientation());
            right.add(b);
        }

        this.setVisible(true);

        this.revalidate();
    }


    public void setButton(JButton b, int size, Orientation orientation){
        try {
            String currPath= Paths.get(".").toAbsolutePath().normalize().toString();
            ImageIcon truckVer = new ImageIcon(ImageIO.read(new File(currPath+"/Images/TruckVer2.png")));
            ImageIcon carHor = new ImageIcon(ImageIO.read(new File(currPath+"/Images/carHor2.png")));
            ImageIcon carVer = new ImageIcon(ImageIO.read(new File(currPath+"/Images/carVer2.png")));
            ImageIcon truckHor = new ImageIcon(ImageIO.read(new File(currPath+"/Images/truckHor2.png")));

            if (orientation == Orientation.HORIZONTAL) {  //piece is horizontal

                if(size==3) {
                    b.setIcon(truckHor);

                  b.setPreferredSize(new Dimension(155,55));
                }
                else {
                    b.setIcon(carHor);
                   b.setPreferredSize(new Dimension(105,55));
                }
            }
            else {  //piece is vertical
               if(size==3) {
                   b.setIcon(truckVer);
                   b.setPreferredSize(new Dimension(55,155));
               }
                else {
                   b.setIcon(carVer);
                  b.setPreferredSize(new Dimension(55,105));
               }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == _btnDelete) { // case button delete
            try {
                _eg.deletePiece();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (_btnDefultPiece.contains(e.getSource())) { // case select piece
            Piece selected = getPieceByButton((JButton) e.getSource());
            if (selected != null) {
                _eg.setSelectedPiece(selected);
                markSelected(((JButton)e.getSource()));
            }
        }

        if(e.getSource()==goToMenu){
            this.setVisible(false);
            getParent().add(new MainMenu(this));
        }
    }
    private void markSelected(JButton b) {
        for (JButton curb:_btnDefultPiece) {
            if(curb!=b)
            {
                b.setOpaque(false);
                b.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
            else {
                b.setOpaque(true);
                b.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            }
        }

    }


    private Piece getPieceByButton(JButton jb) {

        for (int i = 0; i < _btnDefultPiece.size(); i++) {
            if (jb == _btnDefultPiece.get(i)) {
                return _vp.get(i);
            }
        }
        return null;
    }

    /**
     * Sets the defult pieces that can be added
     */
    private Vector<Piece> setPieces() {
        Vector<Piece> arr = new Vector<Piece>();
        Position pos = new Position(0, 0);
        arr.add(new Piece(pos, 2, Role.Reg, Orientation.HORIZONTAL));
        arr.add(new Piece(pos, 3, Role.Reg, Orientation.HORIZONTAL));
        arr.add(new Piece(pos, 2, Role.Reg, Orientation.VERTICAL));
        arr.add(new Piece(pos, 3, Role.Reg, Orientation.VERTICAL));
        return arr;
    }

    /**
     * return a vector of JButtons representing the defult pieces
     *
     * @returnvector of JButtons representing the defult pieces
     */
    private Vector<JButton> getPiecesButtons() {
        Vector<JButton> vjb = new Vector<JButton>();
        for (Piece p : _vp) {
            JButton b = new JButton();
            if (p.get_orientation() == Orientation.HORIZONTAL) {  //piece is horizontal
                b.setPreferredSize(new Dimension(20 * p.get_size(), 20));
            } else {  //piece is vertical
                b.setPreferredSize(new Dimension(20, 20 * p.get_size()));
            }
            if (p.get_role() == Role.Target)     //if the piece is the targerpiece
            {
                b.setBackground(Color.red);
                b.setOpaque(true);
            }
            vjb.add(b);
        }
        return vjb;
    }

    public void paintComponent(Graphics g){
        g.drawImage(background,0,0,null);
    }
}
