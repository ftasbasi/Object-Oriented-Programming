package project.parts.logics;

import project.SimulationRunner;
import project.components.Robot;
import project.parts.Arm;
import project.parts.Base;
import project.parts.Part;
import project.parts.payloads.*;
import project.utility.Common;
import project.utility.SmartFactoryException;

import java.util.Iterator;

public class Builder extends Logic
{
    @Override public void run ( Robot robot )
    {

        // TODO
        // Following messages are appropriate for this class
        // System.out.printf("Robot %02d : Builder cannot build anything, waiting!%n", ...);
        // System.out.printf("Robot %02d : Builder woke up, going back to work.%n", ...);
        // System.out.printf("Robot %02d : Builder attached some parts or relocated a completed robot.%n", ...);

        int myID= (int) Common.get(robot,"serialNo");               //builder ID
        synchronized (SimulationRunner.factory.productionLine.parts){        //since we will do operations on parts, first lock this object

        try{
            while (SimulationRunner.factory.productionLine.parts.size() < 1) {          //if there is no part, then wait
                System.out.printf("Robot %02d : Builder cannot build anything, waiting!%n",myID);
                SimulationRunner.factory.productionLine.parts.wait();                   //this wait will be notified by Supplier
            }

            System.out.printf("Robot %02d : Builder woke up, going back to work.%n",myID);
            Part x=null;
            for (Part c:SimulationRunner.factory.productionLine.parts){                                     //check if there exist any Base, then pick it up
                synchronized (c){
                    if (c instanceof Base && (boolean)Common.get(c,"isWaiting")==false){
                        x=c;
                    }
                }
            }

            Part y=null;
            for (Iterator<Part> iterator = SimulationRunner.factory.productionLine.parts.iterator(); iterator.hasNext();) {         //check all parts on the productionLine whether it is suitable for connecting together or not
                boolean addedPart=false;
                y=iterator.next();
                synchronized (y){                       //while checking all parts on productionLine, make this check safe via locking with syncronized
                if (x == y) {                           //if it is our Base then ignore it.
                    continue;
                }
                if (addedPart){                         //if we added a part to our Base part then finish break this loop to prevent from multiple operation at once.
                    System.out.printf("Robot %02d : Builder attached some parts or relocated a completed robot.%n", myID);
                    break;
                }
                    if (x instanceof Base && (boolean)Common.get(x,"isWaiting")==false){
                    if (Common.get(x, "arm") == null && y instanceof Arm) {
                        Common.set(x, "arm", y);
                        addedPart=true;
                        iterator.remove();

                    } else if (Common.get(x, "payload") != null && Common.get(x, "arm") != null && Common.get(x, "logic") == null && y instanceof Logic) {

                        //if we have both Base+arm+payload then check for correct logic
                        Payload tmpPayload = (Payload) Common.get(x, "payload");
                        if (tmpPayload == null) {
                            Common.set(x, "logic", y);
                            addedPart=true;
                            iterator.remove();
                        } else {
                            //String typesPayload=tmpPayload.getClass().getName();
                            switch (tmpPayload.getClass().getName()) {
                                case "project.parts.payloads.Camera":
                                    if (y instanceof Inspector) {
                                        Common.set(x, "logic", y);
                                        addedPart=true;
                                        iterator.remove();
                                    }
                                    break;
                                case "project.parts.payloads.Gripper":
                                    if (y instanceof Supplier) {
                                        Common.set(x, "logic", y);
                                        addedPart=true;
                                        iterator.remove();
                                    }

                                    break;
                                case "project.parts.payloads.MaintenanceKit":
                                    if (y instanceof Fixer) {
                                        Common.set(x, "logic", y);
                                        addedPart=true;
                                        iterator.remove();
                                    }

                                    break;
                                case "project.parts.payloads.Welder":
                                    if (y instanceof Builder) {
                                        Common.set(x, "logic", y);
                                        addedPart=true;
                                        iterator.remove();
                                    }

                                    break;
                                default:
                                    break;
                            }
                        }

                    } else if (Common.get(x, "arm") != null && Common.get(x, "payload") == null && y instanceof Payload) {
                        //if we have both Base+arm, check for correct Payload
                        Logic tmpLogic = (Logic) Common.get(x, "logic");
                        //String typesLogic=tmpLogic.getClass().getName();
                        if (tmpLogic == null) {
                            Common.set(x, "payload", y);
                            addedPart=true;
                            iterator.remove();
                        } else {
                            switch (tmpLogic.getClass().getName()) {
                                case "project.parts.logics.Inspector":
                                    if (y instanceof Camera) {
                                        Common.set(x, "payload", y);
                                        addedPart=true;
                                        iterator.remove();
                                    }

                                    break;
                                case "project.parts.logics.Supplier":
                                    if (y instanceof Gripper) {
                                        Common.set(x, "payload", y);
                                        addedPart=true;
                                        iterator.remove();
                                    }

                                    break;
                                case "project.parts.logics.Fixer":
                                    if (y instanceof MaintenanceKit) {
                                        Common.set(x, "payload", y);
                                        addedPart=true;
                                        iterator.remove();
                                    }

                                    break;
                                case "project.parts.logics.Builder":
                                    if (y instanceof Welder) {
                                        Common.set(x, "payload", y);
                                        addedPart=true;
                                        iterator.remove();
                                    }

                                    break;
                                default:
                                    break;
                            }
                        }
                    }}
                }
            }

            if (x instanceof Base && Common.get(x, "arm") != null && Common.get(x, "payload") != null && Common.get(x, "logic") != null){
                //if this Base(Robot) is done then check if there is a space to place in Robots, if so place it in
                synchronized (SimulationRunner.factory.robots){
                    if(SimulationRunner.factory.robots.size()<SimulationRunner.factory.maxRobots){
                        SimulationRunner.factory.robots.add((Robot) x);
                        System.out.printf("Robot %02d : Builder attached some parts or relocated a completed robot.%n", myID);
                        synchronized (SimulationRunner.factory.productionLine.parts){
                            //remove produced robot from production line after adding it to correct place
                            SimulationRunner.factory.productionLine.parts.remove(x);

                            if (!SimulationRunner.factory.stopProduction){
                                Common.newstart((Robot)x);
                            }
                        }
                    }else{
                        synchronized (SimulationRunner.factory.storage){
                            if (SimulationRunner.factory.storage.robots.size()<SimulationRunner.factory.storage.maxCapacity){
                                //if there is no space in Robots to place, check if there is a space in Storage, if so place it in.
                                SimulationRunner.factory.storage.robots.add((Robot) x);
                                System.out.printf("Robot %02d : Builder attached some parts or relocated a completed robot.%n", myID);
                                synchronized (SimulationRunner.factory.productionLine.parts){
                                    //remove produced robot from production line after adding it to correct place
                                    SimulationRunner.factory.productionLine.parts.remove(x);
                                }

                            }
                        }

                    }


                }
            }
            synchronized (SimulationRunner.factory.storage.robots){
                if (SimulationRunner.factory.storage.robots.size()>=SimulationRunner.factory.storage.maxCapacity){
                    //if storage space is full, then stop the production.
                    SimulationRunner.factory.initiateStop();
                }
            }

            synchronized (SimulationRunner.productionLineDisplay){
                SimulationRunner.productionLineDisplay.repaint();
            }

            synchronized (SimulationRunner.robotsDisplay){
                SimulationRunner.robotsDisplay.repaint();
            }
            synchronized (SimulationRunner.storageDisplay){
                SimulationRunner.storageDisplay.repaint();
            }

        }catch (Exception e){
            throw new SmartFactoryException( "Failed: Builder "+myID);
        }

        }

    }


}