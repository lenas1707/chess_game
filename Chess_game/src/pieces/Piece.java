package pieces;

import utils.PieceColor;
import utils.Position;

public abstract class Piece {
    protected Position position;
    protected PieceColor color;
    protected boolean hasMoved;

    public Piece(PieceColor color, Position position) {
        this.color = color;
        this.position = position;
        this.hasMoved = false;
    }

    public PieceColor getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(boolean moved) {
        this.hasMoved = moved;
    }

    public abstract boolean isValidMove(Position newPosition, Piece[][] board);

    protected boolean isWithinBounds(Position position) {
        return position.getRow() >= 0 && position.getRow() < 8 &&
                position.getColumn() >= 0 && position.getColumn() < 8;
    }

    protected boolean isPathClear(Position start, Position end, Piece[][] board) {
        int rowStep = Integer.compare(end.getRow(), start.getRow());
        int colStep = Integer.compare(end.getColumn(), start.getColumn());

        Position current = new Position(start.getRow() + rowStep, start.getColumn() + colStep);
        while (!current.equals(end)) {
            if (board[current.getRow()][current.getColumn()] != null) {
                return false;
            }
            current = new Position(current.getRow() + rowStep, current.getColumn() + colStep);
        }
        return true;
    }
}