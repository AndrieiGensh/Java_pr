import org.junit.Assert;
import  org.junit.Test;
import stay_healthy.HumanFactory;

public class HumanFactoryTest
{
    private HumanFactory humanFactory = new HumanFactory();
    @Test
    public void ReturnObjectSexTest()
    {
        Assert.assertTrue((humanFactory.getNewHuman("Male")).getSex().equalsIgnoreCase("Male"));
        Assert.assertTrue((humanFactory.getNewHuman("Female")).getSex().equalsIgnoreCase("Female"));
        Assert.assertNull((humanFactory.getNewHuman("NSHdgf")));
    }
}
