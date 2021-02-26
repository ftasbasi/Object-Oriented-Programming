public class ChaseClosest extends State {

    public ChaseClosest(String nameIn, Agent agentIn) {
        super(nameIn);
        setClosestTargetPos(agentIn);
    }

    @Override
    public void move(Agent agentIn) {

        if(getCounter()<0){//if State step count is over then it is FINISHED
            setAmIdone();//set it as finished
        }else {
            setClosestTargetPos(agentIn);//if you reached to target but you have remaining steps to move then select new target then move.
            agentIn.position.setX(agentIn.position.getX() + getSpeed().getX());
            agentIn.position.setY(agentIn.position.getY() + getSpeed().getY());
        }
        decCounter(1);//decrement the step counter
    }
    // TODO
}