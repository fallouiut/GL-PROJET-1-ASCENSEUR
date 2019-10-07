package views;

import system.Elevator;
import system.ElevatorSystem;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SliderUI;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ElevatorSliderView extends JFrame implements Observer {

    private JSlider slider;
    private ElevatorSystem elevatorSystem;

    public ElevatorSliderView(int steps, ElevatorSystem system) {
        this.slider = new JSlider(JSlider.VERTICAL, 0, steps, 0);

        this.elevatorSystem = system;
        this.elevatorSystem.addObserver(this);

        this.add(this.slider);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("Etage : " + slider.getValue());
            }
        });

        this.setSize(new Dimension(200, 300));
        this.setLocation(new Point(300, 200));
        this.setTitle("Simulateur ascenseur");

    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Update");
        this.slider.setValue(elevatorSystem.getCurrentStage());
    }
}
