package construction.history;

import domain.Grid;

public class GridHistorian {
    private final LimitedStack<GridMemento> undoStack;
    private final LimitedStack<GridMemento> redoStack;
    private final Grid grid;

    public GridHistorian(Grid grid) {
        this.grid = grid;

        undoStack = new LimitedStack<>(5);
        redoStack = new LimitedStack<>(5);
    }

    public void saveState() {
        GridMemento memento = grid.makeSnapshot();
        undoStack.push(memento);
    }

    public void undo() {
        GridMemento memento = undoStack.pop();
        if (memento == null) return;
        redoStack.push(memento);
        grid.restore(memento);
    }

    public void redo() {
        GridMemento memento = redoStack.pop();
        if (memento == null) return;
        undoStack.push(memento);
        grid.restore(memento);
    }
}
