/*

    Event.java - Used to hold events that items have processed 
                 Holds that stage owns the event and the total run time of the item
                 Implements comparable for sorting within the priorityQueue
*/

public class Event implements Comparable<Event> {

    private Stage owner;
    private double runTime;
 
    public Event(Stage owner, double runTime) {
        this.owner = owner;
        this.runTime = runTime;
    }

    public Stage getOwner() {
        return owner;
    }

    @Override
    public int compareTo(Event o) {
        
        if(this.runTime > o.runTime) return 1;

        if(this.runTime < o.runTime) return -1;

        return 0;
    }

    public double getRunTime(){
        return runTime;
    }
}
