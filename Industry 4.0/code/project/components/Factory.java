package project.components;
import project.SimulationRunner;
import project.parts.Base;
import project.parts.Part;
import project.utility.Common;
import project.utility.SmartFactoryException;
import java.util.ArrayList;
import java.util.List;

public class Factory
{
    private static int nextSerialNo = 1 ;

    public static Base createBase ()
    {
        // TODO
        // This function returns a base by applying factory and abstract factory patterns

        //return new Base(nextSerialNo-1);
        try{
            nextSerialNo+=1;
            //ABSTRACT FACTORY USAGE
            return (Base) Common.createFactory.generatePart(nextSerialNo-1);
        }
        catch (Exception e){
            //e.printStackTrace();
            throw new SmartFactoryException( "Failed: createBase!" );
        }

    }

    public static Part createPart (String name )
    {
        // TODO
        // This function returns a robot part by applying factory and abstract factory patterns
        // In case the function needs to throw an exception, throw this: SmartFactoryException( "Failed: createPart!" )


        //creating a Part with respect to given parameter "name"
        try {
            /*
            Class obj=null;
            Part retObj=null;
            switch (name){
                case "Arm":
                    obj=Class.forName("project.parts."+name);
                    break;
                case "Builder":
                case "Fixer":
                case "Inspector":
                case "Supplier":
                    obj=Class.forName("project.parts.logics."+name);
                    break;
                case "Camera":
                case "Gripper":
                case "MaintenanceKit":
                case "Welder":
                    obj=Class.forName("project.parts.payloads."+name);
                    break;
                default:
                    break;

            }
            retObj= (Part) obj.newInstance();
            return retObj;

             */

            //ABSTRACT FACTORY USAGE
            return Common.createFactory.generatePart(name);

        }catch (Exception e){
            //e.printStackTrace();
            throw new SmartFactoryException( "Failed: createPart!" );

        }

    }

    public  int            maxRobots      ;
    public List<Robot>     robots         ;
    public ProductionLine  productionLine ;
    public  Storage        storage        ;
    public  List<Robot>    brokenRobots   ;
    public  boolean        stopProduction ;

    public Factory ( int maxRobots , int maxProductionLineCapacity , int maxStorageCapacity )
    {
        this.maxRobots      = maxRobots                                       ;
        this.robots         = new ArrayList<>()                               ;
        this.productionLine = new ProductionLine( maxProductionLineCapacity ) ;
        this.storage        = new Storage( maxStorageCapacity        )        ;
        this.brokenRobots   = new ArrayList<>()                               ;
        this.stopProduction = false                                           ;

        Base robot ;

        robot = createBase()                                             ;
        Common.set( robot , "arm"     , createPart( "Arm"            ) ) ;
        Common.set( robot , "payload" , createPart( "Gripper"        ) ) ;
        Common.set( robot , "logic"   , createPart( "Supplier"       ) ) ;
        robots.add(robot ) ;

        robot = createBase()                                             ;
        Common.set( robot , "arm"     , createPart( "Arm"            ) ) ;
        Common.set( robot , "payload" , createPart( "Welder"         ) ) ;
        Common.set( robot , "logic"   , createPart( "Builder"        ) ) ;
        robots.add(robot ) ;

        robot = createBase()                                             ;
        Common.set( robot , "arm"     , createPart( "Arm"            ) ) ;
        Common.set( robot , "payload" , createPart( "Camera"         ) ) ;
        Common.set( robot , "logic"   , createPart( "Inspector"      ) ) ;
        robots.add(robot ) ;

        robot = createBase()                                             ;
        Common.set( robot , "arm"     , createPart( "Arm"            ) ) ;
        Common.set( robot , "payload" , createPart( "Camera"         ) ) ;
        Common.set( robot , "logic"   , createPart( "Inspector"      ) ) ;
        robots.add(robot ) ;

        robot = createBase()                                             ;
        Common.set( robot , "arm"     , createPart( "Arm"            ) ) ;
        Common.set( robot , "payload" , createPart( "MaintenanceKit" ) ) ;
        Common.set( robot , "logic"   , createPart( "Fixer"          ) ) ;
        robots.add(robot ) ;

        robot = createBase()                                             ;
        Common.set( robot , "arm"     , createPart( "Arm"            ) ) ;
        Common.set( robot , "payload" , createPart( "MaintenanceKit" ) ) ;
        Common.set( robot , "logic"   , createPart( "Fixer"          ) ) ;
        robots.add(robot ) ;
    }

    public void start ()
    {
        for ( Robot r : robots )  { new Thread( r ).start() ; }
    }

    public void initiateStop ()
    {
        stopProduction = true ;

        synchronized ( robots )
        {
            for ( Robot r : robots )  { synchronized ( r )  { r.notifyAll() ; } }
        }

        synchronized ( productionLine )  { productionLine.notifyAll() ; }
        synchronized ( brokenRobots   )  { brokenRobots  .notifyAll() ; }
    }
}