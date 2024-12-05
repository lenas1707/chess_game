package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PlayerManager {
    private static final String DATA_DIR = "data";
    private static final String RESULTS_FILE = "chess_results.txt";
    private static Set<String> players = new HashSet<>();

    static {
        createDataDirectoryIfNotExists();
        loadPlayers();
    }

    private static void createDataDirectoryIfNotExists() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create data directory: " + e.getMessage());
        }
    }

    private static void loadPlayers() {
        Path filePath = Paths.get(DATA_DIR, RESULTS_FILE);
        if (Files.exists(filePath)) {
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        players.add(parts[0].trim());
                        players.add(parts[1].trim());
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading players from file: " + e.getMessage());
            }
        }
    }

    public static void addPlayers(String... newPlayers) {
        players.addAll(Arrays.asList(newPlayers));
    }

    public static List<String> getPlayers() {
        return new ArrayList<>(players);
    }

    public static void recordGameResult(String winner, String loser) {
        recordGameResult(winner, loser, false);
    }

    public static void recordGameResult(String player1, String player2, boolean isDraw) {
        String result = isDraw ? "Draw" : player1 + " won";
        String resultLine = player1 + " vs " + player2 + "," + result + "," + new Date();

        Path filePath = Paths.get(DATA_DIR, RESULTS_FILE);
        try {
            Files.write(filePath, (resultLine + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Game result recorded successfully.");
        } catch (IOException e) {
            System.err.println("Error recording game result: " + e.getMessage());
        }
    }
}