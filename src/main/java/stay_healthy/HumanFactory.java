package stay_healthy;

public class HumanFactory
{
    public BodyModel getNewHuman(String sex)
    {
        if(sex.equalsIgnoreCase("Male"))
        {
            System.out.println("this is a " + sex);
            return new MaleModel();
        }
        else
        {
            if(sex.equalsIgnoreCase("Female"))
            {
                System.out.println("this is a " + sex);
                return new FemaleModel();
            }
            else
            {
                System.out.println("this is a no one");
                return null;
            }
        }
    }

}
