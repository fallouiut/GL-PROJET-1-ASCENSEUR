package system;

import java.util.ArrayList;
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

    // nombre d'etages max
    private int maxStages;

    // liste des etages montant et descendant
    private Collection<Integer> amountingStages;
    private Collection<Integer> descendingStages;

    // gestionnaire ascenseur (et vue)
    private Elevator elevator;

    // etat de direction de l'ascenseur (utile pour l'arrivée de nouvelles requetes)
    private Elevator.Direction currentDirection;

    // porte (inutilse si ce n'est niveau visuel)
    private Door door;

    public ElevatorSystem(int maxStages) {
        this.door = new Door();
        System.out.println("Initializing system.");
        this.currentStage = 0;
        this.maxStages = maxStages;

        // TODO: a enlever (juste pour tester takeRequest())
        this.currentDirection = Elevator.Direction.TRACT_UP;

        // TODO: a mettre dans start()
        this.amountingStages = new ArrayList<Integer>(this.maxStages);
        this.descendingStages = new ArrayList<Integer>(this.maxStages);

    }

    public void chooseNext() {
        // TODO: pour Mehdi
        int newStage = 5;
        // tu  fais ton algo pour choisir le prochain etage
        // tu appelles la fonction tract() pour lancer le truc en psasant le nouvel etage
        this.tract(newStage);
    }

    public void waitAndGo() {
        while (amountingStages.size() != 0 || descendingStages.size() != 0) {
            chooseNext();
        }
    }

    public void tract(int newStage) {
        this.stageToReach = newStage;
        if(this.currentStage < this.stageToReach) {
            this.getUP();
        } else if(this.currentStage > this.stageToReach) {
            this.getDOWN();
        }
    }

    /**
     * ----- PRENDRE UNE REQUETE -----
     */
    public void takeRequest(int stage) {
        // si le sens demandé est la montée
        if (stage >= 0 && stage <= this.maxStages) {
            /** MOUVEMENT VERS HAUT */
            if (this.currentStage < stage) {
                // si on monte mais que nouvel Etage < etage a atteindre, on peut s'arreter entre temps et continuer
                if (stage < this.stageToReach && this.currentDirection == Elevator.Direction.TRACT_UP) {
                    this.amountingStages.add(stageToReach);
                    this.stageToReach = stage;
                    System.out.println("Stage : " + stage + " devient la priorité");
                } else {
                    this.amountingStages.add(stage);
                    System.out.println("ajouté en liste montée");

                }
            }
            /** MOUVEMENT VERS BAS */
            else if (this.currentStage > stage) {
                // si on descend mais que nouvel Etage > etage a atteindre, on peut s'arreter entre temps et continuer
                if (stage > this.stageToReach && this.currentDirection == Elevator.Direction.TRACT_DOWN) {
                    this.descendingStages.add(stageToReach);
                    this.stageToReach = stage;
                    System.out.println("Stage : " + stage + " devient la priorité");
                } else {
                    this.descendingStages.add(stage);
                    System.out.println("ajoutée en liste descente");
                }
            }
            setChanged();
            notifyObservers(new Notification(Notification.Type.REQUEST_TAKEN_BY_SYSTEM, stage));
        }
    }

    /**
     * ----- MONTER L'ASCENSEUR -----
     */
    public void getUP() {
        if (this.currentStage >= this.stageToReach) {
            throw new IllegalArgumentException("Can't go up if the floor to reach is lower than the current floor.");
        }
        this.currentDirection = Elevator.Direction.TRACT_UP;
        System.out.println("Going up to floor : " + this.stageToReach);
        this.elevator = new Elevator(this, Elevator.Direction.TRACT_UP);
        elevator.start();
    }

    /**
     * ----- DESCENDRE L'ASCENSEUR -----
     */
    public void getDOWN() {
        if (this.currentStage <= this.stageToReach) {
            throw new IllegalArgumentException("Can't go down if the floor to reach is lower than the current floor.");
        }
        this.currentDirection = Elevator.Direction.TRACT_DOWN;
        System.out.println("Going down to floor : " + this.stageToReach);
        this.elevator = new Elevator(this, Elevator.Direction.TRACT_DOWN);
        elevator.start();
    }

    /**
     * ----- ON A PASSE UN ETAGE EN MONTANT-----
     */
    public void stageOverPassedUp() {
        this.currentStage += 1;

        setChanged();
        notifyObservers(new Notification(Notification.Type.STAGE_OVERPASSED, this.currentStage));

        System.out.println("Floor : " + this.currentStage);
        if (this.currentStage == this.stageToReach - 1) {
            synchronized (this.elevator) {
                elevator.stopToNext();
            }
        } else if (this.currentStage == this.stageToReach) {
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

    /**
     * ----- ON A PASSE UN ETAGE EN DESCENDANT -----
     */
    public void stageOverPassedDown() {
        this.currentStage -= 1;

        setChanged();
        notifyObservers();

        System.out.println("Floor : " + this.currentStage);
        if (this.currentStage == this.stageToReach + 1) {
            synchronized (this.elevator) {
                elevator.stopToNext();
            }
        } else if (this.currentStage == this.stageToReach) {
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

    public void setStageToReach(int stageToReach) {
        this.stageToReach = stageToReach;
        System.out.println("Floor to reach : " + this.stageToReach);
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public int getCurrentStage() {
        return currentStage;
    }
}
