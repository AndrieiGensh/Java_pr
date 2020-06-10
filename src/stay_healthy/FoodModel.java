package stay_healthy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FoodModel
{
    private StringProperty name;
    private double kcal;
    private double proteins;
    private double fats;
    private double carbons;
    private StringProperty measure;

    public FoodModel(String name, double kcal, double proteins, double fats, double carbons, String measure)
    {
        nameProperty().set(name);

        this.kcal = kcal;
        this.proteins = proteins;
        this.fats = fats;
        this.carbons = carbons;
        measureProperty().set(measure);
    }

    public void multiply_by(double value)
    {
        this.carbons *= value;
        this.kcal *= value;
        this.fats *= value;
        this.proteins *= value;
    }

    public StringProperty getName()
    {
        return this.name;
    }

    public double getKcal()
    {
        return this.kcal;
    }

    public double getProteins()
    {
        return this.proteins;
    }

    public double getFats()
    {
        return this.fats;
    }

    public double getCarbons()
    {
        return this.carbons;
    }

    public StringProperty nameProperty()
    {
        if(name == null) name = new SimpleStringProperty(this, "name");
        return name;
    }

    public StringProperty measureProperty()
    {
        if(measure == null) measure = new SimpleStringProperty(this, "me");
        return measure;
    }

    public StringProperty getMeasure()
    {
        return this.measure;
    }

    public void setName(String name)
    {
        nameProperty().set(name);
    }

    public void setKcal(double kcal)
    {
        this.kcal = kcal;
    }

    public void setProteins(double proteins)
    {
        this.proteins = proteins;
    }

    public void setFats(double fats)
    {
        this.fats = fats;
    }

    public void setCarbons(double carbons)
    {
        this.carbons = carbons;
    }

    public void setMeasure(String measure)
    {
        measureProperty().set(measure);
    }
}
