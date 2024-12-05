package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoginScreen extends JFrame {
    private JTextField player1Field;
    private JTextField player2Field;
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 400;

    public LoginScreen() {
        setTitle("Chess Game Login");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(20, 20, 20);
                Color color2 = new Color(255, 255, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 10, 20));
        inputPanel.setMaximumSize(new Dimension(300, 100));
        inputPanel.setOpaque(false);

        Font labelFont = new Font("Arial", Font.BOLD, 18);

        JLabel player1Label = new JLabel("Player 1:");
        player1Label.setForeground(Color.WHITE);
        player1Label.setFont(labelFont);
        inputPanel.add(player1Label);

        player1Field = new JTextField();
        player1Field.setPreferredSize(new Dimension(150, 30));
        player1Field.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(player1Field);

        JLabel player2Label = new JLabel("Player 2:");
        player2Label.setForeground(Color.WHITE);
        player2Label.setFont(labelFont);
        inputPanel.add(player2Label);

        player2Field = new JTextField();
        player2Field.setPreferredSize(new Dimension(150, 30));
        player2Field.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(player2Field);

        mainPanel.add(inputPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton startButton = createImageButton("/images/start_button.png", "Start Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String player1 = player1Field.getText().trim();
                String player2 = player2Field.getText().trim();
                if (!player1.isEmpty() && !player2.isEmpty()) {
                    dispose();
                    SwingUtilities.invokeLater(() -> new ChessGameGUI(new String[]{player1, player2}));
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Please enter names for both players.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mainPanel.add(startButton);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton backButton = createImageButton("/images/back_button.png", "Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> new MenuScreen());
            }
        });
        mainPanel.add(backButton);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private JButton createImageButton(String imagePath, String altText) {
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream(imagePath));
            int originalWidth = img.getWidth(null);
            int originalHeight = img.getHeight(null);

            double scale = Math.min(150.0 / originalWidth, 40.0 / originalHeight);
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);

            Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImg);

            JButton button = new JButton(icon);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
            button.setToolTipText(altText);
            button.setPreferredSize(new Dimension(150, 40));
            return button;
        } catch (IOException e) {
            System.err.println("Could not load image for button: " + imagePath + ". Error: " + e.getMessage());
            JButton fallbackButton = new JButton(altText);
            fallbackButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            fallbackButton.setPreferredSize(new Dimension(150, 40));
            return fallbackButton;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen());
    }
}