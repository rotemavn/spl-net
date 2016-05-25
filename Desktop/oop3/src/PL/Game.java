package PL;

import javax.swing.*;


public class Game {
    protected final JFrame frame;

    public Game(){
        frame=new JFrame("Escape Grid"); //creating new game window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1409,830);
        frame.setResizable(false);
        JPanel mainMenu=new MainMenu(null);
        frame.add(mainMenu);
        frame.setVisible(true);

    }


}
