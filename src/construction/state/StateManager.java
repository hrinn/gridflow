package construction.state;

import domain.Grid;
import domain.Snapshot;

import java.util.LinkedList;

public class StateManager {

    private final LinkedList<Snapshot> stateHistory;
    private int statePointer = 0;
    private static final int CAPACITY = 5;

    public StateManager() {
        stateHistory = new LinkedList<>();
    }

    public void loadGridState(Grid grid) {
        stateHistory.addFirst(grid.save());
        printHistory();
    }

    public Snapshot stepBack() {
        if (statePointer >= stateHistory.size() - 1) return null;
        statePointer = statePointer + 1;
        printHistory();
        return stateHistory.get(statePointer);
    }

    public Snapshot stepForward() {
        if (statePointer == 0) return null;
        statePointer = statePointer - 1;
        printHistory();
        return stateHistory.get(statePointer);
    }

    private void printHistory() {
        System.out.print("History:");
        for (Snapshot snapshot : stateHistory) {
            System.out.print(" " + snapshot.getComponentCount());
        }
        System.out.println();
        printPointer();
    }

    private void printPointer() {
        System.out.println("Pointer: " + statePointer);
    }
}
