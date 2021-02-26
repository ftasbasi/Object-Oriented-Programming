public class SellOrderFactory extends OrderFactory {
    // TODO
    public static Order buildOrder(Country owner)
    {
        Order order =  new SellOrder(owner);
        return order;

    }
}