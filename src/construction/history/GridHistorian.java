package construction.history;

import domain.Grid;

public class GridHistorian {

    private final Grid grid;

    private final LimitedStack<GridMemento> undoStack;
    private final LimitedStack<GridMemento> redoStack;
    private GridMemento currentState;

    public GridHistorian(Grid grid) {
        this.grid = grid;

        undoStack = new LimitedStack<>(5);
        redoStack = new LimitedStack<>(5);
    }

    public void saveState(GridMemento memento) {
        undoStack.push(memento);
        redoStack.clear();
        currentState = null;
    }

    public void undo() {
        // get the last state from the top of the stack;
        GridMemento memento = undoStack.pop();

        // check if anything can be redone
        if (memento == null) {
            System.out.println("Nothing to be undone.");
            return;
        }

        // save the current state of the grid if it isn't already
        if (currentState == null) {
            currentState = grid.makeSnapshot();
        }
        redoStack.push(currentState); // push the current state to the redo stack

        // restore the last state
        grid.restore(memento);
        currentState = memento;
    }

    public void redo() {
        GridMemento memento = redoStack.pop();

        if (memento == null) {
            System.out.println("Nothing to be redone.");
            return;
        }

        undoStack.push(currentState);

        grid.restore(memento);
        currentState = memento;
    }
}
