import java.awt.*;

public class Novice extends AgentDecorator {
    public Novice(Agent agentIN) {
        super(agentIN,Level.NOVICE);


    }
    // TODO
    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        drawBadge(g2d);
    }
    public void drawBadge(Graphics2D g2d){
        g2d.setColor(Color.WHITE);
        g2d.fillRect(this.getPosition().getIntX(),this.getPosition().getIntY()-50,20,20);//draw NOVICE badge

    }
}