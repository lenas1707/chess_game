package game;

import pieces.*;
import utils.PieceColor;
import utils.Position;

public class ChessBoard {
    private Piece[][] board;

    public ChessBoard() {
        this.board = new Piece[8][8];
        setupPieces();
    }

    public Piece[][] getBoard() {
        return board;
    }

    public Piece getPiece(int row, int column) {
        if (isValidPosition(row, column)) {
            return board[row][column];
        }
        return null;
    }

    public void setPiece(int row, int column, Piece piece) {
        if (isValidPosition(row, column)) {
            board[row][column] = piece;
            if (piece != null) {
                piece.setPosition(new Position(row, column));
            }
        }
    }

    private void setupPieces() {
        // Rooks
        setPiece(0, 0, new Rook(PieceColor.BLACK, new Position(0, 0)));
        setPiece(0, 7, new Rook(PieceColor.BLACK, new Position(0, 7)));
        setPiece(7, 0, new Rook(PieceColor.WHITE, new Position(7, 0)));
        setPiece(7, 7, new Rook(PieceColor.WHITE, new Position(7, 7)));

        // Knights
        setPiece(0, 1, new Knight(PieceColor.BLACK, new Position(0, 1)));
        setPiece(0, 6, new Knight(PieceColor.BLACK, new Position(0, 6)));
        setPiece(7, 1, new Knight(PieceColor.WHITE, new Position(7, 1)));
        setPiece(7, 6, new Knight(PieceColor.WHITE, new Position(7, 6)));

        // Bishops
        setPiece(0, 2, new Bishop(PieceColor.BLACK, new Position(0, 2)));
        setPiece(0, 5, new Bishop(PieceColor.BLACK, new Position(0, 5)));
        setPiece(7, 2, new Bishop(PieceColor.WHITE, new Position(7, 2)));
        setPiece(7, 5, new Bishop(PieceColor.WHITE, new Position(7, 5)));

        // Queens
        setPiece(0, 3, new Queen(PieceColor.BLACK, new Position(0, 3)));
        setPiece(7, 3, new Queen(PieceColor.WHITE, new Position(7, 3)));

        // Kings
        setPiece(0, 4, new King(PieceColor.BLACK, new Position(0, 4)));
        setPiece(7, 4, new King(PieceColor.WHITE, new Position(7, 4)));

        // Pawns
        for (int i = 0; i < 8; i++) {
            setPiece(1, i, new Pawn(PieceColor.BLACK, new Position(1, i)));
            setPiece(6, i, new Pawn(PieceColor.WHITE, new Position(6, i)));
        }
    }

    public void movePiece(Position start, Position end) {
        Piece piece = getPiece(start.getRow(), start.getColumn());
        if (piece != null) {
            setPiece(end.getRow(), end.getColumn(), piece);
            setPiece(start.getRow(), start.getColumn(), null);
            piece.setMoved(true);
        }
    }

    public boolean isValidPosition(int row, int column) {
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }

}