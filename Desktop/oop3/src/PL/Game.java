package PL;

import javax.swing.*;


class Game {
    protected final JFrame frame;

    public Game(){
        frame=new JFrame("Escape Grid"); //creating new game window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1409,830);
        frame.setResizable(false);
        JPanel mainMenu=new MainMenu();
        frame.add(mainMenu);
        frame.setVisible(true);

    }

    public void setPanel(JPanel panel){

        frame.removeAll();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1409,830);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.add(panel);

    }


}
