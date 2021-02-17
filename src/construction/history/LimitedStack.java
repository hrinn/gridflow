package construction.history;

import java.util.LinkedList;

public class LimitedStack<T> {

    private final int capacity;
    private final LinkedList<T> stack;

    public LimitedStack(int capacity) {
        this.capacity = capacity;
        this.stack = new LinkedList<>();
    }

    public void push(T item) {
        stack.push(item);
        if (stack.size() > capacity) {
            stack.removeLast();
        }
    }

    public T pop() {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.pop();
    }

    public void clear() {
        stack.clear();
    }

    public void printSize() {
        System.err.println(stack.size());
    }
}
