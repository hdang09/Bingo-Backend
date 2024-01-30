package hdang09.utils;


public class ArrayUtil {
    public static int[][] convertStringTo2DArray(String input) {
        // Calculate the number of rows and columns for the 2D array
        int rows = input.split("], \\[").length;
        int cols = input.substring(1, input.indexOf("], [")).split(", ").length;

        // Remove the quotation marks and the brackets from the input
        input = input.replaceAll("\"|\\[|\\]", "");

        // Split the input by commas and store the result in a 1D array
        String[] elements = input.split(", ");

        // Create a 2D array of integers with the same size as the input
        int[][] output = new int[rows][cols];

        // Loop through the elements and convert them to integers
        for (int i = 0; i < elements.length; i++) {
            // Calculate the row and column indices for the 2D array
            int row = i / cols;
            int col = i % cols;

            // Parse the element as an integer and store it in the 2D array
            output[row][col] = Integer.parseInt(elements[i]);
        }

        // Return the 2D array
        return output;
    }
}
