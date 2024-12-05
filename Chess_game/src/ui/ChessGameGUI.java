package ui;

import game.ChessBoard;
import game.ChessGame;
import pieces.*;
import utils.PieceColor;
import utils.PlayerManager;
import utils.Position;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ChessGameGUI extends JFrame {
    private final ChessSquareComponent[][] squares = new ChessSquareComponent[8][8];
    private final ChessGame game = new ChessGame();
    private final Map<Class<? extends Piece>, Map<PieceColor, ImageIcon>> pieceImageMap = new HashMap<>();
    private static final int SQUARE_SIZE = 64;
    private static final Color LIGHT_SQUARE_COLOR = new Color(255, 255, 255);
    private static final Color DARK_SQUARE_COLOR = new Color(0, 0, 0);
    private ImageIcon whiteMarkerIcon;
    private ImageIcon blackMarkerIcon;
    private String player1;
    private String player2;

    public ChessGameGUI(String[] players) {
        this.player1 = players[0];
        this.player2 = players[1];
        setTitle("Chess Game - " + player1 + " vs " + player2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        PlayerManager.addPlayers(player1, player2);

        loadPieceImages();
        loadMarkerImages();
        initializeBoard();
        addGameResetOption();
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void loadPieceImages() {
        String[] pieceNames = {"pawn", "rook", "knight", "bishop", "queen", "king"};
        Class<?>[] pieceClasses = {Pawn.class, Rook.class, Knight.class, Bishop.class, Queen.class, King.class};
        PieceColor[] colors = {PieceColor.WHITE, PieceColor.BLACK};

        for (int i = 0; i < pieceNames.length; i++) {
            Map<PieceColor, ImageIcon> colorMap = new HashMap<>();
            for (PieceColor color : colors) {
                String imagePath = "/images/" + color.toString().toLowerCase() + "_" + pieceNames[i] + ".png";
                try (InputStream is = getClass().getResourceAsStream(imagePath)) {
                    if (is == null) {
                        System.err.println("Could not find image: " + imagePath);
                        continue;
                    }
                    Image image = ImageIO.read(is);
                    Image scaledImage = image.getScaledInstance(SQUARE_SIZE - 10, SQUARE_SIZE - 10, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    colorMap.put(color, scaledIcon);
                } catch (IOException e) {
                    System.err.println("Error loading image: " + imagePath);
                    e.printStackTrace();
                }
            }
            pieceImageMap.put((Class<? extends Piece>) pieceClasses[i], colorMap);
        }
    }

    private void loadMarkerImages() {
        try (InputStream whiteIs = getClass().getResourceAsStream("/images/white_move_overlay.png");
             InputStream blackIs = getClass().getResourceAsStream("/images/black_move_overlay.png")) {
            if (whiteIs == null || blackIs == null) {
                System.err.println("Could not find marker images");
                return;
            }
            Image whiteImage = ImageIO.read(whiteIs);
            Image blackImage = ImageIO.read(blackIs);
            Image scaledWhiteImage = whiteImage.getScaledInstance(SQUARE_SIZE / 2, SQUARE_SIZE / 2, Image.SCALE_SMOOTH);
            Image scaledBlackImage = blackImage.getScaledInstance(SQUARE_SIZE / 2, SQUARE_SIZE / 2, Image.SCALE_SMOOTH);
            whiteMarkerIcon = new ImageIcon(scaledWhiteImage);
            blackMarkerIcon = new ImageIcon(scaledBlackImage);
        } catch (IOException e) {
            System.err.println("Error loading marker images");
            e.printStackTrace();
        }
    }

    private void initializeBoard() {
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Color squareColor = (row + col) % 2 == 0 ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR;
                ImageIcon markerIcon = (row + col) % 2 == 0 ? blackMarkerIcon : whiteMarkerIcon;
                ChessSquareComponent square = new ChessSquareComponent(row, col, squareColor, SQUARE_SIZE, markerIcon);
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(square.getRow(), square.getCol());
                    }
                });
                squares[row][col] = square;
                boardPanel.add(square);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
        refreshBoard();
    }

    private void refreshBoard() {
        System.out.println("Refreshing board");
        ChessBoard board = game.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    ImageIcon icon = pieceImageMap.get(piece.getClass()).get(piece.getColor());
                    squares[row][col].setPieceImage(icon);
                    System.out.println("Set piece at (" + row + ", " + col + "): " + piece.getClass().getSimpleName());
                } else {
                    squares[row][col].clearPieceImage();
                    System.out.println("Cleared piece at (" + row + ", " + col + ")");
                }
                squares[row][col].setMoveMarker(false);
            }
        }
        revalidate();
        repaint();
        System.out.println("Board refresh complete");
    }

    private void handleSquareClick(int row, int col) {
        System.out.println("Square clicked: (" + row + ", " + col + ")");
        boolean moveResult = game.handleSquareSelection(row, col);
        System.out.println("Move result: " + moveResult);
        clearMoveMarkers();
        if (moveResult) {
            System.out.println("Refreshing board after move");
            refreshBoard();
            checkGameState();
            checkGameOver();
        } else if (game.isPieceSelected()) {
            System.out.println("Showing legal moves");
            showLegalMoves(new Position(row, col));
        }
    }

    private void checkGameState() {
        PieceColor currentPlayer = game.getCurrentPlayerColor();
        boolean inCheck = game.isInCheck(currentPlayer);

        if (inCheck) {
            JOptionPane.showMessageDialog(this, currentPlayer + " is in check!");
        }
    }

    private void showLegalMoves(Position position) {
        List<Position> legalMoves = game.getLegalMovesForPieceAt(position);
        for (Position move : legalMoves) {
            squares[move.getRow()][move.getColumn()].setMoveMarker(true);
        }
    }

    private void clearMoveMarkers() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setMoveMarker(false);
            }
        }
    }

    private void addGameResetOption() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(e -> resetGame());
        gameMenu.add(resetItem);

        JMenuItem showPlayersItem = new JMenuItem("Show Players");
        showPlayersItem.addActionListener(e -> {
            List<String> allPlayers = PlayerManager.getPlayers();
            StringBuilder playersList = new StringBuilder("Players who played:\n");
            for (String player : allPlayers) {
                playersList.append(player).append("\n");
            }
            JOptionPane.showMessageDialog(this, playersList.toString(), "Player List", JOptionPane.INFORMATION_MESSAGE);
        });
        gameMenu.add(showPlayersItem);

        JMenuItem backToMenuItem = new JMenuItem("Back to Menu");
        backToMenuItem.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new MenuScreen());
        });
        gameMenu.add(backToMenuItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> closeGame());
        gameMenu.add(exitItem);

        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    private void closeGame() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the game?",
                "Exit Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void resetGame() {
        game.resetGame();
        refreshBoard();
    }

    private void checkGameOver() {
        PieceColor currentPlayerColor =  game.getCurrentPlayerColor();
        if (game.isCheckMate(currentPlayerColor)) {
            PieceColor winnerColor = (currentPlayerColor == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
            String winner = (winnerColor == PieceColor.WHITE) ? player1 : player2;
            JOptionPane.showMessageDialog(this, winner + " wins by checkmate!");
            PlayerManager.recordGameResult(winner, (winner.equals(player1) ? player2 : player1));
            resetGame();
        } else if (game.isStalemate(currentPlayerColor)) {
            JOptionPane.showMessageDialog(this, "The game is a draw by stalemate!");
            PlayerManager.recordGameResult(player1, player2, true);
            resetGame();
        }
    }
}