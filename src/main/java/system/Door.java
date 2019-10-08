package system;

public class Door {

    private boolean isOpened;

    public Door() {
        this.isOpened = false;
    }

    public void open() {
        this.isOpened = true;
        System.out.println("Door opened");
    }

    public void close() {
        this.isOpened = false;
        System.out.println("Door closed");
    }
    
    public boolean getState()
    {
    	return isOpened;
    }
    
}
