package edu.ics211.h09;

import java.util.ArrayList;

/**
 * Class for recursively finding a solution to a Hexadecimal Sudoku problem.
 *
 * @author Biagioni, Edoardo, Cam Moore
 *     date August 5, 2016
 *     missing solveSudoku, to be implemented by the students in ICS 211
 */
public class HexadecimalSudoku {
  //helped by Cam Moore in the lecture.
  /**
   * Find an assignment of values to sudoku cells that makes the sudoku valid.
   *
   * @param sudoku the sudoku to be solved.
   * @return whether a solution was found if a solution was found, the sudoku is
   *         filled in with the solution if no solution was found, restores the
   *         sudoku to its original value.
   */
  public static boolean solveSudoku(int[][] sudoku) {
    // TODO: Implement this method recursively. You may use a recursive helper method.
    //loop over the row
    for (int i = 0; i < sudoku[0].length; i++) {
      //loop over the column
      for (int j = 0; j < sudoku.length; j++) {
        //if cell is empty 
        if (sudoku[i][j] == -1) {
          //if the all legal values are used then return false, base case 2
          if (legalValues(sudoku, i, j) == null) {
            return false;
            //else set the cell to the legal value left in the list
          } else {
            for (int value: legalValues(sudoku, i, j)) {
              sudoku[i][j] = value;
              //if solveSudoku is true then problem solved return true.
              if (solveSudoku(sudoku) == true) {
                return true;
              }
            }
            //problem is not solved so set the cell back to empty and return false
            sudoku[i][j] = -1;
            return false;
          }
        }
      }
    }
    //base case 1, if all cells are fill and this sudoku is valid then solution is found
    //if not then this sudoku is not a solution.
    return checkSudoku(sudoku, false);
  }

  /**
   * Find the legal values for the given sudoku and cell.
   *
   * @param sudoku the sudoku being solved.
   * @param row the row of the cell to get values for.
   * @param column the column of the cell.
   * @return an ArrayList of the valid values.
   */
  public static ArrayList<Integer> legalValues(int[][] sudoku, int row, int column) {
    // TODO: Implement this method. You may want to look at the checkSudoku method
    // to see how it finds conflicts.
    //create an array list and fill with 0 to F.
    ArrayList<Integer> values = new ArrayList<Integer>();
    for (int i = 0; i < 16; i++) {
      values.add(i);
    }
    //remove all the value that is in the row from the list
    for (int i = 0; i < sudoku[row].length; i++) {
      int index = values.indexOf(sudoku[row][i]);
      if (index != -1) {
        values.remove(index);
      }
    }
    //remove all the value that is in the column from the list
    for (int j = 0; j < sudoku.length; j++) {
      int index = values.indexOf(sudoku[j][column]);
      if (index != -1) {
        values.remove(index);
      }
    }
    //remove all the value that is in the 4 x 4 grid from the list
    for (int k = 0; k < 4; k++) {
      for (int m = 0; m < 4; m++) {
        int testRow = row / 4 * 4 + k;
        int testCol = column / 4 * 4 + m;
        int index = values.indexOf(sudoku[testRow][testCol]);
        if (index != -1) {
          values.remove(index);
        }
      }
    }
    //if list is empty mean all the value has been used from the list so return null
    if (values.size() == 0) {
      return null;
      //else return the list with value left.
    } else {
      return values;
    }
  }


  /**
   * checks that the sudoku rules hold in this sudoku puzzle. cells that contain
   * 0 are not checked.
   *
   * @param sudoku the sudoku to be checked.
   * @param printErrors whether to print the error found, if any.
   * @return true if this sudoku obeys all of the sudoku rules, otherwise false.
   */
  public static boolean checkSudoku(int[][] sudoku, boolean printErrors) {
    if (sudoku.length != 16) {
      if (printErrors) {
        System.out.println("sudoku has " + sudoku.length + " rows, should have 16");
      }
      return false;
    }
    for (int i = 0; i < sudoku.length; i++) {
      if (sudoku[i].length != 16) {
        if (printErrors) {
          System.out.println("sudoku row " + i + " has "
              + sudoku[i].length + " cells, should have 16");
        }
        return false;
      }
    }
    /* check each cell for conflicts */
    for (int i = 0; i < sudoku.length; i++) {
      for (int j = 0; j < sudoku.length; j++) {
        int cell = sudoku[i][j];
        if (cell == -1) {
          continue; /* blanks are always OK */
        }
        if ((cell < 0) || (cell > 16)) {
          if (printErrors) {
            System.out.println("sudoku row " + i + " column " + j
                + " has illegal value " + String.format("%02X", cell));
          }
          return false;
        }
        /* does it match any other value in the same row? */
        for (int m = 0; m < sudoku.length; m++) {
          if ((j != m) && (cell == sudoku[i][m])) {
            if (printErrors) {
              System.out.println("sudoku row " + i + " has " + String.format("%X", cell)
                  + " at both positions " + j + " and " + m);
            }
            return false;
          }
        }
        /* does it match any other value it in the same column? */
        for (int k = 0; k < sudoku.length; k++) {
          if ((i != k) && (cell == sudoku[k][j])) {
            if (printErrors) {
              System.out.println("sudoku column " + j + " has " + String.format("%X", cell)
                  + " at both positions " + i + " and " + k);
            }
            return false;
          }
        }
        /* does it match any other value in the 4x4? */
        for (int k = 0; k < 4; k++) {
          for (int m = 0; m < 4; m++) {
            int testRow = (i / 4 * 4) + k; /* test this row */
            int testCol = (j / 4 * 4) + m; /* test this col */
            if ((i != testRow) && (j != testCol) && (cell == sudoku[testRow][testCol])) {
              if (printErrors) {
                System.out.println("sudoku character " + String.format("%X", cell) + " at row "
                    + i + ", column " + j + " matches character at row " + testRow + ", column "
                    + testCol);
              }
              return false;
            }
          }
        }
      }
    }
    return true;
  }


  /**
   * Converts the sudoku to a printable string.
   *
   * @param sudoku the sudoku to be converted.
   * @param debug whether to check for errors.
   * @return the printable version of the sudoku.
   */
  public static String toString(int[][] sudoku, boolean debug) {
    if ((!debug) || (checkSudoku(sudoku, true))) {
      String result = "";
      for (int i = 0; i < sudoku.length; i++) {
        if (i % 4 == 0) {
          result = result + "+---------+---------+---------+---------+\n";
        }
        for (int j = 0; j < sudoku.length; j++) {
          if (j % 4 == 0) {
            result = result + "| ";
          }
          if (sudoku[i][j] == -1) {
            result = result + "  ";
          } else {
            result = result + String.format("%X", sudoku[i][j]) + " ";
          }
        }
        result = result + "|\n";
      }
      result = result + "+---------+---------+---------+---------+\n";
      return result;
    }
    return "illegal sudoku";
  }
}
