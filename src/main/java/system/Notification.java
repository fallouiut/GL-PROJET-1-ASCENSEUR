package system;

public class Notification {
    public enum Type {REQUEST_TAKEN_BY_SYSTEM, STAGE_REACHED, STAGE_OVERPASSED}

    private Type type;
    private int stageConcerned;

    public Notification(Notification.Type type, int stageConcerned) {
        this.type = type;
        this.stageConcerned = stageConcerned;
    }

    public Type getType() {
        return type;
    }

    public int getStageConcerned() {
        return stageConcerned;
    }
}
