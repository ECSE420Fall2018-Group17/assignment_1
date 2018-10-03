package ca.mcgill.ecse420.a1;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatrixMultiplication {
	
	private static final int NUMBER_THREADS = 1;
	private static final int MATRIX_SIZE = 2;

    public static void main(String[] args) {
		
    // Generate two random matrices, same size
    double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
	double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
	
	// Perform the multiplications
	double[][] resultSequential = sequentialMultiplyMatrix(a, b);
	double[][] resultParallel = parallelMultiplyMatrix(a, b);
	
    System.out.println("Sequential Multiply: " + Arrays.deepToString(resultSequential));
    System.out.println("Parallel Multiply: " +Arrays.deepToString(resultParallel));
	}
	
	/**
	 * Returns the result of a sequential matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
	  
	  // Verify that the number of columns in matrix A matches the number of rows in matrix B
	  int aColumns = a[0].length;
	  int aRows = a.length;
	  int bColumns = b[0].length;
	  int bRows = b.length;
	  if (aColumns != bRows) {
	    throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
      }
	  
	  // Initialize empty results matrix
	  double[][] c = new double[aRows][bColumns];
	  
	  // Perform sequential matrix multiplication
      for (int i = 0; i < aRows; i++){
        for (int j = 0; j < bColumns; j++){
          c[i][j] = 0.00000;
            for (int k = 0; k < aColumns; k++) {
              c[i][j] += a[i][k] * b[k][j];
            }
        }
    }

    return c;
	}
	
	/**
	 * Returns the result of a concurrent matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
    public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
      return null;
	}
        
    /**
    * Populates a matrix of given size with randomly generated integers between 0-10.
    * @param numRows number of rows
    * @param numCols number of cols
    * @return matrix
    */
    private static double[][] generateRandomMatrix (int numRows, int numCols) {
      double matrix[][] = new double[numRows][numCols];
      for (int row = 0 ; row < numRows ; row++ ) {
        for (int col = 0 ; col < numCols ; col++ ) {
          matrix[row][col] = (double) ((int) (Math.random() * 10.0));
        }
      }
      return matrix;
    }
	
}
