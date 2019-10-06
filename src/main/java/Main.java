import views.CommandButtonView;
import views.ElevatorSliderView;
import views.CallsButtonView;
import views.SystemsActionView;

public class Main {

    private static final int STEPS = 5;

    public static void main(String args[]) {
        try {
            CallsButtonView operativeView = new CallsButtonView(STEPS);
            ElevatorSliderView elevatorSlider = new ElevatorSliderView(STEPS);
            CommandButtonView commandButtonView = new CommandButtonView(STEPS);
            SystemsActionView systemsActionView = new SystemsActionView();

            operativeView.show();
            elevatorSlider.show();
            commandButtonView.show();
            systemsActionView.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
