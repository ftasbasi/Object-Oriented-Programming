package project.parts.logics;
import project.SimulationRunner;
import project.components.Robot;
import project.utility.Common;
import project.utility.SmartFactoryException;

import java.awt.*;
import java.util.List;

public class Supplier extends Logic
{
    @Override public void run ( Robot robot )
    {

        // TODO
        // Following messages are appropriate for this class
        // System.out.printf( "Robot %02d : Supplying a random part on production line.%n", ...);
        // System.out.printf( "Robot %02d : Production line is full, removing a random part from production line.%n", ...);
        // System.out.printf( "Robot %02d : Waking up waiting builders.%n", ...);
        int myID= (int) Common.get(robot,"serialNo");
    synchronized (SimulationRunner.factory.productionLine.parts){
        try{


            int currentProductionSize= SimulationRunner.factory.productionLine.parts.size();
            int lineCapacity=SimulationRunner.factory.productionLine.maxCapacity;
            boolean work=currentProductionSize<lineCapacity;

            //if there is a free space in productionLine, fill it with new randomly generated part, otherwise remove randomly selected part from productionLine
            if (!work){
                int rndLineNumber=Common.random.nextInt( lineCapacity);
                System.out.printf( "Robot %02d : Production line is full, removing a random part from production line.%n", myID);
                SimulationRunner.factory.productionLine.parts.remove(rndLineNumber);

            }else {
                System.out.printf( "Robot %02d : Supplying a random part on production line.%n",myID);
                SimulationRunner.factory.productionLine.parts.add(Common.createRandomPart());

            }

            synchronized (SimulationRunner.productionLineDisplay){
                SimulationRunner.productionLineDisplay.repaint();
            }

            //notify builders
            System.out.printf( "Robot %02d : Waking up waiting builders.%n", myID);
            SimulationRunner.factory.productionLine.parts.notify();



        }catch (Exception e){
            throw new SmartFactoryException( "Failed: Supplier "+myID);
        }

    }

    }
}