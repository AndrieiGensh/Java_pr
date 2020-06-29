package stay_healthy;


import java.util.ArrayList;

public interface BodyModel
{

    void copyModel(BodyModel model);

    void setAge(int age);

    void setHeight(double height);

    void setWeight(double weight);

    int getAge();

    double getHeight();

    double getWeight();

    String getSex();

    void setId(int id);

    void setName(String name);

    void setActivity_level(String activity_level);

    int getId();

    String getName();

    String getActivity_level();

    void claculateTDEE(String choice);

    void calculateNeeds();

    ArrayList<Double> getNeeds();
}
