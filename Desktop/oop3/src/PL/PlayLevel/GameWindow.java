package PL.PlayLevel;

import BL.Level;
import PL.MainMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by liorbass on 17/05/2016.
 */
public class GameWindow extends JPanel implements ActionListener{
    private javax.swing.Timer _t;
    private JLabel _lblTime;
    private BoardGrid _bg;
    private JButton _undo;
    private JButton goToMenu;
    private int _seconds;
    private int _minuts;
    private int _hours;
    private BufferedImage background;
    private Level level;

    public GameWindow(Level l) throws IOException {
        super();
        this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
        String currPath= Paths.get(".").toAbsolutePath().normalize().toString();

        background=ImageIO.read(new File(currPath+"/Images/background.png"));
        ImageIcon menu = new ImageIcon(ImageIO.read(new File(currPath+"/Images/back.png")));

        goToMenu = new JButton(menu);
        goToMenu.setBorder(null);
        goToMenu.addActionListener(this);
        add(Box.createHorizontalStrut(25));
        Box left=Box.createVerticalBox();
        left.add(goToMenu);
        left.add(Box.createVerticalGlue());
        add(left);

        Box center=Box.createVerticalBox();
        Box right=Box.createHorizontalBox();


        this.level=l;
        _bg=new BoardGrid(l,this);

        center.add(_bg);
        add(center);
        add(right);

        //start timer initialization
        _t=new Timer(1000,this); //move every second
        _seconds=0;
        _hours=0;
        _minuts=0;
        _lblTime=new JLabel();
        _lblTime.setText(getTime());
        _lblTime.setFont(new Font("Candara",Font.BOLD,30));
        //center.add(_lblTime);
       // _t.start();
        //end timer initialization
        //undo initialization
        _undo=new JButton();
        ImageIcon undo = new ImageIcon(ImageIO.read(new File(currPath+"/Images/undo2.png")));
        _undo.setIcon(undo);
        _undo.setBackground(null);
        _undo.setBorder(null);
        _undo.addActionListener(this);
        right.add(_undo);

        //undo initialization
        this.setVisible(true);
        this.revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==_t) {
            _seconds++;
            if (_seconds == 60) {
                _seconds = 0;
                _minuts++;
                if (_minuts == 60) {
                    _minuts = 0;
                    _hours++;
                }
            }
            _lblTime.setText(this.getTime());
            this.repaint();
            //add(_lblTime, BorderLayout.SOUTH);
        }
        else if(e.getSource()==_undo)
        {
            _bg.undoMove();
            this.repaint();
        }

        else if(e.getSource()== goToMenu){
            this.setVisible(false);
            getParent().add(new MainMenu(this));
        }

    }


    /**
     * gets full time string
     * @return a formated string representing time
     */
    private String getTime()
    {
        String ans="";
        if(_hours<10)
            ans+="0"+_hours;
        else
            ans+=_hours;
        ans+="-";
        if(_minuts<10)
            ans+="0"+_minuts;
        else
            ans+=_minuts;
        ans+="-";
        if(_seconds<10)
            ans+="0"+_seconds;
        else
            ans+=_seconds;
        return ans;

    }
    /**
     * What to do if the player finished the game
     */
    public void finished()
    {
        _t.stop();
        JOptionPane.showMessageDialog(this,"You Have Won!");
        /*String bestTime=this.level.get_bestTime();
        String[] t= bestTime.split("-");
        int h=Integer.parseInt(t[0]);
        int m=Integer.parseInt(t[1]);
        int s=Integer.parseInt(t[2]);
        if(h<_hours)
            level.set_bestTime(getTime());
        else if(h==_hours && m<_minuts)
            level.set_bestTime(getTime());
        else if(h==_hours && m==_minuts && s<_seconds)
            level.set_bestTime(getTime());*/
        this.setVisible(false);
        getParent().add(new MainMenu(this));

    }

    public void paintComponent(Graphics g){
        g.drawImage(background,0,0,null);
    }
}
