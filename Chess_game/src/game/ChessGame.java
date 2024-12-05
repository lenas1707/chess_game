package game;

import pieces.*;
import utils.PieceColor;
import utils.Position;

import java.util.List;
import java.util.ArrayList;

public class ChessGame {
    private ChessBoard board;
    private boolean whiteTurn = true;
    private Position selectedPosition;
    private Position lastMovedPawn; // For en passant

    public ChessGame() {
        this.board = new ChessBoard();
    }

    public ChessBoard getBoard() {
        return this.board;
    }

    public void resetGame() {
        this.board = new ChessBoard();
        this.whiteTurn = true;
        this.selectedPosition = null;
        this.lastMovedPawn = null;
    }

    public PieceColor getCurrentPlayerColor() {
        return whiteTurn ? PieceColor.WHITE : PieceColor.BLACK;
    }

    public boolean isPieceSelected() {
        return selectedPosition != null;
    }

    public boolean handleSquareSelection(int row, int col) {
        System.out.println("Handling square selection: (" + row + ", " + col + ")");
        if (selectedPosition == null) {
            Piece selectedPiece = board.getPiece(row, col);
            if (selectedPiece != null && selectedPiece.getColor() == getCurrentPlayerColor()) {
                selectedPosition = new Position(row, col);
                System.out.println("Selected piece: " + selectedPiece.getClass().getSimpleName());
                return false;
            }
        } else {
            boolean moveMade = makeMove(selectedPosition, new Position(row, col));
            System.out.println("Move made: " + moveMade);
            selectedPosition = null;
            return moveMade;
        }
        return false;
    }

    public boolean makeMove(Position start, Position end) {
        System.out.println("Attempting move from " + start + " to " + end);
        Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());
        if (movingPiece == null || movingPiece.getColor() != getCurrentPlayerColor()) {
            System.out.println("Invalid move: No piece or wrong color");
            return false;
        }

        List<Position> legalMoves = getLegalMovesForPieceAt(start);
        System.out.println("Legal moves: " + legalMoves);
        if (!legalMoves.contains(end)) {
            System.out.println("Invalid move: Not in legal moves");
            return false;
        }

        // Check for castling
        if (movingPiece instanceof King && Math.abs(start.getColumn() - end.getColumn()) == 2) {
            performCastling(start, end);
        } else {
            // Check for en passant
            if (movingPiece instanceof Pawn && end.getColumn() != start.getColumn() && board.getPiece(end.getRow(), end.getColumn()) == null) {
                board.setPiece(lastMovedPawn.getRow(), lastMovedPawn.getColumn(), null);
            }

            board.movePiece(start, end);
        }

        // Update lastMovedPawn for en passant
        if (movingPiece instanceof Pawn && Math.abs(start.getRow() - end.getRow()) == 2) {
            lastMovedPawn = end;
        } else {
            lastMovedPawn = null;
        }

