import java.util.*;


public class Common {
    private static final String title = "Gold Wars";
    private static final int windowWidth = 1650;
    private static final int windowHeight = 1000;

    private static final GoldPrice goldPrice = new GoldPrice(400, 45);

    private static final Random randomGenerator = new Random(1234);
    private static final int upperLineY = 60;

    private static final int countryCount=5;




    public static Map<String, String> country_agent_map = new HashMap<String, String>();//a map for country-agent pairs
    public static Map<String, String> country_order_map = new HashMap<String, String>();//a map for country-order pairs




    private static List<Country> countries= new ArrayList<>();
    private static List<Agent> agencies= new ArrayList<>();

    static  {
        // TODO: Here, you can initialize the fields you have declared
        country_agent_map.put("usa","cia");
        country_agent_map.put("israel","mossad");
        country_agent_map.put("turkey","mit");
        country_agent_map.put("china","mss");
        country_agent_map.put("russia","svr");
        country_order_map.put("usa","US");
        country_order_map.put("israel","IL");
        country_order_map.put("turkey","TR");
        country_order_map.put("china","CN");
        country_order_map.put("russia","RU");
        String countryName=null;
        for (int i=0;i<countryCount;i++){
            //Generate Countries and Agents with relations
            countryName=country_agent_map.keySet().toArray()[i].toString();
            countries.add(new Country(countryName,200+(290*i),750));
            agencies.add(new BasicAgent(countries.get(i),country_agent_map.get(countryName),200+(290*i),500));

        }
    }
    //GETTER and SETTER methods
    public static List<Country> getCountries(){
        return countries;
    }
    public static List<Agent> getAgents(){
        return agencies;
    }


    public static String getTitle() { return title; }
    public static int getWindowWidth() { return windowWidth; }
    public static int getWindowHeight() { return windowHeight; }

    public static GoldPrice getGoldPrice() { return goldPrice; }

    public static Random getRandomGenerator() { return randomGenerator; }
    public static int getUpperLineY() { return upperLineY; }

    public static void stepAllEntities() {
        if (randomGenerator.nextInt(200) == 0) goldPrice.step();
        // TODO
        randomGenerator.nextInt();
        for (Country country:countries){
            country.step();

        }
        int index=0;
       for (Agent agent:agencies){
           //decorate agents if needed
           if (agent.getScore()>6000 && agent.myLevel==Level.MASTER){
               agencies.set(index,new Expert(agent));
           }else if (agent.getScore()>4000 && agent.myLevel==Level.NOVICE){
               agencies.set(index,new Master(agent));
           }else if(agent.getScore()>2000 && agent.myLevel==null){
               agencies.set(index,new Novice(agent));
           }else{}

           agent.step();
           index+=1;
        }

    }
}