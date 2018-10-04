package ca.mcgill.ecse420.a1;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplication {
	
	private static final int NUMBER_THREADS = 7;
	private static final int MATRIX_SIZE = 4000;

    public static void main(String[] args) {
		
    // Generate two random matrices, same size
    double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
	double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
	
	// Perform the multiplications and times them
	long startTime;
	long endTime;
	long timeTakenSequential;
	long timeTakenParallel;
	
	startTime = System.currentTimeMillis();
	double[][] resultSequential = sequentialMultiplyMatrix(a, b);
	endTime = System.currentTimeMillis();
	timeTakenSequential = endTime - startTime;
	startTime = System.currentTimeMillis();
	double[][] resultParallel = parallelMultiplyMatrix(a, b);
	endTime = System.currentTimeMillis();
	timeTakenParallel = endTime - startTime;
	
	// Output the results of the multiplication
    //System.out.println("Sequential Multiply: " + Arrays.deepToString(resultSequential));
    System.out.println("Time taken for sequential:" + timeTakenSequential);
    //System.out.println("Parallel Multiply: " +Arrays.deepToString(resultParallel));
    System.out.println("Time taken for parallel:" + timeTakenParallel);
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
      
      // Verify that the number of columns in matrix A matches the number of rows in matrix B
      int aNoOfColumns = a[0].length;
      int aNoOfRows = a.length;
      int bNoOfColumns = b[0].length;
      int bNoOfRows = b.length;
      if (aNoOfColumns != bNoOfRows) {
        throw new IllegalArgumentException("A:Rows: " + aNoOfColumns + " did not match B:Columns " + bNoOfRows + ".");
      }
      
      // Initialize empty results matrix
      double[][] c = new double[aNoOfRows][bNoOfColumns];
      
      
      // Implement the task of determining an single entry in the results matrix
      class WorkerThread implements Runnable {
        
        int rowNumber;
        int colNumber;
        
        public WorkerThread(int rowNumber, int colNumber) {
          this.rowNumber = rowNumber;
          this.colNumber = colNumber;
        }
        
        int aNoOfColumns = a[0].length;

        @Override
        public void run() {
          c[rowNumber][colNumber] = 0;
          for (int i = 0; i < aNoOfColumns ; i++) {
            c[rowNumber][colNumber] += a[rowNumber][i] * b[i][colNumber];
          }
          
        }
        
      }
      
      // Create a fixed thread pool
      ExecutorService executor = Executors.newFixedThreadPool(NUMBER_THREADS);
      
      // Perform parallel matrix multiplication
      for (int i = 0; i < aNoOfRows; i++){
        for (int j = 0; j < bNoOfColumns; j++){
          WorkerThread workerThread = new WorkerThread(i, j);
          executor.execute(workerThread);
        }
      }
      
      // Shutdown the executor
      executor.shutdown();
      try {
          executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      
      return c;
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
