import java.util.Stack;

public class BacktrackingMazeSolver{

    /**
     * Moves a rat from (x1,y1) to (x2,y2), filling in the cells as it goes, and
     * notifying a listener at each step.
     */
    public boolean solve(Maze maze, Maze.MazeListener listener) {

        // TODO - if listener is null, throw an IllegalArgumentException
        // saying "Listener cannot be null"
        if (listener == null) {
          throw new IllegalArgumentException("Listener cannot be null");
        }

        var path = new Stack<Maze.Location>();


        // TODO: initialize the current location to the initial rat location
        Maze.Location current = maze.getInitialRatPosition();

        // Solution loop. At each step, place the rat and notify listener.
        while (true) {

            // TODO: Place the rat in the current cell
            // current.place(Maze.Cell.RAT);

            // TODO: Notify the listener
            listener.mazeChanged(maze);
            // TODO: Did we reach the desired end cell? If so, return true
            if (current.contents() == Maze.Cell.CHEESE) {
              return true;
            }

            // Move to an adjacent open cell, leaving a breadcrumb. If we
            // can't move at all, backtrack. If there's nowhere to backtrack
            // to, we're totally stuck.

            if (current.above().canBeMovedTo()) {
                path.push(current);
                current.place(Maze.Cell.PATH);
                current = current.above();
            } else if (current.toTheRight().canBeMovedTo()) {
                // TODO Fill this in
                path.push(current);
                current.place(Maze.Cell.PATH);
                current = current.toTheRight();
            } else if (current.below().canBeMovedTo()) {
                // TODO Fill this in
                path.push(current);
                current.place(Maze.Cell.PATH);
                current = current.below();
            } else if (current.toTheLeft().canBeMovedTo()) {
                // TODO Fill this in
                path.push(current);
                current.place(Maze.Cell.PATH);
                current = current.toTheLeft();
            } else {

                // TODO Fill this in ... mark this cell TRIED. If the path is
                // empty, return false. Otherwise, back up (by popping from the
                // path that is being built up)
                current.place(Maze.Cell.TRIED);
                if (path.empty()){
                  return false;
                }
                else{
                  current = path.pop();
                }


            }
        }
    }
}
