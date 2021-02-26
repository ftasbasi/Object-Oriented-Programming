import java.awt.*;

public class SellOrder extends Order {

    SellOrder(Country ownerIn)
    {
        super(OrderType.SELL,ownerIn);
        construct();
    }
    //METHODS
    @Override
    protected void construct()
    {
        //System.out.println("Connecting to Sell Order\n");
    }
    @Override
    void executeOrder() {
        if (this.getMaster().getGold() >= getAmount()) {//check whether we have enough gold to sell
            getMaster().setWorth(-getAmount(), (int) (+getAmount() * Common.getGoldPrice().getCurrentPrice()));
        }
    }
    @Override
    public void draw(Graphics2D g2d) {
        if (!getAmIdone()) {
        int x = position.getIntX();
        int y = position.getIntY();
        g2d.setFont(getFont());
        g2d.setColor(Color.PINK);
        drawRemaining(g2d,x,y);
        }
    }

    @Override
    public void step() {
        if(this.position.getY()<59){//if you hit the UpperY line execute the Order and set it as FINISHED

            executeOrder();
            setAmIdone();

        }else {
            //else move it
            this.position.setX(this.position.getX() + getSpeed().getX());

            this.position.setY(this.position.getY() + getSpeed().getY());

        }

    }
}