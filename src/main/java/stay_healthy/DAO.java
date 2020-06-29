package stay_healthy;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public interface DAO<T>
{
    ObservableList<T> getAll();

    ArrayList<Double> getStats();

    void save();

    void update();

    void delete();
}
