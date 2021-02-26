package project.parts.logics;

import project.SimulationRunner;
import project.components.Factory;
import project.components.Robot;
import project.parts.payloads.Payload;
import project.utility.Common;
import project.utility.SmartFactoryException;

import java.util.Iterator;

public class Fixer extends Logic
{
    @Override public void run ( Robot robot )
    {
        // TODO
        // Following messages are appropriate for this class
        // System.out.printf("Robot %02d : Fixed and waken up robot (%02d).%n", ...);
        // System.out.printf("Robot %02d : Nothing to fix, waiting!%n", ...);
        // System.out.printf("Robot %02d : Fixer woke up, going back to work.%n", ...);
        int myID = (int) Common.get(robot, "serialNo"); //fixer ID

        synchronized (SimulationRunner.factory.brokenRobots) {
            try {
                //wait for signal from Inspector if there is no brokenRobot to fix
                while (SimulationRunner.factory.brokenRobots.size() < 1) {
                    if (SimulationRunner.factory.stopProduction){
                        break;
                    }
                    System.out.printf("Robot %02d : Nothing to fix, waiting!%n", myID);
                    SimulationRunner.factory.brokenRobots.wait();
                }
                System.out.printf("Robot %02d : Fixer woke up, going back to work.%n", myID);

                Robot x=null;
                boolean fixed=false;
                for (Iterator<Robot> iterator = SimulationRunner.factory.brokenRobots.iterator(); iterator.hasNext();) {
                    //iterate over all brokenRobots

                        x=iterator.next();
                        synchronized (x) {
                            //safe the brokenRobot while working on it
                            if (Common.get(x, "arm") == null) {
                                //fix the arm
                                Common.set(x, "arm", Factory.createPart("Arm"));

                            }
                            else if (Common.get(x, "logic") == null) {
                                Payload tmpPayload= (Payload) Common.get(x, "payload");
                                //if logic is broken, fix it
                                switch (tmpPayload.getClass().getName()){
                                    case "project.parts.payloads.Camera":
                                        Common.set( x , "logic"   , Factory.createPart("Inspector"));
                                        break;
                                    case "project.parts.payloads.Gripper":
                                        Common.set( x , "logic"   , Factory.createPart("Supplier"));
                                        break;
                                    case "project.parts.payloads.MaintenanceKit":
                                        Common.set( x , "logic"   , Factory.createPart("Fixer"));
                                        break;
                                    case "project.parts.payloads.Welder":
                                        Common.set( x , "logic"   , Factory.createPart("Builder"));
                                        break;
                                    default:
                                        break;
                                }

                            }
                            else if (Common.get(x, "payload") == null) {
                                Logic tmpLogic= (Logic) Common.get(x, "logic");
                                //if payload is broken, fix it
                                switch (tmpLogic.getClass().getName()){
                                    case "project.parts.logics.Inspector":
                                        Common.set( x , "payload"   , Factory.createPart("Camera"));
                                        break;
                                    case "project.parts.logics.Supplier":
                                        Common.set( x , "payload"   , Factory.createPart("Gripper"));
                                        break;
                                    case "project.parts.logics.Fixer":
                                        Common.set( x , "payload"   , Factory.createPart("MaintenanceKit"));
                                        break;
                                    case "project.parts.logics.Builder":
                                        Common.set( x , "payload"   , Factory.createPart("Welder"));
                                        break;
                                    default:
                                        break;
                                }
                            }

                            if (Common.get(x, "arm") != null && Common.get(x, "logic") != null && Common.get(x, "payload") != null) {
                                //if evertyhing is fine (Arm+Payload+Logic), set it as fixed Robot, remove from brokenRobots, then wake it up.
                                fixed=true;
                                iterator.remove();
                                int herID = (int) Common.get(x, "serialNo");
                                System.out.printf("Robot %02d : Fixed and waken up robot (%02d).%n", myID, herID);
                                x.notify();

                            }
                        }
                    if (fixed){
                        //if ay brokenRobot is fixed then end the loop to prevent from multiple robot fix at once.
                        break;
                    }
                }


            } catch (Exception e) {
                throw new SmartFactoryException( "Failed: Fixer "+myID);
                //e.printStackTrace();
            }

        }
    }
}