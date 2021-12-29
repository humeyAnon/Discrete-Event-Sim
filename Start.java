/*

    Start.java - Sub class of Stage.java
                 Used to create items and push them to the next storage if possible
                 Uses the Singleton instance to create unique item ID's
                 Start stage can never starve and has an unlimited supply of items it creates
                 It will block only when the next storage is full
*/

public class Start extends Stage {
    
    private SingletonID instance = SingletonID.getInstance();
    private int m, n;
    private InterQ storage;
    private double startBlocked;

    public Start(String name, int m, int n, InterQ storage) {
        super(name);

        this.m = m;
        this.n = n;
        this.storage = storage;
        this.setItem(createItem(0));
        
    }

    @Override
    public Event processItem(double arriveTime) {
        
        // Start stage is never starved as it has an unlimited amount of items to process
        // If the item has not finished processing dont push 
        if(arriveTime < this.getItemFinishTime()) {
            return null;
        }

        // Check if its blocked by the storage
        if(storage.isStorageFull()) {
            
            blocked();

            startBlocked = arriveTime;

            return null;
        } 

        // If its already been blocked
        else {

            if(this.isBlocked()) {

                this.addBlockTime(arriveTime - startBlocked);
                
                unblock();

                startBlocked = 0;

            }
        }

        // Adding the items departure time stamp
        getItem().addToDepartureTimeStamp(this.getItemFinishTime(), this.getName());
        
         // Add to the stages work time
        this.addWorkTime(this.getItemFinishTime() - this.getItem().getTimeAdded(this.getName())); // - what time it came into the stage
        
        // Send the stage to storage
        storage.insertItem(this.getItem(), this.getItemFinishTime());

        createItem(arriveTime);

        return new Event(this, this.getItemFinishTime());
    }

    private Item createItem(double arriveTime) {

        // Creating stages item
        Item item = new Item(instance.getID());
        this.setItem(item);
            
        // Adding the items arrive timeStamp
        getItem().addToArriveTimeStamp(arriveTime, this.getName());
        
        // Calculating the working time for this item - T2 = T1 - P
        // Setting the items departure time as its already known - Wrong as the next storage may be full
        this.setItemFinishtime(this.calculateProcessTime(m, n) + arriveTime);

        return item;
    }
}