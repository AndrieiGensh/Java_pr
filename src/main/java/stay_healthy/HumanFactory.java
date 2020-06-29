package stay_healthy;

public class HumanFactory
{
    public BodyModel getNewHuman(String sex)
    {
        if(sex.equalsIgnoreCase("Male"))
        {
            return new MaleModel();
        }
        else
        {
            if(sex.equalsIgnoreCase("Female"))
            {
                return new FemaleModel();
            }
            else
            {
                return null;
            }
        }
    }

}
