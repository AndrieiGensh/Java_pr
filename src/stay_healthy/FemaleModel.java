package stay_healthy;

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

        switch(this.activity_level)
        {
            case "None at all":
            {
                this.TDEE*=1.05;
            }
            case "Low":
            {
                this.TDEE*=1.2;
            }
            case "Occasional":
            {
                this.TDEE*=1.375;
            }
            case "Average":
            {
                this.TDEE*=1.55;
            }
            case "High":
            {
                this.TDEE*=1.725;
            }
        }

        switch (choice)
        {
            case "Maintain": {
                this.TDEE *= 1.005;
            }
            case "Put on": {
                this.TDEE *= 1.2;
            }
            case "Lose": {
                this.TDEE *= 0.8;
            }
        }

    }

}
