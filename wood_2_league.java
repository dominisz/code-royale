import java.util.*;
import java.io.*;
import java.math.*;

class Player {

    private static List<Spot> listOfSpots = new ArrayList<>();

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int numSites = in.nextInt();
        for (int i = 0; i < numSites; i++) {
            int siteId = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();
            int radius = in.nextInt();
            Spot spot = new Spot(siteId, x, y, radius);
            listOfSpots.add(spot);
        }

        listOfSpots.sort(Comparator.comparing(spot -> spot.getId()));
        int counter = 0;
        // game loop
        while (true) {
            List<CurrentSpot> listOfCurrentSpots = new ArrayList<>();
            int gold = in.nextInt();
            int touchedSite = in.nextInt(); // -1 if none
            for (int i = 0; i < numSites; i++) {
                int siteId = in.nextInt();
                int ignore1 = in.nextInt(); // used in future leagues
                int ignore2 = in.nextInt(); // used in future leagues
                int structureType = in.nextInt(); // -1 = No structure, 1 = Tower, 2 = Barracks
                int owner = in.nextInt(); // -1 = No structure, 0 = Friendly, 1 = Enemy
                int param1 = in.nextInt();
                int param2 = in.nextInt();
                for(Spot spot : listOfSpots){
                    if(spot.getId() == siteId){
                        CurrentSpot currentSpot = new CurrentSpot(siteId, ignore1, ignore2,
                                structureType, owner, param1, param2, spot.getX(), spot.getY(), spot.getRadius());
                        listOfCurrentSpots.add(currentSpot);
                    }
                }
            }

            List <Unit> listOfUnits = new ArrayList<>();
            int numUnits = in.nextInt();
            for (int i = 0; i < numUnits; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                int owner = in.nextInt();
                int unitType = in.nextInt(); // -1 = QUEEN, 0 = KNIGHT, 1 = ARCHER, 2 = GIANT
                int health = in.nextInt();
                Unit unit = new Unit(x, y, owner, unitType, health);
                listOfUnits.add(unit);
            }

            Queen myQueen = findQueen(listOfUnits, listOfCurrentSpots);
            int closestSpot = myQueen.getClosestSpot();
            String build = myQueen.building();
            String toTrain = myQueen.idOfSpotToTrain(gold);
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // First line: A valid queen action
            // Second line: A set of training instructions
            if(closestSpot != -1){
                System.out.println("BUILD "+ closestSpot + " " + build);
            } else {
                System.out.println("WAIT");
            }
            System.out.println("TRAIN " + toTrain);
        }
    }

    public static boolean isFriendly(int owner){
        return owner == 0;
    }

    public static boolean isQueen(int unitType){
        return unitType == -1;
    }

    public static Queen findQueen(List<Unit> listOfUnits, List<CurrentSpot> listOfCurrentSpots){
        for(Unit unit : listOfUnits){
            if(isFriendly(unit.getOwner()) && isQueen(unit.getUnitType())){
                return new Queen(listOfCurrentSpots, listOfUnits, unit.getX(), unit.getY(), unit.getHealth());
            }
        }
        return new Queen(listOfCurrentSpots, listOfUnits, listOfUnits.get(0).getX(), listOfUnits.get(0).getY(), listOfUnits.get(0).getHealth());
    }
}

class Spot {

    private int id;
    private int x;
    private int y;
    private int radius;

    public Spot(int id, int x, int y, int radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }
}

class CurrentSpot {

    private int spotId;
    private int ignore1;
    private int ignore2;
    private int structureType;
    private int owner;
    private int param1;
    private int param2;
    private int x;
    private int y;
    private int radius;

    public CurrentSpot(int spotId, int ignore1, int ignore2, int structureType, int owner,
                       int param1, int param2, int x, int y, int radius) {
        this.spotId = spotId;
        this.ignore1 = ignore1;
        this.ignore2 = ignore2;
        this.structureType = structureType;
        this.owner = owner;
        this.param1 = param1;
        this.param2 = param2;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public int getSpotId() {
        return spotId;
    }

    public int getIgnore1() {
        return ignore1;
    }

    public int getIgnore2() {
        return ignore2;
    }

    public int getStructureType() {
        return structureType;
    }

    public int getOwner() {
        return owner;
    }

    public int getParam1() {
        return param1;
    }

    public int getParam2() {
        return param2;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getRadius(){
        return radius;
    }
}

class Unit {

    private int x;
    private int y;
    private int owner;
    private int unitType;
    private int health;

    public Unit(int x, int y, int owner, int unitType, int health) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.unitType = unitType;
        this.health = health;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOwner() {
        return owner;
    }

    public int getUnitType() {
        return unitType;
    }

    public int getHealth() {
        return health;
    }
}

class Queen {

    private List<CurrentSpot> listOfCurrentSpots;
    private List<Unit> listOfUnits;
    private List<CurrentSpot> listOfFreeSpots = new ArrayList<>();
    private List<CurrentSpot> listOfEnemySpots = new ArrayList<>();
    private int x;
    private int y;
    private int health;

    public Queen(List<CurrentSpot> listOfCurrentSpots, List<Unit> listOfUnits, int x, int y, int health) {
        this.listOfCurrentSpots = listOfCurrentSpots;
        this.listOfUnits = listOfUnits;
        this.x = x;
        this.y = y;
        this.health = health;
    }

    public List<CurrentSpot> getListOfCurrentSpots() {
        return listOfCurrentSpots;
    }

    public List<Unit> getListOfUnits() {
        return listOfUnits;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public double getDistance(int x1, int y1, int x2, int y2) {

        int firstDifference = x2 - x1;
        int secondDifference = y2 - y1;

        return Math.sqrt(Math.pow(firstDifference, 2) + Math.pow(secondDifference, 2));
    }

    public List<CurrentSpot> getFreeSpots(){
        for(CurrentSpot spot : listOfCurrentSpots){
            if(spot.getOwner() == -1){
                listOfFreeSpots.add(spot);
            }
        }
        return listOfFreeSpots;
    }

    public List<CurrentSpot> getEnemySpots(){
        for(CurrentSpot spot : listOfCurrentSpots){
            if(spot.getOwner() == 1){
                listOfEnemySpots.add(spot);
            }
        }
        return listOfEnemySpots;
    }

    public int getClosestSpot() {
        listOfFreeSpots = getFreeSpots();
        listOfEnemySpots = getEnemySpots();
        int result = -1;
        double difference = 999999999;
        for (CurrentSpot spot : listOfFreeSpots) {
            double count = getDistance(this.x, this.y, spot.getX(), spot.getY());
            if (count < difference) {
                result = spot.getSpotId();
                difference = count;
            }
        }
        return result;
    }

    public String idOfSpotToTrain(int gold){
        String idToTrain = String.valueOf(0);
        for(CurrentSpot spot : listOfCurrentSpots){
            if(Player.isFriendly(spot.getOwner()) && spot.getStructureType() == 2){
                idToTrain = String.valueOf(spot.getSpotId());
            }
        }
        return idToTrain;
    }

    public String building() {
        int counter = 0;
        for(CurrentSpot spot : listOfCurrentSpots){
            if(Player.isFriendly(spot.getOwner()) && spot.getStructureType() == 2){
                counter ++;
            }
        }

        if (counter > 1) {
            return "TOWER";
        } else {
            return "BARRACKS-KNIGHT";
        }
    }
}