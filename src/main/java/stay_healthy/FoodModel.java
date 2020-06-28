package stay_healthy;

import com.sun.org.apache.xerces.internal.impl.dv.xs.DoubleDV;
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
    private double how_much;
    private StringProperty how_much_string;

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
        double factor = 0.0;
        if(this.measure.get().equals("g") || this.measure.get().equals("ml"))
        {
            factor = 100.0;
        }
        else
        {
            factor = 1.0;
        }
        this.carbons *= value / factor;
        this.kcal *= value / factor;
        this.fats *= value / factor;
        this.proteins *= value / factor;
    }

    public StringProperty getName()
    {
        return this.name;
    }

    public StringProperty getHow_much_string()
    {
        return this.how_much_string;
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

    public double getHow_much()
    {
        return this.how_much;
    }

    public StringProperty nameProperty()
    {
        if(name == null) name = new SimpleStringProperty(this, "name");
        return name;
    }

    public StringProperty how_much_stringProperty()
    {
        if(how_much_string == null) how_much_string = new SimpleStringProperty(this, "hm_str");
        return how_much_string;
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

    public void setHow_much(double how_much)
    {
        this.how_much = how_much;
        this.how_much_stringProperty().set(Double.toString(how_much) + measure.get());
    }
}
