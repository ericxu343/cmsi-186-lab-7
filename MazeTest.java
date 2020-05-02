public class MazeTest extends TestSuite {
    public static void main(String[] args) {
        TestSuite.run(new MazeTest());
    }

    public Test[] getTests() {
        return new Test[]{
            new Test("A non square maze", () -> expectThrows(
                () -> Maze.fromString("ooo\noo"),
                IllegalArgumentException.class, "Non-rectangular maze"
            )),
            new Test("Multiple rats", () -> expectThrows(
                () -> Maze.fromString("woor\noorc"),
                IllegalArgumentException.class, "Maze can only have one rat"
            )),
            new Test("Multiple cheeses", () -> expectThrows(
                () -> Maze.fromString("wooo\noccr"),
                IllegalArgumentException.class, "Maze can only have one cheese"
            )),
            new Test("No rats", () -> expectThrows(
                () -> Maze.fromString("wooo\nowcw"),
                IllegalArgumentException.class, "Maze has no rat"
            )),
            new Test("No cheese", () -> expectThrows(
                () -> Maze.fromString("row\noww"),
                IllegalArgumentException.class, "Maze has no cheese"
            )),
            new Test("Location contents", () -> {
                var maze = Maze.fromString("wwor\ncooo");
                expectEqual(maze.new Location(0, 2).contents(), Maze.Cell.OPEN);
                expectEqual(maze.new Location(0, 1).contents(), Maze.Cell.WALL);
                expectEqual(maze.new Location(0, 3).contents(), Maze.Cell.RAT);
                expectEqual(maze.new Location(1, 0).contents(), Maze.Cell.CHEESE);
            }),
            new Test("Location place", () -> {
                var maze = Maze.fromString("wwor\ncooo");
                maze.new Location(0, 2).place(Maze.Cell.CHEESE);
                expectEqual(maze.new Location(0, 2).contents(), Maze.Cell.CHEESE);
            }),
            new Test("Location directions", () -> {
                var maze = Maze.fromString("wwor\ncooo");
                expectEqual(maze.new Location(1, 3).above().isAt(maze.new Location(0, 3)), true);
                expectEqual(maze.new Location(1, 3).below().isAt(maze.new Location(2, 3)), true);
                expectEqual(maze.new Location(1, 3).toTheLeft().isAt(maze.new Location(1, 2)), true);
                expectEqual(maze.new Location(1, 3).toTheRight().isAt(maze.new Location(1, 4)), true);
            }),
            new Test("Location can be moved to", () -> {
                var maze = Maze.fromString("wwor\ncooo");
                expectEqual(maze.new Location(0, 2).canBeMovedTo(), true);
                expectEqual(maze.new Location(1, 0).canBeMovedTo(), true);
                expectEqual(maze.new Location(0, 1).canBeMovedTo(), false); // wall
                expectEqual(maze.new Location(-1, 1).canBeMovedTo(), false); // above maze
                expectEqual(maze.new Location(0, 4).canBeMovedTo(), false); // right of maze
                expectEqual(maze.new Location(2, 1).canBeMovedTo(), false); // below maze
                expectEqual(maze.new Location(1, -1).canBeMovedTo(), false); // left of maze
            }),
            new Test("Location is in maze", () -> {
                var maze = Maze.fromString("wwor\ncooo");
                expectEqual(maze.new Location(0, 2).isInMaze(), true);
                expectEqual(maze.new Location(1, 0).isInMaze(), true);
                expectEqual(maze.new Location(0, 1).isInMaze(), true); // wall
                expectEqual(maze.new Location(-1, 1).isInMaze(), false); // above maze
                expectEqual(maze.new Location(0, 4).isInMaze(), false); // right of maze
                expectEqual(maze.new Location(2, 1).isInMaze(), false); // below maze
                expectEqual(maze.new Location(1, -1).isInMaze(), false); // left of maze
            }),
            new Test("Cell strings", () -> {
                expectEqual(Maze.Cell.OPEN.toString(), " ");
                expectEqual(Maze.Cell.WALL.toString(), "\u2588");
                expectEqual(Maze.Cell.RAT.toString(), "r");
                expectEqual(Maze.Cell.CHEESE.toString(), "c");
                expectEqual(Maze.Cell.PATH.toString(), ".");
                expectEqual(Maze.Cell.TRIED.toString(), "x");
            }),
            new Test("Maze properties", () -> {
                var maze = Maze.fromString("wworwwwow\ncooooowww");
                expectEqual(maze.getWidth(), 9);
                expectEqual(maze.getHeight(), 2);
                expectEqual(maze.getInitialRatPosition().isAt(maze.new Location(0, 3)), true);
                expectEqual(maze.getInitialCheesePosition().isAt(maze.new Location(1, 0)), true);
                expectEqual(maze.toString(), "\u2588\u2588 r\u2588\u2588\u2588 \u2588\nc     \u2588\u2588\u2588");
            }),
            new Test("Backtracking Solver throws if listener is null", () -> expectThrows(
                () -> {
                    var maze = Maze.fromString("roow\ncwoo");
                    new BacktrackingMazeSolver().solve(maze, null);
                },
                IllegalArgumentException.class, "Listener cannot be null"
            )),
            new Test("Backtracking Solver can solve some mazes", () -> {
                var maze = Maze.fromString("roow\ncwoo");
                var doNothingMazeListener = new Maze.MazeListener(){
                    @Override public void mazeChanged(Maze maze) {}
                };
                var solved = new BacktrackingMazeSolver().solve(maze, doNothingMazeListener);
                expectEqual(solved, true);
            }),
            new Test("Backtracking Solver can tell when a maze cannot be solved", () -> {
                var maze = Maze.fromString("rwoc\nowoo");
                var doNothingMazeListener = new Maze.MazeListener(){
                    @Override public void mazeChanged(Maze maze) {}
                };
                var solved = new BacktrackingMazeSolver().solve(maze, doNothingMazeListener);
                expectEqual(solved, false);
            }),
        };
    }
}
