package views;

import system.Elevator;
import system.ElevatorSystem;
import system.Notification;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SliderUI;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

public class ElevatorSliderView extends JFrame implements Observer {

    private JSlider slider;
    private ElevatorSystem elevatorSystem;

    public ElevatorSliderView(int steps, ElevatorSystem system, int first) {
        this.slider = new JSlider(JSlider.VERTICAL, 0, steps, first);

        this.slider.setMajorTickSpacing(steps);
        this.slider.setMinorTickSpacing(0);
        this.slider.setPaintTicks(true);
        this.slider.setPaintLabels(true);


        for (MouseListener listener : slider.getMouseListeners())
            slider.removeMouseListener(listener);

        this.elevatorSystem = system;
        this.elevatorSystem.addObserver(this);

        this.add(this.slider);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        this.slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO: A supprimer si rien a faire
                // TODO: ou notifier system comme quoi le changement s'est fait
            }
        });

        this.setSize(new Dimension(200, 300));
        this.setLocation(new Point(300, 200));
        this.setTitle("Elevator Simulator");

    }

    @Override
    public void update(Observable o, Object arg) {
        Notification action = (Notification) arg;

        switch (action.getType()) {
            case STAGE_OVERPASSED:
                slider.setValue(this.elevatorSystem.getCurrentStage());
                break;
        }

        this.slider.setValue(elevatorSystem.getCurrentStage());
    }
}
