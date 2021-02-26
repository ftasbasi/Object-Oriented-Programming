package project.parts.logics;

import project.SimulationRunner;
import project.components.Robot;
import project.parts.Arm;
import project.parts.payloads.Payload;
import project.utility.Common;
import project.utility.SmartFactoryException;

public class Inspector extends Logic
{
    @Override public void run ( Robot robot )
    {
        // TODO
        // Following messages are appropriate for this class

        // System.out.printf( "Robot %02d : Detected a broken robot (%02d), adding it to broken robots list.%n", ...);
        // System.out.printf( "Robot %02d : Notifying waiting fixers.%n", ...);

        int myID= (int) Common.get(robot,"serialNo");
        synchronized (SimulationRunner.factory.brokenRobots) {

                try {

                    for (Robot x : SimulationRunner.factory.robots) {
                        //iterate over robots
                        synchronized (x) {
                        Arm tempArm = (Arm) Common.get(x, "arm");
                        Logic tempLogic = (Logic) Common.get(x, "logic");
                        Payload tempPayload = (Payload) Common.get(x, "payload");

                        if (tempArm == null || tempLogic == null || tempPayload == null) {
                            //if you find any broken robot that is not placed into brokenRobots data type already, place it in.
                            if (!SimulationRunner.factory.brokenRobots.contains(x)) {
                                System.out.printf("Robot %02d : Detected a broken robot (%02d), adding it to broken robots list.%n", myID, (int) Common.get(x, "serialNo"));
                                SimulationRunner.factory.brokenRobots.add(x);
                                //notify fixers that are waiting
                                System.out.printf( "Robot %02d : Notifying waiting fixers.%n", myID);
                                SimulationRunner.factory.brokenRobots.notify();
                            }

                        }
                    }
                    }


                } catch (Exception e) {
                    throw new SmartFactoryException( "Failed: Inspector "+myID);
                }
        }

    }
}