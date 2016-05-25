package PL;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class MainMenu extends JPanel implements MouseListener {

    private BufferedImage background;
    private JButton option1;
    private JButton option2;
    private JButton option3;


    public MainMenu() {

        try {
            super.removeAll();
            String currPath= Paths.get(".").toAbsolutePath().normalize().toString();
            background= ImageIO.read(new File(currPath+"/Images/backgroundNew.png"));

            setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
            Box buttons=Box.createHorizontalBox();//setting box for menu buttons
            buttons.add(Box.createRigidArea(new Dimension(250,0)));
            add(buttons);

            ImageIcon op1=new ImageIcon(ImageIO.read(new File(currPath+"/Images/newbutton2.png")));
            option1=new JButton(op1);
            option1.setBorder(null);
            option1.addMouseListener(this);
            buttons.add(option1);
            buttons.add(Box.createRigidArea(new Dimension(100,0)));

            ImageIcon op2=new ImageIcon(ImageIO.read(new File(currPath+"/Images/newbutton3.png")));
            option2=new JButton(op2);
            option2.setBorder(null);
            option2.addMouseListener(this);
            buttons.add(option2);
            buttons.add(Box.createRigidArea(new Dimension(100,0)));

            ImageIcon op3=new ImageIcon(ImageIO.read(new File(currPath+"/Images/newbutton1.png")));
            option3=new JButton(op3);
            option3.setBorder(null);
            option3.addMouseListener(this);
            buttons.add(option3);



        } catch (IOException e) {
            System.out.println("Invalid path");
            e.printStackTrace();
        }

    }

    public void paintComponent(Graphics g){
        g.drawImage(background,0,0,null);

    }

    @Override
    /**
     * @param a mouse event
     *          the function redirects the user to the desired screen.
     */
    public void mouseClicked(MouseEvent e) {
        this.removeAll();
        this.setVisible(false);
        if (e.getSource().equals(option1))
            getParent().add(new LevelSelection());
        if (e.getSource().equals(option2))
            getParent().add(new LevelEditor());
        if(e.getSource().equals(option3))
            System.exit(0);

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
