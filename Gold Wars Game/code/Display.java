import javax.swing.*;
import java.awt.*;

public class Display extends JPanel {
    public Display() { this.setBackground(new Color(180, 180, 180)); }

    @Override
    public Dimension getPreferredSize() { return super.getPreferredSize(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Common.getGoldPrice().draw((Graphics2D) g);
        g.drawLine(0, Common.getUpperLineY(), Common.getWindowWidth(), Common.getUpperLineY());
        // TODO
        for (Country country:Common.getCountries()){
            country.draw((Graphics2D) g);

            for (Order x: country.getOrders()){//draw all Orders of current country
                if (!x.getAmIdone()){
                    //if Order is not finished then draw it
                    x.draw((Graphics2D) g);
                }
            }

        }
        for (Agent agent:Common.getAgents()){
            agent.draw((Graphics2D) g);
        }
    }
}