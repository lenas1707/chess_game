package ui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MenuScreen extends JFrame {
    private static final int WINDOW_WIDTH = 650;
    private static final int WINDOW_HEIGHT = 450;
    private Image backgroundImage;

    public MenuScreen() {
        setTitle("Chess Game Menu");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        loadBackgroundImage();

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(240, 220, 180)); // Fallback color if image is missing
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add game logo
        JLabel logoLabel = createLogoLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(logoLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Add Play button
        JButton playButton = createImageButton("/images/play_button.png", "Play");
        playButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginScreen());
        });
        mainPanel.add(playButton);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add About button
        JButton aboutButton = createImageButton("/images/about_button.png", "About");
        aboutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(MenuScreen.this,
                    "Chess Game\nVersion 1.0\nDeveloped by Students",
                    "About Chess Game",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        mainPanel.add(aboutButton);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add Exit button
        JButton exitButton = createImageButton("/images/exit_button.png", "Exit");
        exitButton.addActionListener(e -> System.exit(0));
        mainPanel.add(exitButton);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/images/chess_background.jpg"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Could not load background image: " + e.getMessage());
        }
    }

    private JLabel createLogoLabel() {
        try {
            ImageIcon logoIcon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/images/logo_chess.png")));
            return new JLabel(logoIcon);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Could not load chess logo: " + e.getMessage());
            JLabel fallbackLabel = new JLabel("Chess Game");
            fallbackLabel.setFont(new Font("Arial", Font.BOLD, 36));
            return fallbackLabel;
        }
    }

    private JButton createImageButton(String imagePath, String altText) {
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream(imagePath));
            JButton button = new JButton(new ImageIcon(img));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
            button.setToolTipText(altText); // For accessibility
            return button;
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Could not load image for button: " + imagePath + ". Error: " + e.getMessage());
            JButton fallbackButton = new JButton(altText);
            fallbackButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            return fallbackButton;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuScreen());
    }
}