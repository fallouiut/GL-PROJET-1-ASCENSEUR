import system.Elevator;
import system.ElevatorSystem;
import views.CommandButtonView;
import views.ElevatorSliderView;
import views.CallsButtonView;
import views.SystemsActionView;

public class Main {

    private static final int STEPS = 5;

    public static void main(String args[]) {
        try {
            //CallsButtonView operativeView = new CallsButtonView(STEPS);
            //CommandButtonView commandButtonView = new CommandButtonView(STEPS);
            //SystemsActionView systemsActionView = new SystemsActionView();

            //operativeView.show();
            //commandButtonView.show();
            //systemsActionView.show();

            ElevatorSystem elevatorSystem = new ElevatorSystem();

            ElevatorSliderView elevatorSlider = new ElevatorSliderView(STEPS, elevatorSystem);

            /** ------------- CONFIG SYSTEM --------------- */
            elevatorSystem.frequencyMILLIS = 1000;
            elevatorSystem.setCurrentStage(0);
            elevatorSystem.setStageToReach(3);
            elevatorSystem.getUP();

            /** -------- SHOW VIEWS AFTER STARTING SYSTEM --------------*/
            elevatorSlider.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
