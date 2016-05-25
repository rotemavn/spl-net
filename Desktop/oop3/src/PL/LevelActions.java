package PL;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


class LevelActions extends JPanel implements MouseListener {

    private BufferedImage background;
    JButton goToMenu;

    //constructor
    LevelActions(String path) {
        this.setVisible(true);
        try {
            this.background = ImageIO.read(new File(path));
            setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
            setHeading();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The function sets the "back" button
     * @throws IOException if the image doesn't exist
     */
    private void setHeading() throws IOException {

        String currPath= Paths.get(".").toAbsolutePath().normalize().toString();
        ImageIcon menu = new ImageIcon(ImageIO.read(new File(currPath+"/Images/back.png")));
        goToMenu = new JButton(menu);
        goToMenu.setBorder(null);
        goToMenu.addMouseListener(this);
        add(Box.createVerticalStrut(30));
        Box right=Box.createHorizontalBox();
        right.add(goToMenu);
        right.add(Box.createRigidArea(new Dimension(1200,0)));
        add(right);

    }

    public void paintComponent(Graphics g){
        g.drawImage(background,0,0,null);

    }

    //The function redirects the user back to the main menu.
    @Override
    public void mouseClicked(MouseEvent e) {
        this.setVisible(false);
        if(e.getSource().equals(goToMenu))
            getParent().add(new MainMenu(this));

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


