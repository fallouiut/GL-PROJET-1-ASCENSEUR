import system.ElevatorSystem;
import views.CommandButtonView;
import views.ElevatorSliderView;
import views.CallsButtonView;

import java.io.File;

public class Main {
    private static final int STEPS = 5;
    private static final int FIRST = 0;
    private static final int FREQM = 2000;

    public static void main(String args[]) {
        try {
            /** ------------- CONFIG SYSTEM --------------- */
            ElevatorSystem elevatorSystem = new ElevatorSystem(STEPS);
            elevatorSystem.frequencyMILLIS = FREQM;
            elevatorSystem.setCurrentStage(FIRST);
            /*elevatorSystem.getUP();*/

            ElevatorSliderView elevatorSlider = new ElevatorSliderView(STEPS, elevatorSystem, FIRST);
            CallsButtonView operativeView = new CallsButtonView(STEPS, elevatorSystem);
            CommandButtonView commandButtonView = new CommandButtonView(STEPS, elevatorSystem);
            //SystemsActionView systemsActionView = new SystemsActionView();

            /** -------- SHOW VIEWS AFTER STARTING SYSTEM --------------*/
            elevatorSlider.show();
            operativeView.show();
            commandButtonView.show();
            //systemsActionView.show();

            elevatorSystem.waitToGo();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}