import java.util.Timer;
import java.util.TimerTask;

public class GotoXY extends State {

    public GotoXY(String nameIn, Agent agentIn) {
        super(nameIn);
        setTargetPos(agentIn.position);
    }
    public void move(Agent agentIn) {
        if(getCounter()<0){//if State step count is over then it is FINISHED
            setAmIdone();
        }else if (agentIn.position==getTargetPos() || agentIn.position.distanceTo(getTargetPos().getX(),getTargetPos().getY())<=getSpeed().distanceTo(0.0,0.0)){
            //if I'm on the target (or with a distance smaller than speed increment amount) then set current State as FINISHED
            setAmIdone();
        }else {
            //otherwise move the Agent
            agentIn.position.setX(agentIn.position.getX() + getSpeed().getX());
            agentIn.position.setY(agentIn.position.getY() + getSpeed().getY());

        }

        decCounter(1);//decrement step counter

    }
}