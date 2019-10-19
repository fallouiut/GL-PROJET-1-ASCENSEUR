package system;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ElevatorSystem extends Observable {

    private boolean emergencyState = false;

    // temps par etage
    public static int frequencyMILLIS;

    // etage courant
    private int currentStage;

    // etage a atteindre
    private int stageToReach;

    // nombre d'etages max
    private int minStage;
    private int maxStages;

    // liste des etages montant et descendant, ainsi que les requetes cabine
    private volatile Queue<Integer> cabineRequest;
    private volatile TreeSet<Integer> floorRequestUP;
    private volatile TreeSet<Integer> floorRequestDOWN;

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

        this.elevator = new Elevator(this);
        this.elevator.start();

        this.cabineRequest = new PriorityQueue<Integer>();

        // pour monter faut avoir
        this.floorRequestUP = new TreeSet<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        this.floorRequestDOWN = new TreeSet<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
    }

    /*
     on veut prendre la prochain etage et deduire la direction
     et s'il y a des etahes intermediaire(floorRequestUP si on monte) il faut les desservir d'une pierre deux coup
     donc etage a atteindre = requeteCabine.premier()

     trier la file cabine en fonction de la direction // modifier et peut etr mettre un treetSet
     avec un comparator
     pareil pour file montee et file descente

     puis tu parcours la file adequate en fonction de la direction
     on trie la file dans l'ordre croissant pour montée et décroissant pour descente
     s'il y a des etages entre current_etage et etage_a_atteindre
     etage a atteindre = file.current()
     tu tourne, et ca ca va remonter jusqu'a requeteCabine.premier()
     des que current etage = requeteCabine.premier() c qu'on l'a atteint et on l'enleve de la file

     Ex: la cabine demande 8, on est à 0, on va monter
     etage 3 et 5 veulent monter, on dessert 3 puis 5 puis plus rien dans floorUp
     on monte à 8 qui est tjr le premier de etage cabine

     tu appelles la fonction tract() pour lancer le truc en passant le nouvel etage
*/
    public void chooseNext() {
        if (this.cabineRequest.size() > 0) {
            int nextStage = this.cabineRequest.peek();

            Integer intermediateStage = null;
            if (nextStage > this.currentStage) { // on monte
                for (Integer between : this.floorRequestUP) { // on regarde s'il ya un intermediaire dans la montée
                    // si l'etage se situe entre current-next
                    if (between > this.currentStage && between < nextStage) {
                        intermediateStage = between;
                        break;
                    }
                }
                if (intermediateStage != null) {
                    nextStage = intermediateStage.intValue();
                }
            } else {
                // pareil dans le sens inverse pour la descente
                for (Integer between : this.floorRequestDOWN) {
                    // si l'etage se situe entre current-next
                    if (between < this.currentStage && between > nextStage) {
                        intermediateStage = between;
                        break;
                    }
                }
                if (intermediateStage != null) {
                    nextStage = intermediateStage.intValue();
                }
            }

            this.tract(nextStage);
        } else { // If cabineRequest.size == 0
            // on verifie les appels externes et on choisit le plus proche
            int nextStage;

            /** CALCUL DE LA PLUS PROCHE REQUETE EXTERNE */
            if (!floorRequestDOWN.isEmpty() && !floorRequestUP.isEmpty()) { // Si il en existe une de chaque
                int closestUp = this.getClosestStage("UP");
                int closestDown = this.getClosestStage("DOWN");

                if (Math.abs(currentStage - closestUp) < Math.abs(currentStage - closestDown)) {
                    nextStage = closestUp;
                    this.floorRequestUP.remove(nextStage);
                }
                else {
                    nextStage = closestDown;
                    this.floorRequestDOWN.remove(nextStage);
                }
            } else {
                if (!floorRequestDOWN.isEmpty()) { // S'il existe qu'une requete descendante
                    nextStage = this.getClosestStage("DOWN");
                }
                else if (!floorRequestUP.isEmpty()) { // Si il existe qu'une requete montante
                    nextStage = this.getClosestStage("UP");
                }
                else { // inutile mais pour eviter erreur
                    nextStage = this.currentStage == 0 ? this.maxStages: 0;
                }
            }
            this.tract(nextStage);
        }
    }

    public int getClosestStage(String direction) {
        TreeSet<Integer> listToCheck = direction == "UP" ? this.floorRequestUP: floorRequestDOWN;

        Integer currentClosest = null;
        Integer currentEcart = null;
        for(Integer stage: listToCheck) {
            int ecart = Math.abs(currentStage - stage);
            if(currentEcart == null) {
                currentEcart = ecart;
                currentClosest = stage;
            } else if(ecart < currentEcart) {
                currentClosest = stage;
                currentEcart = ecart;
            }
        }

        return currentClosest;
    }

    public void waitToGo() {

        boolean waiting = false;

        while (floorRequestUP.isEmpty() && floorRequestDOWN.isEmpty() && cabineRequest.isEmpty() && !emergencyState) {
            if (!waiting)
            {
                waiting = true;
                setChanged();
                notifyObservers(new Notification(Notification.Type.STILL, this.currentStage));
            }
            if(emergencyState) {
                System.out.println("\u001B[31m" + "Emergency, can't move anymore" + "\u001B[0m");
            }
        }
        setChanged();
        notifyObservers(new Notification(Notification.Type.MOVING, this.currentStage));
        System.out.println(cabineRequest.toString());
        chooseNext();
    }

    public void tract(int newStage) {
        this.stageToReach = newStage;
        System.out.println("\u001B[32m -Next stage to reach " + this.stageToReach + "\u001B[0m");
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

        this.elevator.go();
        /*
        this.elevator = new Elevator(this, Elevator.Direction.TRACT_UP);
        elevator.start();*/
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

        this.elevator.go();
        /*
        this.elevator = new Elevator(this, Elevator.Direction.TRACT_DOWN);
        elevator.start();*/
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
            this.stageReached();
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
            this.stageReached();
        }
    }

    public void stageReached() {
        System.out.println("Floor : " + this.currentStage + " reached");
        try {
            door.open();
            TimeUnit.SECONDS.sleep(3);
            door.close();
            
            // on l'enleve une fois achevé
            this.cabineRequest.remove(this.currentStage);
            this.floorRequestUP.remove(this.currentStage);
            this.floorRequestDOWN.remove(this.currentStage);

            setChanged();
            notifyObservers(new Notification(Notification.Type.STAGE_REACHED, this.currentStage));


            System.out.println("Attente 5s pour requête cabine des utilisateurs");
            TimeUnit.SECONDS.sleep(5);

            // on retourne à l'etat d'attente(diagramme etat systeme)
            this.waitToGo();

        } catch (Exception ie) {
            System.out.println("Porte fermeture echouee");
            ie.printStackTrace();
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

    public Elevator.Direction getCurrentDirection() {
        return currentDirection;
    }

    public void enableEmergency()
    {
        elevator.stopToNext();
        cabineRequest.clear();
        floorRequestUP.clear();
        floorRequestDOWN.clear();
        emergencyState = true;
        door.close();
    }

    public void disableEmergency()
    {
        emergencyState = false;
        this.waitToGo();
    }

    public int getMaxStage()
    {
        return maxStages;
    }

    public int getMinStage()
    {
        return minStage;
    }

    public void stopElevator()
    {
        elevator.stopToNext();
    }

    public static void main(String[] args) {
        ElevatorSystem system = new ElevatorSystem(5);

        system.cabineRequest.add(0);

        system.floorRequestDOWN.add(1);
        system.floorRequestDOWN.add(2);

        system.currentStage = 5;

        System.out.println("Closest: " + system.getClosestStage("DOWN"));
        //System.out.println(system.floorRequestDOWN.toString());
    }


}
