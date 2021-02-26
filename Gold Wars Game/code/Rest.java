public class Rest extends State {
    public Rest(String nameIn) {
        super(nameIn);
    }

    @Override
    public void move(Agent agent) {
        //Just do nothing while waiting to counter is done for FINISH
        if(getCounter()<0){
            setAmIdone();
        }else {
            decCounter(1);
        }
    }
}