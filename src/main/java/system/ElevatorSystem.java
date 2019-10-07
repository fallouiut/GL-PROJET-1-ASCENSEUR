package system;
import views.CallsButtonView;
import views.CommandButtonView;
import views.ElevatorSliderView;
import views.SystemsActionView;

import java.sql.DriverManager;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

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

    public ElevatorSystem() {
        System.out.println("Initialisation du système");
        this.currentStage = 0;
    }

    /** ----- MONTER L'ASCENSEUR ----- */
    public void getUP() {
        if(this.currentStage >= this.stageToReach) {
            throw new IllegalArgumentException("current stage > stage to reach and wanna go up ?");
        }
        System.out.println("Monter à l'étage : " + this.stageToReach);
        this.elevator = new Elevator(this, Elevator.Direction.TRACT_UP);
        elevator.start();
    }

    /** ----- DESCENDRE L'ASCENSEUR ----- */
    public void getDOWN() {
        if(this.currentStage <= this.stageToReach) {
            throw new IllegalArgumentException("current stage < stage to reach and wanna go down ?");
        }
        System.out.println("Descendre à l'étage : " + this.stageToReach);
        this.elevator = new Elevator(this, Elevator.Direction.TRACT_DOWN);
        elevator.start();
    }

    /** ----- ON A PASSE UN ETAGE EN MONTANT-----*/
    public void stageOverPassedUp() {
        this.currentStage += 1;
        System.out.println("Etage : " + this.currentStage);
        if(this.currentStage == this.stageToReach - 1) {
            synchronized (this.elevator) {
                elevator.stopToNext();
            }
            setChanged();
            notifyObservers();
        } else if(this.currentStage == this.stageToReach) {
            System.out.println("Etage : " + this.currentStage + " atteint");
            setChanged();
            notifyObservers();
        }
    }

    /** ----- ON A PASSE UN ETAGE EN DESCENDANT -----*/
    public void stageOverPassedDown() {
        this.currentStage -= 1;
        System.out.println("Etage : " + this.currentStage);
        if(this.currentStage == this.stageToReach + 1) {
            synchronized (this.elevator) {
                elevator.stopToNext();
                notifyObservers("DOWN");
            }
        } else if(this.currentStage == this.stageToReach) {
            System.out.println("Etage : " + this.currentStage + " atteint");
            notifyObservers("DOWN");
        }
    }

    public void setStageToReach(int stageToReach) {
        System.out.println("Etage demandé: " + this.stageToReach);
        this.stageToReach = stageToReach;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public int getCurrentStage() {
        return currentStage;
    }
}
