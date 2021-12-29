/*
    End.java -  Sub class of Stage.java
                Stage can not be blocked as it has an unlimited size storage in finalItems
                If the stage is starved it need to take an item and will where possible
                Updates the items timestamps, calculates the path it has taken and pushed storage           
*/

import java.util.ArrayList;

public class End extends Stage {

    private int m, n;
    private InterQ prevStorage;
    private ArrayList<Item> finishedItems = new ArrayList<>();
    private int[] pathTaken = new int[4];
    private double startStarve, startBlocked;


    End(String name, int m, int n, InterQ prevStorage ) {
        super(name);
        
        this.m = m;
        this.n = n;
        this.prevStorage = prevStorage;

        // Initially starved
        starve();
        
    }

    @Override
    public Event processItem(double arriveTime) {

        if(arriveTime < this.getItemFinishTime()) {
            return null;
        }

        // Checks if the previous storage is empty
        if(prevStorage.isEmpty()) {

            starve();

            startStarve = arriveTime;

            return null;
        }

        // Previous store has item to pull
        else {

            // Stage has no items and is starving already
            if(this.isStarving()) {

                // Pull item
                this.setItem(prevStorage.removeItem(arriveTime));
                // Add to the stage starve time
                this.addStarveTime(arriveTime - startStarve); 
                // Add to the items timestamp
                this.getItem().addToArriveTimeStamp(arriveTime, this.getName());
                // Get items finish time
                this.setItemFinishtime(this.calculateProcessTime(m, n) + arriveTime);

                unStarve();

                return new Event(this, this.getItemFinishTime());

            }
            // Stage already has an item to process
            else {
                // Add to the items timestamp
                getItem().addToDepartureTimeStamp(arriveTime, this.getName());
                // Add to the stages work time
                this.addWorkTime(this.getItemFinishTime() - this.getItem().getTimeAdded(this.getName()));
                // Get what path the item took
                calculatePath();

                finishedItems.add(this.getItem());

                // pull an item
                this.setItem(prevStorage.removeItem(arriveTime));
                // Add to item timestamp
                this.getItem().addToArriveTimeStamp(arriveTime, this.getName());
                // Get items finish time
                this.setItemFinishtime(this.calculateProcessTime(m, n) + arriveTime);

                unStarve();

                return new Event(this, this.getItemFinishTime());

            }
        }
    }

    private void calculatePath() {
        if(this.getItem().getPathFlags()[0] == 0 && this.getItem().getPathFlags()[1] == 0) {

            pathTaken[0]++; // S2a -> S4a
        }
        else if(this.getItem().getPathFlags()[0] == 0 && this.getItem().getPathFlags()[1] == 1) {
            pathTaken[1]++; // S2a -> S4b
        }
        else if(this.getItem().getPathFlags()[0] == 1 && this.getItem().getPathFlags()[1] == 0) {
            pathTaken[2]++; // S2b -> S4a
        }  
        else {
            pathTaken[3]++; // S2b -> S4b
        }
    }

    public int getPath(int i) {
        return pathTaken[i];
    }

}