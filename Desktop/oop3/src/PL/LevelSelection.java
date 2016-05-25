package PL;
import BL.Level;
import PL.PlayLevel.GameWindow;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.*;
import java.nio.file.Paths;


class LevelSelection extends LevelActions implements MouseListener {

    private final File[] levels;
    protected final File levelsFolder;

    //constructor
    public LevelSelection(){
        super(Paths.get(".").toAbsolutePath().normalize().toString()+"/Images/background2.png");
        String currPath= Paths.get(".").toAbsolutePath().normalize().toString();
        levelsFolder=new File(currPath+"/Levels");
        levels=levelsFolder.listFiles();
        setTabel();
    }

    /**
     * The function creates and places a table with the available levels to play
     */
    private void setTabel(){
        String[] columnsNames={"Level Number","Best Time"};
        Object[][] data=new Object[levels.length][2];
        int i=1;
        for(File f:levels) { //iterating over all the level files in folder
            try {
                BufferedReader bf = new BufferedReader(new FileReader(f));
                String s = bf.readLine();
                while (!s.contains("bt_")) // as long there is no "bt_", count the number of pieces
                    s = bf.readLine();
                s = s.substring(3);
                data[i-1][0] = i;
                data[i-1][1] = s;
                i++;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        DefaultTableModel model=new DefaultTableModel(data,columnsNames);
        final JTable levelTable=new JTable(model);
        JScrollPane sp=new JScrollPane(levelTable); // setting the table in a scroll pane
        sp.setColumnHeaderView(levelTable.getTableHeader());
        levelTable.getTableHeader().setFont(new Font("Candara",Font.BOLD,24));
        levelTable.getTableHeader().setBackground(Color.WHITE);
        levelTable.setPreferredScrollableViewportSize(new Dimension(300, 120));

        //creating a box for the table
        Box b=Box.createHorizontalBox();
        super.add(Box.createGlue());
        b.setMaximumSize(new Dimension(500,500));
        b.add(sp);
        super.add(b);
        super.add(Box.createGlue());

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );

        //setting cells size
        for(int y=0; y<columnsNames.length;y++){
            levelTable.getColumnModel().getColumn(y).setPreferredWidth(70);
            levelTable.getColumnModel().getColumn(y).setCellRenderer(centerRenderer);
        }

        //table design
        levelTable.setBackground(Color.WHITE);
        levelTable.setFont(new Font("Candara",Font.PLAIN,24));
        levelTable.setRowHeight(40);
        levelTable.setGridColor(Color.gray);
        levelTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        levelTable.setSelectionBackground(Color.red);


        levelTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            //implementing the action listener
            public void valueChanged(ListSelectionEvent e) {
                int l=levelTable.getSelectedRow();
                try {
                    startPlaying(l);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     *
     * @param l - level number
     *          creating a new level and showing a new game window.
     */
    public void startPlaying(int l) throws IOException {
        Level level=new Level(levels[l]); // create a new level
        this.setVisible(false);
        getParent().add(new GameWindow(level)); // opening a new game window with the level.


    }
}
