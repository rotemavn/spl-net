package PL;
import BL.Level;
import PL.EditLevel.EditWindow;
import PL.PlayLevel.GameWindow;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.peer.ButtonPeer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


public class LevelEditor extends LevelActions implements MouseListener {

    private final File levelsFolder;
    private final File[] levels;
    private JButton view;
    private JButton delete;
    private JButton add;
    private int target;
    private JList<String> levelList;

    //constructor
    public LevelEditor(){
        super(Paths.get(".").toAbsolutePath().normalize().toString()+"/Images/background3.png");
        levelsFolder=new File(Paths.get(".").toAbsolutePath().normalize().toString()+"/Levels");
        levels=levelsFolder.listFiles();
        try {
            setMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * The function sets a new option menu for editing levels.
     * @throws IOException if any of the images doesn't exist.
     */
    private void setMenu() throws IOException {
        String currPath= Paths.get(".").toAbsolutePath().normalize().toString();
        JLabel instruction1=new JLabel("Choose Level:  "); //creating first label
        instruction1.setFont(new Font("Candara",Font.BOLD,30));
        instruction1.setBackground(null);

        String [] levelNames=new String[levels.length]; //getting level names for the list
        for(int i=1; i<=levelNames.length; i++)
            levelNames[i-1]="Level"+i;

        levelList=new JList<String>(levelNames);

        //designing the list
        levelList.setSelectionBackground(Color.red);
        levelList.setFont(new Font("Candara",Font.PLAIN,30));
        levelList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        JScrollPane sp=new JScrollPane(levelList); //adding the table to a scroll pane
        sp.setMaximumSize(new Dimension(150,150));

        super.add(Box.createRigidArea(new Dimension(0,80))); //creating spacing under the heading

        Box b=Box.createHorizontalBox();// this box contains all the buttons
        b.setMaximumSize(new Dimension(900,80));
        b.add(instruction1);
        b.add(sp);
        b.add(Box.createHorizontalStrut(30));
        JLabel instruction2=new JLabel("Choose action:  "); //creating second lable
        instruction2.setFont(new Font("Candara",Font.BOLD,30));
        instruction2.setBackground(null);
        b.add(instruction2);

        ImageIcon viewI=new ImageIcon(ImageIO.read(new File(currPath+"/Images/viewLevel.png")));
        view=new JButton(viewI);
        view.addMouseListener(this);
        view.setBorder(null);

        ImageIcon deleteI=new ImageIcon(ImageIO.read(new File(currPath+"/Images/deleteLevel.png")));
        delete=new JButton(deleteI);
        delete.addMouseListener(this);
        delete.setBorder(null);

        b.add(view);
        b.add(Box.createHorizontalStrut(30));
        b.add(delete);
        super.add(b);

        //creating two more boxes for other options
        Box b2= Box.createHorizontalBox();
        Box b3=Box.createHorizontalBox();
        JLabel instruction3=new JLabel("Or:  ");
        instruction3.setFont(new Font("Candara",Font.BOLD,30));
        instruction3.setBackground(null);
        ImageIcon n=new ImageIcon(ImageIO.read(new File(currPath+"/Images/newLevel.png")));
        add=new JButton(n);
        add.addMouseListener(this);
        add.setBorder(null);
        b2.add(instruction3);
        b3.add(add);
        super.add(b2);
        super.add(b3);
        super.add(Box.createGlue());

        //implementing the action listener
        levelList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                target=levelList.getSelectedIndex(); // saving the level to be deleted
            }
        });


    }

    /**
     * The function redirects the users to the wanted operation/screen
     * @param e - mouse event
     */
    public void mouseClicked(MouseEvent e) {

        if (e.getSource().equals(view)){ //showing the game window
            Level level=new Level(levels[target]);
            try {
                getParent().add(new GameWindow(level));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if(e.getSource().equals(delete)){ //showing confirmation message and deleting the level
            JFrame window=new JFrame();
            int choice=JOptionPane.showConfirmDialog(window,"Are you sure you wish to delete this level?","Warning",JOptionPane.YES_NO_OPTION);
            if(choice==0)
                deleteLevel();
        }

        if(e.getSource().equals(goToMenu)) // return to the main menu
            super.mouseClicked(e);

        if(e.getSource().equals(add)) {//go to EditWindow
            this.removeAll();
            setVisible(false);
            try {
                getParent().add(new EditWindow());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    /**
     * The function deletes the level requested by the user, and reloads the edit screen
     */
    private void deleteLevel(){
        levels[target].delete(); // removing the level file from the Level folder
        JOptionPane.showMessageDialog(new JFrame(),"The level was deleted","Confirmation",JOptionPane.INFORMATION_MESSAGE);
        setVisible(false);
        getParent().add(new LevelEditor());


    }




}
