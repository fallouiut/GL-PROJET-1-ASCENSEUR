package system;

import java.util.concurrent.TimeUnit;

public class Elevator extends Thread {

    private static int count = 0;
    private int number;

    public enum Direction {TRACT_UP, TRACT_DOWN, STEADY}

    private boolean stop = false;
    private boolean stopToNext = false;

    private Direction requestedDirection;

    private ElevatorSystem system;

    public Elevator(ElevatorSystem system) {
        this.system = system;
        this.stop = true;
        this.number = count++;
    }

    public void run() {

        while (true) {
            synchronized (this) {
                if (!stop) {
                    this.requestedDirection = system.getCurrentDirection();
                    this.move();
                }
            }
        }

    }

    // on boucle en faisant une temporisation et envoie un
    // message au systeme pour dire qu'on a passé un etage (en haut ou bas selon direction)
    public void move() {
        try { // on "avance"
            boolean up = system.getCurrentDirection() == Direction.TRACT_UP ? true : false;
            while (!this.stopToNext) {
                TimeUnit.MILLISECONDS.sleep(ElevatorSystem.frequencyMILLIS);
                if (this.requestedDirection == Direction.TRACT_UP) {
                    this.system.stageOverPassedUp();
                } else {
                    this.system.stageOverPassedDown();
                }
                stop = true;
            }
            // pour le dernier etage
            TimeUnit.MILLISECONDS.sleep(ElevatorSystem.frequencyMILLIS);
            if (this.requestedDirection == Direction.TRACT_UP) {
                this.system.stageOverPassedUp();
            } else {
                this.system.stageOverPassedDown();
            }
        } catch (Exception e) {
            System.out.println("Elevator: tractUp() crash");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void go() {
        synchronized (this) {
            this.stop = false;
        }
    }

    public void stopToNext() {
        this.stopToNext = true;
        System.out.println("Stopping at next floor.");
    }
}
