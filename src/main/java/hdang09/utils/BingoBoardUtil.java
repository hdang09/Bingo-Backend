package hdang09.utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BingoBoardUtil {
    public static int[][] generateBingoBoard(int width, int height, int maxNumberEachRow) {
        // Create a 2D array to store the board
        int[][] board = new int[height][width];

        // Create a random number generator
        Random random = new Random();

        // Loop through each column of the board
        for (int col = 0; col < width; col++) {
            // Calculate the range of numbers for this column
            int min = col * 10 + 1;
            int max = (col + 1) * 10;

            // Generate a list of numbers in the range
            List<Integer> numbers = IntStream.range(min, max).boxed().collect(Collectors.toList());
            Collections.shuffle(numbers);

            // Loop through each row of the column
            for (int row = 0; row < height; row++) {
                board[row][col] = numbers.get(row);
            }
        }

        // Loop through each row of the board
        for (int row = 0; row < height; row++) {
            for (int numberEachRow = 0; numberEachRow < width - maxNumberEachRow; numberEachRow++) {
                // Generate a random column index
                int index = random.nextInt(width);

                // Free number
                board[row][index] = 0;
            }
        }

        // Return the board
        return board;
    }

    public static int generateRandomNumber(int from, int to) {
        Random random = new Random();
        return random.nextInt(to - from) + from;
    }

    public static boolean isBingo(int[][] board, Set<Integer> playerDrawnNumbers, int drawnNumber) {
        playerDrawnNumbers.add(drawnNumber);

        // Calculate width and height of the board
        int width = board[0].length;
        int height = board.length;

        // Calculate the max number of each row
        int maxNumberEachRow = (int) Arrays.stream(board[0]).filter(col -> col != 0).count();

        // Check horizontal
        for (int row = 0; row < height; row++) {
            int count = 0;
            for (int col = 0; col < width; col++) {
                if (playerDrawnNumbers.contains(board[row][col])) {
                    count++;
                }
            }

            if (count == maxNumberEachRow) {
                return true;
            }
        }

        return false;
    }
}




















