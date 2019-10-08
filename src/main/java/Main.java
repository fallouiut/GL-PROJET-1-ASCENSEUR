import system.ElevatorSystem;
import views.CommandButtonView;
import views.ElevatorSliderView;
import views.CallsButtonView;
public class Main {
    private static final int STEPS = 5;

    public static void main(String args[]) {
        try {
            /** ------------- CONFIG SYSTEM --------------- */
            ElevatorSystem elevatorSystem = new ElevatorSystem();
            elevatorSystem.frequencyMILLIS = 2000;
            elevatorSystem.setCurrentStage(0);
            elevatorSystem.setStageToReach(5);
            elevatorSystem.getUP();

            ElevatorSliderView elevatorSlider = new ElevatorSliderView(STEPS, elevatorSystem);
            CallsButtonView operativeView = new CallsButtonView(STEPS, elevatorSystem);
            CommandButtonView commandButtonView = new CommandButtonView(STEPS, elevatorSystem);
            //SystemsActionView systemsActionView = new SystemsActionView();

            /** -------- SHOW VIEWS AFTER STARTING SYSTEM --------------*/
            elevatorSlider.show();
            operativeView.show();
            commandButtonView.show();
            //systemsActionView.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}