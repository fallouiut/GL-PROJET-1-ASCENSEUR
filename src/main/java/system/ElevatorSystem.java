package system;

import java.util.Collection;
import java.util.Observable;
import java.util.concurrent.TimeUnit;


import system.Elevator;

public class ElevatorSystem extends Observable {
    // temps par etage
    public static int frequencyMILLIS;

    // etage courant
    private int currentStage;

    // etage a atteindre
    private int stageToReach;

    // liste des etages montant et descendant
    private Collection<Integer> amountingStages;
    private Collection<Integer> descendingStages;

    private Elevator elevator;

    private Door door;

    public ElevatorSystem() {
        this.door = new Door();
        System.out.println("Initializing system.");
        this.currentStage = 0;
    }

    /** ----- MONTER L'ASCENSEUR ----- */
    public void getUP() {
        if(this.currentStage >= this.stageToReach) {
            throw new IllegalArgumentException("Can't go up if the floor to reach is lower than the current floor.");
        }
        System.out.println("Going up to floor : " + this.stageToReach);
        this.elevator = new Elevator(this, Elevator.Direction.TRACT_UP);
        elevator.start();
    }

    /** ----- DESCENDRE L'ASCENSEUR ----- */
    public void getDOWN() {
        if(this.currentStage <= this.stageToReach) {
            throw new IllegalArgumentException("Can't go down if the floor to reach is lower than the current floor.");
        }
        System.out.println("Going down to floor : " + this.stageToReach);
        this.elevator = new Elevator(this, Elevator.Direction.TRACT_DOWN);
        elevator.start();
    }

    /** ----- ON A PASSE UN ETAGE EN MONTANT-----*/
    public void stageOverPassedUp() {
        this.currentStage += 1;

        setChanged();
        notifyObservers();

        System.out.println("Floor : " + this.currentStage);
        if(this.currentStage == this.stageToReach - 1) {
            synchronized (this.elevator) {
                elevator.stopToNext();
            }
        } else if(this.currentStage == this.stageToReach) {
            System.out.println("Floor : " + this.currentStage + " reached");
            try {
                door.open();
                TimeUnit.SECONDS.sleep(3);
                door.close();
            } catch (Exception ie) {
                System.out.println("Porte fermeture echouee");
                ie.printStackTrace();
            }
        }
    }

    /** ----- ON A PASSE UN ETAGE EN DESCENDANT -----*/
    public void stageOverPassedDown() {
        this.currentStage -= 1;
        System.out.println("Floor : " + this.currentStage);
        if(this.currentStage == this.stageToReach + 1) {
            synchronized (this.elevator) {
                elevator.stopToNext();
                notifyObservers("DOWN");
            }
        } else if(this.currentStage == this.stageToReach) {
            System.out.println("Floor : " + this.currentStage + " reached");
            notifyObservers("DOWN");
        }
    }

    public void setStageToReach(int stageToReach) {
        System.out.println("Floor to reach : " + this.stageToReach);
        this.stageToReach = stageToReach;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public int getCurrentStage() {
        return currentStage;
    }
}
