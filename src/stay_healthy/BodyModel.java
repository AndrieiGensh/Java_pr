package stay_healthy;

public class BodyModel {

    private int age;
    private double height;
    private double weight;
    private  String sex;

    private double KCalNeeds;
    private double FatsNeeds;
    private double CarbohydratesNeeds;
    private double ProteinsNeeds;

    public void setAge(int age)
    {
        this.age = age;
    }

    public void setHeight(double height) { this.height = height; }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public int getAge()
    {
        return age;
    }

    public double getHeight()
    {
        return height;
    }

    public double getWeight()
    {
        return weight;
    }

    public String getSex()
    {
        return sex;
    }

}
