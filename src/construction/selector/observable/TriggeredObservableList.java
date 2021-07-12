package construction.selector.observable;

import java.util.LinkedList;
import java.util.List;

public class TriggeredObservableList<T> extends LinkedList<T> {
    private final List<Observer<T>> observerList;

    public TriggeredObservableList() {
        super();
        observerList = new LinkedList<>();
    }

    public void addObserver(Observer<T> observer) {
        observerList.add(observer);
    }

    public void notifyObservers() {
        List<T> newList = (List<T>)super.clone();
        for (Observer<T> observer : observerList) {
            observer.onListUpdate(newList);
        }
    }

}
