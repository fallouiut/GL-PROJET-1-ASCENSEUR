package system;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.Observable;

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
        System.out.println("Monter à l'étage : " + this.stageToReach);
        this.elevator = new Elevator(this, Elevator.Direction.TRACT_UP);
        elevator.start();
    }


    /** ----- MONTER L'ASCENSEUR ----- */
    public void getDOWN() {
        System.out.println("Descendre à l'étage : " + this.stageToReach);
        this.elevator = new Elevator(this, Elevator.Direction.TRACT_DOWN);
        elevator.start();
    }

    /** ----- ON A PASSE UN ETAGE EN MONTANT-----*/
    public void stageOverPassedUp() {
        this.currentStage += 1;
        System.out.println("Etage : " + this.currentStage);
        if(this.currentStage == this.stageToReach -1) {
            synchronized (this.elevator) {
                elevator.stopToNext();
            }
            notifyObservers();
        } else if(this.currentStage == this.stageToReach) {
            System.out.println("Etage : " + this.currentStage + " atteint");
            notifyObservers();
        }
    }


    /** ----- ON A PASSE UN ETAGE EN DESCENDANT -----*/
    public void stageOverPassedDown() {
        this.currentStage -= 1;
        System.out.println("Etage : " + this.currentStage);
        if(this.currentStage == this.stageToReach -1) {
            synchronized (this.elevator) {
                elevator.stopToNext();
                notifyObservers();
            }
        } else if(this.currentStage == this.stageToReach) {
            System.out.println("Etage : " + this.currentStage + " atteint");
            notifyObservers();
        }
    }

    public void setStageToReach(int stageToReach) {
        System.out.println("Etage demandé: " + this.stageToReach);
        this.stageToReach = stageToReach;
    }
}
