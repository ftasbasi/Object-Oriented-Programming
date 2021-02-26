import java.awt.*;
import java.awt.image.BufferedImage;

public class BuyOrder extends Order {
    // TODO
    BuyOrder(Country owner)
    {
        super(OrderType.BUY,owner);
        construct();

    }

    //METHODS
    @Override
    protected void construct()
    {
        //System.out.println("Connecting to Buy Order\n");
    }

    @Override
    void executeOrder() {//order execution
        int operationAmount= (int) (getAmount()*Common.getGoldPrice().getCurrentPrice());
        if (this.getMaster().getCash()>=operationAmount){               //check whether we have enough cash to buy
            getMaster().setWorth(+getAmount(), operationAmount);
        }
    }

    public void draw(Graphics2D g2d) {
        if (!getAmIdone()) {
            int x = position.getIntX();
            int y = position.getIntY();
            g2d.setFont(getFont());
            g2d.setColor(Color.GREEN);
            drawRemaining(g2d,x,y);
        }
    }

    @Override
    public void step() {
        if(this.position.getY()<59){ //if you hit the UpperY line execute the Order and set it as FINISHED
            executeOrder();
            setAmIdone();
        }else {
            //else move it
            this.position.setX(this.position.getX() + getSpeed().getX());

            this.position.setY(this.position.getY() + getSpeed().getY());
        }
    }
}