        whiteTurn = !whiteTurn;
        System.out.println("Move completed. New turn: " + (whiteTurn ? "White" : "Black"));
        return true;
    }

    private void performCastling(Position kingStart, Position kingEnd) {
        int rookStartCol = kingEnd.getColumn() > kingStart.getColumn() ? 7 : 0;
        int rookEndCol = kingEnd.getColumn() > kingStart.getColumn() ? 5 : 3;

        board.movePiece(kingStart, kingEnd);
        board.movePiece(new Position(kingStart.getRow(), rookStartCol), new Position(kingStart.getRow(), rookEndCol));
    }

    public boolean isInCheck(PieceColor kingColor) {
        Position kingPosition = findKingPosition(kingColor);
        if (kingPosition == null) {
            return false; // If king is not found, it can't be in check
        }
        for (int row = 0; row < board.getBoard().length; row++) {
            for (int col = 0; col < board.getBoard()[row].length; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() != kingColor) {
                    if (piece.isValidMove(kingPosition, board.getBoard())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Position findKingPosition(PieceColor color) {
        for (int row = 0; row < board.getBoard().length; row++) {
            for (int col = 0; col < board.getBoard()[row].length; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof King && piece.getColor() == color) {
                    return new Position(row, col);
                }
            }
        }
        return null; // Return null instead of throwing an exception
    }

    public boolean isCheckMate(PieceColor kingColor) {
        if (!isInCheck(kingColor)) {
            return false;
        }

        return getAllLegalMoves(kingColor).isEmpty();
    }

    public boolean isStalemate(PieceColor color) {
        return !isInCheck(color) && getAllLegalMoves(color).isEmpty();
    }

    private List<Position> getAllLegalMoves(PieceColor color) {
        List<Position> allLegalMoves = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() == color) {
                    allLegalMoves.addAll(getLegalMovesForPieceAt(new Position(row, col)));
                }
            }
        }
        return allLegalMoves;
    }

    public List<Position> getLegalMovesForPieceAt(Position position) {
        Piece selectedPiece = board.getPiece(position.getRow(), position.getColumn());
        if (selectedPiece == null)
            return new ArrayList<>();

        List<Position> potentialMoves = new ArrayList<>();
        switch (selectedPiece.getClass().getSimpleName()) {
            case "Pawn":
                addPawnMoves(position, selectedPiece.getColor(), potentialMoves);
                break;
            case "Rook":
                addLineMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }, potentialMoves);
                break;
            case "Knight":
                addSingleMoves(position, new int[][] { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { -1, 2 },
                        { 1, -2 }, { -1, -2 } }, potentialMoves);
                break;
            case "Bishop":
                addLineMoves(position, new int[][] { { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } }, potentialMoves);
                break;
            case "Queen":
                addLineMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { -1, -1 },
                        { 1, -1 }, { -1, 1 } }, potentialMoves);
                break;
            case "King":
                addSingleMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { -1, -1 },
                        { 1, -1 }, { -1, 1 } }, potentialMoves);
                addCastlingMoves(position, selectedPiece.getColor(), potentialMoves);
                break;
        }

        // Filter out moves that would leave the king in check
        List<Position> legalMoves = new ArrayList<>();
        for (Position move : potentialMoves) {
            if (!wouldBeInCheckAfterMove(selectedPiece.getColor(), position, move)) {
                legalMoves.add(move);
            }
        }

        return legalMoves;
    }

    private boolean isPositionOnBoard(Position position) {
        return position.getRow() >= 0 && position.getRow() < board.getBoard().length &&
                position.getColumn() >= 0 && position.getColumn() < board.getBoard()[0].length;
    }

    private boolean wouldBeInCheckAfterMove(PieceColor kingColor, Position from, Position to) {
        Piece temp = board.getPiece(to.getRow(), to.getColumn());
        board.setPiece(to.getRow(), to.getColumn(), board.getPiece(from.getRow(), from.getColumn()));
        board.setPiece(from.getRow(), from.getColumn(), null);

        boolean inCheck = isInCheck(kingColor);

        board.setPiece(from.getRow(), from.getColumn(), board.getPiece(to.getRow(), to.getColumn()));
        board.setPiece(to.getRow(), to.getColumn(), temp);

        return inCheck;
    }

    private void addLineMoves(Position position, int[][] directions, List<Position> legalMoves) {
        for (int[] d : directions) {
            Position newPos = new Position(position.getRow() + d[0], position.getColumn() + d[1]);
            while (isPositionOnBoard(newPos)) {
                if (board.getPiece(newPos.getRow(), newPos.getColumn()) == null) {
                    legalMoves.add(new Position(newPos.getRow(), newPos.getColumn()));
                    newPos = new Position(newPos.getRow() + d[0], newPos.getColumn() + d[1]);
                } else {
                    if (board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board
                            .getPiece(position.getRow(), position.getColumn()).getColor()) {
                        legalMoves.add(newPos);
                    }
                    break;
                }
            }
        }
    }

    private void addSingleMoves(Position position, int[][] moves, List<Position> legalMoves) {
        for (int[] move : moves) {
            Position newPos = new Position(position.getRow() + move[0], position.getColumn() + move[1]);
            if (isPositionOnBoard(newPos) && (board.getPiece(newPos.getRow(), newPos.getColumn()) == null ||
                    board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board
                            .getPiece(position.getRow(), position.getColumn()).getColor())) {
                legalMoves.add(newPos);
            }
        }
    }

    private void addPawnMoves(Position position, PieceColor color, List<Position> legalMoves) {
        int direction = color == PieceColor.WHITE ? -1 : 1;
        Position newPos = new Position(position.getRow() + direction, position.getColumn());
        if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null) {
            legalMoves.add(newPos);
        }

        if ((color == PieceColor.WHITE && position.getRow() == 6)
                || (color == PieceColor.BLACK && position.getRow() == 1)) {
            newPos = new Position(position.getRow() + 2 * direction, position.getColumn());
            Position intermediatePos = new Position(position.getRow() + direction, position.getColumn());
            if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null
                    && board.getPiece(intermediatePos.getRow(), intermediatePos.getColumn()) == null) {
                legalMoves.add(newPos);
            }
        }

        int[] captureCols = { position.getColumn() - 1, position.getColumn() + 1 };
        for (int col : captureCols) {
            newPos = new Position(position.getRow() + direction, col);
            if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) != null &&
                    board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != color) {
                legalMoves.add(newPos);
            }
        }

        // En passant
        if (lastMovedPawn != null) {
            if (position.getRow() == lastMovedPawn.getRow() && Math.abs(position.getColumn() - lastMovedPawn.getColumn()) == 1) {
                newPos = new Position(position.getRow() + direction, lastMovedPawn.getColumn());
                legalMoves.add(newPos);
            }
        }
    }

    private void addCastlingMoves(Position kingPosition, PieceColor color, List<Position> moves) {
        if (isInCheck(color)) return;

        int row = kingPosition.getRow();
        if ((color == PieceColor.WHITE && row != 7) || (color == PieceColor.BLACK && row != 0)) return;

        // Kingside castling
        if (canCastle(kingPosition, true)) {
            moves.add(new Position(row, 6));
        }

        // Queenside castling
        if (canCastle(kingPosition, false)) {
            moves.add(new Position(row, 2));
        }
    }

    private boolean canCastle(Position kingPosition, boolean kingSide) {
        int row = kingPosition.getRow();
        int rookCol = kingSide ? 7 : 0;
        int step = kingSide ? 1 : -1;

        Piece rook = board.getPiece(row, rookCol);
        if (!(rook instanceof Rook) || rook.hasMoved()) return false;

        for (int col = kingPosition.getColumn() + step; col != rookCol; col += step) {
            if (board.getPiece(row, col) != null) return false;
            if (isSquareUnderAttack(new Position(row, col), getCurrentPlayerColor())) return false;
        }

        return true;
    }

    private boolean isSquareUnderAttack(Position position, PieceColor defendingColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() != defendingColor) {
                    if (piece.isValidMove(position, board.getBoard())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}