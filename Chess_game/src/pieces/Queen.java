package pieces;

import utils.PieceColor;
import utils.Position;

public class Queen extends Piece {
    public Queen(PieceColor color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board) {
        if (!isWithinBounds(newPosition)) {
            return false;
        }

        int rowDiff = Math.abs(newPosition.getRow() - position.getRow());
        int colDiff = Math.abs(newPosition.getColumn() - position.getColumn());

        if ((rowDiff == 0 && colDiff > 0) || (colDiff == 0 && rowDiff > 0) || (rowDiff == colDiff && rowDiff > 0)) {
            if (isPathClear(position, newPosition, board)) {
                Piece targetPiece = board[newPosition.getRow()][newPosition.getColumn()];
                return targetPiece == null || targetPiece.getColor() != this.color;
            }
        }

        return false;
    }
}