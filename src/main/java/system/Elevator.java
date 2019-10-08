package system;

import java.util.concurrent.TimeUnit;

public class Elevator extends Thread {

    public enum Direction { TRACT_UP, TRACT_DOWN }

    private boolean stopToNext = false;
    
    private Door doors;
    
    private Direction requestedDirection;

    private ElevatorSystem system;

    public Elevator(ElevatorSystem system, Direction direction) {
        this.system = system;
        this.requestedDirection = direction;
        doors = new Door();
    }

    public void run() {
        try {
        	if (doors.getState())
        		doors.close();
            switch (this.requestedDirection) {
                case TRACT_UP:
                    this.tractUp();
                    break;
                case TRACT_DOWN:
                    this.tractDown();
                    break;
                default:
                    throw new Exception("Undefined direction");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // on boucle en faisant une temporisation et envoie un
    // message au systeme pour dire qu'on a passé un etage
    public void tractUp() {
        try {
            synchronized (this) {
                while (!this.stopToNext) {
                    TimeUnit.MILLISECONDS.sleep(ElevatorSystem.frequencyMILLIS);
                    //Thread.sleep(ElevatorSystem.frequencyMILLIS);
                    this.system.stageOverPassedUp();
                }
                // pour le dernier etage
                if (!doors.getState())
                	doors.open();
                TimeUnit.MILLISECONDS.sleep(ElevatorSystem.frequencyMILLIS);
                if (doors.getState())
                	doors.close();
                this.system.stageOverPassedUp();
            }
        } catch (Exception e) {
            System.out.println("Elevator: tractUp() crash");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void tractDown() {
        try {
            synchronized (this) {
                while (!this.stopToNext) {
                    Thread.sleep(ElevatorSystem.frequencyMILLIS);
                    this.system.stageOverPassedDown();
                }
                // pour le dernier etage

                TimeUnit.MILLISECONDS.sleep(ElevatorSystem.frequencyMILLIS);
                this.system.stageOverPassedDown();
                if (!doors.getState())
                	doors.open();
                if (doors.getState())
                	doors.close();
            }
        } catch (Exception e) {
            System.out.println("Elevator: tractDown() crash");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopToNext(){
        this.stopToNext = true;
        System.out.println("Stopping at next floor.");
    }
}
