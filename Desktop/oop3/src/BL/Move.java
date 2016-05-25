package BL;

public class Move {
    private Piece _piece;
    private Position _start;
    private Position _end;

    public Move(Piece p,Position start, Position end)
    {
        _piece=p;
        _start=start;
        _end=end;
    }
    public Move(Move m)
    {
        this._piece=m._piece;
        this._start=m._start;
        this._end=m._end;
    }
    public Position GetStart()
    {
        return new Position(_start);
    }
    public Position GetEnd()
    {
        return new Position(_end);
    }
    public Piece getPiece()
    {
        return _piece;
    }
}
