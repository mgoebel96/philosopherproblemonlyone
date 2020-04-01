import java.util.Random;

import static java.lang.Thread.sleep;

public class Philosopher implements Runnable {

    String name;
    Fork right, left;
    Random random = new Random();
    private volatile boolean exit = false;

    public Philosopher(String name, Fork right, Fork left){
        this.name = name;
        this.right = right;
        this.left = left;
    }

    public void run() {
        int i = 100;
        while( i > 0 && !exit) {
            try {
                // Philosopher is thinking
                Logger.printOut (name + " philosphiert.");
                sleep((int) (random.nextDouble()*1000));
                Logger.printOut (name + " hat Hunger.");
                // Philosopher is hungry
                PhilosophersDesk.eatingPhilosopher.acquire();
                // taking right
                right.get();
                // turn left (critical moment)
                sleep((int) (random.nextDouble()*1000));
                // taking left
                left.get();
                Logger.printOut (name + " hat zwei Gabeln. Er kann essen.");
                // holding two forks -> can eat now
                sleep((int) (random.nextDouble()*1000));
            } catch (InterruptedException e) {
                Logger.printOut (e.getMessage());
            }
            PhilosophersDesk.eatingPhilosopher.release();
            right.put();
            left.put();
            i--;
        }
        System.out.println("Der Tisch ist kaputt...");
    }

    public void stop(){
        exit = true;
    }
}