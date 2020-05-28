package stay_healthy;

public class PersonModel extends BodyModel{

    private String name;
    private String activity_level;

    private int id;

    private double TDEE;
    private double fats;
    private double proteins;
    private double carbon;

    public PersonModel(String name, int age, double height, double weight, String sex, String act_level) {
        this.setName(name);
        this.setAge(age);
        this.setHeight(height);
        this.setWeight(weight);
        this.setSex(sex);
        this.setActivity_level(act_level);
    }

    public PersonModel()
    {
        this.setName("None");
        this.setAge(0);
        this.setHeight(0);
        this.setWeight(0);
        this.setSex("None");
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
        if (this.getSex().equals("Male"))
        {
            this.TDEE=(10.0*this.getWeight()+6.25*this.getHeight()*100-5.0*this.getAge()+5.0);
        }
        else
        {
            this.TDEE=(10.0*this.getWeight()+6.25*this.getHeight()*100-5.0*this.getAge()+161.0);
        }

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
