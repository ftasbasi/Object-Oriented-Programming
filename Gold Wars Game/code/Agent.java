import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
enum Level
{
    NOVICE, MASTER, EXPERT
}
public abstract class Agent extends Entity {
    // TODO
    private int score=0;// attribute for keeping the stolen amount score
    private Country master;// attribute for keeping Country information that owns this Agent
    private final String name;// attribute for keeping name of Agent
    protected Level myLevel=null;// attribute for keeping level of Agent
    private State state=new Rest("Rest");// initialization of Agent state
    private Font font=new Font("Arial",Font.BOLD,18);//font of Agent
    private BufferedImage agencyFlag=null;//image for Agent


    public Agent(Country countryIn, String nameIn, double x, double y) {
        super(x, y);
        this.name=nameIn;
        state.getRandomState(this);// starting with random state at the beginning

        try {
            agencyFlag= ImageIO.read(new File("images/"+name+".png"));//read image from file
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.master=countryIn;
    }
    public Agent(Country countryIn, String nameIn, double x, double y,BufferedImage agencyFlagIn,int scoreIn,State stateIn,Font fontIn,Level levelIn) {
        super(x, y);//constructor for decorator operations
        this.name=nameIn;
        this.agencyFlag=agencyFlagIn;
        this.score=scoreIn;
        this.state=stateIn;
        this.font=fontIn;
        this.myLevel=levelIn;
        this.master=countryIn;
    }
    //GETTER and SETTER methods
    public Level getMyLevel() {
        return myLevel;
    }
    public BufferedImage getAgencyFlag(){
        return agencyFlag;
    }
    public State getState(){
        return state;

    }
    public Font getFont() {
        return font;
    }
    public Country getMaster(){
        return master;
    }
    public String getName(){
        return name;
    }
    public void setState(State stateIn){
        this.state=stateIn;
    }
    public int getScore(){
        return score;
    }
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setFont(font);//draw method for Agent
        g2d.setColor(Color.BLACK);
        g2d.drawString(String.format("%s",name.toUpperCase()),position.getIntX()+10,position.getIntY()-5);
        g2d.drawImage(agencyFlag,position.getIntX(),position.getIntY(),100,100,null);
        g2d.setColor(Color.blue);
        g2d.drawString(String.format("%s",state.getName()),position.getIntX(),position.getIntY()+115);
        g2d.setColor(Color.RED);
        g2d.drawString(String.format("%d",score),position.getIntX(),position.getIntY()+130);



    }
    @Override
    public void step() {
        int tempStolen;
        for (Country x:Common.getCountries()){// iterate over all Countries
            if (x.getName()!=this.master.getName()){// if it is not my owner Country then act
                for (Order y: x.getOrders()) {
                    // iterate over all Orders of Countries
                        if (y.position.distanceTo(getPosition().getX(), getPosition().getY()) <= 50 && !y.getAmIdone()) {// catch the order if it's in my radius and not FINISHED
                            tempStolen = (int) (y.getAmount() * Common.getGoldPrice().getCurrentPrice());
                            if (y.getType() == OrderType.BUY && tempStolen <= y.getMaster().getCash()) {// steal the cash if it's a Buy Order
                                y.getMaster().setWorth(0, -tempStolen);
                                this.master.setWorth(0, (int) tempStolen);
                            } else {
                                if (y.getAmount() <= y.getMaster().getGold()) {// steal the gold if it's a Sell Order
                                    y.getMaster().setWorth(-y.getAmount(), 0);
                                    this.master.setWorth(y.getAmount(), 0);
                                }
                            }
                            score += tempStolen;
                            y.setAmIdone();// finish the order
                }
                }
            }

        }
        if (state.getAmIdone()){
            state.getRandomState( this);
        }
        state.move(this);




    }



}