public abstract class State {
    // TODO
    private String name;
    private boolean amIdone=false;//attribute for checking the State is finished or not
    private double counter=600;//counter for calculating Speed and Position operations and finish check
    private Position targetPos=null;
    private Position speed=null;

    public State(String nameIn){
        this.name=nameIn;

    }

    //GETTER and SETTER methods
    public String getName(){
        return name;

    }
    public void decCounter(int x){
        counter-=x;
    }
    public double getCounter(){
        return counter;
    }
    public boolean getAmIdone(){
        return amIdone;
    }
    public void setAmIdone(){
        amIdone=true;
    }
    public Position getSpeed() {
        return speed;
    }
    public Position getTargetPos(){
        return targetPos;
    }
    public void setTargetPos(Position currentPos){

        //set a new target Position
        targetPos=new Position(Common.getRandomGenerator().nextInt(1575),Common.getRandomGenerator().nextInt(670-59) + 59);
        speed=new Position(0,0);
        int rndFactor=Common.getRandomGenerator().nextInt(2)+1;

        speed.setX((targetPos.getX()-currentPos.getX())/counter*(rndFactor));
        speed.setY((targetPos.getY()-currentPos.getY())/counter*(rndFactor));




    }
    public void setClosestTargetPos(Agent agentIn){
        //generate random target position for Agent inside screen
        targetPos=new Position(Common.getRandomGenerator().nextInt(1575),Common.getRandomGenerator().nextInt(670-59) + 59);

        double closestDistance=targetPos.distanceTo(agentIn.getPosition().getX(),agentIn.getPosition().getY());
        double tempOrderDistance;
        for (Country x:Common.getCountries()){//iterate over all countries
            if (x.getName()!=agentIn.getMaster().getName()){//if this my Country pass this Country for order stealing
                for (Order y: x.getOrders()){//iterate over all Orders of Country

                        if (!y.getAmIdone()) {//if it's not done, it means it's alive and moving around
                            tempOrderDistance = y.getPosition().distanceTo(agentIn.getPosition().getX(), agentIn.getPosition().getY());//get this as a possible target Order
                            if (tempOrderDistance < closestDistance) {//if you find closer one pick this as a possible target Order
                                closestDistance = tempOrderDistance;
                                targetPos = y.getPosition();
                            }
                        }

                }
            }
        }


        speed=new Position(0,0);
        int rndFactor=Common.getRandomGenerator().nextInt(10)+1;//generate random speed vector amount

        speed.setX((targetPos.getX()-agentIn.getPosition().getX())/600*(rndFactor));//set speed X axis increment amount
        speed.setY((targetPos.getY()-agentIn.getPosition().getY())/600*(rndFactor));//set speed Y axis increment amount




    }
    public void getRandomState(Agent agentIn){
        int randomState=Common.getRandomGenerator().nextInt(4);//  Generate a random state
        switch (randomState){
            case 0:
                agentIn.setState(new Rest("Rest"));
                break;
            case 1:
                agentIn.setState(new Shake("Shake"));
                break;
            case 2:
                agentIn.setState(new GotoXY("GotoXY",agentIn));
                break;
            case 3:
                agentIn.setState(new ChaseClosest("ChaseClosest",agentIn));
                break;
        }

    }
    public abstract void move(Agent agent);
}