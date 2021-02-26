public class BuyOrderFactory extends OrderFactory {
    // TODO
    public static Order buildOrder(Country owner)
    {

        Order order =  new BuyOrder(owner);
        return order;
    }
}