package construction.selector.observable;

import java.util.List;

public interface Observer<T> {
    void onListUpdate(List<T> newList);
}
