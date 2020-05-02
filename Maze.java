import java.util.stream.Stream;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Maze {

    // A maze is a rectangular array of cells. The reason we use arrays is that
    // the maze has a fixed size, and arrays are the fastest when indexing by
    // position, which is exactly what we do when we search a maze.
    private final Cell[][] cells;

    private Location initialRatLocation;
    private Location initialCheeseLocation;
    private boolean inputIsAllValid;

    /**
     * Builds and returns a new maze given a description in the form of an array
     * of strings, one for each row of the maze, with each string containing o's
     * and w's and r's and c's. o=Open space, w=Wall, r=Rat, c=Cheese.
     *
     * The maze must be rectangular and contain nothing but legal characters. There
     * must be exactly one 'r' and exactly one 'c'.
     *
     * The constructor is private to force users to only construct mazes through one
     * of the factory methods fromString, fromFile, or fromScanner.
     */
    private Maze(String[] lines) {
        // TODO: Fill this in. There is a lot to check for! The maze must be
        // perfectly rectanglar, not contain any illegal characters, have exactly
        // one rat (not less, not more), and have exactly one cheese (not less,
        // not more).

        inputIsAllValid = true;
        int numOfUChars = 0;
        int numOfRats = 0;
        int numOfCheese = 0;
        boolean isRectangular = true;
        cells = new Cell[lines.length][];

        for (int i = 0; i < lines.length; i++){
          cells[i] = new Cell[lines[i].length()];
          if (i > 0){
            if (lines[i].length() != lines[0].length()){
              isRectangular = false;
            }
          }
          for (int j = 0; j < lines[i].length(); j++){
            char aChar = lines[i].charAt(j);
            switch (aChar){
              case 'o':
                  cells[i][j] = Cell.OPEN;
                  break;
              case 'w':
                  cells[i][j] = Cell.WALL;
                  break;
              case 'r':
                  cells[i][j] = Cell.RAT;
                  initialRatLocation = new Location(i, j);
                  numOfRats++;
                  break;
              case 'c':
                  cells[i][j] = Cell.CHEESE;
                  initialCheeseLocation = new Location(i, j);
                  numOfCheese++;
                  break;
              default:
                  numOfUChars++;
                  inputIsAllValid = false;
            }

          }

        }
        if(numOfRats != 1 || numOfCheese !=1 || numOfUChars > 0 || !isRectangular){
          String aStr = genExceptionStr(numOfRats, numOfUChars, numOfCheese, isRectangular);
          throw new IllegalArgumentException(aStr);
        }
    }

    private String genExceptionStr(int numOfRats, int numOfUChars, int numOfCheese, boolean isRectangular){
      if (!isRectangular){
        return "Non-rectangular maze";
      }
      if (numOfUChars > 0){
        return "There are unwanted characters";
      }
      if (numOfRats > 1){
        return "Maze can only have one rat";
      }
      if (numOfRats == 0){
        return "Maze has no rat";
      }
      if (numOfCheese > 1){
        return "Maze can only have one cheese";
      }
      if (numOfCheese == 0){
        return "Maze has no cheese";
      }
      else{
        return "Empty String";
      }
    }

    public static Maze fromString(final String description) {
        return new Maze(description.trim().split("\\s+"));
    }

    public static Maze fromFile(final String filename) throws FileNotFoundException {
        return Maze.fromScanner(new Scanner(new File(filename)));
    }

    public static Maze fromScanner(final Scanner scanner) {
        // TODO: Fill this in. You will want to read line-by-line from the scanner
        // storing each line in an array of strings, then turn the list into
        // an array and pass that to the Maze constructor. Return the newly
        // constructed maze from this method.

        ArrayList<String> anArrayList = new ArrayList<String>();
        while (scanner.hasNextLine()){
          String lineInfo = scanner.nextLine();
          anArrayList.add(lineInfo);
        }

        String[] aStringArray = anArrayList.toArray(new String[0]);

        Maze retMaze = new Maze(aStringArray);
        return retMaze;

    }

    /**
     * A nice representation of a Location, so we don't have to litter our code
     * with separate row and column variables! A location object bundles these
     * two values together. It also includes a whole bunch of nice little methods
     * so that our code reads nicely.
     */
    public class Location {
        private final int row;
        private final int column;

        Location(final int row, final int column) {
            // TODO: Fill this in, it's pretty easy.
            this.row = row;
            this.column = column;
        }

        boolean isInMaze() {
            // TODO: Fill this in. Return whether the row and column is a legal
            // position in this maze.
            return row >= 0 && row < getHeight() && column >= 0 && column < getWidth();

        }

        boolean canBeMovedTo() {
            // TODO: Fill this in. You can move to a space only if it is inside the
            // maze and the cell is open or contains the cheese.
            return isInMaze() && (contents() == Cell.OPEN || contents() == Cell.CHEESE);
            // return true;
        }
        // Do all the conditions here
        boolean hasCheese() {
            // TODO: Fill this in. Returns whether the cell has the cheese. You can
            // use the contents() method to help you here.
            return isInMaze() && contents() == Cell.CHEESE;
        }
        // Reverse the position or index
        Location above() {
            // TODO: Fill this in. It should return a new location whose coordinates
            // are (1) the row above this location's row, and (2) the same column.
            int newRow = row - 1;
            int newCol = column;
            Location currentLoc = new Location(newRow, newCol);
            return currentLoc;

        }
        // Reverse the position or index
        Location below() {
            // TODO: Fill this in. Return the location directly below this one.
            int newRow = row + 1;
            int newCol = column;
            Location currentLoc = new Location(newRow, newCol);
            return currentLoc;
        }

        Location toTheLeft() {
            // TODO: Fill this in. Return the location directly to the left of this one.
            int newRow = row;
            int newCol = column - 1;
            Location currentLoc = new Location(newRow, newCol);
            return currentLoc;
        }

        Location toTheRight() {
            // TODO: Fill this in. Return the location directly to the right of this one.
            int newRow = row;
            int newCol = column + 1;
            Location currentLoc = new Location(newRow, newCol);
            return currentLoc;
        }

        void place(Cell cell) {
            cells[row][column] = cell;
        }

        Cell contents() {
            return cells[row][column];
        }

        boolean isAt(final Location other) {
            // TODO: Fill this in. Returns whether this location and the other location have
            // the same row and column values.
            return (other.row == row) && (other.column == column);

        }
    }

    /**
     * A simple cell value. A cell can be open (meaning a rat has never visited it),
     * a wall, part of the rat's current path, or "tried" (meaning the rat found it
     * to be part of a dead end.
     */
    public static enum Cell {
        OPEN(' '), WALL('\u2588'), TRIED('x'), PATH('.'), RAT('r'), CHEESE('c');

        // This needs a constructor and a toString method. You might need to do some
        // research on Java enums.
        private char shortCode;

        Cell(char code) {
          this.shortCode = code;
        }

        public String toString() {
          return Character.toString(shortCode);
        }

        // convert character to String

    }

    public interface MazeListener {
        void mazeChanged(Maze maze);
    }

    public int getWidth() {
        // TODO: Fill this in. The information comes from the cells array.
        return cells[0].length;
    }

    public int getHeight() {
        // TODO: Fill this in
        return cells.length;
    }

    public Location getInitialRatPosition() {
        // TODO: Fill this in. It is a typical getter, since you already have a field
        // for the initial rat position.
        return initialRatLocation;
    }

    public Location getInitialCheesePosition() {
        // TODO: Fill this in
        return initialCheeseLocation;
    }

    /**
     * Returns a textual description of the maze, separating each row with a newline.
     */
    public String toString() {
        return Stream.of(cells)
            .map(row -> Stream.of(row).map(Cell::toString).collect(joining()))
            .collect(joining("\n"));
    }
}
