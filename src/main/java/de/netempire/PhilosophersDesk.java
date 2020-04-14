package de.netempire;

import de.netempire.classes.Fork;
import de.netempire.classes.Philosopher;
import de.netempire.logger.ResultLogger;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class PhilosophersDesk {

    public static Semaphore eatingPhilosopher = new Semaphore(1, true);

    public static void main(String[] args) {
        startProcess();
    }

    private static void startProcess() {
        Date start = Calendar.getInstance().getTime();

        Fork fork1 = new Fork();
        Fork fork2 = new Fork();
        Fork fork3 = new Fork();
        Fork fork4 = new Fork();
        Fork fork5 = new Fork();

        Philosopher platon = new Philosopher("Platon", fork1, fork2);
        platon.setEatingTime(750);
        Philosopher aristoteles = new Philosopher("Aristoteles",fork2, fork3);
        aristoteles.setEatingTime(1000);
        Philosopher herder = new Philosopher("Herder", fork3, fork4);
        herder.setEatingTime(300);
        Philosopher fichte = new Philosopher("Fichte", fork4, fork5);
        fichte.setEatingTime(1500);
        Philosopher schlegel = new Philosopher("Schlegel", fork5, fork1);
        schlegel.setEatingTime(500);

        Thread platonThread = new Thread(platon);
        platonThread.start();
        Thread aristotelesThread = new Thread(aristoteles);
        aristotelesThread.start();
        Thread schlegelThread = new Thread(schlegel);
        schlegelThread.start();
        Thread fichteThread = new Thread(fichte);
        fichteThread.start();
        Thread herderThread = new Thread(herder);
        herderThread.start();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Runnable controller = () -> {
            if(!platonThread.isAlive() && !herderThread.isAlive() && !aristotelesThread.isAlive() && !fichteThread.isAlive() && !schlegelThread.isAlive()){
                platon.stop();
                herder.stop();
                platon.stop();
                aristoteles.stop();
                schlegel.stop();
                executor.shutdown();
                System.out.println("Der Abend wird beendet.");
                ResultLogger.log("Die Philosophen haben " + computeDuration(start, Calendar.getInstance().getTime()) + " Sekunden zusammen am Tisch gesessen.");
            }
        };
        executor.scheduleAtFixedRate(controller, 0, 4, TimeUnit.SECONDS);
        Thread task2Thread = new Thread(controller);
        task2Thread.start();
    }

    public static int computeDuration(Date to, Date from) {
        long difference = from.getTime() - to.getTime();
        return (int) (difference/1000);
    }
}