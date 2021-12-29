/*

    Factory.java - Runs the simulation up to the time allowed
                   NOTE - There is currently a null exception, it comes up maybe 1/10 run
                   And for the life of me can't figure out why - I'm assuming if the PQ every gets empty somehow
*/


import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;

public class Factory {

    private final double TIME_ALLOWED = 10000000;
    private int m, n, qMax;
    private Stage S0, S1, S2A, S2B, S3, S4A, S4B, S5;
    private InterQ Q01, Q12, Q23, Q34, Q45;
    private ArrayList<Stage> stages = new ArrayList<>();
    public ArrayList<Item> finishedItems = new ArrayList<>();
    private PriorityBlockingQueue<Event> pq = new PriorityBlockingQueue<>();
    private double time = 0.0;
    private Event event;

    // Set up the factory
    // Creating stages, creates storages
    // Linkes all together
    public Factory(int m, int n, int qMax) {
        this.m = m;
        this.n = n;
        this.qMax = qMax;
        setUp();
    }

    public void run() {
        
        // Adding initial job to pq
        pq.add(new Event(S0, S0.getItemFinishTime()));

        // Run while 
        while(time < TIME_ALLOWED) {

            // Grabs the event from PQ
            event = pq.poll();

            // I had issues of deadlocking the storages and it would null pointer
            // This kind of fixed the issue, not ideal and unsure if it fixed it 100% but ran out of time
            if(event == null) {
                for(Stage s : stages) {
                    if(s.isStarving() || s.isBlocked()) {
                        Event event = s.processItem(time);
                        if(event != null) {
                           pq.add(event);
                            break;
                        }
                    }
                }
                event = pq.poll();
            }
            
            // Setting global time to when the event will finish
            time = event.getRunTime();

            // Processing the event from PQ returns an event or nothing if its blocked
            event = event.getOwner().processItem(time);

            // Add the event to the PQ if there is one
            if(event != null) {
                pq.add(event);
            }

            // Check if the previous stage is blocked
            if(event != null) {
                 for(Stage s : event.getOwner().getPrevStages()) {

                    if(s.isBlocked()) {

                        Event newEvent = s.processItem(time);

                        if(newEvent != null) {
                            pq.add(newEvent);
                            break;
                        }
                    }
                }
            }
               
            // Check if next stage is blocked
            if(event != null) {
                 for(Stage s : event.getOwner().getNextStages()) {

                    if(!s.isBlocked()) {

                        Event newEvent = s.processItem(time);

                        if(newEvent != null) {
                            pq.add(newEvent);
                            break;
                        }       
                    }
                }
            }         
        }
    }
    
    public void setUp() {

        // Creating InterStorages
        Q01 = new InterQ(qMax, "Q01");
        Q12 = new InterQ(qMax, "Q12");
        Q23 = new InterQ(qMax, "Q23");
        Q34 = new InterQ(qMax, "Q34");
        Q45 = new InterQ(qMax, "Q45");

        // Creating stages
        stages.add(S0 = new Start("S0", m, n, Q01));
        stages.add(S1 = new Middle("S1", m, n, Q01, Q12));
        stages.add(S2A = new Middle("S2A", 2 * m, 2 * n, Q12, Q23));
        stages.add(S2B = new Middle("S2B", 2 * m, 2 * n, Q12, Q23));
        stages.add(S3 = new Middle("S3", m, n, Q23, Q34));
        stages.add(S4A = new Middle("S4A", 2 * m, 2 * n, Q34, Q45));
        stages.add(S4B = new Middle("S4B", 2 * m, 2 * n, Q34, Q45));
        stages.add(S5 = new End("S5", m, n, Q45));

        // Linking stages together
        S0.addNextStage(S1);

        S1.addPrevStage(S0);    
        S1.addNextStage(S2A);
        S1.addNextStage(S2B);           

        S2A.addPrevStage(S1);
        S2A.addNextStage(S3);
        S2B.addPrevStage(S1);
        S2B.addNextStage(S3);

        S3.addPrevStage(S2A);
        S3.addPrevStage(S2B);
        S3.addNextStage(S4A);
        S3.addNextStage(S4B);

        S4A.addPrevStage(S3);
        S4A.addNextStage(S5);
        S4B.addPrevStage(S3);
        S4B.addNextStage(S5);

        S5.addPrevStage(S4A);
        S5.addPrevStage(S4B);

    }
    
    // Uses stringbuilder to make output - Ideally would just have a toString in each method
    // But ran out of time so mucked this up
    public String printTheseStatistics() {

        StringBuilder sb = new StringBuilder();

        sb.append("Production Stations: \n ----------------------------------------- \n");
        sb.append("Stage: \t Work[%] \t Starve[t] \t Block [t] \n");
        sb.append(S0.getName() + "\t" + S0.getWorkTime() + "\t\t" + S0.getStarveTime() + "\t\t" + S0.getBlockTime() + "\n");
        sb.append(S1.getName() + "\t" + S1.getWorkTime() + "\t\t" + S1.getStarveTime() + "\t" + S1.getBlockTime() + "\n");
        sb.append(S2A.getName() + "\t" + S2A.getWorkTime() + "\t\t" + S2A.getStarveTime() + "\t" + S2A.getBlockTime() + "\n");
        sb.append(S2B.getName() + "\t" + S2B.getWorkTime() + "\t\t" + S2B.getStarveTime() + "\t" + S2B.getBlockTime() + "\n");
        sb.append(S3.getName() + "\t" + S3.getWorkTime() + "\t\t" + S3.getStarveTime() + "\t" + S3.getBlockTime() + "\n");
        sb.append(S4A.getName() + "\t" + S4A.getWorkTime() + "\t\t" + S4A.getStarveTime() + "\t" + S4A.getBlockTime() + "\n");
        sb.append(S4B.getName() + "\t" + S4B.getWorkTime() + "\t\t" + S4B.getStarveTime() + "\t" + S4B.getBlockTime() + "\n");
        sb.append(S5.getName() + "\t" + S5.getWorkTime() + "\t\t" + S5.getStarveTime() + "\t" + S5.getBlockTime() + "\n");

        sb.append("Storage Queues: \n ----------------------------------------- \n");
        sb.append("Store\tAvgTime[t]\tAvgItems\n");
        sb.append("Q01\t" +  Q01.getAverageItemTime() + "\t\t" + Q01.getAverageItems() + "\n");
        sb.append("Q12\t" +  Q12.getAverageItemTime() + "\t\t" + Q12.getAverageItems() + "\n");
        sb.append("Q23\t" +  Q23.getAverageItemTime() + "\t\t" + Q23.getAverageItems() + "\n");
        sb.append("Q34\t" +  Q34.getAverageItemTime() + "\t\t" + Q34.getAverageItems() + "\n");
        sb.append("Q45\t" +  Q45.getAverageItemTime() + "\t\t" + Q45.getAverageItems() + "\n");

        sb.append("Production Paths: \n ----------------------------------------- \n");
        sb.append("S2a -> S4a:\t " + ((End) S5).getPath(0) + "\n");
        sb.append("S2a -> S4b:\t " + ((End) S5).getPath(1) + "\n");
        sb.append("S2b -> S4a:\t " + ((End) S5).getPath(2) + "\n");
        sb.append("S2b -> S4b:\t " + ((End) S5).getPath(3) + "\n");

        return sb.toString();
    }
}   