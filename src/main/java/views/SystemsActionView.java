package views;

import system.ElevatorSystem;
import system.Notification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class SystemsActionView extends JFrame implements Observer {

    private ElevatorSystem elevatorSystem;
    private JButton orderUp, orderDown, orderNextStep, orderEmergency;
    boolean emergency = false;

    public SystemsActionView (ElevatorSystem elevatorSystem) {
        this.elevatorSystem = elevatorSystem;
        this.elevatorSystem.addObserver(this);

        this.orderDown = new JButton("Down");
        this.orderUp = new JButton("Up");
        this.orderNextStep = new JButton("Stop at next floor");
        this.orderEmergency = new JButton("Activate Emergency");


        this.orderUp.setSize(new Dimension(100, 250));
        this.orderDown.setSize(new Dimension(100, 250));
        this.orderEmergency.setSize(new Dimension(100, 250));
        this.orderNextStep.setSize(new Dimension(100, 250));

        this.enableListeners();

        this.add(this.orderUp);
        this.add(this.orderDown);
        this.add(this.orderNextStep);
        this.add(this.orderEmergency);


        this.setLayout(new FlowLayout());

        this.setSize(new Dimension(200, 300));
        this.setLocation(new Point(1000, 200));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("System Simulator");
    }

    private void enableListeners() {
        this.orderUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("System Action : Up");
                int stageToReach = elevatorSystem.getCurrentStage() + 1;
                if (stageToReach > elevatorSystem.getMaxStage())
                    System.out.println("You already are at the highest floor.");
                else
                    elevatorSystem.takeRequestOnCabine(stageToReach);
            }
        });
        this.orderDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("System Action : Down");
                int stageToReach = elevatorSystem.getCurrentStage() - 1;
                if (stageToReach < elevatorSystem.getMinStage())
                    System.out.println("You already are at the lowest floor.");
                else
                    elevatorSystem.takeRequestOnCabine(stageToReach);
            }
        });
        this.orderNextStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("System Action : Stop at next floor");
                elevatorSystem.stopElevator();
            }
        });
        this.orderEmergency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!emergency)
                {
                    System.out.println("System Action : Activate Emergency");
                    elevatorSystem.enableEmergency();
                    orderEmergency.setText("Deactivate Emergency");
                    emergency = true;
                }
                else
                {
                    System.out.println("System Action : Deactivate Emergency");
                    elevatorSystem.disableEmergency();
                    orderEmergency.setText("Activate Emergency");
                    emergency = false;
                }
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        Notification action = (Notification) arg;

        switch (action.getType()) {
            case MOVING :
                elevatorMoving();
                break;
            case STILL :
                elevatorStill();
                break;
        }
    }

    private void elevatorStill() {
        orderUp.setEnabled(true);
        orderDown.setEnabled(true);
        orderNextStep.setEnabled(false);
    }

    private void elevatorMoving() {
        orderUp.setEnabled(false);
        orderDown.setEnabled(false);
        orderNextStep.setEnabled(true);
    }
}