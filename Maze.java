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
        cells = new Cell[lines.length][];

        for (int i = 0; i < lines.length; i++){
          cells[i] = new Cell[lines[i].length()];
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
                  break;
              case 'c':
                  cells[i][j] = Cell.CHEESE;
                  initialCheeseLocation = new Location(i, j);
                  break;
              default:
                  inputIsAllValid = false;
            }
          }
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
            int totalRow = Maze.this.getHeight();
            int totalCol = Maze.this.getWidth();
            if ((row < 0 || row >= totalRow) && (column < 0 || this.column >= totalCol)){
              return false;
            }

        }

        boolean canBeMovedTo(int i, int j) {
            // TODO: Fill this in. You can move to a space only if it is inside the
            // maze and the cell is open or contains the cheese.

            if (Maze.this.cells[i][j].isInMaze() && (Maze.this.cells[i][j] = Cell.OPEN || Maze.this.cells[i][j] = Cell.CHEESE)){
              // initialRatLocation = Maze.this.cells[i][j];
              return true;
            }
            // return true;
        }

        boolean hasCheese() {
            // TODO: Fill this in. Returns whether the cell has the cheese. You can
            // use the contents() method to help you here.
            /*
            if (Maze.this.cells[i][j].contentEquals(Cell.CHEESE)){
              return true;
            }
            */
        }

        Location above() {
            // TODO: Fill this in. It should return a new location whose coordinates
            // are (1) the row above this location's row, and (2) the same column.

        }

        Location below() {
            // TODO: Fill this in. Return the location directly below this one.
        }

        Location toTheLeft() {
            // TODO: Fill this in. Return the location directly to the left of this one.
        }

        Location toTheRight() {
            // TODO: Fill this in. Return the location directly to the right of this one.
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

        public char toChar() {
          return this.shortCode;
        }

    }

    public interface MazeListener {
        void mazeChanged(Maze maze);
    }

    public int getWidth() {
        // TODO: Fill this in. The information comes from the cells array.
    }

    public int getHeight() {
        // TODO: Fill this in
    }

    public Location getInitialRatPosition() {
        // TODO: Fill this in. It is a typical getter, since you already have a field
        // for the initial rat position.
    }

    public Location getInitialCheesePosition() {
        // TODO: Fill this in
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
