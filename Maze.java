import java.util.stream.Stream;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Maze {


    private final Cell[][] cells;

    private Location initialRatLocation;
    private Location initialCheeseLocation;
    private boolean inputIsAllValid;


    private Maze(String[] lines) {

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

        ArrayList<String> anArrayList = new ArrayList<String>();
        while (scanner.hasNextLine()){
          String lineInfo = scanner.nextLine();
          anArrayList.add(lineInfo);
        }

        String[] aStringArray = anArrayList.toArray(new String[0]);

        Maze retMaze = new Maze(aStringArray);
        return retMaze;

    }


    public class Location {
        private final int row;
        private final int column;

        Location(final int row, final int column) {
            this.row = row;
            this.column = column;
        }

        boolean isInMaze() {
            return row >= 0 && row < getHeight() && column >= 0 && column < getWidth();

        }

        boolean canBeMovedTo() {
            return isInMaze() && (contents() == Cell.OPEN || contents() == Cell.CHEESE);

        }

        boolean hasCheese() {
            return isInMaze() && contents() == Cell.CHEESE;
        }

        Location above() {
            int newRow = row - 1;
            int newCol = column;
            Location currentLoc = new Location(newRow, newCol);
            return currentLoc;

        }

        Location below() {
            int newRow = row + 1;
            int newCol = column;
            Location currentLoc = new Location(newRow, newCol);
            return currentLoc;
        }

        Location toTheLeft() {
            int newRow = row;
            int newCol = column - 1;
            Location currentLoc = new Location(newRow, newCol);
            return currentLoc;
        }

        Location toTheRight() {
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
            return (other.row == row) && (other.column == column);

        }
    }


    public static enum Cell {
        OPEN(' '), WALL('\u2588'), TRIED('x'), PATH('.'), RAT('r'), CHEESE('c');

        private char shortCode;

        Cell(char code) {
          this.shortCode = code;
        }

        public String toString() {
          return Character.toString(shortCode);
        }


    }

    public interface MazeListener {
        void mazeChanged(Maze maze);
    }

    public int getWidth() {
        return cells[0].length;
    }

    public int getHeight() {
        return cells.length;
    }

    public Location getInitialRatPosition() {

        return initialRatLocation;
    }

    public Location getInitialCheesePosition() {
        return initialCheeseLocation;
    }


    public String toString() {
        return Stream.of(cells)
            .map(row -> Stream.of(row).map(Cell::toString).collect(joining()))
            .collect(joining("\n"));
    }
}
