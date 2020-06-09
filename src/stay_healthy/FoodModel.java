package stay_healthy;

public class FoodModel
{
    private String name;
    private double kcal;
    private double proteins;
    private double fats;
    private double carbons;
    private String measure;

    public FoodModel(String name, double kcal, double proteins, double fats, double carbons, String measure)
    {
        this.name = name;
        this.kcal = kcal;
        this.proteins = proteins;
        this.fats = fats;
        this.carbons = carbons;
        this.measure = measure;
    }

    public String getName()
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

    public String getMeasure()
    {
        return this.measure;
    }

    public void setName(String name)
    {
        this.name = name;
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
        this.measure = measure;
    }
}
