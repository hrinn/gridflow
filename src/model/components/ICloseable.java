package model.components;

public interface ICloseable extends IToggleable {
    boolean isClosed();
    boolean isClosedByDefault();
}
