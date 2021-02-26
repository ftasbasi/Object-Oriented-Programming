import java.awt.*;

public class Expert extends AgentDecorator {
    public Expert(Agent agentIN) {
        super( agentIN,Level.EXPERT);

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
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(this.getPosition().getIntX()+25,this.getPosition().getIntY()-50,20,20);//draw MASTER badge
        g2d.setColor(Color.RED);
        g2d.fillRect(this.getPosition().getIntX()+50,this.getPosition().getIntY()-50,20,20);//draw EXPERT badge
    }
}