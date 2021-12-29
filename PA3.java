/*

    PA3.java - Main class, creates the factory with the desired inputs from the user
               Runs the simulation and prints out the desired output
*/

class PA3 {

    public static void main(String args[]) {

        int m = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        int qMax = Integer.parseInt(args[2]);

        Factory theBestFactoryEver = new Factory(m, n, qMax);
        theBestFactoryEver.run();

        System.out.println(theBestFactoryEver.printTheseStatistics());

    }
}