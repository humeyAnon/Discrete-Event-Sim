/*

    InterQ.java - Used for storage between stages, calculates the total items processed
                  The average items in queue through the simulation and the time
                  Updates the items timestamps           
*/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class InterQ {

    private int qMax, totalItemsInQueue;
    private double totalTime, timeEntered, timeLeft;
    private String name;
    private Queue<Item> storage;
    private int totalItemsProcessed;
    private ArrayList<Integer> avgItemInQueue = new ArrayList<>();

    
    public InterQ(int qMax, String name) {

        this.qMax = qMax;
        this.name = name;
        totalItemsInQueue = 0;
        storage = new LinkedList<Item>();

    }

    // Make sure the Q is not full prior to inserting
    // Increase the items stats
    public void insertItem(Item item, double time) {

        storage.add(item);
        timeEntered = time;

        increaseTotalItems();
        avgItemInQueue.add(totalItemsInQueue);

        // Add the arrival time to item timestamps
        item.addToArriveTimeStamp(time, this.name);
        

    }

    // Stage is taking an item - Update the items departure timestamp
    // Update the queues statistics
    public Item removeItem(double timeLeaving) {

        Item item = storage.poll();

        // Add the departure time to item timestamp
        item.addToDepartureTimeStamp(timeLeaving, this.name);

        timeLeft = timeLeaving;
        totalTime += timeLeaving - timeEntered;
        
        decreaseTotalItem();
        totalItemsProcessed++;
        avgItemInQueue.add(totalItemsInQueue);

        return item;
    }

    // Calculates the avg item in the queue over the whole simulation
    public String getAverageItems() {

        int size = 0;
        
        for(int i = 0; i < avgItemInQueue.size(); i++) {
            size += avgItemInQueue.get(i);
        }

        return String.format("%4.2f", (double)size/avgItemInQueue.size());

    }

    // Getter for the avg item count
    public String getAverageItemTime() {
        return String.format("%4.2f", (double)totalTime/totalItemsProcessed);
    }

    public void increaseTotalItems() {
        totalItemsInQueue++;
    }

    public void decreaseTotalItem() {
        totalItemsInQueue--;
    }

    public int getTotalItemsInQueue() {
        return totalItemsInQueue;
    }

    // Returns the total time of having items in the Q
    public String getTotalTime() {
        return String.format("%4.2f", totalTime);
    }

    // Checks if the Q is full
    public boolean isStorageFull() {

        if(storage.size() == qMax) return true;

        return false;

    }

    // Checks if the Q is empty
    public boolean isEmpty() {
        
        if(storage.size() == 0) {
            return true;
        }
        return false;
    }
    
    // Name Getter
    public String getName() {
        return name;
    }

}
