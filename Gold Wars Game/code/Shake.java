public class Shake extends State {

    public Shake(String nameIn) {
        super(nameIn);
    }

    @Override
    public void move(Agent agentIn) {

            if(getCounter()<0){//if State step count is over then it is FINISHED
                setAmIdone();
            }else {
                //SHAKE(change) the position in every 5 steps
            switch ((int) (getCounter()%5)) {
                case 0:
                    agentIn.position.setX(agentIn.position.getIntX() + 1);//RIGHT
                    decCounter(1);
                    break;
                case 1:
                    agentIn.position.setX(agentIn.position.getIntX() - 2);//LEFT
                    decCounter(1);
                    break;
                case 2:
                    agentIn.position.setX(agentIn.position.getIntX() + 1);//UP
                    agentIn.position.setY(agentIn.position.getIntY() + 1);
                    decCounter(1);
                    break;
                case 3:
                    agentIn.position.setY(agentIn.position.getIntY() - 2);//DOWN
                    decCounter(1);
                    break;
                case 4:
                    agentIn.position.setY(agentIn.position.getIntY() + 1);//ORIGINAL POSITION
                    decCounter(1);
                    break;
            }

        }

    }



    // TODO
}