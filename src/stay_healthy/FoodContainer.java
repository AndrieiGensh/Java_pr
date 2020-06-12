package stay_healthy;

public class FoodContainer implements Container
{
    private FoodModel[] foodArray;
    private double kcal = 0.0;
    private double proteins = 0.0;
    private double fats = 0.0;
    private double carbons = 0.0;

    public  FoodContainer(FoodModel[] foodArray)
    {
        this.foodArray = foodArray.clone();
    }

    public void sumUp()
    {
        for (FoodModel foodModel : foodArray) {
            this.kcal += foodModel.getKcal();
            this.proteins += foodModel.getProteins();
            this.fats += foodModel.getFats();
            this.carbons += foodModel.getCarbons();
        }
    }

    public double getKcal()
    {
        return  this.kcal;
    }

    public double getCarbons()
    {
        return carbons;
    }

    public double getFats()
    {
        return fats;
    }

    public double getProteins()
    {
        return proteins;
    }

    public int getArraySize()
    {
        return this.foodArray.length;
    }

    @Override
    public Iterator getIterator()
    {
        return new FoodIterator();
    }

    private class FoodIterator implements  Iterator
    {
        int food_index;

        @Override
        public boolean has_next_item()
        {
            return food_index < foodArray.length;
        }

        @Override
        public Object next_item()
        {
            if(this.has_next_item())
            {
                return foodArray[food_index++];
            }
            return null;
        }
    }


}
