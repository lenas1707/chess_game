package pieces;

import utils.PieceColor;
import utils.Position;

public class Knight extends Piece {
    public Knight(PieceColor color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board) {
        if (!isWithinBounds(newPosition)) {
            return false;
        }

        int rowDiff = Math.abs(newPosition.getRow() - position.getRow());
        int colDiff = Math.abs(newPosition.getColumn() - position.getColumn());

        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
            Piece targetPiece = board[newPosition.getRow()][newPosition.getColumn()];
            return targetPiece == null || targetPiece.getColor() != this.color;
        }

        return false;
    }
}