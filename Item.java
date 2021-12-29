/*

    Item.java - Item holds timestamps in a hashmap on when it arrives and departs stages
                Holds a flag array to calculate what path the item has taken            
*/

import java.util.HashMap;
import java.util.Map;


public class Item {
    
    private Map<String, double[]> stageData = new HashMap<>();// Stage data
    private Map<String, int[]> storageData; // storage data
    private String uid;
    private int[] pathFlags = new int[]{0,0};
    private final int ARRIVE_TIME_STAMP = 0, DEPARTURE_TIME_STAMP = 1;

    public Item(String uid) {
        createMap();
        this.uid = uid;
    }

    // Creating the map of timestamps for arrival - leave time
    // Key is the stage hold all timeslots
    public void createMap() {

        stageData.put("S0", new double[2]);
        stageData.put("Q01", new double[2]);
        stageData.put("S1", new double[2]);
        stageData.put("Q12", new double[2]);
        stageData.put("S2A", new double[2]);
        stageData.put("S2B", new double[2]);
        stageData.put("Q23", new double[2]);
        stageData.put("S3", new double[2]);
        stageData.put("Q34", new double[2]);
        stageData.put("S4A", new double[2]);
        stageData.put("S4B", new double[2]);
        stageData.put("Q45", new double[2]);
        stageData.put("S5", new double[2]);

    }

    // Adding the arrival time to whatever stage/queue the item is in
    public void addToArriveTimeStamp(double time, String name) {
        
        double[] timeStamp = stageData.get(name);

        timeStamp[ARRIVE_TIME_STAMP] = time;
    }

    // Adding the departure time to whatever stage/queue the item
    public void addToDepartureTimeStamp(double time, String name) {

        double[] timeStamp = stageData.get(name);
        timeStamp[DEPARTURE_TIME_STAMP] = time;
    }

    public double getTimeAdded(String stageName) {

        double[] timeStamp = stageData.get(stageName);

        return timeStamp[0];
    }

    public double getTimeLeft(String storageName) {
        double[] timeStamp = stageData.get(storageName);

        return timeStamp[1];
    }

    public double getDifference(String storageName, String stageName) {

       return stageData.get(storageName)[0] - stageData.get(stageName)[0];

    }

    public int[] getPathFlags() {
        return pathFlags;
    }

    public void addPath(String name) {

        switch(name) {

            case "S2A":

                pathFlags[0] = 0;

            break;

            case "S2B":

                pathFlags[0] = 1;

            break;

            case "S4A":

                pathFlags[1] = 0;

            break;

            case "S4B":

                pathFlags[1] = 1;

            break;

            default: // Not apart of the path flags
            break;

        }

    }

}
