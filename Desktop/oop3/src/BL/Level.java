package BL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Created by liorbass on 17/05/2016.
 */
public class Level {

    private final int SIZE = 6;
    Vector<Piece> _vp;
    String _bestTime;
    Stack<Move> _sm;
    final Position _finish=new Position(2,3);

    //region constractors
    public Level() {
        _vp = new Vector<Piece>();
        _bestTime = "00-00-00";
        _sm = new Stack<Move>();
        _vp = new Vector<Piece>();
    }

    public Level(Vector<Piece> vp) {
        _vp = new Vector<Piece>(vp.size());
        _bestTime = "00-00-00";
        _sm = new Stack<Move>();
        for (Piece p : vp) {
            this.addPiece(new Piece(p));
        }
    }

    public Level(Level l) {
        _vp = new Vector<Piece>();
        this._bestTime = l._bestTime;
        this._sm = l._sm;
        for (Piece p : l._vp) {
            this.addPiece(new Piece(p));
        }
    }

    /**
     *
     * @param levelFile - a file which contains all the details of the level.
     */
    public Level(File levelFile){

        try {
            BufferedReader bf = new BufferedReader(new FileReader(levelFile));
            String s = bf.readLine();

            int count=0;
            while(!s.contains("bt_")){
                count++;
                s=bf.readLine();
            }
            bf = new BufferedReader(new FileReader(levelFile));
            String s2 = bf.readLine();
            _vp=new Vector<Piece>(count);
            while(!s2.contains("bt_")){
                _vp.add(new Piece(s2));
                s2=bf.readLine();
            }
            if(s2.contains("_bt")){
                _bestTime=s2.substring(3);
            }
            _sm = new Stack<Move>();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
    //endregion
    //region moves

    /**
     * Moves a Piece in the board
     *
     * @param pic piece to move
     * @param pos the new position of the piece
     * @return true if succeed, false if impossible move
     */
    public boolean move(Piece pic, Position pos) {
        Move m = new Move(pic, pic.get_start(), pos);
        if (isValidMove(m)) {
            pic.move(pos);
            _sm.push(m);
            return true;
        }
        return false;
    }

    /**
     * Revert the level to the state befor the move
     *
     * @return true if succeed
     */
    public Move undoMove() {
        boolean ans = true;
        if(!_sm.empty()) {
            Move m = _sm.pop();
            m.getPiece().move(m.GetStart());
            return m;
        }
        return null;
    }
    //endregion

    /**
     * Checks if given move is valid
     *
     * @param m move to check
     * @return true if move is valid, false otherwise
     */
    public boolean isValidMove(Move m) {
        boolean ans = false;
        if (m.GetEnd().isValid()) {
            if (m.getPiece().get_orientation() == Orientation.HORIZONTAL) {
                if (m.GetEnd().getX() + m.getPiece().get_size() <= SIZE) {
                    if (m.GetStart().getY() == m.GetEnd().getY())
                        ans = true;
                }


            } else {
                if (m.GetEnd().getY() + m.getPiece().get_size() <= SIZE) {
                    if (m.GetStart().getX() == m.GetEnd().getX())
                        ans = true;
                }
            }
            //start setup for conflicts check
            //Vector<Piece> tvp=new Vector<Piece>(_vp);
            //tvp.remove(m.getPiece());
            //Level tlvl= new Level(tvp);
            //Piece tp=new Piece(m.getPiece());
            //tp.move(m.GetEnd());
            //end setup for conflict check
            //ans=ans&tlvl.AddPiece(tp); // check for pieces conflicts
            ans = ans & canPlace(m.getPiece(), m.GetEnd());
        }
        return ans;
    }

    /**
     * Adds piece to current Level
     *
     * @param p the piece to add
     * @return true if piece was added successfully, false otherwise
     */
    public boolean addPiece(Piece p) {
        if (canPlace(p, p.get_start())) {
            _vp.add(p);
            return true;
        }
        return false;
    }

    /**
     * Checks if it's possible to add or move a piece in the current level
     *
     * @param p the piece to check
     * @return true if will can place the pos, false otherwise
     */
    public boolean canPlace(Piece p, Position pos) {
        //create temp piece with the new position,


        Piece tempPiece = new Piece(pos, p.get_size(), p.get_role(), p.get_orientation());
            //check if it's colliding with any existing piece other than then original
        boolean ans = pos.isValid() &p.get_end().isValid();
        if(ans) {
            for (Piece o : _vp) {
                if (o != p) {
                    if (p.get_start().isValid() && p.get_end().isValid()) {
                        if (tempPiece.areCollaiding(o))
                            ans = false;
                    }
                }
            }
        }
        return ans;
    }

    /**
     * Checks if the current Level is valid
     *
     * @return
     */
    public boolean checkValid() {
        return false;
    }

    /**
     * Converts current Level to string
     *
     * @return
     */
    public String toString() {
        String ans = "";
        for (Piece p : _vp) {
            ans =ans+ p.toString() + "\n";
        }
        ans+="bt_"+_bestTime+"\n";

        return ans;
    }

    /**
     * Saves the Level to txt file
     *
     * @return
     */
    public boolean Save() {
        return false;
    }

    /**
     * Geter of Pieces
     *
     * @return vector of peices
     */
    public Vector<Piece> get_Pieces() {
        return _vp;
    }

    /**
     * Searches for a piece in a position
     *
     * @param p position to look for
     * @return the piece which start equals to p, null if there is no such piece
     */
    public Piece getPieceByPosition(Position p) {
        for (Piece lp : _vp) {
            if (lp.get_start().equals(p)) {
                return lp;
            }
        }
        return null;
    }
    public Position get_finish()
    {
        return new Position(_finish);
    }

    /**
     * Removes a piece from current level
     * @param p piece to remove
     * @return true if succeded, false otherwise
     */
    public boolean removePiece(Piece p)
    {
        return _vp.removeElement(p);
    }

    public String get_bestTime(){
        return this._bestTime;
    }

    public void set_bestTime(String bt){
        _bestTime=bt;
    }
}
