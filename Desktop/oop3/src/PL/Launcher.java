package PL;

import BL.Orientation;
import BL.Piece;
import BL.Position;
import BL.Role;
import BL.*;

import java.util.Vector;

public class Launcher {
    public static void main(String[] args)
    {
        /*Piece p1= new Piece(new Position(2,2),2, Role.Target, Orientation.HORIZONTAL);
        Piece p2= new Piece(new Position(3,3),3, Role.Reg,Orientation.VERTICAL);
        Piece p3= new Piece(new Position(4,2),2, Role.Reg,Orientation.VERTICAL);
        Piece p4= new Piece(new Position(0,1),2, Role.Reg,Orientation.VERTICAL);
        Vector<Piece> vp= new Vector<Piece>(4);
        vp.add(p1);
        vp.add(p2);
        vp.add(p3);
        vp.add(p4);
        BL.Level l=new BL.Level(vp);
        String level=l.toString();
        System.out.println(level);

        GameWindow gw= new GameWindow(l);
        EditWindow ew=new EditWindow();*/

      Game game=new Game();

    }
}
