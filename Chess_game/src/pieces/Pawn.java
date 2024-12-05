package pieces;

import utils.PieceColor;
import utils.Position;

public class Pawn extends Piece {
    private boolean hasMoved; // Atributo para rastrear se o peão já se moveu

    public Pawn(PieceColor color, Position position) {
        super(color, position);
        this.hasMoved = false; // Peão criado nunca foi movido
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board) {
        // Removendo os logs de depuração

        // Verificando se a nova posição está dentro dos limites do tabuleiro
        if (!isWithinBounds(newPosition)) {
            return false;
        }

        int rowDiff = newPosition.getRow() - position.getRow();
        int colDiff = newPosition.getColumn() - position.getColumn();

        // Determinando a direção com base na cor
        int forwardDirection = (color == PieceColor.WHITE) ? -1 : 1;

        // Movimento normal (uma casa para frente)
        if (colDiff == 0 && rowDiff == forwardDirection) {
            if (board[newPosition.getRow()][newPosition.getColumn()] == null) {
                return true;
            } else {
                return false;
            }
        }

        // Movimento inicial (duas casas para frente)
        if (!hasMoved && colDiff == 0 && rowDiff == 2 * forwardDirection) {
            int middleRow = position.getRow() + forwardDirection;
            if (board[newPosition.getRow()][newPosition.getColumn()] == null &&
                    board[middleRow][position.getColumn()] == null) {
                return true;
            } else {
                return false;
            }
        }

        // Movimento de captura (diagonal)
        if (Math.abs(colDiff) == 1 && rowDiff == forwardDirection) {
            Piece targetPiece = board[newPosition.getRow()][newPosition.getColumn()];
            if (targetPiece != null && targetPiece.getColor() != this.color) {
                return true;
            } else {
                return false;
            }
        }

        // Se nenhum dos casos for válido, o movimento é inválido
        return false;
    }



    public void move(Position newPosition) {
        this.position = newPosition;
        this.hasMoved = true; // Marca que o peão já foi movido
    }
}
