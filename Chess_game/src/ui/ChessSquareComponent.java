package ui;

import javax.swing.*;
import java.awt.*;

public class ChessSquareComponent extends JPanel {
    private final int row;
    private final int col;
    private final Color color;
    private final int size;
    private ImageIcon pieceImage;
    private final ImageIcon markerIcon;
    private boolean showMoveMarker;

    public ChessSquareComponent(int row, int col, Color color, int size, ImageIcon markerIcon) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.size = size;
        this.markerIcon = markerIcon;
        this.showMoveMarker = false;
        setPreferredSize(new Dimension(size, size));
        setLayout(new BorderLayout());
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPieceImage(ImageIcon icon) {
        if (pieceImage != null) {
            remove(findComponentByName("pieceLabel"));
        }
        pieceImage = icon;
        if (icon != null) {
            JLabel pieceLabel = new JLabel(icon);
            pieceLabel.setName("pieceLabel");
            add(pieceLabel, BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }

    public void clearPieceImage() {
        if (pieceImage != null) {
            remove(findComponentByName("pieceLabel"));
            pieceImage = null;
        }
        revalidate();
        repaint();
    }

    public void setMoveMarker(boolean show) {
        showMoveMarker = show;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, size, size);

        if (showMoveMarker && markerIcon != null) {
            markerIcon.paintIcon(this, g, (size - markerIcon.getIconWidth()) / 2, (size - markerIcon.getIconHeight()) / 2);
        }
    }

    private Component findComponentByName(String name) {
        for (Component component : getComponents()) {
            if (name.equals(component.getName())) {
                return component;
            }
        }
        return null;
    }
}