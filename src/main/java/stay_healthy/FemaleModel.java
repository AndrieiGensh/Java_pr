package stay_healthy;

import java.util.ArrayList;

public class FemaleModel implements BodyModel
{
    private String name;
    private String activity_level;

    private int age;
    private double height;
    private double weight;

    public final String sex = "female";

    private int id;

    private double TDEE;
    private double fats;
    private double proteins;
    private double carbon;

    private double KCalNeeds;
    private double FatsNeeds;
    private double CarbohydratesNeeds;
    private double ProteinsNeeds;

    public void copyModel(BodyModel model)
    {
        this.setActivity_level(model.getActivity_level());
        this.setAge(model.getAge());
        this.setHeight(model.getHeight());
        this.setName(model.getName());
        this.setWeight(model.getWeight());
        this.setId(model.getId());
    }

    public String getSex()
    {
        return this.sex;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public void setHeight(double height) { this.height = height; }

    public void setWeight(double weight)
    {
        this.weight = weight;
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

    public FemaleModel(String name, int age, double height, double weight, String act_level) {
        this.setName(name);
        this.setAge(age);
        this.setHeight(height);
        this.setWeight(weight);
        this.setActivity_level(act_level);
    }

    public FemaleModel()
    {
        this.setName("None");
        this.setAge(0);
        this.setHeight(0);
        this.setWeight(0);
        this.setActivity_level("None");
        this.id = -1;
    }

    public void setId(int id)
    {
        this.id=id;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public void setActivity_level(String activity_level)
    {
        this.activity_level = activity_level;
    }

    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return name;
    }

    public String getActivity_level()
    {
        return activity_level;
    }

    public void claculateTDEE(String choice)
    {
        this.TDEE=(9.56*this.getWeight()+1.85*this.getHeight()*100-4.68*this.getAge()+655.0);

        if ("None at all".equals(this.activity_level)) {
            this.TDEE *= 1.05;

        } else if ("Low".equals(this.activity_level)) {
            this.TDEE *= 1.2;

        } else if ("Occasional".equals(this.activity_level)) {
            this.TDEE *= 1.375;

        } else if ("Average".equals(this.activity_level)) {
            this.TDEE *= 1.55;

        } else if ("High".equals(this.activity_level)) {
            this.TDEE *= 1.725;
        }

        if ("Maintain".equals(choice)) {
            this.TDEE *= 1.005;

        } else if ("Put on".equals(choice)) {
            this.TDEE *= 1.2;

        } else if ("Lose".equals(choice)) {
            this.TDEE *= 0.8;
        }

    }

    public void calculateNeeds()
    {
        this.CarbohydratesNeeds = (double)((int)(this.TDEE * 43 / 100.0 / 4.0)*100)/100;
        this.FatsNeeds = (double)((int)(this.TDEE * 25 / 100.0 / 9.0)*100)/100;
        this.ProteinsNeeds = (double)((int)(this.weight * 2.1)*100)/100;
    }

    public ArrayList<Double> getNeeds()
    {
        ArrayList<Double> needs = new ArrayList<Double>();
        needs.add(this.TDEE);
        needs.add(this.ProteinsNeeds);
        needs.add(this.FatsNeeds);
        needs.add(this.CarbohydratesNeeds);

        return needs;
    }

}
