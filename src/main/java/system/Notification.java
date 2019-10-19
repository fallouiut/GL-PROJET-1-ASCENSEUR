package system;

public class Notification {
    public enum Type {REQUEST_CABINE_TAKEN_BY_SYSTEM, REQUEST_FLOOR_TAKEN_BY_SYSTEM, STAGE_REACHED, STAGE_OVERPASSED, MOVING, STILL}

    private Type type;
    private int stageConcerned;
    private Elevator.Direction direction;

    public Notification(Notification.Type type, int stageConcerned) {
        this.type = type;
        this.stageConcerned = stageConcerned;
    }

    public Notification(Notification.Type type, int stageConcerned, Elevator.Direction direction) {
        this.type = type;
        this.stageConcerned = stageConcerned;
        this.direction = direction;
    }

    public Elevator.Direction getDirection() {
        return direction;
    }

    public Type getType() {
        return type;
    }

    public int getStageConcerned() {
        return stageConcerned;
    }
}
