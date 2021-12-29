/*

    Middle.java - Sub class of Stage.java
                  Stage has the possiblity of being starving and blocked
                  If the stage is starving it needs an item and will take one if possible 
                  if the stage is blocked it has an item and is waiting for the next Stage to take from the storage  
                  updates the items timestamps and stages statistic times               
*/

public class Middle extends Stage {

    private int m, n;
    private double startStarve, startBlocked;
    InterQ prevStorage, nextStorage;

    public Middle(String name, int m, int n, InterQ prevStorage, InterQ nextStorage) {
        super(name);

        this.m = m;
        this.n = n;
        this.prevStorage = prevStorage;
        this.nextStorage = nextStorage;

        // Initially starved
        starve();

    }

    @Override
    public Event processItem(double arriveTime) {

        // Checking if the item is ready to be processed
        if(arriveTime < this.getItemFinishTime()) {
            return null;
        }

        if(prevStorage.isEmpty()) {

            startStarve = arriveTime;
            starve();
            return null;
        }
        // Was starving, unstarve and increase how long it starved for
        if(this.isStarving()) {

            unStarve();
            this.addStarveTime(arriveTime - startStarve);

        }
        // Need to block, start what time its been blocked
        else if(nextStorage.isStorageFull()) {

            blocked();
            startBlocked = arriveTime;
            return null;

        }
        else {
            // Was already blocked
            if(this.isBlocked()) {

                this.addBlockTime(arriveTime - startBlocked);
                unblock();

            }
        }
        // Already had an item so push
        if(this.getItem() != null) {
            // Add to the items departure time
            this.getItem().addToDepartureTimeStamp(arriveTime, this.getName());
            // Add to the stages worktime
            addWorkTime(this.getItemFinishTime() - this.getItem().getTimeAdded(this.getName()));
            // Push to next storage
            nextStorage.insertItem(this.getItem(), arriveTime);
        }  
        
        // Prev isnt empty, so grab the next item
        this.setItem(prevStorage.removeItem(arriveTime));
        // Add to the timestamp
        this.getItem().addToArriveTimeStamp(arriveTime, this.getName());
        // Get the times finished time
        this.setItemFinishtime(this.calculateProcessTime(m, n) + arriveTime);
        // Add to the items path flags
        this.getItem().addPath(this.getName());

        return new Event(this, this.getItemFinishTime());
    }
}
