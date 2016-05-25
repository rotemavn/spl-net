package BL;

public class Position {
    private int _x;
    private int _y;
    //region constractors
    public Position (String s)
    {
        String[] pos=s.split("\\."); //error with this line - doesn't perform split
        this._x=Integer.parseInt(pos[0]);
        this._y=Integer.parseInt(pos[1]);
    }
    public Position (int x, int y)
    {
        this._x=x;
        this._y=y;
    }
    public Position (Position p)
    {
        this._x=p._x;
        this._y=p._y;
    }
    //endregion
    //region getters
    public int getX()
    {
        return _x;
    }
    public int getY()
    {
        return _y;
    }
    //endregion
    public String toString()
    {
        return _x+"."+_y;
    }
    public boolean isValid()
    {
        return _x>=0&_y>=0&_x<6&_y<6;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Position)
        {   Position t=(Position)obj;
            return this._y==t._y&&this._x==t._x;
        }
        return false;
    }
    //region move
    public Position moveLeft(){
        return new Position(_x-1,_y);
    }
    public Position moveRight(){
        return new Position(_x+1,_y);
    }
    public Position moveUp(){
        return new Position(_x,_y-1);
    }
    public Position moveDown() {
        return new Position(_x,_y+1);
    }
    //endregion
}