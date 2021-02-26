public abstract class AgentDecorator extends Agent {
    // pass all attributes to constructor
    public AgentDecorator(Agent agentIn,Level levelIn) {
        super(agentIn.getMaster(),agentIn.getName(),agentIn.getPosition().getX(),agentIn.getPosition().getY(),agentIn.getAgencyFlag(),agentIn.getScore(),agentIn.getState(),agentIn.getFont(),levelIn);
    }



}