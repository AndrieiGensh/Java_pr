package stay_healthy;

import javafx.collections.ObservableList;

import java.util.List;

public interface DAO<T>
{
    ObservableList<T> getAll();

    void save();

    void update();

    void delete();
}
