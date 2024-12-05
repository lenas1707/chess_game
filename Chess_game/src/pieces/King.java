package pieces;

import utils.PieceColor;
import utils.Position;

public class King extends Piece {
    public King(PieceColor color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board) {
        if (!isWithinBounds(newPosition)) {
            return false;
        }

        int rowDiff = Math.abs(newPosition.getRow() - position.getRow());
        int colDiff = Math.abs(newPosition.getColumn() - position.getColumn());

        if (rowDiff <= 1 && colDiff <= 1) {
            Piece targetPiece = board[newPosition.getRow()][newPosition.getColumn()];
            return targetPiece == null || targetPiece.getColor() != this.color;
        }

        // Castling logic is handled in the ChessGame class
        return false;
    }
}