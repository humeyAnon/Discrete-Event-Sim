/*

    Stage.java - Stage super class, holds all the information the sub classes need
                 Any variables needed for one particular stage is in the sub class               
*/

import java.util.ArrayList;
import java.util.Random;

public abstract class Stage {

    private double starveTime = 0.0, workTime = 0.0, blockTime = 0.0;
    private String name;
    private boolean blocked, starve;
    private Random r = new Random();
    private Item item;
    private double itemFinishTime;
    private ArrayList<Stage> next = new ArrayList<>();
    private ArrayList<Stage> prev = new ArrayList<>();
   
    public Stage(String name) {

        this.name = name;

    }

    public abstract Event processItem(double arriveTime);

    // State modifiers
    protected void starve() {
        this.starve = true;
    }

    protected void blocked() {
        this.blocked = true;
    }

    protected void unblock() {
        this.blocked = false;
    }

    protected void unStarve() {
        this.starve = false;
    }

    // Getters & Setters
    public String getStarveTime() {
        return String.format("%4.2f", starveTime);
    }

    // Returns as a % for statistic output
    public String getWorkTime() {

        return String.format("%4.2f", (workTime/10000000)* 100);
    }

    public String getBlockTime() {
        return String.format("%4.2f", blockTime);
    }

    public void addStarveTime(double starveTime) {
        this.starveTime += starveTime;
    }

    public void addWorkTime(double workTime) {
        this.workTime += workTime;
    }

    public void addBlockTime(double blockTime) {
        this.blockTime += blockTime;
    }
    
    // Calculate the items processing time through
    public double calculateProcessTime(int m, int n) {

        return (m + (n * (r.nextDouble()-0.5)));

    }

    public void addNextStage(Stage s) {
        this.next.add(s);
    }

    public void addPrevStage(Stage s) {
        this.prev.add(s);
    }
    
    public String getName() {
        return this.name;
    }


    public boolean isBlocked() {
        if (this.blocked) return true;

        return false;
    }

    public boolean isStarving() {
        if(this.starve) return true;

        return false;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getItemFinishTime() {
        return itemFinishTime;
    }

    public void setItemFinishtime(double itemFinishTime) {
        this.itemFinishTime = itemFinishTime;
    }  

    public ArrayList<Stage> getNextStages() {
        return this.next;
    }

    public ArrayList<Stage> getPrevStages() {
        return this.prev;
    }
}