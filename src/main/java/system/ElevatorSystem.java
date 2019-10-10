package system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    // liste des etages montant et descendant, ainsi que les requetes cabine
    private volatile List<Integer> cabineRequest;
    private volatile List<Integer> floorRequestUP;
    private volatile List<Integer> floorRequestDOWN;

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
        this.floorRequestDOWN = new ArrayList<Integer>(this.maxStages);
        this.floorRequestUP = new ArrayList<Integer>(this.maxStages);
        this.cabineRequest = new ArrayList<Integer>(this.maxStages);
    }

    public void chooseNext() {
        // TODO: pour Mehdi
        int newStage = this.cabineRequest.get(0);
        this.cabineRequest.remove(0);
        // essaie de trouver un algo
        // si il faut supprimer les 3 listes et en faire une vas-y a ce moment il faudra me le dire pour que je modifie
        // les deux fonctions qui prennent les requetes

        // mon idée est s'il ny a personne dans l'ascenseur a ce moment tu traites les requetes des etages
        // sinon s'il tu dois monter et qu'entre temps il y a des gens qui veulent ussi monter, tu les traites aussi
        // pareil si tu descent

        // il y a des milliers de strategies, dsl tu a le truc le plus complexe a faire, si je finis je le fais avec toi

        // il faut aussi que l'ascenseur puisse s'arreter entre temps sil y a un etage dans le sens de direction
        // ca se fera surement ailleurs dans tractDown ou tractUp
        // mais le probleme est qu'on va perdre l'etage de base
        // donc on aura atteint des que currentStage == stageToReach == file.premier()
        // donc si file.premier() = 8 et qu'il y a 5 qui veut monter
        // stage to reach = 5 mais on continue a aller vers 8

        // tu appelles la fonction tract() pour lancer le truc en passant le nouvel etage
        this.tract(newStage);
    }

    public void waitToGo() {
        while (floorRequestDOWN.isEmpty() && floorRequestDOWN.isEmpty() && cabineRequest.isEmpty()) {
            continue;
        }
        System.out.println("Requete, choose next");
        chooseNext();
    }

    public void tract(int newStage) {
        this.stageToReach = newStage;
        if (this.currentStage < this.stageToReach) {
            this.getUP();
        } else if (this.currentStage > this.stageToReach) {
            this.getDOWN();
        }
    }

    /**
     * ----- PRENDRE UNE REQUETE DES ETAGES (montant ou descendant) -----
     */
    public void takeRequestOnFloor(int stage, Elevator.Direction direction) {
        // si le sens demandé est la montée
        if (stage >= 0 && stage <= this.maxStages) {
            /** LETAGE DEMANDE UNE MONTEE */
            if (direction == Elevator.Direction.TRACT_UP) {
                if (!this.floorRequestUP.contains(stage)) {
                    this.floorRequestUP.add(stage);
                }
            }
            /** LETAGE DEMANDE UNE DESCENTE */
            else {
                if (!this.floorRequestDOWN.contains(stage)) {
                    this.floorRequestDOWN.add(stage);
                }
            }
            System.out.println("Recu");
            setChanged();
            notifyObservers(new Notification(Notification.Type.REQUEST_FLOOR_TAKEN_BY_SYSTEM, stage, direction));
        }
    }

    public void takeRequestOnCabine(int stage) {
        if (stage >= 0 && stage <= this.maxStages && !this.cabineRequest.contains(stage)) {
            this.cabineRequest.add(stage);
            setChanged();
            notifyObservers(new Notification(Notification.Type.REQUEST_CABINE_TAKEN_BY_SYSTEM, stage));
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

                setChanged();
                notifyObservers(new Notification(Notification.Type.STAGE_REACHED, this.currentStage));

                // on retourne à l'etat d'attente(diagramme etat systeme)
                this.waitToGo();

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
        notifyObservers(new Notification(Notification.Type.STAGE_OVERPASSED, this.currentStage));

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

                setChanged();
                notifyObservers(new Notification(Notification.Type.STAGE_REACHED, this.currentStage));

                // on retourne à l'etat d'attente(diagramme etat systeme)
                this.waitToGo();

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
