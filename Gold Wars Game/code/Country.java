import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Country extends Entity {
    private Agent myAgent;
    private final int orderCount=2;//how many order does a country own at a time
    private final String name;//name of Country
    private int orderGeneratorCounter=0;
    public Order[] orders=new Order[orderCount];//order table for Country, all orders are placed into this array
    private int gold=50;
    private double cash=10000;
    private Font font=new Font("Arial",Font.BOLD,18);
    private State state=new Rest("Rest");//state initialization for country
    public Country(String nameIn,double x, double y) {
        super(x, y);
        this.name=nameIn;

        for (int i=0;i<orderCount;i++){
            orders[i]=OrderFactory.buildOrder(this);//generate random Order for this country
            if(Common.getRandomGenerator().nextInt(2)==1) {//it's for making randomization better
                orders[i].setAmIdone();
            }
        }
        try {
            countryFlag= ImageIO.read(new File("images/"+name+".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //GETTER and SETTER methods
    public double getWorth(){
        return cash+gold*Common.getGoldPrice().getCurrentPrice();
    }
    public void setWorth(int goldDelta,int cashDelta){
        gold+=goldDelta;
        cash+=cashDelta;
    }
    public double getCash(){
        return cash;
    }
    public int getGold(){return gold;}
    private BufferedImage countryFlag=null;
    public String getName(){
        return name;
    }
    public Agent getMyAgent(){
        return myAgent;
    }
    @Override
    public void draw(Graphics2D g2d) {

        g2d.drawImage(countryFlag,position.getIntX(),position.getIntY(),100,100,null);
        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        g2d.drawString(String.format("%s",name.toUpperCase()),position.getIntX(),position.getIntY()+120);
        g2d.setColor(Color.ORANGE);
        g2d.drawString(String.format("%d gold",gold),position.getIntX(),position.getIntY()+140);
        g2d.setColor(Color.green);
        g2d.drawString(String.format("%d cash",(int) getCash()),position.getIntX(),position.getIntY()+160);
        g2d.setColor(Color.blue);
        g2d.drawString(String.format("Worth: %d",(int) getWorth()),position.getIntX(),position.getIntY()+180);

    }
    public Order[] getOrders(){
        return orders;
    }
    @Override
    public void step() {

        for (int x=0;x<orderCount;x++){

            if (orders[x].getAmIdone() && orderGeneratorCounter%800!=0){//if Order is finished and my Order generation counter is valid then generate an Order
                orderGeneratorCounter = 1;
                if(Common.getRandomGenerator().nextInt(2)==1) {//this is for better randomization of Order generation
                    Order newOrder=OrderFactory.buildOrder(this);
                    if (newOrder.getType()==OrderType.BUY){
                        if(this.getCash()>=newOrder.getAmount()*Common.getGoldPrice().getCurrentPrice()){// If Country have enough cash to BUY this amount, own this new Order
                            orders[x] =newOrder;
                        }

                    }else {
                        if(this.getGold()>=newOrder.getAmount()){// If Country have enough Gold to Sell for this amount, own this new Order
                            orders[x] =newOrder;
                        }

                    }

                }
            }else {
                orders[x].step();//if Order is not finished them move it
            }

        }

        orderGeneratorCounter++;//increase the Order generation counter

    }
}