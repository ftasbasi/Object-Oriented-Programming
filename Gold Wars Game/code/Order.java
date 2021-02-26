import java.awt.*;

enum OrderType
{
    BUY, SELL
}
public abstract class Order extends Entity {
    private Country master;//owner Country
    private static OrderType type = null;
    private final int amount=Common.getRandomGenerator().nextInt(4)+1;//random amount of Gold to sell or buy
    private boolean amIdone=false;//Order attribute for checking whether it's done or not
    private Position targetPos=null;//random target of Order
    private Position speed=null;//speed of Order
    private Font font=new Font("Default",Font.BOLD, (int) 12);
    Order(OrderType typeIn,Country owner)
    {
        super(owner.position.getX(),owner.position.getY());
        master=owner;
        this.type = typeIn;
        setTargetPos(this.position);

    }
    //GETTER and SETTER methods
    public OrderType getType(){
        return type;
    }
    public Font getFont(){
        return font;
    }
    public Position getSpeed(){return speed;}
    public Country getMaster(){return master;}
    abstract void construct();
    abstract void executeOrder();//abstract class to be overridden in concrete subclasses
    public boolean getAmIdone(){
        return amIdone;
    }
    public void setAmIdone(){
        amIdone=true;
    }
    public int getAmount(){
        return amount;
    }
    public void drawRemaining(Graphics2D g2d,int x,int y){//this function is helper for draw() function to reduce code duplication
        g2d.fillOval(x - 10, y - 10,20,20);
        g2d.drawString(Common.country_order_map.get(this.getMaster().getName()),x-10,y-15);// Put country tag to top part of order
        g2d.setColor(Color.BLACK);// Put text into circle
        g2d.drawString(String.valueOf(getAmount()), x - 5, y + 5);// Put text into circle
    }
    public void setTargetPos(Position currentPos){


        targetPos=new Position(Common.getRandomGenerator().nextInt(1575),60);//generate random target in screen
        speed=new Position(0,0);
        int rndFactor=Common.getRandomGenerator().nextInt(2)+1;
        speed.setX((targetPos.getX()-currentPos.getX())/600*(rndFactor));//random speed generation
        speed.setY((targetPos.getY()-currentPos.getY())/600*(rndFactor));




    }

}