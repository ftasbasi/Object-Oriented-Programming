package project.utility;

import project.SimulationRunner;
import project.components.Factory;
import project.components.Robot;
import project.parts.Base;
import project.parts.Part;

import java.lang.reflect.Field;
import java.util.Random;

public class Common
{
    public static Random random = new Random() ;
    //ABSTRACT FACTORY IMPLEMENTATION
    public abstract static class createFactory{
        public createFactory(){

        }
        public static Part generatePart(int serialNumber){
            return BaseFactory.generatePart(serialNumber);
        }
        public static Part generatePart(String name){
            return PartFactory.generatePart(name);
        }

    }

    public static class BaseFactory extends createFactory{
       public static Part generatePart(int serialNumber){
           try{

               Base retBase = Base.class.getConstructor(Integer.TYPE).newInstance(serialNumber);
               return retBase;
           }
           catch (Exception e){
               e.printStackTrace();
               throw new SmartFactoryException( "Failed: createBase!" );
           }
       }

    }

    public static class PartFactory extends createFactory{
        public static Part generatePart(String name){
            //creating a Part with respect to given parameter "name"
            try {
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
            }catch (Exception e){
                //e.printStackTrace();
                throw new SmartFactoryException( "Failed: createPart!" );

            }
        }


    }
    public static synchronized Object get (Object object , String fieldName )
    {
        // TODO
        // This function retrieves (gets) the private field of an object by using reflection
        // In case the function needs to throw an exception, throw this: SmartFactoryException( "Failed: get!" )
        Object value=null;
        try{

            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            value = field.get(object);

        }catch (Exception e){
            e.printStackTrace();
            throw new SmartFactoryException( "Failed: get!" );
        }

        return value;

    }

    public static synchronized void set ( Object object , String fieldName , Object value )
    {
        // TODO
        // This function modifies (sets) the private field of an object by using reflection
        // In case the function needs to throw an exception, throw this: SmartFactoryException( "Failed: set!" )
        try{

            Field fieldIn=object.getClass().getDeclaredField(fieldName);
            boolean isAccessible=fieldIn.canAccess(object);
            if (!isAccessible){
                fieldIn.setAccessible(true);
            }

            fieldIn.set(object,value);


        }catch (Exception e){
            e.printStackTrace();
            throw new SmartFactoryException( "Failed: set!" );
        }
    }
    public static void newstart (Robot newRobot)
    {
        //function for starting newly produced robots to speed up production
        new Thread( newRobot ).start() ;
    }
    public static Part createRandomPart(){
        Part retPart=null;
        switch ( Common.random.nextInt( 10 ) )
        {
            case 0:
                retPart = Factory.createPart("Arm");
                break;
            case 1:
                retPart = Factory.createPart("Builder");
                break;
            case 2:
                retPart = Factory.createPart("Fixer");
                break;
            case 3:
                retPart =Factory.createPart("Inspector");
                break;
            case 4:
                retPart = Factory.createPart("Supplier");
                break;
            case 5:
                retPart = Factory.createPart("Camera");
                break;
            case 6:
                retPart = Factory.createPart("Gripper");
                break;
            case 7:
                retPart = Factory.createPart("MaintenanceKit");
                break;
            case 8:
                retPart = Factory.createPart("Welder");
                break;
            case 9:
                retPart = Factory.createBase();
                break;
        }
        return retPart;
    }
}