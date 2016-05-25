package BL;

public class Piece {
    private Position _start;
    private int _size;
    private Role _role;
    private Orientation _orientation;
    //region constractors
    public  Piece(String s)
    {
        String[] t= s.split("_");
        _start=new Position(t[0]);
        _size= Integer.parseInt(t[1]);
        _role=Role.valueOf(t[2]);
        _orientation=Orientation.valueOf(t[3]);

    }

    public Piece(Position start,int size, Role role, Orientation orientation)
    {
        _start=new Position(start);
        _size=size;
        _role=role;
        _orientation=orientation;
    }
    public Piece(Piece p)
    {
        this._start=new Position(p._start);
        this._role=p._role;
        this._size=p._size;
        this._orientation=p._orientation;
    }
    //endregion
    //region move
    public boolean move(Position start)
    {
        _start=new Position(start);
        return true;
    }

    /**
     * gets other piece, checks if it has a connected position with current piece
     * @param p piece to check against
     * @return true if is collaiding, false otherwise
     */
    public boolean areCollaiding(Piece p)
    {
        boolean ans=false;
        Position otherStart=p.get_start();
        Position otherEnd=p.get_end();
        Position thisStart=this.get_start();
        Position thisEnd=this.get_end();

        boolean xCollaid=isInRange(thisStart.getX(),thisEnd.getX(),otherStart.getX(),otherEnd.getX());
        boolean yCollaid=isInRange(thisStart.getY(),thisEnd.getY(),otherStart.getY(),otherEnd.getY());
        if (xCollaid&&yCollaid)
            ans=true;
        return ans;
    }
    /**
     * Checks to ranges of integers have correlation
     *
     * @param range1Start the start of the first range
     * @param range1End   the end of the first range
     * @param range2Start the start of the second range
     * @param range2End   the end of the second range
     * @return true if the two ranges has correlation, false otherwise
     */
    private boolean isInRange(int range1Start, int range1End, int range2Start, int range2End) {
        boolean ans = false;
        if (range2Start >= range1Start & range2Start <= range1End)
            ans = true;
        if (range2End >= range1Start & range2Start <= range1Start)
            ans = true;
        return ans;
    }
    //endregion

    public String toString()
    {
        return _start+"_"+_size+"_"+_role+"_"+_orientation;
    }
    //region gettrs
    public Role get_role()
    {
        return _role;
    }
    public Orientation get_orientation()
    {
        return _orientation;
    }
    public int get_size()
    {
        return _size;
    }
    public Position get_start()
    {
        return new Position(_start);
    }
    public Position get_end()
    {
        Position ans;
        if(this._orientation==Orientation.HORIZONTAL)
        {
            return new Position(_start.getX()+_size-1,_start.getY());
        }
        else
            return new Position(_start.getX(),_start.getY()+_size-1);
    }
    public void set_role(Role r)
    {
        this._role=r;
    }
    //endregion
    public boolean equals(Piece p) {
        boolean ans=true;
        ans=this._start.equals(p._start)&&this.get_size()==p._size&&this._role==p._role&&this._orientation==p._orientation;
        return ans;
    }
}

