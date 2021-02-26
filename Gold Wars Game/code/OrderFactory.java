
public abstract class OrderFactory {
    // TODO

    public OrderFactory()
    {

    }
    public static Order buildOrder(Country ownerIn)
    {   //random Order generation
        Order order=null;
        int rndFactor=Common.getRandomGenerator().nextInt(2);
        switch(rndFactor)
        {
            case 0:
                order = BuyOrderFactory.buildOrder(ownerIn);
                break;
            case 1:
                order = SellOrderFactory.buildOrder(ownerIn);
                break;
        }
        return order;

    }

}